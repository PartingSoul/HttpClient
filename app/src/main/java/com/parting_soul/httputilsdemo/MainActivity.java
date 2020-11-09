package com.parting_soul.httputilsdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parting_soul.http.bean.FilePair;
import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.HttpClient;
import com.parting_soul.http.net.HttpCore;
import com.parting_soul.http.net.OnHttpCallback;
import com.parting_soul.http.net.disposables.CompositeDisposable;
import com.parting_soul.http.net.disposables.Disposable;
import com.parting_soul.http.net.exception.HttpRequestException;
import com.parting_soul.http.net.request.FormRequest;
import com.parting_soul.http.net.request.JsonRequest;
import com.parting_soul.http.net.request.MultipartRequest;
import com.parting_soul.http.threadpool.ThreadPoolManager;
import com.parting_soul.httputilsdemo.utils.HttpManager;
import com.parting_soul.httputilsdemo.utils.LogUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author parting_soul
 */
public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView mTvMsg;
    public String PIC_PATH;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv_img);
        mTvMsg = findViewById(R.id.tv_msg);
        PIC_PATH = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/book1.jpg";
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_get_img_by_async:
                getImageByAsync();
                break;
            case R.id.bt_get_img_by_sync:
                getImageBySync();
                break;
            case R.id.bt_post_text_by_async:
                postInfoByAsync();
                break;
            case R.id.bt_post_text_by_sync:
                postInfoBySync();
                break;
            case R.id.bt_uploadImg:
                uploadImage();
                break;
            case R.id.bt_post_json_by_async:
                postJsonByAsync();
                break;
            case R.id.bt_exit_stop_request:
                startActivity(new Intent(this, ActivityB.class));
                break;
            default:
                break;
        }
    }

    private void getImageByAsync() {
        final FormRequest request = new FormRequest("https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg");
        request.addHeader("header", "12112");
        HttpManager.get(request, new OnHttpCallback() {

            @Override
            public void onStart(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }

            @Override
            public void onSuccess(Response response) {
                byte[] data = response.getBytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(PIC_PATH));
                    iv.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }

    private void getImageBySync() {
        final FormRequest request = new FormRequest("http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg");
        ThreadPoolManager.getInstance().getThreadPoolExecutor()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = HttpManager.get(request);
                            byte[] data = response.getBytes();
                            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            iv.post(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(bitmap);
                                }
                            });
                        } catch (HttpRequestException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void postInfoByAsync() {
        final FormRequest request = new FormRequest("http://ip.tianqiapi.com/");
        request.addParam("ip", "27.193.13.255")
                .addHeader("app", "app")
                .addHeader("machine", "machine");

        HttpManager.post(request, new OnHttpCallback() {

            @Override
            public void onStart(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }

            @Override
            public void onSuccess(Response response) {
                final String result = response.getString();
                LogUtils.d("" + result);
                mTvMsg.setText(result);
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }

    private void postInfoBySync() {
        final FormRequest request = new FormRequest("https://api.ecook.cn/public/loginByMobileAndCode.shtml");
        request.addParam("ip", "27.193.13.255")
                .addHeader("app", "app")
                .addHeader("machine", "machine");
        ThreadPoolManager.getInstance().getThreadPoolExecutor()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Response response = HttpManager.post(request);
                            mTvMsg.post(new Runnable() {
                                @Override
                                public void run() {
                                    mTvMsg.setText(response.getString());
                                }
                            });
                        } catch (HttpRequestException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 加入书架
     */
    private void postJsonByAsync() {
        List<Map<String, String>> lists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, String> maps = new HashMap<>();
            maps.put("bookId", String.valueOf(35 + i));
            lists.add(maps);
        }
        Gson gson = new Gson();
        String json = gson.toJson(lists);

        final JsonRequest request = new JsonRequest(Config.ADD_TO_SHELF, json);
        HttpManager.post(request, new OnHttpCallback() {

            @Override
            public void onStart(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }

            @Override
            public void onSuccess(Response response) {
                mTvMsg.setText(response.getString());
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }

    private void uploadImage() {
        final MultipartRequest request = new MultipartRequest(Config.UPLOAD_IMG);
        request.addFilePair("avatarFile", new FilePair(PIC_PATH))
                .addParam("nickname", "nickname" + Math.random())
                .addParam("sex", 0)
                .addHeader("eere", "dsds");

        HttpManager.post(request, new OnHttpCallback() {

            @Override
            public void onStart(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }

            @Override
            public void onSuccess(Response response) {
                mTvMsg.setText(response.getString());
                LogUtils.d("" + response.getString());
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
        //及时切断任务
        mCompositeDisposable.dispose();
    }

}
