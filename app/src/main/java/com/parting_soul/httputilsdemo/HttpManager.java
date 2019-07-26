package com.parting_soul.httputilsdemo;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.HttpClient;
import com.parting_soul.http.net.OnHttpCallback;
import com.parting_soul.http.net.request.BaseRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public class HttpManager {
    private static HttpClient mHttpClient = new HttpClient();
    private static Map<String, String> mCommHeaders;

    static {
        mCommHeaders = new HashMap<>();
        mCommHeaders.put("machine", MachineManager.instance().getMachine(App.getContext()));
        mCommHeaders.put("device", Build.MODEL);
        mCommHeaders.put("terminal", "0");
        mCommHeaders.put("app", App.getContext().getPackageName());
        mCommHeaders.put("version", "1.0");
        mCommHeaders.put("sys_version", String.valueOf(Build.VERSION.SDK_INT));
        mCommHeaders.put("Authorization"
                , Base64.encodeToString("96dca22b408c4cd49c887b33093d4a40".getBytes(), Base64.NO_WRAP).trim());
    }

    public static void get(@NonNull BaseRequest request, OnHttpCallback listener) {
        addCommonHeaders(request);
        mHttpClient.get(request, listener);
    }

    public static void post(@NonNull BaseRequest request, OnHttpCallback listener) {
        addCommonHeaders(request);
        mHttpClient.post(request, listener);
    }

    public static Response get(@NonNull BaseRequest request) {
        addCommonHeaders(request);
        return mHttpClient.get(request);
    }

    public static Response post(@NonNull BaseRequest request) {
        addCommonHeaders(request);
        return mHttpClient.post(request);
    }

    /**
     * 添加公共请求头
     *
     * @param request
     */
    private static void addCommonHeaders(@NonNull BaseRequest request) {
        Map<String, String> requestHeaders = request.getHeaders();
        if (requestHeaders == null) {
            requestHeaders = new HashMap<>();
        }

        for (Map.Entry<String, String> commEntry : mCommHeaders.entrySet()) {
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


}
