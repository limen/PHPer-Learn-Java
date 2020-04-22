package com.limengxiang.jtq.message;

import java.util.Date;

public interface MessageInterface {

    void setMsgId(String id);

    public String getMsgId();

    public void setBody(String body);

    public String getBody();

    void setStatus(int status);

    public int getStatus();

    public void setSchedule(Date schedule);

    public Date getSchedule();

    void setReceiveAt(Date t);

    public Date getReceiveAt();

    void setTakeawayAt(Date t);

    public Date getTakeawayAt();

}
