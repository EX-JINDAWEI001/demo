package com.example.demo.controller;

import com.example.demo.service.AsyncDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/async")
public class AsyncDemoController {

    @Autowired
    private AsyncDemoService asyncDemoService;

    @RequestMapping("/asyncDemo.do")
    public String asyncDemo(HttpServletRequest request, HttpServletResponse response) {
        try {
            asyncDemoService.asyncDemo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "AsyncTask is going on!!!";
    }

}
