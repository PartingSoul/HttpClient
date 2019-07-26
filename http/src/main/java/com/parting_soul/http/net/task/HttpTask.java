package com.parting_soul.http.net.task;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parting_soul.http.net.request.BaseRequest;

/**
 * @author parting_soul
 * @date 2019-07-25
 */
public class HttpTask implements Runnable {
    private BaseRequest mRequest;

    public HttpTask(@NonNull BaseRequest request) {
        this.mRequest = request;
    }

    @Override
    public void run() {
        if (mRequest == null) {
            return;
        }
        Log.e("run====> ", Thread.currentThread().getName());
        mRequest.execute();
    }

}
