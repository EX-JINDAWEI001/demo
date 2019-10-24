package com.example.demo.common.system;

import com.example.demo.common.utils.SpringBeansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class SystemStartupHandler {

    private static Logger logger = LoggerFactory.getLogger(SystemStartupHandler.class);

    public void startup() {
        logger.info("system startup init...");
        Map<String, SimpleServiceInit> maps = SpringBeansUtil.getApplicationContext()
                .getBeansOfType(SimpleServiceInit.class);
        if (maps != null && !maps.isEmpty()) {
            Collection<SimpleServiceInit> cls = maps.values();
            for (SimpleServiceInit ssi : cls) {
                logger.info("start to init class:{}", ssi.getClass().getName());
                ssi.init();
            }
        }
    }

}
