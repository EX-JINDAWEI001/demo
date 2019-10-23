package com.example.demo.common.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultiPartConfig {
    @Bean
    public MultipartConfigElement multipartConfig() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件大小;
        factory.setMaxFileSize(DataSize.of(20480, DataUnit.KILOBYTES));
        // 设置单次请求的上传数据总大小;
        factory.setMaxRequestSize(DataSize.of(20480, DataUnit.KILOBYTES));
        return factory.createMultipartConfig();
    }
}
