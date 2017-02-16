package com.example.l.location;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2017/2/15.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
//        SDKInitializer.initialize(getApplicationContext());
    }
}
