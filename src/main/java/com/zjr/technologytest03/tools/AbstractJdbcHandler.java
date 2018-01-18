package com.zjr.technologytest03.tools;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 采用SQL处理MQ消息
 */
public abstract class AbstractJdbcHandler implements Handler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;

    private Connection conn;

    public boolean initConnection(){
        try {
            conn = dataSource.getConnection();
        }catch (SQLException ex){
            logger.error("InitConnectionError[{}]，ErrorMsg={}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void closeConnection(){
        DbUtils.closeQuietly(conn);
    }

    /**
     * 处理MQ消息
     * @param message
     * @return Action
     */
    @Override
    public Action handle(Message message){
        /**
         * 初始化数据库连接
         */
        if(!this.initConnection()){
            logger.error("InitConnectionError[{}]", this.getClass().getSimpleName());
            return Action.ReconsumeLater;
        }

        /**
         * 处理业务逻辑
         */
        try {
            return this.handle(message, conn);
        }catch (Exception ex){
            logger.error("HandlerMsgError[{}-{}]，ErrorMsg={}", this.getClass().getSimpleName(), message, ex.getMessage(), ex);
            return Action.ReconsumeLater;
        }finally {
            this.closeConnection();
        }
    }

    /**
     * 采用JDBC处理MQ消息
     * @param message
     * @param conn
     * @return Action
     */
    public abstract Action handle(Message message, Connection conn);

}
