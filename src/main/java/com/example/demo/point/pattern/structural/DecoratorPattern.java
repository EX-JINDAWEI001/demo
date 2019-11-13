package com.example.demo.point.pattern.structural;

// 装饰模式(Decorator Pattern);
// 一句话: 代替继承关系, 外部使用统一接口, 多层累加扩展功能
// 关键词: 多层扩展;

// 角色:
// 抽象构件(Component):          定义一个抽象接口以规范准备接收附加责任的对象;
// 具体构件(ConcreteComponent):  实现抽象构件, 通过装饰角色为其添加一些职责;
// 抽象装饰(Decorator):          继承抽象构件, 并包含具体构件的实例, 可以通过其子类扩展具体构件的功能;
// 具体装饰(ConcreteDecorator):  实现抽象装饰的相关方法, 并给具体构件对象添加附加的责任;

public class DecoratorPattern {
    // 用法;
    public static void usage() {
        // 先画线条, 再上色, 最后签名;
        Painting component = new Signature(new Color(new Line()));
        component.draw();
    }

    // (Component)画作;
    interface Painting {
        // 绘画;
        void draw();
    }

    // (ConcreteComponent)线条;
    static class Line implements Painting {
        // 绘画;
        @Override
        public void draw() {
            System.out.println("Line.draw().");
        }
    }

    // (Decorator)抽象装饰, 这个类在有些场景可省略, Color/Signature直接implements Painting;
    static abstract class Decorator implements Painting {
        // 抽象构件;
        private Painting component;

        // 无参构造;
        public Decorator(Painting component) {
            this.component = component;
        }

        // 绘画;
        @Override
        public void draw() {
            this.component.draw();
        }
    }

    // (ConcreteDecorator)上色;
    static class Color extends Decorator {
        // 有参构造;
        public Color(Painting component) {
            super(component);
        }

        @Override
        public void draw() {
            // 绘画;
            super.draw();
            // 上色(扩展功能);
            this.color();
        }

        // 上色(扩展功能);
        private void color() {
            System.out.println("Color.color().");
        }
    }

    // (ConcreteDecorator)签名;
    static class Signature extends Decorator {
        // 有参构造;
        public Signature(Painting component) {
            super(component);
        }

        @Override
        public void draw() {
            // 绘画;
            super.draw();
            // 签名(扩展功能);
            this.sign();
        }

        // 签名(扩展功能);
        private void sign() {
            System.out.println("Signature.sign().");
        }
    }
}
