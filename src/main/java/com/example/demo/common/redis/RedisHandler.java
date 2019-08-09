package com.example.demo.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisHandler.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //===============String================
    public void set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("set error:{}", e);
        }
    }

    public String get(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("get error:{}", e);
            return null;
        }
    }

    public Boolean expire(String key, Long expireTime) {
        try {
            return stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("expire error:{}", e);
            return false;
        }
    }

    public Boolean setex(String key, String value, Long expireTime) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            logger.error("setex error:{}", e);
            return false;
        }
    }

    public Boolean setnx(String key, String value) {
        try {
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.error("setnx error:{}", e);
            return false;
        }
    }

    public Boolean setnxex(String key, String value, Long expireTime) {
        try {
            boolean isDone = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if(isDone) {
                stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
            return isDone;
        } catch (Exception e) {
            logger.error("setnxex error:{}", e);
            return false;
        }
    }

    //===============Hash================
    public void hset(String key, String field, String value) {
        try {
            stringRedisTemplate.opsForHash().put(key, field, value);
        } catch (Exception e) {
            logger.error("hset error:{}", e);
        }
    }

    public String hget(String key, String field) {
        try {
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            return hashOperations.get(key, field);
        } catch (Exception e) {
            logger.error("hget error:{}", e);
            return null;
        }
    }

    //===============List================
    public void lpush(String key, String value) {
        try {
            stringRedisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            logger.error("lpush error:{}", e);
        }
    }

    public String rpop(String key) {
        try {
            ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
            return listOperations.rightPop(key);
        } catch (Exception e) {
            logger.error("rpop error:{}", e);
            return null;
        }
    }

    //===============Set================
    public void sadd(String key, String value) {
        try {
            stringRedisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            logger.error("sadd error:{}", e);
        }
    }

    public String spop(String key) {
        try {
            SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
            return setOperations.pop(key);
        } catch (Exception e) {
            logger.error("rpop error:{}", e);
            return null;
        }
    }

    //===============zSet================
    public void zadd(String key, String value, Double score) {
        try {
            stringRedisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            logger.error("zadd error:{}", e);
        }
    }

    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, Long start, Long end) {
        try {
            ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
            return zSetOperations.rangeWithScores(key, start, end);
        } catch (Exception e) {
            logger.error("zRangeWithScores error:{}", e);
            return null;
        }
    }

}
