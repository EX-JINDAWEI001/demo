package com.example.demo.point.pattern.behavioral;

// 迭代器模式(Iterator Pattern):
// 一句话: 将存储元素与遍历元素的具体实现解耦;
// 关键词: 存储与遍历集合元素;

// 角色:
// 抽象聚合(Aggregate):         定义存储聚合对象, 以及创建迭代器对象的接口, 通常包含add()/remove()等方法;
// 具体聚合(ConcreteAggregate): 实现抽象聚合类, 返回一个具体迭代器的实例;
// 抽象迭代器(Iterator):         定义访问和遍历聚合元素的接口, 通常包含first()/next()/hasNext()等方法;
// 具体迭代器(Concretelterator): 实现抽象迭代器接口中所定义的方法, 完成对聚合对象的遍历, 记录遍历的当前位置;

import java.util.Arrays;

public class IteratorPattern {
    // 用法;
    public static void usage() {
        Aggregate a = new ConcreteAggregate();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        Iterator iterator = new Concretelterator(a);
        while (iterator.hasNext()) {
            Object o = iterator.next();
            System.out.println(o);
        }
    }

    // (Aggregate)抽象聚合;
    static abstract class Aggregate {
        // 元素数组;
        protected Object[] elements = {};
        // 元素数量;
        protected int size = 0;

        // 添加;
        public abstract void add(Object e);

        // 删除;
        public abstract void remove(int e);
    }

    // (ConcreteAggregate)具体聚合;
    static class ConcreteAggregate extends Aggregate {
        // 添加;
        public void add(Object element) {
            // 拷贝元素;
            super.elements = Arrays.copyOf(super.elements, super.size + 1);
            // 最后元素;
            super.elements[super.size++] = element;
        }

        // 删除;
        public void remove(int index) {
            // 校验边界;
            if (index < 0 || index >= super.size) {
                return;
            }
            // 元素移位;
            int len = super.size - (index + 1);
            if (len > 0) {
                System.arraycopy(super.elements, index + 1, super.elements, index, len);
            }
            // 元素数量;
            super.elements[--super.size] = null;
        }
    }

    // (Iterator)抽象迭代器;
    interface Iterator {
        // 首个元素;
        Object first();

        // 下个元素;
        Object next();

        // 是否有下个元素?
        boolean hasNext();
    }

    // (Concretelterator)具体迭代器;
    static class Concretelterator implements Iterator {
        // 抽象聚合;
        private Aggregate aggregate;
        // 当前下标;
        private int index = -1;

        // 有参构造;
        public Concretelterator(Aggregate aggregate) {
            this.aggregate = aggregate;
        }

        // 首个元素;
        public Object first() {
            if (this.aggregate.size > 0) {
                this.index = 0;
                return this.aggregate.elements[this.index];
            }
            return null;
        }

        // 下个元素;
        public Object next() {
            if (this.hasNext()) {
                return this.aggregate.elements[++this.index];
            }
            return null;
        }

        // 是否有下个元素?
        public boolean hasNext() {
            return this.index < this.aggregate.size - 1;
        }
    }
}
