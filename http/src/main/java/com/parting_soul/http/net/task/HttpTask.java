package com.parting_soul.http.net.task;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.HttpConfig;
import com.parting_soul.http.net.HttpCore;
import com.parting_soul.http.net.OnHttpCallback;
import com.parting_soul.http.net.disposables.Disposable;
import com.parting_soul.http.net.exception.HttpRequestException;
import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.threadpool.ThreadPoolManager;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author parting_soul
 * @date 2019-07-25
 */
public class HttpTask implements Runnable, TaskState, Disposable, Delayed {
    private int DEFAULT_DELAY_COUNT;
    private AtomicBoolean isDisposed = new AtomicBoolean(false);
    private BaseRequest mRequest;
    private OnHttpCallback mHttpCallback;
    private Handler mHandler;
    private HttpConfig mHttpConfig;

    /**
     * 失败重试次数
     */
    private int mRetryCount;

    /**
     * 重试延迟时间
     */
    private long delayTime;


    public HttpTask(Handler handler, HttpConfig httpConfig, @NonNull BaseRequest request) {
        this.mRequest = request;
        this.mHandler = handler;
        this.mHttpCallback = mRequest.getOnHttpCallback();
        this.mHttpConfig = httpConfig;
        DEFAULT_DELAY_COUNT = mHttpConfig.getRetryCount();
        onStart();
    }

    @Override
    public void run() {
        if (isDisposed.get()) {
            //任务被丢弃
            ThreadPoolManager.getInstance().cancel(this);
            mRequest.setOnHttpCallback(null);
            mRequest = null;
            mHttpCallback = null;
            Log.d("HttpTask===>>run ", "discard");
        } else {
            //正常执行请求
            Response response = null;

            try {
                response = HttpCore.request(mHttpConfig, mRequest);
                onSuccess(response);
                //任务执行完成，移除缓存
                ThreadPoolManager.getInstance().removeTaskInCache(this);
                Log.d("HttpTask===>>run ", Thread.currentThread().getName() + " finish");
            } catch (HttpRequestException e) {
                if (isRetry()) {
                    //重试
                    setDelayTime(mHttpConfig.getRetryDelayTime());
                    ThreadPoolManager.getInstance().addDelayTask(this);
                } else {
                    //重试次数已满，回调错误
                    onFailed(e.getCode(), e.getMessage());
                    ThreadPoolManager.getInstance().removeTaskInCache(this);
                    Log.d("HttpTask===>>run ", Thread.currentThread().getName() + " finish");
                }
            }

        }
    }

    @Override
    public void dispose() {
        isDisposed.set(true);
    }

    @Override
    public boolean isDisposed() {
        return isDisposed.get();
    }

    @Override
    public void onStart() {
        if (!canCallback()) {
            Log.d("HttpTask===>>callback ", "discard");
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHttpCallback.onStart(HttpTask.this);
            }
        });
    }

    @Override
    public void onSuccess(final Response response) {
        if (!canCallback()) {
            Log.d("HttpTask===>>callback ", "discard");
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHttpCallback.onSuccess(response);
            }
        });
    }

    @Override
    public void onFailed(final int code, final String error) {
        if (!canCallback()) {
            Log.d("HttpTask===>>callback ", "discard");
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHttpCallback.onFailed(code, error);
            }
        });
    }

    private boolean canCallback() {
        return mHttpCallback != null && !isDisposed.get();
    }


    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime + System.currentTimeMillis();
    }

    /**
     * 已重试次数加一
     */
    public void retryRecord() {
        mRetryCount++;
    }

    /**
     * 是否重试
     *
     * @return
     */
    public boolean isRetry() {
        return mRetryCount < DEFAULT_DELAY_COUNT;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null || !(o instanceof HttpTask)) return 1;
        HttpTask other = (HttpTask) o;
        return delayTime < other.getDelayTime() ? -1 : delayTime == other.delayTime ? 0 : 1;
    }

}
