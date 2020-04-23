package com.limengxiang.basics.controller;

import com.limengxiang.jtq.TimingQueue;
import com.limengxiang.jtq.message.DefaultIDStrategy;
import com.limengxiang.jtq.message.DefaultMessage;
import com.limengxiang.jtq.queue.DefaultQueueStorage;
import com.limengxiang.jtq.slice.DefaultTimingSlicer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
public class TimingQueueController {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private TimingQueue timingQueue;

    @Bean
    public TimingQueue getTimingQueue() throws IOException {
        InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(stream).openSession(true);
        DefaultQueueStorage queueDAO = sqlSession.getMapper(DefaultQueueStorage.class);

        timingQueue = new TimingQueue("security_remind");
        timingQueue.setQueueStorage(queueDAO);
        timingQueue.setIdStrategy(new DefaultIDStrategy());
        timingQueue.setSlicer(new DefaultTimingSlicer(redisTemplate));

        return timingQueue;
    }

    @RequestMapping(value = "/tq-push")
    public String push(@RequestParam("body") String body) {
        DefaultMessage msg = new DefaultMessage();
        msg.setBody(body);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        msg.setSchedule(cal.getTime());
        msg.setStatus(TimingQueue.StatusWaiting);
        return timingQueue.push(msg);
    }

    @RequestMapping(value = "/tq-get")
    public Object get(@RequestParam("time") String time) {
        System.out.println("Pull messages at time: " + time);
        SimpleDateFormat ft = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        try {
            Date t = ft.parse(time);
            return timingQueue.pull(t);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
