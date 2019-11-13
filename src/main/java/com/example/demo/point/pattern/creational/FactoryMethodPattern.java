package com.example.demo.point.pattern.creational;

// 工厂方法模式(Factory Method Pattern): 又称工厂模式/多态工厂模式/虚拟构造器模式, 通过定义工厂父类负责定义创建对象的公共接口, 而子类则负责生成具体的对象;
// 一句话: 对外暴露工厂父类/工厂接口;
// 关键词: 无;

// 角色:
// 具体工厂(ConcreteCreator):   描述具体工厂;
// 抽象工厂(Creator):           描述具体工厂的公共接口;
// 抽象产品(Product):           描述具体产品的公共接口;
// 具体产品(ConcreteProduct):   描述生产的具体产品;

public class FactoryMethodPattern {
    // 用法;
    public static void usage() {
        IFactory factory = null;
        // 正方形工厂;
        factory = new SquareFactory();
        factory.getShape().draw();
        // 圆形;
        factory = new CircleFactory();
        factory.getShape().draw();
    }

    // (ConcreteCreator)正方形工厂;
    static class SquareFactory implements IFactory{
        @Override
        public IShape getShape() {
            return new Square();
        }
    }

    // (ConcreteCreator)圆形工厂;
    static class CircleFactory implements IFactory{
        @Override
        public IShape getShape() {
            return new Circle();
        }
    }

    // (Creator)抽象工厂;
    interface IFactory {
        // 创建对象;
        IShape getShape();
    }

    // (Product)形状接口;
    interface IShape {
        // 绘画;
        void draw();
    }

    // (ConcreteProduct)正方形;
    static class Square implements IShape {
        @Override
        public void draw() {
            System.out.println("Square.draw().");
        }
    }

    // (ConcreteProduct)圆形;
    static class Circle implements IShape {
        @Override
        public void draw() {
            System.out.println("Circle.draw().");
        }
    }
}
