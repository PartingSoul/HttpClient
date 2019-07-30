package com.parting_soul.http.net.exception;

/**
 * @author parting_soul
 * @date 2019-07-29
 */
public class HttpRequestException extends Exception {
    private int code = -1;
    private String error;


    public HttpRequestException(Throwable cause) {
        super(cause);
    }

    public HttpRequestException(int code, String error) {
        super("Internet request error ,code = " + code + " error =  " + error);
        this.code = code;
        this.error = error;
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


}
