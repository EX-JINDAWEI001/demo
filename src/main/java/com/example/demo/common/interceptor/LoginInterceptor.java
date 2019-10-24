package com.example.demo.common.interceptor;

import com.example.demo.common.annotation.Auth;
import com.example.demo.common.constans.Constants;
import com.example.demo.common.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            logger.info("handler can not cast to HandlerMethod...");
            return true;
        }

        Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);
        if (auth == null) {
            logger.info("can not find @Auth in this uri:{}", request.getRequestURI());
            return true;
        }

        String admin = auth.user();
        if (!admin.equals(CookieUtil.getCookieValue(request, Constants.USER_KEY))) {
            logger.info("not admin, permission denied!");
            response.setStatus(403);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}
