package com.example.demo.point.proxy.jdk;

public interface Actor {
    void sayHello();

    // JDK8 默认方法
    default void hello(String in) {
        System.out.println(in);
    }

    // JDK8 静态方法
    static void blowHorn() {
        System.out.println("按喇叭!!!");
    }

    // JDK9 私有方法
    /*private void run() {
        System.out.println("跑起来!!!");
    }*/
}
