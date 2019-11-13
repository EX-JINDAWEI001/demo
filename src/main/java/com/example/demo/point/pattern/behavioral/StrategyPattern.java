package com.example.demo.point.pattern.behavioral;

// 策略模式(Strategy Pattern):
// 一句话: 封装一组可以互相替代的算法族, 并且可以根据需要动态地去替换Context使用的算法;
// 关键词: 切换(简化版的桥接模式);

// 角色:
// 环境(Context):              持有一个策略类的引用, 最终给客户端调用;
// 抽象策略(Strategy):          定义一个公共接口, 各种不同的算法以不同的方式实现这个接口, 环境角色使用这个接口调用不同的算法, 一般使用接口或抽象类实现;
// 具体策略(ConcreteStrategy):  实现抽象策略定义的接口, 提供具体的算法实现;

public class StrategyPattern {
    // 用法;
    public static void usage() {
        Context context = null;
        // 方式1: 通过构造函数切换策略;
        context = new Context(new ConcreteStrategyA());
        context.z();
        context = new Context(new ConcreteStrategyB());
        context.z();
        // 方式2: 通过字段切换策略;
        context = new Context();
        context.setStrategy(new ConcreteStrategyA());
        context.z();
        context.setStrategy(new ConcreteStrategyB());
        context.z();
    }

    static class Context {
        // 抽象策略;
        private Strategy strategy;

        // 无参构造;
        public Context() {
        }

        // 有参构造;
        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        // 抽象策略;
        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }

        // 方法;
        public void z() {
            this.strategy.a();
        }
    }

    // (Strategy)抽象策略;
    interface Strategy {
        // 方法;
        void a();
    }

    // (ConcreteStrategy)具体策略;
    static class ConcreteStrategyA implements Strategy {
        // 方法;
        @Override
        public void a() {
            System.out.println("ConcreteStrategyA.a().");
        }
    }

    // (ConcreteStrategy)具体策略;
    static class ConcreteStrategyB implements Strategy {
        // 方法;
        @Override
        public void a() {
            System.out.println("ConcreteStrategyB.a().");
        }
    }
}
