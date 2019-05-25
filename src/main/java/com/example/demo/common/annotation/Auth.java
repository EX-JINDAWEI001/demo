package com.example.demo.common.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
    String user() default "";
}
