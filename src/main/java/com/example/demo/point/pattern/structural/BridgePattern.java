package com.example.demo.point.pattern.structural;

// 桥接模式(Bridge Pattern):
// 一句话: 代替继承关系, 自由切换各个实现;
// 关键词: 切换(扩展版的策略模式);

// 角色:
// 实现化角色(Implementor):              定义实现化角色的接口, 供扩展抽象化角色调用;
// 具体实现化(ConcreteImplementor):      给出实现化角色接口的具体实现;
// 抽象化角色(Abstraction):              定义抽象类, 并包含1个对实现化对象的引用;
// 扩展抽象化角色(RefinedAbstraction):    是抽象化角色的子类, 实现父类中的业务方法, 并通过组合关系调用实现化角色中的业务方法;

public class BridgePattern {
    // 用法;
    public static void usage() {
        // 方式1: 通过构造函数桥接;
        Abstraction a1 = new RefinedAbstraction(new ConcreteImplementorA());
        a1.b();
        // 方式2: 通过字段桥接;
        Abstraction a2 = new RefinedAbstraction();
        a2.setImplementor(new ConcreteImplementorB());
        a2.b();
    }

    // (Implementor)实现化角色;
    interface Implementor {
        // 方法;
        void a();
    }

    // (ConcreteImplementor)具体实现化;
    static class ConcreteImplementorA implements Implementor {
        // 方法;
        @Override
        public void a() {
            System.out.println("ConcreteImplementorA.a()");
        }
    }

    // (ConcreteImplementor)具体实现化;
    static class ConcreteImplementorB implements Implementor {
        // 方法;
        @Override
        public void a() {
            System.out.println("ConcreteImplementorB.a()");
        }
    }

    // (Abstraction)抽象化角色, 这个类在有些场景可省略;
    static abstract class Abstraction {
        // 实现化角色;
        protected Implementor implementor;

        // 无参构造;
        protected Abstraction() {
        }

        // 有参构造;
        protected Abstraction(Implementor implementor) {
            this.implementor = implementor;
        }

        // 实现化角色;
        public void setImplementor(Implementor implementor) {
            this.implementor = implementor;
        }

        // 方法;
        public abstract void b();
    }

    // (RefinedAbstraction)扩展抽象化角色;
    static class RefinedAbstraction extends Abstraction {
        // 无参构造;
        public RefinedAbstraction() {
            super();
        }

        // 有参构造;
        public RefinedAbstraction(Implementor implementor) {
            super(implementor);
        }

        // 方法;
        @Override
        public void b() {
            super.implementor.a();
            this.c();
        }

        // 扩展功能;
        private void c() {
            System.out.println("RefinedAbstraction.c()");
        }
    }
}
