package com.example.demo.common.config;

import com.example.demo.common.annotation.Uid;
import com.example.demo.common.constans.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import java.util.List;

@Configuration
public class UidConfig implements WebMvcConfigurer {

    @Autowired
    private UidArgumentResolver uidArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(uidArgumentResolver);
    }

    @Component
    public static class UidArgumentResolver implements HandlerMethodArgumentResolver {

        private boolean requestScopeEnable = true;

        @Override
        public boolean supportsParameter(MethodParameter methodParameter) {
            if (methodParameter.hasParameterAnnotation(Uid.class)) {
                return true;
            }
            return false;
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
            Uid annotation = methodParameter.getParameterAnnotation(Uid.class);
            if (annotation != null) {
                String uid = nativeWebRequest.getHeader(Constants.USER_KEY);
                if (StringUtils.isBlank(uid)) {
                    ServletWebRequest request = (ServletWebRequest) nativeWebRequest;
                    Cookie[] cookies = request.getRequest().getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (Constants.USER_KEY.equals(cookie.getName())) {
                                uid = cookie.getValue();
                                break;
                            }
                        }
                    }
                }
                if (requestScopeEnable) {
                    nativeWebRequest.setAttribute(Constants.USER_KEY, uid, NativeWebRequest.SCOPE_REQUEST);
                }
                if (annotation.isRequired() && uid == null) {
                    throw new IllegalArgumentException("user not login!!!");
                }
                return uid;
            }
            return new Object();
        }

    }

}
