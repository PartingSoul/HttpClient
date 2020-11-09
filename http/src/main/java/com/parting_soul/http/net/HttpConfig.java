package com.parting_soul.http.net;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public class HttpConfig {

    /**
     * 默认错误重试次数
     */
    private int DEFAULT_RETRY_COUNT = 3;

    /**
     * 默认错误重试延迟时间
     */
    private int DEFAULT_RETRY_DELAY_TIME = 2 * 1000;


    /**
     * 连接超时时间
     */
    private int CONNECTION_TIMEOUT = 10 * 1000;

    /**
     * 读取超时时间
     */
    private int READ_SOCKET_TIMEOUT = 10 * 1000;

    /**
     * 信任所有证书
     */
    private boolean isTrustAllCertificate = false;

    /**
     * 设置连接超时时间
     *
     * @param milliseconds
     */
    public void setConnectTimeout(int milliseconds) {
        this.CONNECTION_TIMEOUT = milliseconds;
    }

    /**
     * 设置连接超时时间
     *
     * @param milliseconds
     */
    public void setReadTimeout(int milliseconds) {
        this.READ_SOCKET_TIMEOUT = milliseconds;
    }

    public int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    public int getReadSocketTimeout() {
        return READ_SOCKET_TIMEOUT;
    }

    /**
     * 设置重试次数
     *
     * @param retryCount
     */
    public void setRetryCount(int retryCount) {
        this.DEFAULT_RETRY_COUNT = retryCount;
    }

    /**
     * 设置重试延迟时间
     *
     * @param retryDelayTime
     */
    public void setRetryDelayTime(int retryDelayTime) {
        this.DEFAULT_RETRY_DELAY_TIME = retryDelayTime;
    }

    public int getRetryCount() {
        return DEFAULT_RETRY_COUNT;
    }

    public int getRetryDelayTime() {
        return DEFAULT_RETRY_DELAY_TIME;
    }

    public boolean isTrustAllCertificate() {
        return isTrustAllCertificate;
    }

    public void setTrustAllCertificate(boolean trustAllCertificate) {
        isTrustAllCertificate = trustAllCertificate;
    }
}
