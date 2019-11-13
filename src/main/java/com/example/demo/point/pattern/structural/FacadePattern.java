package com.example.demo.point.pattern.structural;

// 外观模式(Facade Pattern):
// 一句话: 集成多个子系统的功能;
// 关键词: 集成简化;

// 角色:
// 外观(Facade):         为多个子系统对外提供一个共同的接口;
// 子系统(SubSystem):    实现系统的部分功能, 客户可以通过外观角色访问它;

public class FacadePattern {
    // 用法;
    public static void usage() {
        new Facade().facade();
    }

    // (Facade)外观;
    static class Facade {
        // 字段;
        private SubSystemA subSystemA;
        private SubSystemB subSystemB;
        private SubSystemC subSystemC;

        // 无参构造;
        public Facade() {
            this.subSystemA = new SubSystemA();
            this.subSystemB = new SubSystemB();
            this.subSystemC = new SubSystemC();
        }

        // 方法;
        public void facade() {
            this.subSystemA.a();
            this.subSystemB.b();
            this.subSystemC.c();
        }
    }

    // (SubSystem)子系统;
    static class SubSystemA {
        // 方法;
        public void a() {
            System.out.println("SubSystemA.a().");
        }
    }

    // (SubSystem)子系统;
    static class SubSystemB {
        // 方法;
        public void b() {
            System.out.println("SubSystemB.b().");
        }
    }

    // (SubSystem)子系统;
    static class SubSystemC {
        // 方法;
        public void c() {
            System.out.println("SubSystemB.c().");
        }
    }
}
