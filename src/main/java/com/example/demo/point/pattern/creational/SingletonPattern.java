package com.example.demo.point.pattern.creational;

// 单例模式(Singleton Pattern):
// 一句话: 自我创建并保证JVM全局唯一实例;
// 关键词: 全局唯一;

public class SingletonPattern {
    // 用法;
    public static void usage() {
        // √饿汉式(类装载时实例化);
        Singleton1.getInstance().print();
        // 懒汉式(严格意义上不是单例模式, 仅作为错误反例);
        Singleton2.getInstance().print();
        // 懒汉式(多线程并发时性能较差);
        Singleton3.getInstance().print();
        // √双检锁/双重校验锁(DCL, Double-Checked Locking, 多线程并发时性能较高);
        Singleton4.getInstance().print();
        // √静态内部类(对静态域使用延迟初始化);
        Singleton5.getInstance().print();
        // √枚举;
        Singleton6.INSTANCE.print();
    }

    // 饿汉式(类装载时实例化);
    // 线程安全, 非懒加载(Lazy Loading);
    static class Singleton1 {
        // 唯一实例;
        private static Singleton1 instance = new Singleton1();

        // 无参构造(屏蔽);
        private Singleton1() {
        }

        // 获取实例;
        public static Singleton1 getInstance() {
            return instance;
        }

        // 示例方法;
        public void print() {
            System.out.println("Singleton1.print().");
        }
    }

    // 懒汉式(严格意义上不是单例模式, 仅作为错误反例);
    // 线程不安全, 懒加载(Lazy Loading);
    static class Singleton2 {
        // 唯一实例;
        private static Singleton2 instance;

        // 无参构造(屏蔽);
        private Singleton2() {
        }

        // 获取实例;
        public static Singleton2 getInstance() {
            // 线程不安全(没有加同步锁);
            if (instance == null) {
                instance = new Singleton2();
            }
            return instance;
        }

        // 示例方法;
        public void print() {
            System.out.println("Singleton2.print().");
        }
    }

    // 懒汉式(多线程并发时性能较差);
    // 线程安全, 懒加载(Lazy Loading);
    static class Singleton3 {
        // 唯一实例;
        private static Singleton3 instance;

        // 无参构造(屏蔽);
        private Singleton3() {
        }

        // 获取实例;
        // 同步锁synchronized加在method上, 粒度较大, 有多线程并发的性能问题;
        public static synchronized Singleton3 getInstance() {
            if (instance == null) {
                instance = new Singleton3();
            }
            return instance;
        }

        // 示例方法;
        public void print() {
            System.out.println("Singleton3.print().");
        }
    }

    // 双检锁/双重校验锁(DCL, Double-Checked Locking, 多线程并发时性能较高);
    // 线程安全, 懒加载(Lazy Loading);
    static class Singleton4 {
        // 唯一实例;
        private static volatile Singleton4 instance;
        // 特殊的锁变量;
        private static byte[] lock = new byte[0];

        // 无参构造(屏蔽);
        private Singleton4 (){}

        // 获取实例;
        public static Singleton4 getInstance() {
            // 第1次校验;
            if (instance == null) {
                // 同步锁synchronized加载代码区块;
                synchronized (lock) {
                    // 第2次校验;
                    if (instance == null) {
                        instance = new Singleton4();
                    }
                }
            }
            return instance;
        }

        // 示例方法;
        public void print() {
            System.out.println("Singleton4.print().");
        }
    }

    // 静态内部类(对静态域使用延迟初始化);
    // 线程安全, 懒加载(Lazy Loading);
    static class Singleton5 {
        // 类装载时实例化;
        private static class SingletonHolder {
            private static Singleton5 instance = new Singleton5();
        }

        // 无参构造(屏蔽);
        private Singleton5 (){}

        // 获取实例;
        public static Singleton5 getInstance() {
            return SingletonHolder.instance;
        }

        // 示例方法;
        public void print() {
            System.out.println("Singleton5.print().");
        }
    }

    // 枚举;
    // 线程安全, 非懒加载(Lazy Loading);
    public enum Singleton6{
        // 唯一实例;
        INSTANCE;

        // 示例方法;
        public void print() {
            System.out.println("Singleton6.print().");
        }
    }
}
