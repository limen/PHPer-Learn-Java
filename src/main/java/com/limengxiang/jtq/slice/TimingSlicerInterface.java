package com.limengxiang.jtq.slice;

import java.util.Date;

public interface TimingSlicerInterface {

    public boolean push(String id, Date t);

    public String pull(Date date);

}
