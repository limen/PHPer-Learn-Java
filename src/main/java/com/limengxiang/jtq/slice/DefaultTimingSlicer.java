package com.limengxiang.jtq.slice;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * Redis portable
 */
public class DefaultTimingSlicer implements TimingSlicerInterface {

    private RedisTemplate<String, String> redisTemplate;
    private SliceStrategy sliceStrategy;

    public DefaultTimingSlicer() {

    }

    public DefaultTimingSlicer(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.sliceStrategy = new DefaultSliceStrategy();
    }

    public DefaultTimingSlicer(RedisTemplate<String, String> template, SliceStrategy strategy) {
        this.redisTemplate = template;
        this.sliceStrategy = strategy;
    }

    @Override
    public Long push(String id, Date timing) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        String sliceKey = sliceStrategy.key(timing);
        return ops.leftPush(sliceKey, id);
//        return redisTemplate.opsForList().leftPush(sliceStrategy.key(timing), id);
    }

    @Override
    public String pull(Date t) {
        return redisTemplate.opsForList().rightPop(sliceStrategy.key(t));
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public SliceStrategy getSliceStrategy() {
        return sliceStrategy;
    }

    public void setSliceStrategy(SliceStrategy sliceStrategy) {
        this.sliceStrategy = sliceStrategy;
    }
}
