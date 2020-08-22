package com.example.demo.service.impl;

import com.example.demo.common.aggregate.AggregateHandler;
import com.example.demo.common.aggregate.AggregateUtil;
import com.example.demo.common.aggregate.InvokeThread;
import com.example.demo.common.system.SimpleServiceInit;
import com.example.demo.common.vo.AggregateDTO;
import com.example.demo.common.utils.SpringBeansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AggregateServiceImpl implements SimpleServiceInit {

    private static final Logger logger = LoggerFactory.getLogger(AggregateServiceImpl.class);

    private Map<String, AggregateDTO> urlMethodMap = new HashMap<>();

    private ExecutorService pool;

    /**
     * 此处可根据业务需求进行配置
     */
    private Set<String> methods = new HashSet<>(Arrays.asList("/demo/test1.do", "/demo/test2.do", "/demo/test3.do"));

    @Override
    public void init() {
        pool = Executors.newFixedThreadPool(4);
        initUrlMappingMethods();
    }

    /**
     * 将目标接口的url和method信息加载到urlMethodMap中
     */
    private void initUrlMappingMethods() {
        Map<String, AggregateHandler> cls = SpringBeansUtil.getApplicationContext().getBeansOfType(AggregateHandler.class);
        for (AggregateHandler cl : cls.values()) {
            Collection<String> urls = AggregateUtil.getUrlsByRequestMapping(cl.getClass());
            for (String url : urls) {
                if (methods.contains(url)) {
                    Method method = AggregateUtil.getUrlMappingMethod(cl.getClass(), url);
                    if (method != null) {
                        AggregateDTO dto = new AggregateDTO();
                        dto.setUrl(url);
                        dto.setMethod(method);
                        dto.setInstance(cl);
                        urlMethodMap.put(url, dto);
                    } else {
                        logger.warn("initUrlMappingMethods method is null, url:{}", url);
                    }
                }
            }
        }
    }

    public Map<String, Map<String, Object>> aggregate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Map<String, Object>> sortedMap = new LinkedHashMap<>();
        Collection<AggregateDTO> dtos = urlMethodMap.values();
        if (dtos.isEmpty()) {
            for (String url : methods) {
                sortedMap.put(url, this.getTempMap());
            }
        }

        Map<String, Map<String, Object>> retMap = new HashMap<>();
        InvokeThread t;
        for (AggregateDTO dto : dtos) {
            t = new InvokeThread(retMap, dto, request, response);
            pool.execute(t);
        }

        //等待执行结果
        while (retMap.size() != dtos.size()) {
            try {
                synchronized (retMap) {
                    retMap.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        //排序，保持与前端的顺序一致
        for (String url : methods) {
            if (!retMap.containsKey(url) || retMap.get(url) == null) {
                sortedMap.put(url, getTempMap());
            } else {
                sortedMap.put(url, retMap.get(url));
            }
        }

        return sortedMap;
    }

    private Map<String, Object> getTempMap() {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("resultCode", "000");
        tempMap.put("resultMsg", "SUCCESS");
        tempMap.put("resultData", null);
        return tempMap;
    }

}
