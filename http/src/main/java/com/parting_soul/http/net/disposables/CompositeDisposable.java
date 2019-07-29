package com.parting_soul.http.net.disposables;

import java.util.HashSet;

/**
 * @author parting_soul
 * @date 2019-07-26
 */
public class CompositeDisposable implements Disposable {

    private HashSet<Disposable> mDisposables;
    private boolean isDisposed;

    @Override
    public void dispose() {
        if (isDisposed || mDisposables == null) {
            return;
        }
        for (Disposable disposable : mDisposables) {
            disposable.dispose();
        }
        isDisposed = true;
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    public void add(Disposable compress) {
        if (mDisposables == null) {
            mDisposables = new HashSet<>();
        }
        this.mDisposables.add(compress);
    }

}
