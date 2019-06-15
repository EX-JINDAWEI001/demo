package com.example.demo.controller;

import com.example.demo.common.enums.ResultVoEnum;
import com.example.demo.common.vo.ResultVo;
import com.example.demo.service.TraceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequestMapping("/trace")
@RestController
public class TraceController {

    private static final Logger logger = LoggerFactory.getLogger(TraceController.class);

    @Autowired
    private TraceService traceService;

    @RequestMapping("/trace.do")
    public ResultVo trace(HttpServletRequest request, HttpServletResponse response, @RequestParam Map paraMap){
        try {
            logger.info("trace paraMap:{}", paraMap);
            traceService.trace(paraMap);
            return new ResultVo(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getCode(), null);
        } catch (Exception e) {
            logger.error("trace error:{}", e);
            return new ResultVo(ResultVoEnum.FAILED.getCode(), ResultVoEnum.FAILED.getCode(), null);
        }
    }

}
