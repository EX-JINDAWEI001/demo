package com.example.demo.common.asyn;

import java.util.concurrent.*;

/**
 * 通过单例模式初始化线程池
 */
public class AsyncHandlerSingleton {
    // 线程池;
    public ExecutorService executor;
    // 唯一实例;
    private static volatile AsyncHandlerSingleton instance;
    // 特殊的锁变量;
    private static final byte[] lock = new byte[0];

    // 无参构造(屏蔽);
    private AsyncHandlerSingleton() {
        System.out.println("init instance");
        int processors = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                processors * 2,
                processors * 5,
                300,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(128),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    // 获取实例;
    public static AsyncHandlerSingleton getInstance() {
        // 第1次校验;
        if (instance == null) {
            // 同步锁synchronized加载代码区块;
            synchronized (lock) {
                // 第2次校验;
                if (instance == null) {
                    instance = new AsyncHandlerSingleton();
                }
            }
        }
        return instance;
    }

    public static <T> T getFutureResult(Future<T> future) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(200, TimeUnit.MILLISECONDS);
    }

    /*public static void main(String[] args) {
        AsyncHandlerSingleton.getInstance().executor.submit(() -> {
            try {
                System.out.println("啦啦啦啦啦啦啦啦啦啦啦111111。。。。。。。。");
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    System.out.println(1);
                    AsyncHandlerSingleton.getInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        AsyncHandlerSingleton.getInstance().executor.submit(() -> {
            try {
                System.out.println("啦啦啦啦啦啦啦啦啦啦啦222222。。。。。。。。");
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    System.out.println(2);
                    AsyncHandlerSingleton.getInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }*/
}
