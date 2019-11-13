package com.example.demo.point.pattern.behavioral;

// 中介者模式(Mediator Pattern):
// 一句话: 一系列的对象需要互相调用, 则通过中介对象转发, 从而使其解耦;
// 关键词: 封装交互, "网状结构"改为"星形结构";

// 角色:
// 抽象中介者(Mediator):             它是中介者的接口, 在里面定义各个同事之间交互需要的方法, 可以是公共的通讯方法, 也可以是小范围的交互方法;
// 具体中介者(ConcreteMediator):     实现中介者接口, 定义一个集合来管理同事对象, 并负责具体的协调各同事对象的交互关系;
// 抽象同事类(Colleague):            定义同事类的接口, 持有中介者对象, 提供同事对象交互的抽象方法, 实现所有相互影响的同事类的公共功能;
// 具体同事类(ConcreteColleague):    是抽象同事类的实现者, 当需要与其他同事对象交互时, 由中介者对象负责后续的交互;

public class MediatorPattern {
    // 用法;
    public static void usage() {
        // 场景很难假设, 目前还没找到更好的表达方式, 后续补上;
        // ...;
    }
}
