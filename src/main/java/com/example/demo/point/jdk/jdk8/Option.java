package com.example.demo.point.jdk.jdk8;

import java.util.Optional;

public class Option {
    public static void main(String[] args) {
        // empty 构建空的optional对象
        Optional<String> empty = Optional.empty();

        String user = null;
        // ofNull 构造optional对象，内部user如果为空，就构建空的optional对象
        Optional<String> userOptionalOfNull = Optional.ofNullable(user);

        // JDK8写法
        if (userOptionalOfNull.isPresent()) {
            String v1 = userOptionalOfNull.get();
        }
        String v2 = userOptionalOfNull.orElse("user is empty!!!");
        String v3 = userOptionalOfNull.orElseGet(() ->"user is empty!!!");

        // JDK10写法
        String v4 = userOptionalOfNull.orElseThrow();


        // of 构造为null的optional 对象时会报nullPointerException
        Optional<String> userOptional = Optional.of(user);

        Optional<Object> objectOptional = Optional.of(null);
        System.out.println(objectOptional);
    }

}
