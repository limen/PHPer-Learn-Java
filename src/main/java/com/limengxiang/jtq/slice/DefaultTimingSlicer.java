package com.limengxiang.jtq.slice;

import java.util.Date;

/**
 * Implement using MongoDB
 */
public class DefaultTimingSlicer implements TimingSlicerInterface {

    private TimingSlicerInterface dao;

    public DefaultTimingSlicer() {

    }

    public DefaultTimingSlicer(TimingSlicerInterface dao) {
        this.dao = dao;
    }

    @Override
    public boolean push(String id, Date timing) {
        return dao.push(id, timing);
    }

    @Override
    public String pull(Date t) {
        return dao.pull(t);
    }

}
