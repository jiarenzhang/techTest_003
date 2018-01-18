package com.zjr.technologytest03.tools;


import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author LIUKANGJIN
 */
@Configuration
@ConditionalOnClass({Producer.class, Consumer.class})
@EnableConfigurationProperties({RocketMQProperties.class})
public class RocketMQAutoConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String DEFAULT_PRODUCER_ID = "DEFAULT_PRODUCER";

    private final static String DEFAULT_CONSUMER_ID = "DEFAULT_CONSUMER";

    @Autowired
    private RocketMQProperties properties;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(Producer.class)
    public Producer producer(){
        Properties mqProperties = new Properties();
        if(null == properties.getProducerId() || "".equals(properties.getProducerId())){
            mqProperties.setProperty(PropertyKeyConst.ProducerId, DEFAULT_PRODUCER_ID);
        }else{
            mqProperties.setProperty(PropertyKeyConst.ProducerId, properties.getProducerId());
        }
        mqProperties.setProperty(PropertyKeyConst.AccessKey, properties.getAccessKey());
        mqProperties.setProperty(PropertyKeyConst.SecretKey, properties.getSecretKey());
        mqProperties.setProperty(PropertyKeyConst.ONSAddr, properties.getONSAddr());
        Producer producer = null;
        try{
            producer = ONSFactory.createProducer(mqProperties);
            producer.start();
            logger.info("CreateRocketMQProducerSuccess, ProducerID={}", mqProperties.getProperty(PropertyKeyConst.ProducerId));
        }catch (Exception ex){
            logger.error("CreateRocketMQProducerError, ErrorMsg={}", ex.getMessage(), ex);
        }
        return producer;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(Consumer.class)
    public Consumer consumer(){
        Properties mqProperties = new Properties();
        if(null == properties.getConsumerId() || "".equals(properties.getConsumerId())){
            mqProperties.setProperty(PropertyKeyConst.ConsumerId, DEFAULT_CONSUMER_ID);
        }else{
            mqProperties.setProperty(PropertyKeyConst.ConsumerId, properties.getConsumerId());
        }
        mqProperties.setProperty(PropertyKeyConst.AccessKey, properties.getAccessKey());
        mqProperties.setProperty(PropertyKeyConst.SecretKey, properties.getSecretKey());
        mqProperties.setProperty(PropertyKeyConst.ONSAddr, properties.getONSAddr());
        mqProperties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(properties.getConsumeThreadNums()));
        Consumer consumer = null;
        try{
            consumer = ONSFactory.createConsumer(mqProperties);
            logger.info("CreateRocketMQConsumerSuccess, ConsumerID={}", mqProperties.getProperty(PropertyKeyConst.ConsumerId));
        }catch (Exception ex){
            logger.error("CreateRocketMQConsumerError, ErrorMsg={}", ex.getMessage(), ex);
        }
        return consumer;
    }

}
