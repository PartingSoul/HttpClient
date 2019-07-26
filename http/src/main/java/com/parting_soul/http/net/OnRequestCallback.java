package com.parting_soul.http.net;

import com.parting_soul.http.bean.Response;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public interface OnRequestCallback {
    void onResponse(Response response);
}
