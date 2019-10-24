package com.example.demo.point.proxy.staticProxy;

import com.example.demo.point.proxy.Actor;
import com.example.demo.point.proxy.ActorImpl;

public class StaticProxy implements Actor {

    private Actor actor;

    public StaticProxy(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void sayHello() {
        System.out.println("start static proxy...");
        actor.sayHello();
        System.out.println("completed static proxy...");
    }

    /**
     * 测试静态代理
     *
     * @param args
     */
    public static void main(String[] args) {
        Actor actor = new ActorImpl();
        Actor staticProxy = new StaticProxy(actor);
        staticProxy.sayHello();
    }

}
