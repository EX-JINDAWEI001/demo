package com.example.demo.component.thread;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadTest extends Thread {

//    private static boolean flag = false;

    private static AtomicBoolean flag = new AtomicBoolean(false);

//    private static volatile boolean flag = false;

    public void run() {
        System.out.println("t1:" + Thread.currentThread().getId());
        while (!flag.get()) {
//            synchronized (this) {}
//            System.out.println("aaaaa");
        }
        System.out.println("quit!");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadTest t1 = new ThreadTest();
        t1.start();
        Thread.sleep(50);
        ThreadTest.flag.set(true);
        System.out.println("main:" + Thread.currentThread().getId());
    }

}



