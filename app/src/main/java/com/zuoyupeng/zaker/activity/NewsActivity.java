package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.adapter.NewsAdapter;
import com.zuoyupeng.zaker.bean.NewsTab;
import com.zuoyupeng.zaker.dao.SQLIteTable;
import com.zuoyupeng.zaker.dao.XUtilDb;
import com.zuoyupeng.zaker.dao.XUtilDbNews;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends Activity {

    TextView tvTitle;
    Button btnBack;
    ListView lvNews;
    List<SQLIteTable> list;
    NewsAdapter adapter;
    DbManager dbManager,dbManagerNews;//数据库
    List<SQLIteTable> listNews = new ArrayList<>();
    List<SQLIteTable> all = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        setData();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.news_title_tv);
        btnBack = (Button) findViewById(R.id.news_title_btn_back);
        lvNews = (ListView) findViewById(R.id.news_lv);
        //查找新闻页数据库
        DbManager.DaoConfig daoConfigNews = XUtilDbNews.getDaoConfig();
        dbManagerNews = x.getDb(daoConfigNews);
        //插入主界面数据库
        DbManager.DaoConfig daoConfig = XUtilDb.getDaoConfig();
        dbManager = x.getDb(daoConfig);
        queryMain();
        //查询新闻页数据库全部数据集合
        query();
        adapter = new NewsAdapter(listNews, NewsActivity.this,dbManager);
        lvNews.setAdapter(adapter);
    }

    private void setData() {

        final String str = getIntent().getStringExtra("titleChannel");
        tvTitle.setText(str);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NewsActivity.this,ReadActivity.class);
                intent.putExtra("Api_url",listNews.get(i).getApi_url());
                intent.putExtra("TitleNews",listNews.get(i).getTitleNews());
                startActivity(intent);
            }
        });
    }

    //查找数据库
    public void query() {
        try {
            list = dbManagerNews.findAll(SQLIteTable.class);
            //遍历数据集合
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getTitleCha().equals(getIntent().getStringExtra("titleChannel"))){
                    listNews.add(list.get(i));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void queryMain(){
        try {
            all = dbManager.findAll(SQLIteTable.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
