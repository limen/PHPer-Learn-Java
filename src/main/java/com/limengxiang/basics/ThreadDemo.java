package com.limengxiang.basics;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadDemo {

    private int counter;
    private ReentrantLock lock;

    public ThreadDemo(int cnt) {
        counter = cnt;
        lock = new ReentrantLock();
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    class MyThread extends Thread {

        private int id;

        MyThread(int id) {
            this.id = id;
        }

        /**
         * 线程的业务逻辑
         */
        @Override
        public void run() {
            // 加锁
            lock.lock();
            System.out.println("Thread #" + id);
            setCounter(getCounter() + 1);
            // 解锁
            lock.unlock();
        }
    }

    public void run(int num) {
        ArrayList<MyThread> threads = new ArrayList<MyThread>();
        for (int i=0; i<num; i++) {
            threads.add(new MyThread(i));
        }
        // Java版 foreach
        for (MyThread th : threads) {
            th.start();
        }
        // join使主线程等待线程结束, 否则主线程结束时，其它线程可能尚未结束
        for (MyThread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadDemo demo1 = new ThreadDemo(0);
        // 启动10个线程
        demo1.run(10);
        System.out.println("Counter final:" + demo1.getCounter());
    }
}
