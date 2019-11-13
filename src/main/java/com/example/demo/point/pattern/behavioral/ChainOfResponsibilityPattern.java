package com.example.demo.point.pattern.behavioral;

// 责任链模式(Chain Of Responsibility Pattern):
// 一句话: 多个处理者以链方式组织起来, 处理客户端的请求;
// 关键词: 1个事务拆分为多块, 以链式保证顺序;

// 角色:
// 抽象处理者(Handler):          定义一个处理请求的接口, 包含抽象处理方法和一个后继连接;
// 具体处理者(ConcreteHandler):  实现抽象处理者的处理方法, 判断能否处理本次请求, 如果可以处理请求则处理, 否则将该请求转给它的后继者;

// 注:
// 责任链模式有更好的替代方案, 具体可参考Apache Commons Chain:
// http://commons.apache.org/proper/commons-chain/index.html
// 快速用法:
// https://www.jianshu.com/p/a9b772bbe31a

public class ChainOfResponsibilityPattern {
    // 用法;
    public static void usage() {
        Handler root = new ConcreteHandlerA();
        Handler b = new ConcreteHandlerB();
        Handler c = new ConcreteHandlerC();
        root.setSuccessor(b);
        b.setSuccessor(c);
        root.z(5);
        root.z(15);
        root.z(25);
        root.z(35);
    }

    // (Handler)抽象处理者;
    static abstract class Handler {
        // 继任者;
        protected Handler successor;

        // 继任者;
        public void setSuccessor(Handler successor) {
            this.successor = successor;
        }

        // 处理请求;
        public abstract void z(int request);
    }

    // (ConcreteHandler)具体处理者;
    static class ConcreteHandlerA extends Handler {
        // 处理请求;
        @Override
        public void z(int request) {
            if (request >= 0 && request < 10) {
                System.out.println(String.format("ConcreteHandlerA.z(%s).", request));
            } else if (super.successor != null) {
                super.successor.z(request);
            }
        }
    }

    // (ConcreteHandler)具体处理者;
    static class ConcreteHandlerB extends Handler {
        // 处理请求;
        @Override
        public void z(int request) {
            if (request >= 10 && request < 20) {
                System.out.println(String.format("ConcreteHandlerB.z(%s).", request));
            } else if (super.successor != null) {
                super.successor.z(request);
            }
        }
    }

    // (ConcreteHandler)具体处理者;
    static class ConcreteHandlerC extends Handler {
        // 处理请求;
        @Override
        public void z(int request) {
            if (request >= 20 && request < 30) {
                System.out.println(String.format("ConcreteHandlerC.z(%s).", request));
            } else if (super.successor != null) {
                super.successor.z(request);
            }
        }
    }
}
