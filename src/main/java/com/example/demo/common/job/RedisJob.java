package com.example.demo.common.job;

import com.example.demo.component.redis.RedisLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisJob {

    private static final Logger logger = LoggerFactory.getLogger(RedisJob.class);

    @Autowired
    private RedisLockHandler redisLockHandler;

    private static final String LOCK_KEY = "LOCK_KEY";

    @Scheduled(cron = "0/3 * * * * ?")
    public void doJob(){
        String requestId = UUID.randomUUID().toString();
        boolean isLock = false;
        try {
            isLock = redisLockHandler.lock(LOCK_KEY, requestId, 30l);
            if (isLock) {
                logger.info("redisJob get lock 1111111111.......");
            }
        } catch (Exception e) {
            logger.error("redisJob get lock. error", e);
        } finally {
            if (isLock) {
                logger.info("redisJob release lock 1111111111.......:{}", redisLockHandler.unLock(LOCK_KEY, requestId));
            }
        }
    }

}
