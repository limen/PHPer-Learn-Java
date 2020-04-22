package com.limengxiang.basics.model;

import com.limengxiang.jtq.message.MessageInterface;

import java.util.Date;

public class MessageModel implements MessageInterface {

    private String id;
    private String body;
    private int status;
    private Date schedule;
    private Date receiveAt;
    private Date takeawayAt;

    public MessageModel() {
        id = "hello,world!";
        status = 0;
        receiveAt = new Date();
    }

    @Override
    public String getMsgId() {
        return id;
    }

    @Override
    public void setMsgId(String id) {
        this.id = id;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public Date getSchedule() {
        return schedule;
    }

    @Override
    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    @Override
    public Date getReceiveAt() {
        return receiveAt;
    }

    @Override
    public void setReceiveAt(Date receivedAt) {
        this.receiveAt = receivedAt;
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
