package com.example.demo.common.aggregate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class AggregateUtil {

    private static Logger logger = LoggerFactory.getLogger(AggregateUtil.class);

    public static <T> T getDTO(HttpServletRequest request, Class<T> clazz) {
        Method[] methods = clazz.getMethods();
        if (methods != null && methods.length > 0) {

            try {
                T n = clazz.newInstance();
                for (Method m : methods) {
                    String mn = m.getName();
                    if (mn.startsWith("set") && mn.length() >= 4) {
                        String l = mn.substring(3, 4);
                        mn = mn.replaceFirst("set" + l, l.toLowerCase());
                        String valStr = request.getParameter(mn);
                        if (StringUtils.isEmpty(valStr)) {
                            continue;
                        }

                        Field f = getField(clazz, mn);
                        if (f == null) {
                            logger.warn("field is null, fieldName is :{}", mn);
                        }

                        Object val = null;
                        String tps = f.getType().toString();
                        logger.info("field types is :{}", tps);
                        if (tps.equals(byte.class.toString()) || tps.equals(Byte.class.toString())) {
                            val = Byte.parseByte(valStr);
                        } else if (tps.equals(short.class.toString()) || tps.equals(Short.class.toString())) {
                            val = Short.parseShort(valStr);
                        } else if (tps.equals(int.class.toString()) || tps.equals(Integer.class.toString())) {
                            val = Integer.parseInt(valStr);
                        } else if (tps.equals(long.class.toString()) || tps.equals(Long.class.toString())) {
                            val = Long.parseLong(valStr);
                        } else if (tps.equals(float.class.toString()) || tps.equals(Float.class.toString())) {
                            val = Float.parseFloat(valStr);
                        } else if (tps.equals(double.class.toString()) || tps.equals(Double.class.toString())) {
                            val = Double.parseDouble(valStr);
                        } else if (tps.equals(char.class.toString()) || tps.equals(Character.class.toString())) {
                            val = valStr.toCharArray();
                        } else if (tps.equals(boolean.class.toString()) || tps.equals(Boolean.class.toString())) {
                            val = Boolean.parseBoolean(valStr);
                        } else {
                            val = valStr;
                        }

                        try {
                            m.invoke(n, val);
                        } catch (Exception e) {
                            logger.error("do method error :{}", e);
                        }
                    }
                }
                return n;
            } catch (Exception e) {
                logger.error("getDTO error : {}", e);
            }
        }
        return null;
    }

    private static <T extends Object> Field getField(Class<T> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch(Exception e){
            //ignore
        }
        Class<? super T> sClazz = clazz.getSuperclass();
        if(!sClazz.toString().equals(Object.class.toString())){
            return getField(sClazz, fieldName);
        }
        return null;
    }

    public static Method getUrlMappingMethod(Class<?> clazz, String reqUrl){
        reqUrl = getFullPath(reqUrl);
        RequestMapping a1 = clazz.getAnnotation(RequestMapping.class);
        String classUrl = getUrlByRequestMapping(a1);

        Method[] declaredMethods = clazz.getMethods();
        if (declaredMethods != null){
            for (Method m : declaredMethods){
                RequestMapping a2 = m.getAnnotation(RequestMapping.class);
                if(a2 == null){
                    continue;
                }
                String methodUrl = getUrlByRequestMapping(a2);
                String url = null;
                if(classUrl != null){
                    if(methodUrl != null){
                        url = classUrl + methodUrl;
                    }else{
                        url = methodUrl;
                    }
                }
                if(reqUrl.equals(url)){
                    return m;
                }
            }
        }
        return null;
    }

    public static Collection<String> getUrlsByRequestMapping(Class<?> clazz){
        RequestMapping a1 = clazz.getAnnotation(RequestMapping.class);
        String classUrl = getUrlByRequestMapping(a1);

        Method[] declaredMethods = clazz.getMethods();
        Collection<String> rets = new ArrayList<>();
        if (declaredMethods != null){
            for (Method m : declaredMethods){
                RequestMapping a2 = m.getAnnotation(RequestMapping.class);
                if(a2 == null){
                    continue;
                }
                String methodUrl = getUrlByRequestMapping(a2);
                String url = null;
                if(classUrl != null){
                    if(methodUrl != null){
                        url = classUrl + methodUrl;
                    }else{
                        url = methodUrl;
                    }
                }
                rets.add(url);
            }
        }
        return rets;
    }

    private static String getUrlByRequestMapping(RequestMapping requestmapping) {
        String classUrl = null;
        if(requestmapping != null){
            String[] v1 = requestmapping.value();
            if(v1 != null && v1.length >= 1){
                classUrl = v1[0];
            }
        }
        return getFullPath(classUrl);
    }

    public static String getFullPath(String reqUrl) {
        if(reqUrl != null){
            if(!reqUrl.startsWith("/")){
                reqUrl = "/" + reqUrl;
            }
            if(reqUrl.contains("//")){
                reqUrl = reqUrl.replaceAll("//", "/");
            }
        }else{
            reqUrl = "";
        }
        return reqUrl;
    }

}
