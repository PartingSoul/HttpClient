package com.parting_soul.httputilsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.OnHttpCallback;
import com.parting_soul.http.net.disposables.CompositeDisposable;
import com.parting_soul.http.net.disposables.Disposable;
import com.parting_soul.http.net.request.FormRequest;
import com.parting_soul.httputilsdemo.utils.HttpManager;
import com.parting_soul.httputilsdemo.utils.LogUtils;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public class ActivityB extends AppCompatActivity {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b);
        tv = findViewById(R.id.tv);
//
//        for (int i = 0; i < 200; i++) {
//            runTask();
//        }

        runTask();
        runTask();
        runTask();

    }

    private void runTask() {
        final FormRequest request = new FormRequest("http://gank.io/api/xiandu/categoriedds");
        request.addParam("type", "collectionsort")
                .addParam("version", "15.5.0");
        HttpManager.get(request, new OnHttpCallback() {

            @Override
            public void onStart(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }

            @Override
            public void onSuccess(Response response) {
//                count++;
                tv.setText(response.getString());
                LogUtils.e("success ");
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

}
