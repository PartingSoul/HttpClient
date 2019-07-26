package com.parting_soul.http.net.params;

import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.utils.Headers;
import com.parting_soul.http.utils.UrlUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * 将参数写入网络输出流
 *
 * @author parting_soul
 * @date 2019-07-23
 */
public abstract class BaseParamsOutputStrategy {

    public final OutputStream writeParams(HttpURLConnection connection, BaseRequest request) throws IOException {
        connection.setRequestProperty(Headers.CONTENT_TYPE, getContentType());
        connection.setRequestProperty(Headers.ACCEPT_CHARSET, UrlUtils.UTF_8);
        BufferedOutputStream httpBos = new BufferedOutputStream(connection.getOutputStream());
        onWriteParams(httpBos, request);
        httpBos.flush();
        return httpBos;
    }

    /**
     * 设置请求体内容类型
     *
     * @return
     */
    protected abstract String getContentType();


    protected abstract void onWriteParams(OutputStream writer, BaseRequest request) throws IOException;
}
