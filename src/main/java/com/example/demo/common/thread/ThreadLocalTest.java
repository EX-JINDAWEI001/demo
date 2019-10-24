package com.example.demo.common.thread;

public class ThreadLocalTest {
    // 主线程、t1、t2等,各线程均维护一个自己的值,相互之间互不干扰,保证线程安全;
    private static final ThreadLocal<Integer> safeInt = new ThreadLocal<>();

    // 线程不安全;
    private static int unsafeInt = 1;

    public static void main(String[] args) {
        new Thread(
                () -> {
                    int num = 0;
                    while (num++ < 5) {
                        safeInt.set(num + 10000);
                        unsafeInt = 1000;
                        System.out.println(String.format("t1-{%s}:{%d}", num, safeInt.get()));
                    }
                }
        ).start();

        new Thread(
                () -> {
                    int num = 0;
                    while (num++ < 5) {
                        safeInt.set(num);
                        unsafeInt = 100;
                        System.out.println(String.format("t2-{%s}:{%d}", num, safeInt.get()));
                    }
                }
        ).start();

        safeInt.set(999);
        System.out.println("safeInt：" + safeInt.get());
        System.out.println("unsafeInt：" + unsafeInt);
    }
}
