package com.zuoyupeng.zaker.application;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.BuildConfig;
import org.xutils.x;

import okhttp3.OkHttpClient;

/**
 * Created by zuo on 2016/7/24.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //iny
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                .readTimeout(10000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
