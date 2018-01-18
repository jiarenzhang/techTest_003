package com.zjr.technologytest03.tools;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MqMessageListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, Handler> handlerMaps = new HashMap<>();

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        Handler handler = handlerMaps.get(message.getTag());
        if(handler == null){
            handler = HandlerFactory.getHandlerByTag(message.getTag());
            if(handler != null){
                handlerMaps.put(message.getTag(), handler);
            }
        }

        if(handler != null){
            return handler.handle(message);
        }else{
            logger.error("MqMessageListenerError, Message=[]", message);
            return Action.ReconsumeLater;
        }
    }
}
