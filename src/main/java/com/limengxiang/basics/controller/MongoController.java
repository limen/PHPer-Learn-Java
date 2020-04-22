package com.limengxiang.basics.controller;

import com.limengxiang.basics.model.SliceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MongoController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/mongo-insert", method = RequestMethod.GET)
    public Object insert(@RequestParam("sliceId") String sliceId) {
        SliceModel sliceModel = new SliceModel();
        sliceModel.setSliceId(sliceId);
        ArrayList<String> msgIds = new ArrayList<>();
        msgIds.add("msg1");
        msgIds.add("msg1");
        msgIds.add("msg1");
        msgIds.add("msg1");
        sliceModel.setMsgIds(msgIds);
        mongoTemplate.insert(sliceModel);
        return sliceModel.getId();
    }

    @RequestMapping(value = "/mongo-get", method = RequestMethod.GET)
    public SliceModel get(@RequestParam("sliceId") String sliceId) {
        Criteria criteria = Criteria.where("slice_id").is(sliceId);
        Query q = Query.query(criteria);
        return mongoTemplate.findOne(q, SliceModel.class);
    }

    @RequestMapping(value = "/mongo-push", method = RequestMethod.GET)
    public Object push(@RequestParam("sliceId") String sliceId, @RequestParam("msgId") String msgId) {
        Update update = new Update().push("msg_ids", msgId);
        Query q = Query.query(Criteria.where("slice_id").is(sliceId));
        return mongoTemplate.updateFirst(q, update, SliceModel.class);
    }

    @RequestMapping(value = "/mongo-pop", method = RequestMethod.GET)
    public Object pop(@RequestParam("sliceId") String sliceId) {
        Update update = new Update().pop("msg_ids", Update.Position.FIRST);
        Query q = Query.query(Criteria.where("slice_id").is(sliceId));
        return mongoTemplate.updateFirst(q, update, SliceModel.class);
    }
}
