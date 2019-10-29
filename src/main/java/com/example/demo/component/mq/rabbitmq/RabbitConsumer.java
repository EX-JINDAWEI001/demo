package com.example.demo.component.mq.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "MyQueue")
public class RabbitConsumer {

    @RabbitHandler
    public void consumer(String msg) {
        System.out.println(JSON.parseObject(msg, MyMessage.class).toString());
    }

}
