package com.example.demo.common.job;

import com.example.demo.common.mongo.MongoLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MongoJob {

    private static final Logger logger = LoggerFactory.getLogger(MongoJob.class);

    @Autowired
    private MongoLockHandler mongoLockHandler;

    private static final String LOCK_KEY = "LOCK_KEY";

    @Scheduled(cron = "0/3 * * * * ?")
    public void doJob(){
        boolean isLock = false;
        try {
            isLock = mongoLockHandler.lock(LOCK_KEY, 30000);
            if (isLock) {
                logger.info("mongoJob get lock 1111111111.......");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.error("mongoJob get lock. error", e);
        } finally {
            if (isLock) {
                logger.info("mongoJob release lock 1111111111.......{}", mongoLockHandler.unLock(LOCK_KEY));
            }
        }
    }
}
