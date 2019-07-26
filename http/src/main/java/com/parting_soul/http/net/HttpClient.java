package com.parting_soul.http.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.net.task.HttpTask;
import com.parting_soul.http.threadpool.ThreadPoolManager;

import java.net.HttpURLConnection;


/**
 * @author parting_soul
 * @date 2019-07-25
 */
public class HttpClient {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public final void post(@NonNull BaseRequest request, OnHttpCallback callback) {
        request.setRequestMethod(HttpMethod.POST);
        request(request, callback);
    }

    public final Response post(@NonNull BaseRequest request) {
        return HttpCore.post(request);
    }

    public final Response get(@NonNull BaseRequest request) {
        return HttpCore.get(request);
    }

    public final void get(@NonNull BaseRequest request, OnHttpCallback callback) {
        request.setRequestMethod(HttpMethod.GET);
        request(request, callback);
    }

    private void request(@NonNull BaseRequest request, final OnHttpCallback callback) {
        request.setOnRequestCallback(new OnRequestCallback() {
            @Override
            public void onResponse(final Response response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        processCallback(response, callback);
                    }
                });
            }
        });
        HttpTask task = new HttpTask(request);
        if (canCallback()) {
            callback.onStart();
        }
        ThreadPoolManager.getInstance().addTask(task);
    }


    /**
     * 处理结果回调
     *
     * @param response
     * @param callback
     */
    private void processCallback(Response response, OnHttpCallback callback) {
        if (!canCallback()) {
            return;
        }
        if (response.getCode() == HttpURLConnection.HTTP_OK) {
            callback.onSuccess(response);
        } else {
            callback.onFailed(response.getCode(), response.getError());
        }
    }

    /**
     * 是否满足回调要求
     *
     * @return
     */
    private boolean canCallback() {
        return true;
    }

}
