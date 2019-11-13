package com.example.demo.point.pattern.structural;

// 享元模式(Flyweight Pattern):
// 一句话: 共享对象(池);
// 关键词: 共享;

// 角色:
// 抽象享元(Flyweight):             所有的具体享元类的基类, 非享元的外部状态以参数的形式通过方法传入;
// 具体享元(ConcreteFlyweight):     实现抽象享元角色中所规定的接口;
// 非享元(UnsharableFlyweight):     不可以共享的外部状态, 它以参数的形式注入具体享元的相关方法中;
// 享元工厂(FlyweightFactory):      负责创建和管理享元角色;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyweightPattern {
    // 用法;
    public static void usage() {
        // 黑先手;
        AbstractChess chess10 = FlyweightFactory.getChess("Black");
        chess10.put(new Position(1, 1,0));
        // 白放子;
        AbstractChess chess20 = FlyweightFactory.getChess("White");
        chess20.put(new Position(2, 2,0));
        // 黑放子;
        AbstractChess chess11 = FlyweightFactory.getChess("Black");
        chess11.put(new Position(3, 1,1));
        // 白放子;
        AbstractChess chess21 = FlyweightFactory.getChess("White");
        chess21.put(new Position(4, 2,1));
    }

    // (Flyweight)围棋的棋子;
    static abstract class AbstractChess {
        // 唯一标识(校验唯一);
        protected String seq;
        // 半径;
        protected float radius;
        // 填色;
        protected String color;

        // 有参构造;
        public AbstractChess(float radius, String color) {
            this.seq = UUID.randomUUID().toString();
            this.radius = radius;
            this.color = color;
        }

        // 绘制;
        public void draw() {
            System.out.println(String.format("Chess.draw(): %s, %s.", this.radius, this.color));
        }

        // 放子(非享元的外部状态以参数的形式通过方法传入);
        abstract void put(Position position);
    }

    // (ConcreteFlyweight)围棋的棋子;
    static class Chess extends AbstractChess {
        // 有参构造;
        public Chess(float radius, String color) {
            super(radius, color);
        }

        // 放子(非享元的外部状态以参数的形式通过方法传入);
        @Override
        public void put(Position position) {
            // 位置;
            System.out.println(String.format("Chess.put(): seq: %s, color: %s, idx: %s, x: %s, y: %s.", super.seq, super.color, position.idx, position.x, position.y));
            // 绘制;
            this.draw();
        }
    }

    // (UnsharableFlyweight)位置;
    static class Position {
        // 第?步;
        private int idx;
        // 坐标;
        private int x, y;

        // 有参构造;
        public Position(int idx, int x, int y) {
            this.idx = idx;
            this.x = x;
            this.y = y;
        }
    }

    // (FlyweightFactory)享元工厂;
    static class FlyweightFactory {
        // 共享池(两个对象: 黑棋与白棋);
        private static Map<String, AbstractChess> chesses = new HashMap<>();

        public static AbstractChess getChess(String color) {
            // 存在并发问题(真实应用场景中要加同步锁);
            if (!chesses.containsKey(color)) {
                chesses.put(color, new Chess(0.50f, color));
            }
            return chesses.get(color);
        }
    }
}
