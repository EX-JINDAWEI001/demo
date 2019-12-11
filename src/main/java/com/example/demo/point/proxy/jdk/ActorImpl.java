package com.example.demo.point.proxy.jdk;

import com.example.demo.point.proxy.jdk.Actor;

public class ActorImpl implements Actor {
    @Override
    public void sayHello() {
        System.out.println("Hello Proxy World !");
    }
}
