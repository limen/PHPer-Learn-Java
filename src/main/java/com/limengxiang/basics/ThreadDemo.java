package com.limengxiang.basics;

/**
 * 线程状态demo
 * 能够短暂地观察到 TIME_WAITED 状态
 */

class Mutex {
    synchronized void talk(Thread t) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Hello, I am thread #%d\n", t.getId());
    }
}

class MyThread extends Thread {

    private Mutex mt;

    MyThread(Mutex m) {
        mt = m;
    }

    @Override
    public void run() {
        mt.talk(this);
    }
}

class ThreadWatcher extends Thread {

    private MyThread[] threads;

    ThreadWatcher(MyThread ...ths) {
        threads = ths;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 线程进入TIME_WAITED状态，等待1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Watcher ID: %d, State: %s\n", this.getId(), this.getState());
            for (MyThread t : threads) {
                System.out.printf("Watched thread ID: %d, State: %s\n", t.getId(), t.getState());
            }
        }
    }
}

public class ThreadDemo {

    public static void main(String[] args) {
        Mutex mut = new Mutex();
        MyThread th1, th2, th3;
        th1 = new MyThread(mut);
        th2 = new MyThread(mut);
        th3 = new MyThread(mut);
        ThreadWatcher tw = new ThreadWatcher(th1, th2, th3);
        th1.start();
        th2.start();
        th3.start();
        tw.start();
    }
}
