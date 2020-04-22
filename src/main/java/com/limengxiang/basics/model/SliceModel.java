package com.limengxiang.basics.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "jtq_slice")
public class SliceModel implements Serializable {

    @Id
    private ObjectId id;

    @Field("slice_id")
    private String sliceId;

    @Field("msg_ids")
    private ArrayList<String> msgIds;

    public String getSliceId() {
        return sliceId;
    }

    public void setSliceId(String sliceId) {
        this.sliceId = sliceId;
    }

    public ArrayList<String> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(ArrayList<String> msgIds) {
        this.msgIds = msgIds;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
