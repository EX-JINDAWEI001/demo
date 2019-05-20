package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class DemoController {

    private Logger logger = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping("/test1.do")
    public String test1(HttpServletRequest request, HttpServletResponse response) {
        logger.info("test1 input params:{}", JSON.toJSONString(request.getParameterMap()));
        return "HELLO WORLD !!!";
    }

    @RequestMapping("/test2.do")
    public String test2(HttpServletRequest request, HttpServletResponse response) {
        return "TEST GITHUB !";
    }

}
