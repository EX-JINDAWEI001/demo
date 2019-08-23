package com.example.demo.common.asyn;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Component
public class AsyncHandler {

    public static ExecutorService executor;

    @PostConstruct
    public void afterPropertiesSet() {
        int processors = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor( processors * 2, processors * 3,
                300, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(128),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @PreDestroy
    public void destroy() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    public <T> T getFutureResult(Future<T> future) throws InterruptedException,
            ExecutionException, TimeoutException {
        return future.get(200, TimeUnit.MILLISECONDS);
    }

}
