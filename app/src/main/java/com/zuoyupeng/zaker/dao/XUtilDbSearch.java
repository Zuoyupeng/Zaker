package com.zuoyupeng.zaker.dao;

import org.xutils.DbManager;

import java.io.File;

public class XUtilDbSearch {

    static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig(){
        File file = new File("/sdcard/Zaza/db/");
        if(file.exists()){
            file.mkdirs();
        }
        if(daoConfig == null){
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("zaker.db")
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
