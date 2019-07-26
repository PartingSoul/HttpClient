package com.parting_soul.http.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理器
 *
 * @author parting_soul
 * @date 2019-07-25
 */
public class ThreadPoolManager {
    private static ThreadPoolManager sThreadPoolManager;

    /**
     * cpu核心数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    /**
     * 最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * 非核心线程闲置最大闲置时长
     */
    private static final int KEEP_ALIVE_SECONDS = 30;

    /**
     * 线程池任务阻塞队列
     */
    private final BlockingQueue<Runnable> mPoolWorkQueue =
            new LinkedBlockingQueue<>(128);

    /**
     * 线程池
     */
    private final ThreadPoolExecutor THREAD_POOL_EXECUTOR;


    /**
     * 线程工厂
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Task #" + mCount.getAndIncrement());
        }
    };


    /**
     * 任务队列
     */
    private LinkedBlockingDeque<Runnable> mTaskQueue = new LinkedBlockingDeque<>();


    /**
     * 调度者，将请求队列中的任务请求取出，分配给线程池中的线程
     */
    private Runnable mDispatcher = new Runnable() {
        @Override
        public void run() {
            Runnable task;
            for (; ; ) {
                // TODO: 2019-07-25  待优化 若被打断
                try {
                    task = mTaskQueue.take();
                    THREAD_POOL_EXECUTOR.execute(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private ThreadPoolManager() {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                mPoolWorkQueue, sThreadFactory, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //线程达到最大数并且任务队列满或或者其他未知错误
                addTask(r);
            }
        });

        //启动调度线程
        THREAD_POOL_EXECUTOR.execute(mDispatcher);
    }

    public static ThreadPoolManager getInstance() {
        if (sThreadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (sThreadPoolManager == null) {
                    sThreadPoolManager = new ThreadPoolManager();
                }
            }
        }
        return sThreadPoolManager;
    }


    /**
     * 添加任务
     *
     * @param task
     */
    public void addTask(Runnable task) {
        if (task == null) {
            return;
        }
        // TODO: 2019-07-25  待优化 若被打断
        try {
            mTaskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
