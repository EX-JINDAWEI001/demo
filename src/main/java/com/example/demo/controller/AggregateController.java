package com.example.demo.controller;

import com.example.demo.service.impl.AggregateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AggregateController {

    @Autowired
    private AggregateServiceImpl aggregateService;

    @RequestMapping("/aggregate.do")
    public Map<String, Map<String, Object>> aggregate(HttpServletRequest request, HttpServletResponse response) {
        return aggregateService.aggregate(request, response);
    }

}
