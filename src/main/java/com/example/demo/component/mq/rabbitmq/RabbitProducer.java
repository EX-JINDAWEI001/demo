package com.example.demo.component.mq.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducer {

    @Autowired
    private RabbitTemplate template;

    public void send(String exchange, String routingKey, MyMessage msg) {
        template.convertAndSend(exchange, routingKey, JSON.toJSONString(msg));
    }

}
