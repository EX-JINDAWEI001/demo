package com.example.demo.common.redis;

import com.example.demo.common.mq.rocketmq.RocketMessageHandler;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConsumer implements RocketMessageHandler {

    @Autowired
    private RedisHandler redisHandler;

    @Override
    public void doService(MessageExt msg) {
        redisHandler.lpush(msg.getKeys(), msg.getMsgId());
    }

}
