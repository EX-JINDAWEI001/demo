package com.example.demo.point.proxy.jdk;

public interface Actor {
    void sayHello();

    default void hello(String in){
        System.out.println(in);
    }
}
