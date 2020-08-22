package com.example.demo.service.impl;

import com.example.demo.common.system.SimpleServiceInit;
import com.example.demo.service.TraceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TraceServiceImpl implements TraceService, SimpleServiceInit {

    private List<Map<String, Object>> synList1 = new ArrayList<>();
    private List<Map<String, Object>> synList2 = new ArrayList<>();
    private List<Map<String, Object>> synList3 = new ArrayList<>();

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void trace(Map paraMap) {
        int turn = count.addAndGet(1) % 3;
        if (turn == 1) {
            synchronized (synList1) {
                synList1.add(paraMap);
                synList1.notifyAll();
            }
        } else if (turn == 2) {
            synchronized (synList2) {
                synList2.add(paraMap);
                synList2.notifyAll();
            }
        } else if (turn == 0) {
            synchronized (synList3) {
                synList3.add(paraMap);
                synList3.notifyAll();
            }
        }
    }

    @Override
    public void init() {
        // 启用3个线程分担埋点压力;
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.execute(new TraceThreadImpl(synList1));
        pool.execute(new TraceThreadImpl(synList2));
        pool.execute(new TraceThreadImpl(synList3));
    }

}
