package com.example.demo.common.aggregate;

import com.example.demo.common.vo.AggregateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AggregateListenerImpl implements AggregateListener {

    private static final Logger logger = LoggerFactory.getLogger(AggregateListenerImpl.class);

    /**
     * 聚合相关
     * 可提取到父类中去
     *
     * @param dto
     * @param request
     * @param response
     * @return
     */
    @Override
    public Map<String, Object> changeProperty(AggregateDTO dto, HttpServletRequest request, HttpServletResponse response) {
        Object ret = null;
        try {
            Method m = dto.getMethod();
            Object[] args = getParams(request, response, m);
            ret = m.invoke(dto.getInstance(), args);
        } catch (Exception e) {
            logger.error("changeProperty error:{}", e);
        }
        Map<String, Object> retMap = new HashMap<>();
        retMap.put(dto.getUrl(), ret);
        return retMap;
    }

    private Object[] getParams(HttpServletRequest request, HttpServletResponse response, Method m) {
        Object[] args = null;
        Class<?>[] paramTypes = m.getParameterTypes();
        if (paramTypes != null) {
            args = new Object[paramTypes.length];
            String paramName = null;
            for (int index = 0; index < paramTypes.length; index++) {
                paramName = paramTypes[index].getName();
                if (paramName.equals("javax.servlet.http.HttpServletRequest")) {
                    args[index] = request;
                } else if (paramName.equals("javax.servlet.http.HttpServletResponse")) {
                    args[index] = response;
                } else {
                    args[index] = AggregateUtil.getDTO(request, paramTypes[index]);
                }
            }
        }
        return args;
    }

}
