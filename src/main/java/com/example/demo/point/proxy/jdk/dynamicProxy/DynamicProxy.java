package com.example.demo.point.proxy.jdk.dynamicProxy;

import com.example.demo.point.proxy.jdk.Actor;
import com.example.demo.point.proxy.jdk.ActorImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {

    private Object realClass;

    public DynamicProxy(Object realClass) {
        super();
        this.realClass = realClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke start...");
        Object invoke = method.invoke(realClass, args);
        System.out.println("invoke end...");
        return invoke;
    }

    /**
     * 动态代理测试
     *
     * @param args
     */
    public static void main(String[] args) {
        Actor actor = new ActorImpl();
        Actor dynamicProxy = (Actor) Proxy.newProxyInstance(
                actor.getClass().getClassLoader(),
                actor.getClass().getInterfaces(),
                new DynamicProxy(actor)
        );

        dynamicProxy.sayHello();
        dynamicProxy.hello("default method");
        Actor.blowHorn();
    }

}
