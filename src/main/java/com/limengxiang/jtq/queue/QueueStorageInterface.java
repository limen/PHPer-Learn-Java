package com.limengxiang.jtq.queue;

import com.limengxiang.jtq.message.MessageInterface;

/**
 * 消息持久存储
 */
public interface QueueStorageInterface {

    public Integer add(MessageInterface msg);

    public MessageInterface get(String id);

    public Integer update(MessageInterface msg);

}
