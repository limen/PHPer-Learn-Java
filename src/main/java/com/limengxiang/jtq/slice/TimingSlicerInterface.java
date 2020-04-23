package com.limengxiang.jtq.slice;

import com.limengxiang.jtq.TimingQueueInterface;

import java.util.Date;

/**
 * 分片接口，将消息队列按投递时间横向分割
 */
public interface TimingSlicerInterface {

    public Long push(String id, Date t);

    public String pull(Date date);

    public void setTimingQueue(TimingQueueInterface timingQueue);

    public TimingQueueInterface getTimingQueue();

    public void setSliceStrategy(SliceStrategyInterface strategy);

    public SliceStrategyInterface getSliceStrategy();

}
