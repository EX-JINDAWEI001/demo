package com.example.demo.point.proxy.cglib;

public class ProxyGame {

    public void play() {
        System.out.println("打篮球很厉害");
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        ProxyGame proxyGame = (ProxyGame) cglibProxy.newInstall(new ProxyGame());
        proxyGame.play();
    }

}