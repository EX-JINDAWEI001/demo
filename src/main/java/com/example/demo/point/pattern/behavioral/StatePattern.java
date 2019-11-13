package com.example.demo.point.pattern.behavioral;

// 状态模式(State Pattern):
// 一句话: 重复调用Context对象同样的方法, 具体状态的状态发生转移, 则调用结果将不同;
// 关键词: 状态转移;

// 角色:
// 环境(Context):             也称为上下文, 它定义客户感兴趣的接口, 维护一个当前状态, 并将与状态相关的操作委托给当前状态对象来处理;
// 抽象状态(State):            定义一个接口, 用以封装环境对象中的特定状态所对应的行为;
// 具体状态(ConcreteState):    实现抽象状态所对应的行为;

public class StatePattern {
    // 用法;
    public static void usage() {
        Context context = new Context(new ConcreteStateA());
        context.z();
        context.z();
        context.z();
        context.z();
    }

    // (Context)环境;
    static class Context {
        // 抽象状态;
        private State state;

        // 有参构造;
        public Context(State state) {
            this.state = state;
        }

        // 方法;
        public void z() {
            this.state.handle(this);
        }
    }

    // (State)抽象状态;
    interface State {
        // 方法;
        void handle(Context context);
    }

    // (ConcreteState)具体状态;
    static class ConcreteStateA implements State {
        // 方法;
        public void handle(Context context) {
            System.out.println("ConcreteStateA.handle(context).");
            context.state = new ConcreteStateB();
        }
    }

    // (ConcreteState)具体状态;
    static class ConcreteStateB implements State {
        // 方法;
        public void handle(Context context) {
            System.out.println("ConcreteStateB.handle(context).");
            context.state = new ConcreteStateC();
        }
    }

    // (ConcreteState)具体状态;
    static class ConcreteStateC implements State {
        // 方法;
        public void handle(Context context) {
            System.out.println("ConcreteStateC.handle(context).");
            context.state = new ConcreteStateA();
        }
    }
}
