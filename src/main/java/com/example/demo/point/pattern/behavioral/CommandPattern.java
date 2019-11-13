package com.example.demo.point.pattern.behavioral;

// 命令模式(Command Pattern):
// 一句话: 电视机遥控器(命令发送者)通过按遥控器的某按键(具体命令)遥控电视机(命令接收者);
// 关键词: 请求与执行解耦;

// 角色:
// 抽象命令类(Command):              声明执行命令的接口, 拥有执行命令的抽象方法execute();
// 具体命令角色(ConcreteCommand):    是抽象命令类的具体实现类, 它拥有接收者对象, 并通过调用接收者的功能来完成命令要执行的操作;
// 接收者(Receiver):                执行命令功能的相关操作, 是具体命令对象业务的真正实现者;
// 请求者(Invoker):                 是请求的发送者, 它通常拥有很多的命令对象, 并通过访问命令对象来执行相关请求, 它不直接访问接收者;

// 注:
// 请参考.NET版MKT项目的老框架(.NET Framework+DDD+CQRS), 里面有用到查询职责分离模式(CQRS), 职责部分就是基于命令模式;
// 通过.NET泛型与反射机制, 根据具体命令角色(ConcreteCommand)类型定位其对应的CommandHandler(接收者Receiver), 以反射动态调用;

public class CommandPattern {
    // 用法;
    public static void usage() {
        ICommand command = new ConcreteCommand(new Receiver());
        Invoker invoker = new Invoker(command);
        invoker.request();
    }

    // (Command)抽象命令类;
    interface ICommand {
        // 执行;
        void exec();
    }

    // (ConcreteCommand)具体命令角色;
    static class ConcreteCommand implements ICommand {
        private Receiver receiver;

        // 有参构造;
        public ConcreteCommand(Receiver receiver) {
            this.receiver = receiver;
        }

        // 执行;
        @Override
        public void exec() {
            receiver.a();
        }
    }

    // (Receiver)接收者, 在某些场景, 可以省略;
    static class Receiver {
        // 方法;
        public void a() {
            System.out.println("Receiver.a().");
        }
    }

    // (Invoker)请求者;
    static class Invoker {
        // 抽象命令类;
        private ICommand command;

        // 有参构造;
        public Invoker(ICommand command) {
            this.command = command;
        }

        // 请求;
        public void request() {
            this.command.exec();
        }
    }
}
