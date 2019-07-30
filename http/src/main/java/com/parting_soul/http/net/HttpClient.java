package com.parting_soul.http.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.exception.HttpRequestException;
import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.net.task.HttpTask;
import com.parting_soul.http.threadpool.ThreadPoolManager;

import java.util.HashMap;
import java.util.Map;


/**
 * @author parting_soul
 * @date 2019-07-25
 */
public final class HttpClient {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Map<String, String> mHeaders;
    private HttpConfig mHttpConfig = new HttpConfig();


    public void setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
    }

    public final void post(@NonNull BaseRequest request, OnHttpCallback callback) {
        request.setRequestMethod(HttpMethod.POST);
        request(request, callback);
    }

    public final Response post(@NonNull BaseRequest request) throws HttpRequestException {
        return HttpCore.post(mHttpConfig, request);
    }

    public final Response get(@NonNull BaseRequest request) throws HttpRequestException {
        return HttpCore.get(mHttpConfig, request);
    }

    public final void get(@NonNull BaseRequest request, OnHttpCallback callback) {
        request.setRequestMethod(HttpMethod.GET);
        request(request, callback);
    }

    private void request(@NonNull BaseRequest request, final OnHttpCallback callback) {
        addHeaders(request);
        if (callback != null) {
            request.setOnHttpCallback(callback);
        }
        HttpTask task = new HttpTask(mHandler, mHttpConfig, request);
        ThreadPoolManager.getInstance().addTask(task);
    }


    /**
     * 添加请求头
     *
     * @param request
     */
    private void addHeaders(@NonNull BaseRequest request) {
        if (mHeaders == null) {
            return;
        }

        Map<String, String> requestHeaders = request.getHeaders();
        if (requestHeaders == null || requestHeaders.isEmpty()) {
            requestHeaders = new HashMap<>();
        }

        for (Map.Entry<String, String> commEntry : mHeaders.entrySet()) {
            String key = commEntry.getKey();
            String value = commEntry.getValue();

            if (requestHeaders.containsKey(key) &&
                    !TextUtils.isEmpty(requestHeaders.get(key))) {
                //过滤,代码中已经设置了对应的值,以代码中参数的值为准
                continue;
            }
            requestHeaders.put(key, value);
        }
        request.setHeaders(requestHeaders);
    }

    /**
     * 设置配置信息
     *
     * @param httpConfig
     */
    public void setHttpConfig(@NonNull HttpConfig httpConfig) {
        this.mHttpConfig = httpConfig;
    }

}
