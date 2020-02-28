package com.parting_soul.http.threadpool;

import android.util.Log;

import com.parting_soul.http.net.task.HttpTask;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Future;
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
    private volatile static ThreadPoolManager sThreadPoolManager;

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
    private final BlockingQueue<Runnable> mPoolWorkBlockingQueue =
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

    // TODO: 2019-07-29 待优化 若请求的任务相同
    /**
     * 任务与任务状态map
     */
    private Map<Runnable, Future> mTaskStateMap = new ConcurrentHashMap<>();

    /**
     * 调度者，将请求队列中的任务请求取出，分配给线程池中的线程
     */
    private Runnable mDispatcher = new Runnable() {
        @Override
        public void run() {
            Runnable task;
            for (; ; ) {
                try {
                    task = mTaskQueue.take();
                    Future future = THREAD_POOL_EXECUTOR.submit(task);
                    mTaskStateMap.put(task, future);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * 错误重试延迟队列
     */
    private DelayQueue<HttpTask> mErrorRetryQueue = new DelayQueue<>();


    /**
     * 重试的线程调度
     */
    private Runnable mRetryDispatcher = new Runnable() {
        @Override
        public void run() {
            HttpTask task;
            for (; ; ) {
                try {
                    task = mErrorRetryQueue.take();
                    if (task.isRetry()) {
                        Future future = THREAD_POOL_EXECUTOR.submit(task);
                        task.retryRecord();
                        mTaskStateMap.put(task, future);
                        Log.e("Retry===>", "retry " + task);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private ThreadPoolManager() {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                mPoolWorkBlockingQueue, sThreadFactory, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                addTask(r);
            }
        });

        //启动调度线程
        THREAD_POOL_EXECUTOR.execute(mDispatcher);
        //错误重试调度线程
        THREAD_POOL_EXECUTOR.execute(mRetryDispatcher);
    }

    /**
     * 设置线程池阻塞队列满时的拒绝策略
     *
     * @param handler
     */
    public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
        if (handler != null) {
            THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(handler);
        }
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
        try {
            mTaskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消任务
     *
     * @param task
     */
    public void cancel(Runnable task) {
        mTaskQueue.remove(task);
        mErrorRetryQueue.remove(task);
        Future future = mTaskStateMap.get(task);
        if (future != null) {
            future.cancel(true);
            removeTaskInCache(task);
        }
    }


    /**
     * 添加延迟任务
     */
    public void addDelayTask(HttpTask task) {
        if (task == null) {
            return;
        }
        mErrorRetryQueue.put(task);
    }

    /**
     * 移除缓存的任务
     *
     * @param task
     */
    public void removeTaskInCache(Runnable task) {
        if (mTaskStateMap != null) {
            mTaskStateMap.remove(task);
        }
    }


    public ThreadPoolExecutor getThreadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }

}
