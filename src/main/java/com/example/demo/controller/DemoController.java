package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.aggregate.AggregateListenerImpl;
import com.example.demo.common.annotation.Auth;
import com.example.demo.common.annotation.Uid;
import com.example.demo.common.guava.GuavaDemo;
import com.example.demo.common.http.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/demo")
@RestController
public class DemoController extends AggregateListenerImpl {

    private Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Resource()
    private HttpClientUtil httpClientUtil;

    @Autowired
    private GuavaDemo guavaDemo;

    @Auth(user = "admin")
    @RequestMapping("/test1.do")
    public String test1(HttpServletRequest request, HttpServletResponse response, @Uid(isRequired = true) String uid) {
        logger.info("test1 input params:{}", JSON.toJSONString(request.getParameterMap()));
        return "HELLO:" + uid;
    }

    @RequestMapping("/test2.do")
    public String test2(HttpServletRequest request, HttpServletResponse response) {
        httpClientUtil.httpPostRequest("http://localhost:8800/async/asyncDemo.do", null);
        return httpClientUtil.httpGetRequest("http://localhost:8800/async/asyncDemo.do");
    }

    @RequestMapping("/test3.do")
    public Object test3(HttpServletRequest request, HttpServletResponse response) {
        return guavaDemo.fetchCache();
    }

    @RequestMapping("/getAppLoadingList.do")
    public Map<String, Object> getAppLoadingList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //路径
        System.out.println("contextPath:" + request.getContextPath());
        System.out.println("servletPath:" + request.getServletPath());
        System.out.println("pathInfo:" + request.getPathInfo());
        System.out.println("pathTranslated:" + request.getPathTranslated());
        System.out.println("realPath:" + request.getServletContext().getRealPath("/getAppLoadingList.do"));
        System.out.println("RequestURI:" + request.getRequestURI());
        System.out.println("RequestURL:" + request.getRequestURL());

        //参数
        System.out.println("ParameterValues:" + request.getParameterValues("appId"));
        System.out.println("ParameterMap:" + JSON.toJSONString(request.getParameterMap()));
        System.out.println("HeaderNames:" + JSON.toJSONString(request.getHeaderNames()));
//		System.out.println("Parts:" + request.getParts());

        //cookie
        System.out.println("cookies:" + request.getCookies());

        //语言环境
        System.out.println("locale:" + request.getLocale());
        System.out.println("locales:" + JSON.toJSONString(request.getLocales()));

        //servletContext
        ServletContext sc = request.getServletContext();
        sc.setAttribute("jdw", "jindawei");
        System.out.println("" + sc.getAttribute("jdw"));

        response.sendError(10, "唧唧复唧唧");
        //异步
        request.startAsync();
        AsyncContext ac = request.getAsyncContext();
        System.out.println(ac.hasOriginalRequestAndResponse());
        System.out.println(request.isAsyncSupported());
        System.out.println(request.isAsyncStarted());
        ac.setTimeout(1000);
        System.out.println(ac.getTimeout());
        ac.complete();
        String appId = request.getParameter("appId");
        Map map = new HashMap();
        map.put("jdw", appId);
        return map;
    }

}
