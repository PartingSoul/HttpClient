package com.parting_soul.httputilsdemo;

import android.app.Application;
import android.content.Context;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public class App extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
