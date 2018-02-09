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
        //发送消息的代码一定要捕获异常，不然会重复发送。
        SendResult result = null;
        try {
            Message destMessage = new Message("ccw_busi_local", "Tag李四",
                    ("神棍").getBytes());
//            key我获取的是纳秒值 你可以随机定义【到时候可以根据日志查询key查到这条消息】
            destMessage.setKey(System.nanoTime() + "");
//            startDeliverTime消息投递时间，60000表示一分钟，当前系统时间1分钟后投递这条消息
            destMessage.setStartDeliverTime(System.currentTimeMillis() + 60000);
            result = producer.send(destMessage);
            if (result != null) {
                LOG.info("send mq message success!");
            } else {
                LOG.info("result is null...");
            }
        } catch (Exception e) {
            try {
                //如果有异常，休眠1秒
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
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
        consumer.start();
    }

}
