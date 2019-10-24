package com.example.demo.service.impl;

import com.example.demo.common.aggregate.AggregateListenerList;
import com.example.demo.common.aggregate.AggregateUtil;
import com.example.demo.common.aggregate.ChangePropertyThread;
import com.example.demo.common.aggregate.AggregateListener;
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

    private Map<String, AggregateDTO> urlMethodMap = new HashMap();

    private ExecutorService pool;

    /**
     * 此处可根据业务需求进行配置
     */
    private String[] methods = {"/demo/test1.do", "/demo/test2.do", "/demo/test3.do"};

    @Override
    public void init() {
        pool = Executors.newFixedThreadPool(4);
        initUrlMappingMethods();
        initAggregateConfigs();
    }

    /**
     * 将受监听的所有的url和method信息加载到urlMethodMap中
     */
    private Map<String, AggregateListener> initUrlMappingMethods() {
        Map<String, AggregateListener> cls = SpringBeansUtil.getApplicationContext().getBeansOfType(AggregateListener.class);
        for (AggregateListener cl : cls.values()){
            Collection<String> urls = AggregateUtil.getUrlsByRequestMapping(cl.getClass());
            for(String url : urls){
                Method method = AggregateUtil.getUrlMappingMethod(cl.getClass(), url);
                if(method != null){
                    AggregateDTO dto = new AggregateDTO();
                    dto.setUrl(url);
                    dto.setMethod(method);
                    dto.setInstance(cl);
                    urlMethodMap.put(url, dto);
                }else{
                    logger.warn("initUrlMappingMethods method is null, url:{}", url);
                }
            }
        }
        return cls;
    }

    private boolean initAggregateConfigs() {
        AggregateListenerList.getInstance().clear();
        for (String url : methods) {
            AggregateDTO dto = urlMethodMap.get(url);
            if(dto != null){
                AggregateListenerList.getInstance().addListener(url, dto);
            } else {
                return false;
            }
        }
        return true;
    }

    public Map<String, Map<String, Object>> aggregate(HttpServletRequest request, HttpServletResponse response) {

        Set<String> existUrls = new HashSet<>();
        for(String url : methods) {
            if(AggregateListenerList.getInstance().isExisted(url)) {
                existUrls.add(url);
            } else {
                if(initAggregateConfigs()) {
                    existUrls.add(url);
                }
            }
        }

        Map<String, Map<String, Object>> retMap = new HashMap<>();
        Map<String, Map<String, Object>> sortedMap = new LinkedHashMap<>();
        if(existUrls.isEmpty()) {
            for (String tName : methods) {
                sortedMap.put(tName, retMap.get(tName));
            }
            return sortedMap;
        }

        Collection<AggregateDTO> listener = AggregateListenerList.getInstance().getListener(existUrls.toArray(new String[existUrls.size()]));
        if (listener == null || listener.isEmpty()) {
            return null;
        }

        ChangePropertyThread t = null;
        for (AggregateDTO dto : listener) {
            t  = new ChangePropertyThread(retMap, dto, request, response);
            pool.execute(t);
        }

        //等待执行结果
        while (retMap.size() != listener.size()) {
            try {
                synchronized (retMap) {
                    retMap.wait(2);
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
        return  tempMap;
    }

}
