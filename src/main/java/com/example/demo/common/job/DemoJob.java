package com.example.demo.common.job;

import com.example.demo.common.mongo.MongoLockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoJob {

    @Autowired
    private MongoLockHandler mongoLockHandler;

    @Scheduled(cron = "0/5 * * * * ?")
    public void doJob() {
        System.out.println("demoJob doJob------------");
        System.out.println(mongoLockHandler.getLock("test", 1000));
    }

}
