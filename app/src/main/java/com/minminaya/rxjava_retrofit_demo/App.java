package com.minminaya.rxjava_retrofit_demo;

import android.app.Application;

/**
 * Created by Niwa on 2017/4/11.
 */

public class App extends Application {

    private static App INSTANCE;

    public static App getINSTANCE() {
        return INSTANCE;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
