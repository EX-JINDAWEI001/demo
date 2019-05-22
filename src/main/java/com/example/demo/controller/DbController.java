package com.example.demo.controller;

import com.example.demo.common.enums.ResultVoEnum;
import com.example.demo.common.vo.ResultVo;
import com.example.demo.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/db")
@RestController
public class DbController {

    private static Logger logger = LoggerFactory.getLogger(DbController.class);

    /**
     * 测试数据库连接
     *
     * @param request
     * @param response
     * @return
     * @throws SQLException
     */
    @RequestMapping("/test.do")
    public ResultVo<Object> test(HttpServletRequest request, HttpServletResponse response) {
        logger.info("DB test start");
        try {
            String sql = "select * from cust";
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            List<String> list = new ArrayList<String>();
            while (resultSet.next()) {
                list.add(resultSet.getString("cust_name"));
            }
            return new ResultVo<>(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getMsg(), list);
        } catch (Exception e) {
            logger.error("DB test error:{}", e);
            return new ResultVo<>(ResultVoEnum.FAILED.getCode(), ResultVoEnum.FAILED.getMsg());
        } finally {
            DBUtil.closeConnection();
        }
    }

}
