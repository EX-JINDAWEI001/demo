package com.example.demo.point.pattern.behavioral;

// 访问者模式;
// 一句话: 面对复杂元素的集合, 切换处理方式(具体访问者);
// 关键词: 切换具体访问者;

// 角色:
// 抽象访问者(Visitor):          定义一个访问具体元素的接口, 为每个具体元素类对应一个访问操作visit(), 该操作中的参数类型标识了被访问的具体元素;
// 具体访问者(ConcreteVisitor):  实现抽象访问者角色中声明的各个访问操作, 确定访问者访问一个元素时该做什么;
// 抽象元素(Element):           声明一个包含接受操作accept()的接口, 被接受的访问者对象作为accept()方法的参数;
// 具体元素(ConcreteElement):   实现抽象元素角色提供的accept()操作, 其方法体通常都是visitor.visit(this), 另外具体元素中可能还包含本身业务逻辑的相关操作;
// 对象结构(ObjectStructure):   是一个包含元素角色的容器, 提供让访问者对象遍历容器中的所有元素的方法, 通常由List/Set/Map等聚合类实现;

import java.util.ArrayList;
import java.util.List;

public class VisitorPattern {
    // 用法;
    public static void usage() {
        ObjectStructure o = new ObjectStructure();
        o.add(new ConcreteElementA());
        o.add(new ConcreteElementB());
        o.accept(new ConcreteVisitor1());
        // o.accept(new ConcreteVisitor2());
    }

    // (ObjectStructure)对象结构;
    static class ObjectStructure {
        // 元素集合;
        private List<Element> elements = new ArrayList<>();

        // 增加;
        public void add(Element element) {
            this.elements.add(element);
        }

        // 移除;
        public void remove(Element element) {
            this.elements.remove(element);
        }

        // 接受;
        public void accept(Visitor visitor) {
            this.elements.forEach(e -> {
                e.accept(visitor);
            });
        }
    }

    // (Visitor)抽象访问者;
    interface Visitor
    {
        // 访问A类型元素;
        void visitConcreteElementA(ConcreteElementA concreteElementA);
        // 访问B类型元素;
        void visitConcreteElementB(ConcreteElementB concreteElementB);
    }

    // (ConcreteVisitor)具体访问者;
    static class ConcreteVisitor1 implements Visitor {
        @Override
        public void visitConcreteElementA(ConcreteElementA concreteElementA) {
            System.out.println("ConcreteVisitor1.visitConcreteElementA().");
        }

        @Override
        public void visitConcreteElementB(ConcreteElementB concreteElementB) {
            System.out.println("ConcreteVisitor1.visitConcreteElementB().");
        }
    }

    // (ConcreteVisitor)具体访问者;
    static class ConcreteVisitor2 implements Visitor {
        @Override
        public void visitConcreteElementA(ConcreteElementA concreteElementA) {
            System.out.println("ConcreteVisitor2.visitConcreteElementA().");
        }

        @Override
        public void visitConcreteElementB(ConcreteElementB concreteElementB) {
            System.out.println("ConcreteVisitor2.visitConcreteElementB().");
        }
    }

    // (Element)抽象元素;
    interface Element {
        // 接受;
        void accept(Visitor visitor);
    }

    // (ConcreteElement)具体元素;
    static class ConcreteElementA implements Element {
        // 接受;
        @Override
        public void accept(Visitor visitor) {
            visitor.visitConcreteElementA(this);
        }

        // 方法;
        public void a() {
        }
    }

    // (ConcreteElement)具体元素;
    static class ConcreteElementB implements Element {
        // 接受;
        @Override
        public void accept(Visitor visitor) {
            visitor.visitConcreteElementB(this);
        }

        // 方法;
        public void b() {
        }
    }
}
