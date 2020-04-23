package com.limengxiang.jtq.slice;

import java.util.Date;

public interface TimingSlicerInterface {

    public Long push(String id, Date t);

    public String pull(Date date);

}
