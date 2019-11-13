package com.example.demo.point.pattern.structural;

// 组合模式(Composite Pattern):
// 一句话: 对象集合的树形结构;
// 关键词: 树形;

// 抽象构件(Component):     为树枝构件声明公共接口;
// 树叶构件(Leaf):          组合中的叶节点对象;
// 树枝构件(Composite):     实现抽象构件声明的接口, 存储和管理子节点;

import java.util.ArrayList;
import java.util.List;

public class CompositePattern {
    // 用法;
    public static void usage() {
        Component root = new Composite("root");
        Component c11 = new Composite("c11");
        Component c12 = new Composite("c12");
        root.add(c11);
        root.add(c12);
        Component c11_21 = new Leaf("c11_21");
        Component c11_22 = new Leaf("c11_22");
        c11.add(c11_21);
        c11.add(c11_22);
        root.printChildrens();
        c11.printChildrens();
    }

    // (Component)抽象构件;
    static abstract class Component {
        // 名称;
        private String name;

        // 有参构造;
        public Component(String name) {
            this.name = name;
        }

        // 追加(子节点);
        public abstract void add(Component c);

        // 移除(子节点);
        public abstract void remove(Component c);

        // 获取(子节点);
        public abstract List<Component> getChildrens();

        // 打印(子节点);
        public abstract void printChildrens();
    }

    // (Leaf)树叶构件;
    static class Leaf extends Component {
        // 有参构造;
        public Leaf(String name) {
            super(name);
        }

        // 追加(子节点);
        @Override
        public void add(Component c) {
            // 没有子节点;
        }

        // 移除(子节点);
        @Override
        public void remove(Component c) {
            // 没有子节点;
        }

        // 获取(子节点);
        @Override
        public List<Component> getChildrens() {
            // 没有子节点;
            return null;
        }

        // 打印(子节点);
        @Override
        public void printChildrens() {
            // 没有子节点;
        }
    }

    // (Composite)树枝构件;
    static class Composite extends Component {
        // 子节点集合;
        private List<Component> childrens = new ArrayList<>();

        // 有参构造;
        public Composite(String name) {
            super(name);
        }

        // 追加(子节点);
        @Override
        public void add(Component c) {
            this.childrens.add(c);
        }

        // 移除(子节点);
        @Override
        public void remove(Component c) {
            this.childrens.remove(c);
        }

        // 获取(子节点);
        @Override
        public List<Component> getChildrens() {
            return this.childrens;
        }

        // 打印(子节点);
        @Override
        public void printChildrens() {
            this.childrens.forEach(c -> {
                System.out.println(c.name);
            });
        }
    }
}
