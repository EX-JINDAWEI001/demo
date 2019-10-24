package com.example.demo.common.aggregate;

import com.example.demo.common.vo.AggregateDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AggregateListener {
    Map<String, Object> changeProperty(AggregateDTO dto, HttpServletRequest request, HttpServletResponse response);
}
