package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.NewsTab;
import com.zuoyupeng.zaker.dao.SQLIteSearchTable;
import com.zuoyupeng.zaker.dao.SQLIteTable;
import com.zuoyupeng.zaker.dao.XUtilDb;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import okhttp3.Call;

public class SplashActivity extends Activity {

    List<NewsTab> listCha;
    DbManager dbManager;
    SharedPreferences sharedPreferences;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            return false;
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        //数据库
        DbManager.DaoConfig daoConfig = XUtilDb.getDaoConfig();
        dbManager = x.getDb(daoConfig);
        //判断是否是第一次加载
        if (sharedPreferences.getBoolean("isFirst", true)) {
            onSplashNet();
            LogUtils.i("SplashActivity","splash是第一次加载");
        }else{
            handler.sendEmptyMessageDelayed(0, 2000);
            LogUtils.i("SplashActivity","splash不是第一次加载");
        }
    }

    /**
     * 网络请求并解析
     */
    public void onSplashNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_CHANNEL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                LogUtils.i("SplashActivity","splash网络请求失败");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start();
                finish();
                Toast.makeText(SplashActivity.this, R.string.splash_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String s, int i) {
                LogUtils.i("SplashActivity","splash网络请求成功");
                listCha = NetJson.searchNews(s);//解析赋值
                insert();//插入数据库
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        });
    }

    /**
     * 插入数据库
     */
    public void insert() {
        try {
            SQLIteTable table = new SQLIteTable();
            for (int i = 0; i < listCha.size()-(listCha.size()-7); i++) {
                table.setTitleCha(listCha.get(i).titleCha);
                table.setTitleNews(listCha.get(i).list.get(0).titleNews);
                table.setPic(listCha.get(i).list.get(0).pic);
                table.setApi_url(listCha.get(i).list.get(0).api_url);
                table.setBlock_color(listCha.get(i).list.get(0).block_color);
                dbManager.save(table);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变状态位，不是第一次进入程序
     */
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
        LogUtils.i("SplashActivity","状态位改变，不是第一次进入");
    }
}
