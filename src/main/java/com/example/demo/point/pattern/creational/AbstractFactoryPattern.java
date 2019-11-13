package com.example.demo.point.pattern.creational;

// 抽象工厂模式(Abstract Factory Pattern):
// 一句话: 工厂的工厂(超级工厂);
// 关键词: 两层工厂;

// 角色:
// 具体工厂(ConcreteFactory):  主要是实现抽象工厂中的多个抽象方法, 完成具体产品的创建;
// 抽象工厂(AbstractFactory):  提供了创建产品的接口, 它包含多个创建产品的方法newProduct(), 可以创建多个不同等级的产品;
// 抽象产品(Product):          定义产品的规范, 描述了产品的主要特性和功能, 抽象工厂模式有多个抽象产品;
// 具体产品(ConcreteProduct):  实现了抽象产品角色所定义的接口, 由具体工厂来创建, 它同具体工厂之间是多对一的关系;

public class AbstractFactoryPattern {
    // 用法;
    public static void usage() {
        // 相关定义;
        AbstractFactory factory = null;
        IShape shape = null;
        IColor color = null;
        // 形状工厂;
        factory = new ShapeFactory();
        shape = factory.getShape(IShape.SQUARE);
        shape.draw();
        shape = factory.getShape(IShape.CIRCLE);
        shape.draw();
        // 颜色工厂;
        factory = new ColorFactory();
        color = factory.getColor(IColor.RED);
        color.fill();
        color = factory.getColor(IColor.BLUE);
        color.fill();
    }

    // (ConcreteFactory)形状工厂;
    static class ShapeFactory extends AbstractFactory {
        // 创建形状;
        @Override
        public IShape getShape(int shapeType){
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

        // 创建颜色;
        @Override
        public IColor getColor(int colorType) {
            return null;
        }
    }

    // (ConcreteFactory)颜色工厂;
    static class ColorFactory extends AbstractFactory {
        // 创建形状;
        @Override
        public IShape getShape(int shapeType){
            return null;
        }

        // 创建颜色;
        @Override
        public IColor getColor(int colorType) {
            switch (colorType) {
                // 长方形;
                case IColor.RED:
                    return new Red();
                // 圆形;
                case IColor.BLUE:
                    return new Blue();
                // 其他;
                default:
                    return null;
            }
        }
    }

    // (AbstractFactory)抽象工厂;
    static abstract class AbstractFactory {
        // 创建形状;
        public abstract IShape getShape(int shapeType);
        // 创建颜色;
        public abstract IColor getColor(int colorType);
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

    // (Product)颜色接口;
    interface IColor {
        // 红色;
        int RED = 1;
        // 蓝色;
        int BLUE = 2;

        // 填充;
        void fill();
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

    // (ConcreteProduct)红色;
    static class Red implements IColor {
        @Override
        public void fill() {
            System.out.println("Red.fill().");
        }
    }

    // (ConcreteProduct)蓝色;
    static class Blue implements IColor {
        @Override
        public void fill() {
            System.out.println("Blue.fill().");
        }
    }
}
