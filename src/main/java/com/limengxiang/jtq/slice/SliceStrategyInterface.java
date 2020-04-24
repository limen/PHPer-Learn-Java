package com.limengxiang.jtq.slice;

import java.util.Date;

/**
 * 基于时间的分片策略
 */
public interface SliceStrategyInterface {

    public String key(Date t);

}
