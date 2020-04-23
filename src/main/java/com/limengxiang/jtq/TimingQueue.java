package com.limengxiang.jtq;

import com.limengxiang.jtq.message.IDStrategy;
import com.limengxiang.jtq.message.MessageInterface;
import com.limengxiang.jtq.queue.QueueInterface;
import com.limengxiang.jtq.slice.TimingSlicerInterface;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimingQueue implements TimingQueueInterface {

    public static final Integer StatusWaiting = 0;
    public static final Integer StatusAway = 1;


    private IDStrategy idStrategy;
    private QueueInterface queue;
    private TimingSlicerInterface slicer;

    public String push(MessageInterface msg) {
        if (msg.getMsgId() == null) {
            msg.setMsgId(idStrategy.id());
        }
        if (msg.getReceiveAt() == null) {
            msg.setReceiveAt(new Date());
        }
        if (msg.getTakeawayAt() == null) {
            msg.setTakeawayAt(new Date());
        }
        queue.push(msg);
        slicer.push(msg.getMsgId(), msg.getSchedule());
        return msg.getMsgId();
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

    public IDStrategy getIdStrategy() {
        return idStrategy;
    }

    public void setIdStrategy(IDStrategy idStrategy) {
        this.idStrategy = idStrategy;
    }
}
