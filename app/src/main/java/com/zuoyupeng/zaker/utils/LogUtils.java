package com.zuoyupeng.zaker.utils;

import android.util.Log;

/**
 * Created by zuo on 2016/8/4.
 */
public class LogUtils {
    private static boolean openLog = true;

    public static void i(String s,String str){
        if(openLog) {
            Log.i(s, str);
        }
    }
}
