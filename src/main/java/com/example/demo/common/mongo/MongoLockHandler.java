package com.example.demo.common.mongo;

import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MongoLockHandler {

    private static final Logger logger = LoggerFactory.getLogger(MongoLockHandler.class);

    @Resource
    private MongoTemplate mongoTemplate;

    public boolean lock(String key, long expire) {
        List<MongoLock> locks = this.getLockByKey(key);
        if (locks.size() > 0) {
            if (locks.get(0).getExpire() >= System.currentTimeMillis()) {//锁已经被别人获取
                logger.info("锁已经被别人获取");
                return false;
            } else {//释放过期的锁,以便进行新一轮竞争
                this.releaseExpiredLock(key, System.currentTimeMillis());
                logger.info("释放过期的锁");
            }
        }

        //开始新一轮竞争
        int value = this.upsertLock(key, 1, System.currentTimeMillis() + expire);
        logger.info("锁为：{}", value);
        return value == 1 ? true : false;

    }

    public boolean unLock(String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        DeleteResult result = mongoTemplate.remove(query, MongoLock.class);
        return result.getDeletedCount() > 0;
    }

    private Integer upsertLock(String key, int value, long expireTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));

        Update update = new Update();
        update.inc("value", value);
        update.set("expire", expireTime);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);   //存在则更新,否则插入
        options.returnNew(true); //返回更新后的值

        //此处貌似不是线程安全的,这种实现方式不能作为分布式锁使用??????????
        MongoLock mongoLock = mongoTemplate.findAndModify(query, update, options, MongoLock.class);
        return mongoLock.getValue();
    }

    private void releaseExpiredLock(String key, long expireTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        query.addCriteria(Criteria.where("expire").lt(expireTime));
        mongoTemplate.remove(query, MongoLock.class);
    }

    private List<MongoLock> getLockByKey(String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        return mongoTemplate.find(query, MongoLock.class);
    }

}
