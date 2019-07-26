package com.parting_soul.http.bean;

import com.parting_soul.http.utils.UrlUtils;

import java.net.HttpURLConnection;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public final class Response {
    private String string;

    private byte[] bytes;

    private int code;

    private String error;

    private String charset = UrlUtils.UTF_8;

    public String getString() {
        if (string == null && bytes != null) {
            string = new String(bytes);
        }
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    private Response(byte[] data, String charset) {
        this.bytes = data;
        this.charset = charset;
        this.code = HttpURLConnection.HTTP_OK;
    }

    private Response(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public Response() {
    }

    public static Response create(byte[] data, String charset) {
        return new Response(data, charset);
    }

    public static Response create(int code, String error) {
        return new Response(code, error);
    }
}
