package com.zjr.technologytest03;

import com.aliyun.openservices.ons.api.*;
import com.google.gson.reflect.TypeToken;
import com.zjr.technologytest03.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Technologytest03ApplicationTests {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Producer producer;
    @Autowired
    private Consumer consumer;

    @Test
    public void test01() {
        Message destMessage = new Message("ccw_busi_local", "Tag李四",
                ("神棍").getBytes());
        destMessage.setKey(System.nanoTime() + "");
//        destMessage.setStartDeliverTime(System.currentTimeMillis() + refundTime);
        SendResult result = producer.send(destMessage);
        System.out.println(result);
    }

    @Test
    public void test02() {
        //消息消费者订阅消息
        consumer.subscribe("ccw_busi_local", "*", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext consumeContext) {
                LOG.info("==开始消费:{}==", this.getClass().getName());
                LOG.info("==消息ID={},消息TAG={}==", message.getMsgID(), message.getTag());
                LOG.info("====消息Body：{}====", JsonUtils.toJson(new String(message.getBody())));
                Map<String, String> map = JsonUtils.fromJson(new String(message.getBody()), new TypeToken<Map<String, String>>() {
                });
                LOG.info("==参数：{}==", JsonUtils.toJson(map));
                if (map == null) {
                    //返回ReconsumeLater,消息将重试
                    return Action.ReconsumeLater;
                }
                if (message.getTopic().equals("ccw_busi_local")) {
                    System.out.println("执行消费逻辑。。。");
                }
                return null;
            }
        });
    }

}
