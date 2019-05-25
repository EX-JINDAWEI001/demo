package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CookieUtil {

    public static Cookie createCookie(String cookieName, String cookieValue, String domain){
        return doCreateCookie(cookieName, cookieValue, domain, null);
    }

    private static  Cookie doCreateCookie(String cookieName, String cookieValue, String domain, String path) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if(StringUtils.isNotBlank(domain)){
            cookie.setDomain(domain.trim());
        }
        if(StringUtils.isNotBlank(path)){
            cookie.setPath(path.trim());
        }else{
            cookie.setPath("/");
        }
        cookie.setSecure(false);
        return cookie;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName){
        String cookieStr = request.getHeader("Cookie");
        if (StringUtils.isBlank(cookieStr)){
            return null;
        }

        int idx = cookieStr.indexOf(cookieName);
        if(idx == -1){
            return null;
        }

        idx = cookieStr.indexOf("=", idx);
        if(idx == -1){
            return null;
        }

        int idx2 = cookieStr.indexOf(";",idx);
        String value = null;
        if(idx2 == -1){
            value = cookieStr.substring(idx+1);
        }else{
            value = cookieStr.substring(idx+1, idx2);
        }
        try{
            String charset = request.getCharacterEncoding();
            charset = StringUtils.isBlank(charset) ? "ISO-8859-1" : charset;
            return URLDecoder.decode(value.trim(), charset);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
