package com.example.demo.common.dubbo.provider;

import com.example.demo.common.dubbo.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "hello dubbo " + name;
    }
}
