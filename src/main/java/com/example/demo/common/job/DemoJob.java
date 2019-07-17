package com.example.demo.common.job;

import com.example.demo.common.mongo.MongoLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DemoJob {

    private static final Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Autowired
    private MongoLockHandler mongoLockHandler;

    @Resource
    private MongoTemplate mongoTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void doJob() {
        logger.info("demoJob doJob------------");
        if (mongoLockHandler.getLock("test", 1000)) {
            Criteria criteria = new Criteria();
            criteria.andOperator(Criteria.where("age").gt(10), Criteria.where("age").lt(30));
            Query query = new Query(criteria);
            mongoTemplate.remove(query, "jdw");
        }
        logger.info("demoJob doJob collection jdw is{}", mongoTemplate.findAll(Map.class, "jdw"));
    }

}
