package com.example.demo.component.redis;

import com.example.demo.component.mq.rocketmq.RocketMessageHandler;
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
