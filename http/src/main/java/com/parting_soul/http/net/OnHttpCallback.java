package com.parting_soul.http.net;

import com.parting_soul.http.bean.Response;

public interface OnHttpCallback {

    /**
     * 在主线程中调用
     */
    void onStart();

    void onSuccess(Response response);

    void onFailed(int code, String error);
}