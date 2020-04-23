package com.limengxiang.jtq;

import com.limengxiang.jtq.message.MessageInterface;

import java.util.Date;

public interface TimingQueueInterface {

    public String name();

    public String push(MessageInterface msg);

    public MessageInterface pull();

    public MessageInterface pull(Date t);

    public MessageInterface poll(String id);

    public Integer consumed(MessageInterface msg);

}
