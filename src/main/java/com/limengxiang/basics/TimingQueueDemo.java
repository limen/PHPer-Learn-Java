package com.limengxiang.basics;

import com.limengxiang.basics.dao.TimingSlicerDAO;
import com.limengxiang.basics.model.MessageModel;
import com.limengxiang.jtq.TimingQueue;
import com.limengxiang.jtq.queue.DefaultQueue;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class TimingQueueDemo {
    public static void main(String[] args) throws IOException {
        InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(stream).openSession(true);
        DefaultQueue queueDAO = sqlSession.getMapper(DefaultQueue.class);

        TimingQueue timingQueue = new TimingQueue();
        timingQueue.setQueue(queueDAO);
        timingQueue.setSlicer(new TimingSlicerDAO());

        MessageModel msg = new MessageModel();
        msg.setBody("Hello, World!");
        msg.setSchedule(new Date());
        msg.setReceiveAt(new Date());
        msg.setTakeawayAt(new Date());
        msg.setStatus(TimingQueue.StatusWaiting);
        timingQueue.push(msg);
    }
}
