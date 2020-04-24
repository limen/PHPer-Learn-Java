package com.limengxiang.jtq.slice;

import com.limengxiang.jtq.TimingQueueInterface;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * 基于 Redis LIST 实现分片
 */
public class DefaultTimingSlicer implements TimingSlicerInterface {

    private RedisTemplate<String, String> redisTemplate;
    private SliceStrategyInterface sliceStrategy;
    private TimingQueueInterface timingQueue;

    public DefaultTimingSlicer() {

    }

    public DefaultTimingSlicer(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.sliceStrategy = new DefaultSliceStrategy();
    }

    public DefaultTimingSlicer(RedisTemplate<String, String> template, SliceStrategyInterface strategy) {
        this.redisTemplate = template;
        this.sliceStrategy = strategy;
    }

    @Override
    public Long push(String id, Date timing) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        return ops.leftPush(getSliceKey(timing), id);
    }

    @Override
    public String pull(Date t) {
        return redisTemplate.opsForList().rightPop(getSliceKey(t));
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public SliceStrategyInterface getSliceStrategy() {
        return sliceStrategy;
    }

    public void setSliceStrategy(SliceStrategyInterface sliceStrategy) {
        this.sliceStrategy = sliceStrategy;
    }

    @Override
    public TimingQueueInterface getTimingQueue() {
        return timingQueue;
    }

    @Override
    public void setTimingQueue(TimingQueueInterface timingQueue) {
        this.timingQueue = timingQueue;
    }

    private String getSliceKey(Date t) {
        return this.timingQueue.name() + ":slice:" + sliceStrategy.key(t);
    }
}
