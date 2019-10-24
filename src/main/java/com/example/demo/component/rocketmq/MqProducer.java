package com.example.demo.component.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;

    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;

    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Bean
    public DefaultMQProducer initMqProducer() {
        try {
            DefaultMQProducer producer = new DefaultMQProducer(groupName);
            producer.setNamesrvAddr(namesrvAddr);
            producer.setMaxMessageSize(maxMessageSize);
            producer.setSendMsgTimeout(sendMsgTimeout);
            producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
            producer.start();
            return producer;
        } catch (MQClientException e) {
            logger.error("initMqProducer error:{}", e);
            return null;
        }
    }

}
