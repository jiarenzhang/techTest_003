package com.zjr.technologytest03.tools;


public class HandlerFactory {

    public static Handler getHandlerByTag(String tag){
        return SpringContextHolder.getBean(tag);
    }

}
