package com.parting_soul.httputilsdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parting_soul.http.bean.FilePair;
import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.OnHttpCallback;
import com.parting_soul.http.net.request.FormRequest;
import com.parting_soul.http.net.request.JsonRequest;
import com.parting_soul.http.net.request.MultipartRequest;
import com.parting_soul.http.utils.LogUtils;

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
    private EditText mEtVerifyCode;
    public String PIC_PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv_img);
        mTvMsg = findViewById(R.id.tv_msg);
        mEtVerifyCode = findViewById(R.id.et_verify_code);
        PIC_PATH = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/book1.jpg";
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_get_img:
                getImage();
                break;
            case R.id.bt_get_text:
                getInfo();
                break;
            case R.id.bt_post_text:
                getInfoByPost();
                break;
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_uploadImg:
                uploadImage();
                break;
            case R.id.bt_add_to_shelf:
                addToShelf();
                break;
            default:
                break;
        }
    }

    /**
     * 加入书架
     */
    private void addToShelf() {
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
            public void onStart() {

            }

            @Override
            public void onSuccess(Response response) {
                LogUtils.d(response.getString());
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
                .addParam("sex", 0);

        HttpManager.post(request, new OnHttpCallback() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Response response) {
                LogUtils.d(response.getString());
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }

    private void login() {
        final String code = mEtVerifyCode.getText().toString();
        final FormRequest request = new FormRequest(Config.LOGIN);
        request.addParam("mobile", "xxx")
                .addParam("code", code);

        HttpManager.post(request, new OnHttpCallback() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Response response) {
                final String result = response.getString();
                LogUtils.d("" + result);
                Gson gson = new Gson();
                final LoginBean bean = gson.fromJson(result, LoginBean.class);
                if (200 == bean.getCode()) {
                    Config.saveUserInfo(bean);
                    mTvMsg.setText(bean.getData().getSession());
                    Toast.makeText(App.getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }


    private void getInfoByPost() {
        final FormRequest request = new FormRequest("http://ip.tianqiapi.com/");
        request.addParam("ip", "27.193.13.255");

        HttpManager.post(request, new OnHttpCallback() {

            @Override
            public void onStart() {

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

    private void getInfo() {
        final FormRequest request = new FormRequest("http://gank.io/api/xiandu/categories");
        request.addParam("type", "collectionsort")
                .addParam("version", "15.5.0");
        HttpManager.get(request, new OnHttpCallback() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Response response) {
                LogUtils.d(response.getString());
            }

            @Override
            public void onFailed(int code, String error) {
                LogUtils.e("code = " + code + " error = " + error);
            }
        });
    }

    private void getImage() {
        final FormRequest request = new FormRequest("https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg");
        HttpManager.get(request, new OnHttpCallback() {

            @Override
            public void onStart() {
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


}
