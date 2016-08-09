package com.zuoyupeng.zaker.dao;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by zuo on 2016/7/27.
 */
public class XUtilDbNews {

    static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig(){
        File file = new File("/sdcard/Zaza/db/");
        if(file.exists()){
            file.mkdirs();
        }
        if(daoConfig == null){
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("zakerNews.db")
                    .setDbDir(file)
                    .setDbVersion(1)
                    .setAllowTransaction(true)//允许开启事物
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }
}
