package com.limengxiang.jtq.slice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 按分钟分片
 */
public class DefaultSliceStrategy implements SliceStrategyInterface {

    private SimpleDateFormat df;

    public DefaultSliceStrategy() {
        df = new SimpleDateFormat("yyyyMMddhhmm");
    }

    @Override
    public String key(Date t) {
        return df.format(t);
    }
}
