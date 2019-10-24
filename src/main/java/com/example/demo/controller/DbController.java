package com.example.demo.controller;

import com.example.demo.common.enums.ResultVoEnum;
import com.example.demo.component.redis.RedisHandler;
import com.example.demo.common.vo.ResultVo;
import com.example.demo.common.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequestMapping("/db")
@RestController
public class DbController {

    private static Logger logger = LoggerFactory.getLogger(DbController.class);

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisHandler redisHandler;

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
        logger.info("MONGODB test start......");
        try {
            IndexOperations indexOperations = mongoTemplate.indexOps("jdw");
            if (indexOperations.getIndexInfo().size() <= 1) {
                Index index = new Index();
                index.on("date", Sort.Direction.DESC);
                index.expire(30, TimeUnit.SECONDS);
                indexOperations.ensureIndex(index);
            }

            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("name", "西门吹雪");
            paraMap.put("age", 18);
            paraMap.put("address", "湖北武汉");
            paraMap.put("date", new Date());
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

    @RequestMapping("/test3.do")
    public ResultVo<Object> test3(HttpServletRequest request, HttpServletResponse response) {
        logger.info("redis test start......");
        try {
            redisHandler.set("string", "value");
            redisHandler.hset("hash", "field", "value");
            redisHandler.lpush("list", "value");
            redisHandler.sadd("set", "value");
            redisHandler.zadd("zset", "value1", 10d);
            redisHandler.zadd("zset", "value2", 200d);
            redisHandler.zadd("zset", "value3", 100d);

            Map<String, Object> ret = new HashMap<>();
            ret.put("string", redisHandler.get("string"));
            ret.put("hash", redisHandler.hget("hash", "field"));
            ret.put("list", redisHandler.rpop("list"));
            ret.put("set", redisHandler.spop("set"));
            ret.put("zset", redisHandler.zRangeWithScores("zset", 0l, -1l));
            return new ResultVo<>(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getMsg(), ret);
        } catch (Exception e) {
            logger.error("redis test error:{}", e);
            return new ResultVo<>(ResultVoEnum.FAILED.getCode(), ResultVoEnum.FAILED.getMsg());
        }
    }

}
