package com.parting_soul.http.net.request;

import com.parting_soul.http.annotation.HttpMethodRule;
import com.parting_soul.http.bean.FilePair;
import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.HttpCore;
import com.parting_soul.http.net.HttpMethod;
import com.parting_soul.http.net.OnRequestCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public abstract class BaseRequest<E extends BaseRequest> {
    private Map<String, Object> mParams;
    private Map<String, String> mHeaders;
    private String url;
    private OnRequestCallback mRequestCallback;
    private String requestMethod = HttpMethod.GET;

    public BaseRequest(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return mParams;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Map<String, FilePair> getFilePairs() {
        return null;
    }


    public String getJson() {
        return null;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * 设置请求方式
     *
     * @param requestMethod
     */
    public E setRequestMethod(@HttpMethodRule String requestMethod) {
        this.requestMethod = requestMethod;
        return (E) this;
    }

    /**
     * 请求地址
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求参数
     *
     * @param params
     * @return
     */
    public E setParams(Map<String, Object> params) {
        this.mParams = params;
        return (E) this;
    }

    /**
     * 设置请求头
     *
     * @param headers
     * @return
     */
    public E setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
        return (E) this;
    }

    /**
     * 设置请求路径
     *
     * @param url
     * @return
     */
    public E setUrl(String url) {
        this.url = url;
        return (E) this;
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public E addParam(String key, Object value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        mParams.put(key, value);
        return (E) this;
    }

    /**
     * 添加请求头
     *
     * @param key
     * @param value
     * @return
     */
    public E addHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.put(key, value);
        return (E) this;
    }

    public OnRequestCallback getOnRequestCallback() {
        return mRequestCallback;
    }

    public BaseRequest setOnRequestCallback(OnRequestCallback callback) {
        this.mRequestCallback = callback;
        return this;
    }

    /**
     * 执行请求
     */
    public final void execute() {
        Response response = HttpCore.request(this);
        if (mRequestCallback != null) {
            mRequestCallback.onResponse(response);
        }
    }

}
