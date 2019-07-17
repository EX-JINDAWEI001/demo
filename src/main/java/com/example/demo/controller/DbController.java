package com.example.demo.controller;

import com.example.demo.common.enums.ResultVoEnum;
import com.example.demo.common.vo.ResultVo;
import com.example.demo.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/db")
@RestController
public class DbController {

    private static Logger logger = LoggerFactory.getLogger(DbController.class);

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 测试数据库连接
     *
     * @param request
     * @param response
     * @return
     * @throws SQLException
     */
    @RequestMapping("/test1.do")
    public ResultVo<Object> test1(HttpServletRequest request, HttpServletResponse response) {
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

    @RequestMapping("/test2.do")
    public ResultVo<Object> test2(HttpServletRequest request, HttpServletResponse response) {
        logger.info("MONGODB test start");
        try {
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("name", "西门吹雪");
            paraMap.put("age", 18);
            paraMap.put("address", "湖北武汉");
            mongoTemplate.insert(paraMap, "jdw");

            Query query = new Query();
            query.addCriteria(Criteria.where("name").is("西门吹雪"));

            List<Map> list = mongoTemplate.find(query, Map.class, "jdw");
            return new ResultVo<>(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getMsg(), list);
        } catch (Exception e) {
            logger.error("MONGODB test error:{}", e);
            return new ResultVo<>(ResultVoEnum.FAILED.getCode(), ResultVoEnum.FAILED.getMsg());
        }
    }

}
