package com.parting_soul.http.net.params;

import android.text.TextUtils;

import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.net.request.JsonRequest;
import com.parting_soul.http.net.request.MultipartRequest;
import com.parting_soul.http.utils.ContentType;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public class ParamsOutputStrategyFactory {


    //todo 待优化 策略可以复用，避免重复创建多余的对象
    public static BaseParamsOutputStrategy getParamsOutputStrategy(String contentType) {
        BaseParamsOutputStrategy strategy = null;
        if (!TextUtils.isEmpty(contentType) && contentType.startsWith(ContentType.MULTIPART_FORM_DATA)) {
            // multipart/form-data
            strategy = new MultipartFormParamsOutputStrategy();
        } else if (!TextUtils.isEmpty(contentType) && contentType.startsWith(ContentType.APPLICATION_JSON)) {
            // application/json
            strategy = new JsonParamsOutputStrategy();
        } else {
            // application/x-www-form-urlencoded
            strategy = new FormParamsOutputStrategy();
        }
        return strategy;
    }


    public static BaseParamsOutputStrategy getParamsOutputStrategy(BaseRequest request) {
        BaseParamsOutputStrategy strategy = null;
        if (request instanceof MultipartRequest) {
            // multipart/form-data
            strategy = new MultipartFormParamsOutputStrategy();
        } else if (request instanceof JsonRequest) {
            // application/json
            strategy = new JsonParamsOutputStrategy();
        } else {
            // application/x-www-form-urlencoded
            strategy = new FormParamsOutputStrategy();
        }
        return strategy;
    }
}
