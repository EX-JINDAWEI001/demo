package com.example.demo.controller;

import com.example.demo.common.constans.Constants;
import com.example.demo.common.enums.ResultVoEnum;
import com.example.demo.common.vo.ResultVo;
import com.example.demo.common.utils.CookieUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private static final int activeTime = 60 * 15;

    @RequestMapping("/login.do")
    public ResultVo login(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.createCookie(Constants.USER_KEY, Constants.USER_NAME, Constants.DOMAIN);
        cookie.setMaxAge(activeTime);
        response.addCookie(cookie);
        return new ResultVo(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getMsg(), cookie);
    }

    @RequestMapping("/logout.do")
    public ResultVo logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constants.USER_KEY.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return new ResultVo(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getMsg(), cookies);
    }

}
