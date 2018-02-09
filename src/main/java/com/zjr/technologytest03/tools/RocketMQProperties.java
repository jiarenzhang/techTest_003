package com.zjr.technologytest03.tools;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
//读取properties配置文件
@PropertySource("classpath:applicatioon.properties")
@ConfigurationProperties(prefix = "cc.aliyun.rocketmq")
public class RocketMQProperties {

    private String producerId;
    private String consumerId;
    // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
    private String accessKey;
    // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
    private String secretKey;
    // 设置 TCP 接入域名
    private String ONSAddr;
    private Integer consumeThreadNums = 1;

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getONSAddr() {
        return ONSAddr;
    }

    public void setONSAddr(String ONSAddr) {
        this.ONSAddr = ONSAddr;
    }

    public Integer getConsumeThreadNums() {
        return consumeThreadNums;
    }

    public void setConsumeThreadNums(Integer consumeThreadNums) {
        this.consumeThreadNums = consumeThreadNums;
    }

    @Override
    public String toString() {
        return "RocketMQProperties{" +
                "producerId='" + producerId + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", ONSAddr='" + ONSAddr + '\'' +
                ", consumeThreadNums=" + consumeThreadNums +
                '}';
    }
}
