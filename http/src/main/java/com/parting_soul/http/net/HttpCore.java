package com.parting_soul.http.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.parting_soul.http.bean.Response;
import com.parting_soul.http.net.exception.HttpRequestException;
import com.parting_soul.http.net.params.BaseParamsOutputStrategy;
import com.parting_soul.http.net.params.ParamsOutputStrategyFactory;
import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.utils.AllowX509TrustManager;
import com.parting_soul.http.utils.FileUtils;
import com.parting_soul.http.utils.Headers;
import com.parting_soul.http.utils.UrlUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-22
 */
public class HttpCore {

    /**
     * get 请求
     *
     * @param httpConfig
     * @param request
     */
    public static Response get(HttpConfig httpConfig, @NonNull BaseRequest request) throws HttpRequestException {
        Response response;
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream bos = null;

        String requestUrl = request.getUrl();
        String params = UrlUtils.buildParams(request.getParams());

        //拼接url参数
        if (!TextUtils.isEmpty(params)) {
            if (!requestUrl.contains("?")) {
                requestUrl += "?";
            }
            requestUrl += params;
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.GET);
            connection.setConnectTimeout(httpConfig.getConnectionTimeout());
            connection.setReadTimeout(httpConfig.getReadSocketTimeout());
            connection.setDoInput(true);

            //设置header
            setHeaders(connection, request.getHeaders());
            //设置支持的编码方式
            connection.setRequestProperty(Headers.ACCEPT_CHARSET, UrlUtils.UTF_8);

            //连接
            connection.connect();

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                bufferedInputStream = new BufferedInputStream(inputStream);

                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;

                while ((len = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, len);
                }

                response = Response.create(bos.toByteArray(), UrlUtils.UTF_8);
            } else {
                throw new HttpRequestException(responseCode, responseMessage);
            }
        } catch (Exception e) {
            throw e instanceof HttpRequestException ? (HttpRequestException) e : new HttpRequestException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            FileUtils.closeQuietly(bufferedInputStream, bos);
        }
        return response;
    }

    /**
     * 设置Header
     *
     * @param connection
     * @param headers
     */
    public static void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }


    /**
     * post请求
     *
     * @param httpConfig
     * @param request
     */
    public static Response post(HttpConfig httpConfig, @NonNull BaseRequest request) throws HttpRequestException {
        Response response;
        HttpURLConnection connection = null;
        OutputStream httpBos = null;
        BufferedInputStream httpBis = null;
        ByteArrayOutputStream bos = null;

        String requestUrl = request.getUrl();

        try {
            URL url = new URL(requestUrl);
            if (httpConfig.isTrustAllCertificate()) {
                AllowX509TrustManager.allowAllSSL();
            }
            //得到connection对象。
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod(HttpMethod.POST);
            connection.setConnectTimeout(httpConfig.getConnectionTimeout());
            connection.setReadTimeout(httpConfig.getReadSocketTimeout());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            Map<String, String> headers = request.getHeaders();
            //设置header
            setHeaders(connection, headers);

            //将数据写入输出流
            BaseParamsOutputStrategy outputStrategy = ParamsOutputStrategyFactory.getParamsOutputStrategy(request);
            httpBos = outputStrategy.writeParams(connection, request);

            //连接
            connection.connect();

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                httpBis = new BufferedInputStream(inputStream);
                bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int len = -1;

                while ((len = httpBis.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, len);
                }
                response = Response.create(bos.toByteArray(), UrlUtils.UTF_8);
            } else {
                throw new HttpRequestException(responseCode, responseMessage);
            }

        } catch (Exception e) {
            throw e instanceof HttpRequestException ? (HttpRequestException) e : new HttpRequestException(e);
        } finally {
            FileUtils.closeQuietly(httpBis, httpBos, bos);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }


    public static Response request(HttpConfig httpConfig, BaseRequest request) throws HttpRequestException {
        Response response;
        switch (request.getRequestMethod()) {
            case HttpMethod.GET:
                response = get(httpConfig, request);
                break;
            default:
                response = post(httpConfig, request);
                break;
        }
        return response;
    }

}
