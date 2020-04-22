package com.limengxiang.jtq.queue;

import com.limengxiang.jtq.message.MessageInterface;

public interface QueueInterface {

    public Integer push(MessageInterface msg);

    public MessageInterface get(String id);

}
