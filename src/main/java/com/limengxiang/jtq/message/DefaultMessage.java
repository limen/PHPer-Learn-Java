package com.limengxiang.jtq.message;

import java.util.Date;

public class DefaultMessage implements MessageInterface {

    private String msgId;
    private String body;
    private Integer status;
    private Date schedule;
    private Date receiveAt;
    private Date consumeAt;

    @Override
    public void setMsgId(String id) {
        this.msgId = id;
    }

    @Override
    public String getMsgId() {
        return this.msgId;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }

    @Override
    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    @Override
    public Date getSchedule() {
        return this.schedule;
    }

    @Override
    public void setReceiveAt(Date t) {
        this.receiveAt = t;
    }

    @Override
    public Date getReceiveAt() {
        return this.receiveAt;
    }

    @Override
    public void setConsumeAt(Date t) {
        consumeAt = t;
    }

    @Override
    public Date getConsumeAt() {
        return consumeAt;
    }

}
