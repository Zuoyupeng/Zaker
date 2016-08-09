package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.adapter.ChannelAdapter;
import com.zuoyupeng.zaker.adapter.NoNetSearchAdapter;
import com.zuoyupeng.zaker.bean.NewsTab;
import com.zuoyupeng.zaker.dao.SQLIteSearchTable;
import com.zuoyupeng.zaker.dao.SQLIteTable;
import com.zuoyupeng.zaker.dao.XUtilDbNews;
import com.zuoyupeng.zaker.dao.XUtilDbSearch;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import okhttp3.Call;

public class SearchActivity extends Activity {

    Button btnBack;
    ListView lv;
    List<NewsTab> listCha;
    ChannelAdapter adapter;
    List<SQLIteSearchTable> list;
    NoNetSearchAdapter searchAdapter;
    DbManager dbManagerNews, dbManagerSearch;
    SharedPreferences sharedPreferences;
    ProgressWheel progressWheel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setData();
    }

    private void initView() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        btnBack = (Button) findViewById(R.id.search_title_btn_back);
        lv = (ListView) findViewById(R.id.search_lv);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        //创建新闻页数据库
        DbManager.DaoConfig daoConfig = XUtilDbNews.getDaoConfig();
        dbManagerNews = x.getDb(daoConfig);
        //创建搜索页数据库
        DbManager.DaoConfig daoConfigSearch = XUtilDbSearch.getDaoConfig();
        dbManagerSearch = x.getDb(daoConfigSearch);
    }

    private void setData() {
        progressWheel.setProgress(0.0f);
        progressWheel.setBarColor(Color.BLUE);
        progressWheel.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float v) {
                if(v== 0){
                    progressWheel.setProgress(1.0f);
                }else if(v==1){
                    progressWheel.setProgress(0.0f);
                }
            }
        });
        onChannelNet();//网络解析
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onChannelNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_CHANNEL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.i("SplashActivity","splash网络请求失败");
                query();//搜索搜索页数据库全部数据集合
                searchAdapter = new NoNetSearchAdapter(list, SearchActivity.this);
                lv.setAdapter(searchAdapter);
                progressWheel.stopSpinning();
                searchAdapter.setNotifyList(list);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(SearchActivity.this, NewsActivity.class);
                        intent.putExtra("titleChannel", list.get(i).getTitleChannel());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onResponse(String s, int i) {
                Log.i("SplashActivity","splash网络请求成功");
                listCha = NetJson.searchNews(s);
                //判断是否是第一次进入搜索页
                if (sharedPreferences.getBoolean("isFirst", true)) {
                    insertN();//插入数据到新闻页数据库
                    Log.i("SearchActivity","第一次进入搜索页");
                }
                adapter = new ChannelAdapter(listCha, SearchActivity.this);
                lv.setAdapter(adapter);
                adapter.setNotifyList(listCha);
                progressWheel.stopSpinning();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(SearchActivity.this, NewsActivity.class);
                        intent.putExtra("titleChannel", listCha.get(i).titleCha);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void insertN() {
        SQLIteTable table2 = new SQLIteTable();
        try {
            for (int i = 0; i < listCha.size(); i++) {
                table2.setTitleCha(listCha.get(i).titleCha);
                for (int j = 0; j < listCha.get(i).list.size(); j++) {
                    table2.setTitleNews(listCha.get(i).list.get(j).titleNews);
                    table2.setApi_url(listCha.get(i).list.get(j).api_url);
                    table2.setBlock_color(listCha.get(i).list.get(j).block_color);
                    table2.setPic(listCha.get(i).list.get(j).pic);
                    dbManagerNews.save(table2);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void query() {
        try {
            list = dbManagerSearch.findAll(SQLIteSearchTable.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetWorkUtils.REFRESH = 0;
        LogUtils.i("SearchActivity", "获取焦点，状态位变为0");
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetWorkUtils.REFRESH = 1;
        LogUtils.i("SearchActivity", "失去焦点，状态位变为1");
    }
}
