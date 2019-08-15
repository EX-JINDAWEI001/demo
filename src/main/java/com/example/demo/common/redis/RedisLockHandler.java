package com.example.demo.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockHandler.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String UNLOCK_LUA;

    public static final String LOCK_LUA;

    static {
        StringBuilder unlockLua = new StringBuilder();
        unlockLua.append("if redis.call('get', KEYS[1]) == ARGV[1] ");
        unlockLua.append("then ");
        unlockLua.append("    return redis.call('del', KEYS[1]) ");
        unlockLua.append("else ");
        unlockLua.append("    return 0 ");
        unlockLua.append("end ");
        UNLOCK_LUA = unlockLua.toString();

        StringBuilder lockLua = new StringBuilder();
        lockLua.append("if redis.call('setnx', KEYS[1], ARGV[1]) == 1 ");
        lockLua.append("then ");
        lockLua.append("    redis.call('expire', KEYS[1], ARGV[2]) return true ");
        lockLua.append("else ");
        lockLua.append("    return false ");
        lockLua.append("end ");
        LOCK_LUA = lockLua.toString();
    }

    public boolean lock(String lockKey, String requestId, Long expireTime) {

        return stringRedisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);

        //若不支持上述原子操作，可使用如下方式
//        RedisScript redisScript = RedisScript.of(LOCK_LUA, Boolean.class);
//        List<String> keys = new ArrayList<>();
//        keys.add(lockKey);
//        return (Boolean)stringRedisTemplate.execute(redisScript, new FastJsonRedisSerializer<>(Object.class),
//                new FastJsonRedisSerializer<>(Object.class), keys, requestId, expireTime);

    }

    /**
     * 仅适用于单机模式，集群模式不支持
     * @param key
     * @param requestId
     */
    public void unLockSingle(String key, String requestId) {
//        DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>();
//        redisScript.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
//        redisScript.setResultType(Object.class);

        RedisScript redisScript = RedisScript.of(UNLOCK_LUA, Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        stringRedisTemplate.execute(redisScript, keys, requestId);
        logger.info("unLock completely=====");
    }

    /**
     * 单机和集群都适用，
     * 但有个条件，单机模式时，必须排除掉io.lettuce包，确保连接使用的是Jedis实例
     * @param key
     * @param requestId
     * @return
     */
    public boolean unLock(String key,String requestId) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(requestId);

            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long result = stringRedisTemplate.execute((RedisCallback<Long>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }

                // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                return 0L;
            });
            return result != null && result > 0;
        } catch (Exception e) {
            logger.error("release lock error", e);
        }
        return false;
    }

}
