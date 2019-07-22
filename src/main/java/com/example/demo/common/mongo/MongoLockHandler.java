package com.example.demo.common.mongo;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MongoLockHandler {

    @Resource
    private MongoTemplate mongoTemplate;

    public boolean getLock(String key, long expire) {
        List<MongoLock> locks = this.getLockByKey(key);
        if (locks.size() > 0) {
            if (locks.get(0).getExpire() >= System.currentTimeMillis()) {//锁已经被别人获取
                return false;
            } else {//释放过期的锁,以便进行新一轮竞争
                this.releaseExpiredLock(key, System.currentTimeMillis());
            }
        }

        //开始新一轮竞争
        int value = this.upsertLock(key, 1, System.currentTimeMillis() + expire);
        return value == 1 ? true : false;

    }

    public void releaseLock(String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        mongoTemplate.remove(query, MongoLock.class);
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
