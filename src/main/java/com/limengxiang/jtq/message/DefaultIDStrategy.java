package com.limengxiang.jtq.message;

import com.limengxiang.xid.Generator;

/**
 * TODO 唯一ID生成算法
 */
public class DefaultIDStrategy implements IDStrategyInterface {

    @Override
    public String id() {
        try {
            return Generator.gen().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
