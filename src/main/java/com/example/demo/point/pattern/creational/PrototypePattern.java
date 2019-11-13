package com.example.demo.point.pattern.creational;

// 原型模式(Prototype Pattern):
// 一句话: 自我克隆创建对象, 性能快;
// 关键词: 克隆;

// 角色:
// 原型(Prototype):            声明1个克隆自身的接口;
// 具体原型(ConcretePrototype): 实现1个克隆自身的实现;

// 原型模式中的克隆分为"浅克隆"和"深克隆":
// 浅克隆: 复制基本类型的字段, 引用类型的字段只复制引用(内存地址), 不复制对象;
// 深克隆: 全新的对象;

public class PrototypePattern {
    // 用法;
    public static void usage() {
        // 写法1;
        Prototype p11 = new ConcretePrototype1();
        Prototype p12 = p11.shallowClone();
        // 写法2;
        ConcretePrototype2 p21 = new ConcretePrototype2();
        ConcretePrototype2 p22 = p21.shallowClone();
    }

    // 写法1;
    // (Prototype)原型;
    static abstract class Prototype {
        // 字段;
        // ...;

        // 浅克隆;
        public abstract Prototype shallowClone();
    }

    // (ConcretePrototype)具体原型;
    static class ConcretePrototype1 extends Prototype implements Cloneable {
        // 浅克隆;
        @Override
        public Prototype shallowClone() {
            try {
                return (Prototype) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    // 写法2;
    // (ConcretePrototype)具体原型;
    static class ConcretePrototype2 implements Cloneable {
        // 字段;
        // ...;

        // 浅克隆;
        public ConcretePrototype2 shallowClone() {
            try {
                return (ConcretePrototype2) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}
