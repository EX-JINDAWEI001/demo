package com.example.demo.component.dubbo.provider;

import com.example.demo.component.dubbo.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "hello dubbo " + name;
    }
}
