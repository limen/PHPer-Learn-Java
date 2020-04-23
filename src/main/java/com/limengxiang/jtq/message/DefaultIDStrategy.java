package com.limengxiang.jtq.message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO 唯一ID生成算法
 */
public class DefaultIDStrategy implements IDStrategy {

    private AtomicInteger counter;

    public DefaultIDStrategy() {
        counter = new AtomicInteger();
    }

    @Override
    public String id() {
        return "" + counter.incrementAndGet();
    }
}
