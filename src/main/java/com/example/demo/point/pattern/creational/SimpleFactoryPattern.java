package com.example.demo.point.pattern.creational;

// 简单工厂模式(Simple Factory Pattern): 又叫做静态工厂方法模式(Static Factory Method), 不属于23种GOF设计模式之一, 工厂决定创建出哪一种产品类的实例, 是工厂模式家族中最简单实用的模式;
// 一句话: 按参数值对应创建对象;
// 关键词: 无;

// 角色:
// 工厂(Creator/Factory):     被外界调用, 根据传入不同参数从而创建不同具体产品类的实例;
// 抽象产品(Product):          具体产品的父类, 描述产品的公共接口;
// 具体产品(ConcreteProduct):  抽象产品的子类, 描述生产的具体产品;

public class SimpleFactoryPattern {
    // 用法;
    public static void usage() {
        IShape shape = null;
        // 正方形;
        shape = factory.getShape(IShape.SQUARE);
        shape.draw();
        // 圆形;
        shape = factory.getShape(IShape.CIRCLE);
        shape.draw();
    }

    // (Creator/Factory)工厂;
    static class factory {
        // 创建对象;
        public static IShape getShape(int shapeType) {
            switch (shapeType) {
                // 正方形;
                case IShape.SQUARE:
                    return new Square();
                // 圆形;
                case IShape.CIRCLE:
                    return new Circle();
                // 其他;
                default:
                    return null;
            }
        }
    }

    // (Product)形状接口;
    interface IShape {
        // 正方形;
        int SQUARE = 1;
        // 圆形;
        int CIRCLE = 2;

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
