package com.parting_soul.http.net.task;

import com.parting_soul.http.bean.Response;

/**
 * @author parting_soul
 * @date 2019-07-29
 */
interface TaskState {
    void onStart();

    void onSuccess(Response response);

    void onFailed(int code, String error);
}
