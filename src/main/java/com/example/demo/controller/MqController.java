package com.example.demo.controller;

import com.example.demo.common.enums.ResultVoEnum;
import com.example.demo.common.vo.ResultVo;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/mq")
public class MqController {

    @Autowired
    private DefaultMQProducer producer;

    @Value("${rocketmq.consumer.topics}")
    private String demoTopic;

    @RequestMapping("/test.do")
    public ResultVo test(HttpServletRequest request) throws InterruptedException,
            RemotingException,
            MQClientException,
            MQBrokerException,
            UnsupportedEncodingException {
        Message message = new Message(demoTopic, "TagA",
                request.getParameter("key"),
                "hello rocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.send(message);
        return new ResultVo(ResultVoEnum.SUCCESS.getCode(), ResultVoEnum.SUCCESS.getMsg(), null);
    }

}
