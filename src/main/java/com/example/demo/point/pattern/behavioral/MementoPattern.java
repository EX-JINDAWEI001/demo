package com.example.demo.point.pattern.behavioral;

// 备忘录模式(Memento Pattern):
// 一句话: 记录一个对象(Originator)的内部状态, 当用户后悔时能撤销当前操作, 使数据恢复到它原先的状态;
// 关键词: 撤销/后退Ctrl+Z/回滚/备份/悔棋;

// 角色:
// 备忘录(Memento):        负责存储发起人的内部状态, 在需要的时候提供这些内部状态给发起人;
// 管理者(Caretaker):      对备忘录进行管理, 提供保存与获取备忘录的功能, 但其不能对备忘录的内容进行访问与修改;
// 发起人(Originator):     记录当前时刻的内部状态信息, 提供创建备忘录和恢复备忘录数据的功能, 实现其他业务功能, 它可以访问备忘录里的所有信息;

// 注:
// 示例中管理者(Caretaker)仅备忘1次, 如果要作无限次回退(Ctrl+Z), 可使用数组;

public class MementoPattern {
    // 用法;
    public static void usage() {
        Caretaker store = new Caretaker();
        Originator domain = new Originator();
        domain.do1();
        System.out.println(domain.mementoCopy.toString());
        store.update(domain.backup());
        domain.do2();
        System.out.println(domain.mementoCopy.toString());
        domain.restore(store.get());
        System.out.println(domain.mementoCopy.toString());
    }

    // (Memento)备忘录;
    static class Memento {
        // 备忘字段;
        protected int a;
        protected String b;

        // 有参构造;
        public Memento(int a, String b) {
            this.a = a;
            this.b = b;
        }

        // 打印;
        @Override
        public String toString() {
            return String.format("a: %s, b: %s.", this.a, this.b);
        }
    }

    // (Caretaker)管理者;
    static class Caretaker {
        // 备忘录(备忘1次);
        private Memento memento = new Memento(0,null);
        // 备忘无限次;
        // private Memento[] mementoes;

        // 加载;
        public Memento get() {
            return this.memento;
        }

        // 更新;
        public void update(Memento memento) {
            this.memento = memento;
        }
    }

    // (Originator)发起人;
    static class Originator {
        // 备忘录(副本);
        private Memento mementoCopy = new Memento(0, null);

        // 备忘录(副本);
        public Memento getMemento() {
            return this.mementoCopy;
        }

        // 业务;
        public void do1() {
            this.mementoCopy.a++;
            this.mementoCopy.b = "Originator.do1()";
        }

        // 业务;
        public void do2() {
            this.mementoCopy.a = 1000;
            this.mementoCopy.b = "Originator.do2()";
        }

        // 创建备忘录(备份);
        public Memento backup() {
            // 拷贝副本(不可引用赋值);
            return new Memento(this.mementoCopy.a, this.mementoCopy.b);
        }

        // 恢复备忘录(回滚);
        public void restore(Memento m) {
            this.mementoCopy.a = m.a;
            this.mementoCopy.b = m.b;
        }
    }
}
