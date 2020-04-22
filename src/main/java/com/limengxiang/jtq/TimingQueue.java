package com.limengxiang.jtq;

import com.limengxiang.jtq.message.MessageInterface;
import com.limengxiang.jtq.queue.QueueInterface;
import com.limengxiang.jtq.slice.TimingSlicerInterface;

import java.util.Date;

public class TimingQueue implements TimingQueueInterface {

    public static final Integer StatusWaiting = 0;
    public static final Integer StatusAway = 1;

    private QueueInterface queue;
    private TimingSlicerInterface slicer;

    public Integer push(MessageInterface msg) {
        Integer msgId = queue.push(msg);
//        slicer.push(msgId, msg.getSchedule());
        return msgId;
    }

    public MessageInterface pull() {
        Date t = new Date();
        return pull(t);
    }

    public MessageInterface pull(Date t) {
        String msgId = slicer.pull(t);
        return queue.get(msgId);
    }

    public MessageInterface poll(String id) {
        return queue.get(id);
    }

    public void setQueue(QueueInterface queue) {
        this.queue = queue;
    }

    public void setSlicer(TimingSlicerInterface slicer) {
        this.slicer = slicer;
    }
}
