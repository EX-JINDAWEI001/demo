package com.example.demo.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractThread extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(AbstractThread.class);

    private boolean cycle = false;

    public AbstractThread(){
        init(false, this.getClass().getSimpleName() + "_" + System.currentTimeMillis());
    }

    public AbstractThread(boolean cycle) {
        init(cycle, this.getClass().getSimpleName() + "_" + System.currentTimeMillis());
    }

    public void init(boolean cycle, String threadName){
        this.cycle = cycle;
        setName(threadName);
        setDaemon(true);
    }

    @Override
    public void run() {
        do {
            doThreadService();
        } while (cycle);
    }

    protected abstract void doThreadService();

}
