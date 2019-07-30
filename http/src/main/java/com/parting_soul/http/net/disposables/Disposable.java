package com.parting_soul.http.net.disposables;

public interface Disposable {

    /**
     * 销毁
     */
    void dispose();

    /**
     * 销毁状态
     *
     * @return
     */
    boolean isDisposed();
}