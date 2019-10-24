package com.example.demo.common.aggregate;

import com.example.demo.common.vo.AggregateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AggregateListenerList {

    private static final Logger logger = LoggerFactory.getLogger(AggregateListenerList.class);

    private static AggregateListenerList cll;

    private Map<String, AggregateDTO> clm;

    private AggregateListenerList() {
        clm = new ConcurrentHashMap<>();
    }

    public synchronized static AggregateListenerList getInstance() {
        if (cll == null) {
            cll = new AggregateListenerList();
        }
        return cll;
    }

    public Collection<AggregateDTO> getListener(String... urls) {
        Collection<AggregateDTO> cl = new ArrayList<>();
        if (urls != null && urls.length > 0) {
            for (String url : urls) {
                url = AggregateUtil.getFullPath(url);
                if (clm.containsKey(url)) {
                    cl.add(clm.get(url));
                } else {
                    logger.warn("getListener url is {}, value is null", url);
                }
            }
        }
        return cl;
    }

    public void addListener(String url, AggregateDTO dto) {
        url = AggregateUtil.getFullPath(url);
        clm.put(url, dto);
    }

    public boolean isExisted(String url) {
        url = AggregateUtil.getFullPath(url);
        return clm.containsKey(url);
    }

    public void clear() {
        clm.clear();
    }

}
