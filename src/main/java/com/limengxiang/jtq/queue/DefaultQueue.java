package com.limengxiang.jtq.queue;

import com.limengxiang.jtq.message.DefaultMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mysql
 */
@Mapper
public interface DefaultQueue extends QueueInterface {
    public DefaultMessage get(String msgId);
}
