package com.example.demo.common.aggregate;

import com.example.demo.component.thread.AbstractThread;
import com.example.demo.common.vo.AggregateDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ChangePropertyThread extends AbstractThread {

    private Map<String, Map<String, Object>> retMap;

    private AggregateDTO dto;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public ChangePropertyThread(Map<String, Map<String, Object>> retMap, AggregateDTO dto,
                                HttpServletRequest request, HttpServletResponse response) {
        super(false);
        this.retMap = retMap;
        this.dto = dto;
        this.request = request;
        this.response = response;
    }

    @Override
    protected void doThreadService() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Map<String,Object> ret = ((AggregateListener)dto.getInstance()).changeProperty(dto, request, response);
        synchronized (retMap) {
            retMap.put(dto.getUrl(), ret);
            retMap.notifyAll();
        }
    }
}
