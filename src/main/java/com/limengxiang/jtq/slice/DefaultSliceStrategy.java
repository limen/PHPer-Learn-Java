package com.limengxiang.jtq.slice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 默认按分钟分片
 */
public class DefaultSliceStrategy implements SliceStrategy {

    private SimpleDateFormat df;

    public DefaultSliceStrategy() {
        df = new SimpleDateFormat("yyyyMMddhhmm");
    }

    @Override
    public String key(Date t) {
        return "jtq_slice_" + df.format(t);
    }
}
