package com.limengxiang.jtq.message;

import java.util.Date;

public class DefaultMessage implements MessageInterface {

    private String id;
    private String body;
    private int status;
    private Date schedule;
    private Date receiveAt;
    private Date takeawayAt;

    @Override
    public void setMsgId(String id) {
        this.id = id;
    }

    @Override
    public String getMsgId() {
        return this.id;
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
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
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
    public void setTakeawayAt(Date t) {
        takeawayAt = t;
    }

    @Override
    public Date getTakeawayAt() {
        return takeawayAt;
    }

}
