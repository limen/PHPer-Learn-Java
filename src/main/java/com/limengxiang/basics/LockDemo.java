package com.limengxiang.basics;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 排它锁demo
 *
 * 计数器累加测试排它锁
 *
 */
public class LockDemo {
    static class Counter {
        private Integer cnt = 0;

        public Integer getCnt() {
            return cnt;
        }

        public void setCnt(Integer cnt) {
            this.cnt = cnt;
        }
    }

    private static Counter counter = new Counter();
    private static ReentrantLock lock = new ReentrantLock();

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.printf("Thread ID: %d, waiting for lock\n", this.getId());

            // lock 是阻塞的, 拿到锁之后才会继续往下执行
            lock.lock();

            System.out.printf("Thread ID: %d, got lock\n", this.getId());
            try {
                System.out.printf("Thread ID: %d, sleeping\n", this.getId());

                // 线程进入 TIME_WAITED 状态
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.setCnt(counter.getCnt() + 1);
            System.out.printf("Thread ID: %d, Count: %d \n", this.getId(), counter.getCnt());

            // 解除锁定
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MyThread th1 = new MyThread();
        MyThread th2 = new MyThread();
        MyThread th3 = new MyThread();
        MyThread th4 = new MyThread();
        MyThread th5 = new MyThread();
        System.out.println(th1.getId());
        System.out.println(th2.getId());
        System.out.println(th3.getId());
        System.out.println(th4.getId());
        System.out.println(th5.getId());
        th1.start();
        th2.start();
        th3.start();
        th4.start();
        th5.start();
    }
}

