package com.example.demo.component.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;

public interface RocketMessageHandler {
    void doService(MessageExt msg);
}
