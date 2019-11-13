package com.example.demo.point.pattern.structural;

// 适配器模式:
// 一句话: 人家干不了这事, 你偏要人家硬上;
// 关键词: 硬转换;

// 角色:
// 源(Adaptee):       被适配者, 它是已经存在的现有实现, 只是不符合现有的目标要求, 而需要利用适配器包装;
// 目标(Target):       期待的接口;
// 适配器(Adapter):    通过继承/接口的各种组合方式, 把源转换为目标;

public class AdapterPattern {
    // 用法;
    public static void usage() {
        Target target = new Adapter();
        target.b();
    }

    // (Adaptee)源;
    static class Adaptee {
        // 源方法;
        public void a() {
            System.out.println("Adaptee.a()");
        }
    }

    // (Target)目标;
    interface Target {
        // 目标方法;
        void b();
    }

    // (Adapter)适配器;
    static class Adapter implements Target {
        // 目标方法;
        @Override
        public void b() {
            new Adaptee().a();
        }
    }
}
