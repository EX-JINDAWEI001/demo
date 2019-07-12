package com.example.demo.common.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void doJob() {
        System.out.println("demoJob doJob------------");
    }

}
