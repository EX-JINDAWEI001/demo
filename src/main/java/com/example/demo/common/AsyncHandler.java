package com.example.demo.common;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Component
public class AsyncHandler {

    public ExecutorService executor;

    @PostConstruct
    public void afterPropertiesSet() {
        int processors = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(processors * 2, processors * 2 + 1,
                1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(128));
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
