package com.limengxiang.basics.dao;

import com.limengxiang.jtq.slice.TimingSlicerInterface;

import java.util.Date;

public class TimingSlicerDAO implements TimingSlicerInterface {
    @Override
    public boolean push(String id, Date t) {
        return false;
    }

    @Override
    public String pull(Date date) {
        return "abc";
    }
}
