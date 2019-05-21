package com.example.demo.common;

import java.util.concurrent.Callable;

public abstract class AsyncTask<T> implements Callable<T> {

    public abstract T doTask();

    @Override
    public T call() {
        try {
            return doTask();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
