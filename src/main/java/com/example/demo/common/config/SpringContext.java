package com.example.demo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

//@ImportResource等价于<import resource="spring-context.xml" />
@ImportResource(locations = {"classpath:spring-context.xml"})
@Configuration
public class SpringContext {}


