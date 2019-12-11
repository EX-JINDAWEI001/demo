package com.example.demo.point.proxy.asm;

public class MyMain {
    // 类中有 两个参数 五个方法
    int i = 1;
    int j = 2;

    public void t01() {

    }

    public void t02(String str) {

    }

    public String t03() {
        System.out.println(123);
        return "hello";
    }

    public String t04(String str) {
        return str;
    }

    public int t05(int i) {
        return i;
    }
}
