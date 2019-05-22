package com.example.demo.service.impl;

import com.example.demo.common.AsyncTask;
import com.example.demo.service.AsyncDemoService;
import com.example.demo.common.AsyncHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncDemoServiceImpl implements AsyncDemoService {

    Logger logger = LoggerFactory.getLogger(AsyncDemoServiceImpl.class);

    @Autowired
    private AsyncHandler asyncHandler;

    @Override
    public void asyncDemo() throws Exception{
        Future<String> future = doAsyncTask();
        doOtherthing();
        String result = asyncHandler.getFutureResult(future);
        logger.info("asyncDemo msg:{}", result);
    }

    private Future<String> doAsyncTask() {
        return asyncHandler.executor.submit(new AsyncTask<String>() {
            @Override
            public String doTask() {
                try {
                    Thread.sleep(2000);
                    return "AsyncTask is done !!!";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "AsyncTask is interrupted !!!";
                }
            }
        });
    }

    private void doOtherthing() throws InterruptedException {
        Thread.sleep(3000);
    }

}