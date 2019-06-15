package com.example.demo.service.impl;

import com.example.demo.common.system.SimpleServiceInit;
import com.example.demo.service.TraceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TraceServiceImpl implements TraceService, SimpleServiceInit {

    private List<Map<String, Object>> synList = new ArrayList<>();

    @Override
    public void trace(Map paraMap) {
        synchronized (synList){
            synList.add(paraMap);
            synList.notifyAll();
        }
    }

    @Override
    public void init() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new TraceThreadImpl(synList));
    }

}
