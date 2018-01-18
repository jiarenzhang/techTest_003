package com.zjr.technologytest03.tools;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;

public interface Handler {

    /**
     * 处理MQ消息
     * @param message
     * @return
     */
    Action handle(Message message);
}
