package com.example.demo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"spring-context.xml"})
public class SpringContext {
}