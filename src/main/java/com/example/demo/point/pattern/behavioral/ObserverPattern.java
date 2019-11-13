package com.example.demo.point.pattern.behavioral;

// 观察者模式(Observer Pattern):
// 一句话: 定义对象间的一种一对多的依赖关系, 当一个对象的状态发生改变时, 所有依赖于它的对象都得到通知并被自动更新;
// 关键词: 根据状态信号通知集合全部遍历执行, List<T>.forEach(o -> {o.update(state);});

// 角色:
// 抽象观察者(Observer):            它是一个抽象类或接口, 它包含了一个更新自己的抽象方法, 当接到具体主题的更改通知时被调用;
// 具体观察者(ConcreteObserver):    实现抽象观察者中定义的抽象方法, 以便在得到目标的更改通知时更新自身的状态;
// 抽象主题(Subject):               也叫抽象目标类, 它提供一个用于保存观察者对象的聚集类, 和增加/删除观察者对象的方法, 以及通知所有观察者的抽象方法;
// 具体主题(ConcreteSubject):       也叫具体目标类, 它实现抽象目标中的通知方法, 当具体主题的内部状态发生改变时, 通知所有注册过的观察者对象;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ObserverPattern {
    // 用法;
    public static void usage() {
        // 方式1(自己写);
        Subject subject = new ConcreteSubject();
        subject.add(new ConcreteObserverA());
        subject.add(new ConcreteObserverB());
        subject.setState("OPEN");
        subject.setState("CLOSE");
        // 方式2(使用JDK);
        CS cs = new CS();
        cs.addObserver(new COA());
        cs.addObserver(new COB());
        cs.setState("OPEN");
        cs.setState("CLOSE");
    }

    // 方式1(自己写);
    // (Observer)抽象观察者;
    interface Observer {
        // 更新;
        void update(String state);
    }

    // (ConcreteObserver)具体观察者;
    static class ConcreteObserverA implements Observer {
        // 更新;
        public void update(String state) {
            System.out.println(String.format("ConcreteObserverA.update(): %s.", state));
        }
    }

    // (ConcreteObserver)具体观察者;
    static class ConcreteObserverB implements Observer {
        // 更新;
        public void update(String state) {
            System.out.println(String.format("ConcreteObserverB.update(): %s.", state));
        }
    }

    // (Subject)抽象主题(有些场景可省略);
    static abstract class Subject {
        // 观察者集合;
        protected List<Observer> observers = new ArrayList<>();
        // 内部状态;
        private String state;

        // 内部状态;
        public void setState(String state) {
            // 改变内部状态;
            this.state = state;
            // 通知观察者;
            this.notice();
        }

        // 增加观察者;
        public void add(Observer o) {
            observers.add(o);
        }

        // 删除观察者;
        public void remove(Observer o) {
            observers.remove(o);
        }

        // 通知观察者;
        protected abstract void notice();
    }

    // (ConcreteSubject)具体主题;
    static class ConcreteSubject extends Subject {
        // 通知观察者;
        protected void notice() {
            super.observers.forEach(o -> {
                o.update(super.state);
            });
        }
    }

    // 方式2(使用JDK);
    // 使用JDK中的java.util.Observable类和java.util.Observer接口, 实现观察者模式;
    // (ConcreteObserver)具体观察者;
    static class COA implements java.util.Observer {
        // 更新;
        public void update(Observable o, Object arg) {
            System.out.println(String.format("COA.update(): %s.", arg));
        }
    }

    // (ConcreteObserver)具体观察者;
    static class COB implements java.util.Observer {
        // 更新;
        public void update(Observable o, Object arg) {
            System.out.println(String.format("COB.update(): %s.", arg));
        }
    }

    // (ConcreteSubject)具体主题;
    static class CS extends Observable {
        // 观察者集合;
        private String state;

        // 内部状态;
        public void setState(String state) {
            // 改变内部状态;
            this.state = state;
            // 标识内部状态已改变;
            super.setChanged();
            // 通知观察者(注意: 倒序通知);
            super.notifyObservers(state);
        }
    }
}
