package com.example.demo.point.pattern.structural;

// 代理模式(Proxy Pattern):
// 一句话: 想娶她, 先过她父母这关(婚前婚后的各种刁难闹心);
// 关键词: 控制访问;

// 角色:
// 抽象主题(Subject):       通过接口或抽象类, 声明真实主题和代理对象实现的业务方法;
// 真实主题(RealSubject):   实现抽象主题中的具体业务, 是代理对象所代表的真实对象, 是最终要引用的对象;
// 代理(Proxy):            提供与真实主题相同的接口, 其内部含有对真实主题的引用, 可以访问/控制/扩展真实主题的功能;

public class ProxyPattern {
    // 用法;
    public static void usage() {
        Proxy proxy = new Proxy();
        String response = proxy.request();
        System.out.println(response);
    }

    // (Subject)抽象主题;
    interface Subject {
        // 请求;
        String request();
    }

    // (RealSubject)真实主题;
    static class RealSubject implements Subject {
        // 请求;
        public String request() {
            return "RealSubject.a().";
        }
    }

    // (Proxy)代理;
    static class Proxy implements Subject {
        // 真实主题;
        private RealSubject realSubject = new RealSubject();

        // 请求;
        public String request() {
            // 校验授权(婚前刁难);
            if (!this.authorize()) {
                return null;
            }
            // 请求(终于娶到手);
            String response = this.realSubject.request();
            // 写入日志(婚后闹心);
            this.log();
            // 响应结果;
            return response;
        }

        // 校验授权(婚前刁难);
        private boolean authorize() {
            // ...;
            return true;
        }

        // 写入日志(婚后刁难);
        private void log() {
            System.out.println("耗时: 10ms.");
        }
    }
}
