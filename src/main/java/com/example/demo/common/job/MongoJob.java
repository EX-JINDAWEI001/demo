package com.example.demo.common.job;

import com.example.demo.component.mongo.MongoLockHandler;
import com.example.demo.common.system.SimpleServiceInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MongoJob implements SimpleServiceInit {

    private static final Logger logger = LoggerFactory.getLogger(MongoJob.class);

    @Autowired
    private MongoLockHandler mongoLockHandler;

    private static final String LOCK_KEY = "LOCK_KEY";

    @Scheduled(cron = "0 1 * * * ?")
    public void doJob() {
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

    @Override
    public void init() {

        Runnable r1 = () -> {
            while (true) {
                logger.info("aaaaa:{}", mongoLockHandler.lock(LOCK_KEY, 3000));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                logger.info("bbbbb:{}", mongoLockHandler.lock(LOCK_KEY, 3000));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(r1).start();
        new Thread(r2).start();
    }

}
