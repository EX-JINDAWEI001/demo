package com.example.demo.common.mq.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;

public interface RocketMessageHandler {
    void doService (MessageExt msg);
}
