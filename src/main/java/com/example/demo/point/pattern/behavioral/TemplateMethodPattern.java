package com.example.demo.point.pattern.behavioral;

// 模板方法模式(Template Method Pattern):
// 一句话: 父类确定步骤及其顺序, 具体步骤由子类实现, 整体模板已经架构好, 你只需要填充自己的特定内容就可以(如PPT模板);
// 关键词: (传统面向对象编程中的抽象方法子类覆盖)父类的抽象方法由子类实现;

// 角色:
// 抽象类(AbstractClass):      负责给出一个算法的轮廓和骨架, 它由一个模板方法和若干个基本方法构成;
// 具体子类(ConcreteClass):     实现抽象类中所定义的抽象方法;

public class TemplateMethodPattern {
    // 用法;
    public static void usage() {
        AbstractClass ac1 = new ConcreteClassA();
        ac1.TemplateMethod();
        AbstractClass ac2 = new ConcreteClassB();
        ac2.TemplateMethod();
    }

    // (AbstractClass)抽象类;
    static abstract class AbstractClass {
        // 父类的其他步骤;
        public void a() {
            System.out.println("AbstractClass.a().");
        }

        // 抽象方法;
        public abstract void b();
        public abstract void c();

        // 父类的其他步骤;
        public void d() {
            System.out.println("AbstractClass.d().");
        }

        // 模板方法;
        public void TemplateMethod()
        {
            this.a();
            this.b();
            this.c();
            this.d();
        }
    }

    // (ConcreteClass)具体子类;
    static class ConcreteClassA extends AbstractClass {
        // 抽象方法;
        @Override
        public void b() {
            System.out.println("ConcreteClassA.b().");
        }

        // 抽象方法;
        @Override
        public void c() {
            System.out.println("ConcreteClassA.c().");
        }
    }

    // (ConcreteClass)具体子类;
    static class ConcreteClassB extends AbstractClass {
        // 抽象方法;
        @Override
        public void b() {
            System.out.println("ConcreteClassB.b().");
        }

        // 抽象方法;
        @Override
        public void c() {
            System.out.println("ConcreteClassB.c().");
        }
    }
}
