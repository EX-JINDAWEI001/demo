package com.example.demo.common.system;

import com.example.demo.common.utils.SpringBeansUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            SystemStartupHandler systemStartupHandler = SpringBeansUtil.getBean(SystemStartupHandler.class);
            systemStartupHandler.startup();
        }
    }

}
