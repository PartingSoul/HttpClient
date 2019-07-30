package com.parting_soul.httputilsdemo.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.HttpClient;
import com.parting_soul.http.net.HttpConfig;
import com.parting_soul.http.net.OnHttpCallback;
import com.parting_soul.http.net.exception.HttpRequestException;
import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.httputilsdemo.App;

import java.util.HashMap;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public class HttpManager {
    private static HttpClient sHttpClient = new HttpClient();

    static {
        Map<String, String> headers = new HashMap<>();
        headers.put("machine", MachineManager.instance().getMachine(App.getContext()));
        headers.put("device", Build.MODEL);
        headers.put("terminal", "0");
        headers.put("app", App.getContext().getPackageName());
        headers.put("version", "1.0");
        headers.put("sys_version", String.valueOf(Build.VERSION.SDK_INT));
        headers.put("Authorization"
                , Base64.encodeToString("96dca22b408c4cd49c887b33093d4a40".getBytes(), Base64.NO_WRAP).trim());
        sHttpClient.setHeaders(headers);


        // 可自行配置，不配置则使用默认配置
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setRetryCount(2);
        httpConfig.setRetryDelayTime(2000);
        httpConfig.setConnectTimeout(8 * 1000);
        httpConfig.setReadTimeout(8 * 1000);
        sHttpClient.setHttpConfig(httpConfig);
    }

    public static void get(@NonNull BaseRequest request, OnHttpCallback listener) {
        sHttpClient.get(request, listener);
    }

    public static void post(@NonNull BaseRequest request, OnHttpCallback listener) {
        sHttpClient.post(request, listener);
    }

    public static Response get(@NonNull BaseRequest request) throws HttpRequestException {
        return sHttpClient.get(request);
    }

    public static Response post(@NonNull BaseRequest request) throws HttpRequestException {
        return sHttpClient.post(request);
    }

}
