package com.example.demo.point.proxy.cglib;

/**
 * @author 黄豪强
 * @create 2019/7/24 8:51
 */
public class ProxyGame {

    public void play() {
        System.out.println("打篮球很厉害");
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        ProxyGame playGame = (ProxyGame) cglibProxy.newInstall(new ProxyGame());
        playGame.play();
    }
}