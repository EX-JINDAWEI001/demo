package com.example.demo.service.impl;

import com.example.demo.common.thread.AbstractThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TraceThreadImpl extends AbstractThread {

    private static final Logger logger = LoggerFactory.getLogger(TraceThreadImpl.class);

    List<Map<String, Object>> synList;

    public TraceThreadImpl(List<Map<String, Object>> synList){
        super(true);
        this.synList = synList;
        logger.info("TraceThreadImpl init thread name is :{}", getName());
    }

    @Override
    protected void doThreadService() {
        try {
            synchronized (synList){
                synList.wait();
            }
            doService();
        } catch (Exception e) {
            logger.error("doThreadService threadName is : {}, Exception is :{}", getName(), e);
        }
    }

    /**
     * 具体业务
     */
    private void doService() {
        logger.info("doThreadService synList is :{}", synList);
        int a = 1/0;
    }

}
