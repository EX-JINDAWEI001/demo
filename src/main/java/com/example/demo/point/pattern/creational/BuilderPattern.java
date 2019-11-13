package com.example.demo.point.pattern.creational;

// 建造者模式(Builder Pattern):
// 一句话: 将部件和其组装过程分开, 一步一步创建1个复杂的对象;
// 关键词: 多步组装;

// 角色:
// 产品角色(Product):            1个具体的产品对象;
// 抽象建造者(Builder):          创建1个Product对象的各个部件指定的抽象接口;
// 具体建造者(ConcreteBuilder):  实现抽象接口, 构建和装配各个部件;
// 指挥者(Director):            构建1个使用Builder接口的对象, 主要有两个作用: 隔离客户与对象的生产过程, 负责控制产品对象的生产过程;

import java.util.ArrayList;
import java.util.List;

public class BuilderPattern {
    // 用法;
    public static void usage() {
        // 抽象建造者;
        Builder builder = new ConcreteBuilder1();
        // 装配产品;
        new Director().Construct(builder);
        // 最终产品;
        Product product = builder.build();
        product.print();
    }

    // (Product)产品角色;
    static class Product {
        // 部件;
        private List<String> parts = new ArrayList<>();

        // 构建部件;
        public void add(String part) {
            this.parts.add(part);
        }

        // 打印;
        public void print() {
            this.parts.forEach(System.out::println);
        }
    }

    // (Builder)抽象建造者;
    interface Builder {
        // 构建部件;
        void buildPartA();

        // 构建部件;
        void buildPartB();

        // 最终产品;
        Product build();
    }

    // (ConcreteBuilder)具体建造者;
    static class ConcreteBuilder1 implements Builder {
        // 产品;
        private Product product = new Product();

        // 构建部件;
        @Override
        public void buildPartA() {
            this.product.add("ConcreteBuilder1.buildPartA().");
        }

        // 构建部件;
        @Override
        public void buildPartB() {
            this.product.add("ConcreteBuilder1.buildPartB().");
        }

        // 最终产品;
        @Override
        public Product build() {
            return this.product;
        }
    }

    // (ConcreteBuilder)具体建造者;
    static class ConcreteBuilder2 implements Builder {
        // 产品;
        private Product product = new Product();

        // 构建部件;
        @Override
        public void buildPartA() {
            this.product.add("ConcreteBuilder2.buildPartA().");
        }

        // 构建部件;
        @Override
        public void buildPartB() {
            this.product.add("ConcreteBuilder2.buildPartB().");
        }

        // 最终产品;
        @Override
        public Product build() {
            return this.product;
        }
    }

    // (Director)指挥者;
    static class Director{
        // 装配各个部件;
        public void Construct(Builder builder)
        {
            builder.buildPartA();
            builder.buildPartB();
        }
    }
}
