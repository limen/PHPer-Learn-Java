package com.limengxiang.jtq;

import com.limengxiang.jtq.message.IDStrategyInterface;
import com.limengxiang.jtq.message.MessageInterface;
import com.limengxiang.jtq.queue.QueueStorageInterface;
import com.limengxiang.jtq.slice.TimingSlicerInterface;

import java.util.Date;

public class TimingQueue implements TimingQueueInterface {

    public static final Integer StatusWaiting = 0;
    public static final Integer StatusOccupy = 1;
    public static final Integer StatusConsumed = 2;

    private String name;
    private QueueStorageInterface queueStorage;
    private IDStrategyInterface idStrategy;
    private TimingSlicerInterface slicer;

    public TimingQueue() {

    }

    public TimingQueue(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    public String push(MessageInterface msg) {
        prepareMessage(msg);
        Integer added = queueStorage.add(msg);
        if (added == 1) {
            slicer.push(msg.getMsgId(), msg.getSchedule());
            return msg.getMsgId();
        }
        return null;
    }

    public MessageInterface pull() {
        Date t = new Date();
        return pull(t);
    }

    public MessageInterface pull(Date t) {
        String msgId = slicer.pull(t);
        MessageInterface msg = queueStorage.get(msgId);
        if (msg == null) {
            return null;
        }
        msg.setStatus(StatusOccupy);
        queueStorage.update(msg);
        return msg;
    }

    public MessageInterface poll(String id) {
        return queueStorage.get(id);
    }

    @Override
    public Integer consumed(MessageInterface msg) {
        msg.setStatus(StatusConsumed);
        msg.setConsumeAt(new Date());
        return queueStorage.update(msg);
    }

    public void setQueueStorage(QueueStorageInterface queueStorage) {
        this.queueStorage = queueStorage;
    }

    public void setSlicer(TimingSlicerInterface slicer) {
        this.slicer = slicer;
        this.slicer.setTimingQueue(this);
    }

    public void setIdStrategy(IDStrategyInterface idStrategy) {
        this.idStrategy = idStrategy;
    }

    private void prepareMessage(MessageInterface msg) {
        msg.setMsgId(idStrategy.id());
        msg.setReceiveAt(new Date());
    }
}
