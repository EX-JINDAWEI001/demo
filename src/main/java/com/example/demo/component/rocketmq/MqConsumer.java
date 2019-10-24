package com.example.demo.component.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class MqConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${rocketmq.consumer.groupName}")
    private String groupName;

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.consumer.topics}")
    private String topics;

    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;

    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    private Map<String, RocketMessageHandler> handlerMap;

    @Bean
    public DefaultMQPushConsumer initMqConsumer() {
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
            consumer.setNamesrvAddr(namesrvAddr);
            consumer.subscribe(topics, "*");

            consumer.setConsumeThreadMin(consumeThreadMin);
            consumer.setConsumeThreadMax(consumeThreadMax);
            consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

            /**
             * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
             * 如果非第一次启动，那么按照上次消费的位置继续消费
             */
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            /**
             * 设置消费模型，集群还是广播，默认为集群
             * 集群模式下才支持重试机制
             */
            consumer.setMessageModel(MessageModel.CLUSTERING);

            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
//                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                try {
                    for (MessageExt msg : msgs) {
                        RocketMessageHandler handler = handlerMap.get(msg.getKeys());
                        handler.doService(msg);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    logger.error("rocket mq consumer msg error:", e);
                    //防止重试，失败后不重试！！！
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
            return consumer;
        } catch (MQClientException e) {
            logger.error("initMqConsumer error :{}", e);
            return null;
        }
    }

    public void setHandlerMap(Map<String, RocketMessageHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

}
