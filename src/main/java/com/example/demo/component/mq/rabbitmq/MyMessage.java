package com.example.demo.component.mq.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyMessage {
    private String id;
    private String name;
    private int age;
    private String eventDesc;
}
