package com.zuoyupeng.zaker.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.adapter.ReadPagerFragmentAdapter;
import com.zuoyupeng.zaker.bean.ReadBean;
import com.zuoyupeng.zaker.dao.SQLIteReadTable;
import com.zuoyupeng.zaker.dao.XUtilDbRead;
import com.zuoyupeng.zaker.fragment.BaseFragment;
import com.zuoyupeng.zaker.fragment.ReadFragment;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ReadActivity extends FragmentActivity {

    ViewPager vp;
    List<BaseFragment> list = new ArrayList<>();
    ReadPagerFragmentAdapter adapter;
    List<ReadBean> listRead;
    DbManager dbManagerRead;
    SharedPreferences sharedPreferences;
    ProgressWheel progressWheel;
    ImageView iv;
    int count = 0;
    private List<ReadBean> listMore;
    public String ttt;
    public String ima;
    public String nextUrl;
    private int allCount;
    private boolean isFirst = true;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            progressWheel.stopSpinning();
            iv.setVisibility(View.VISIBLE);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv.setVisibility(View.GONE);
                    setData();
                }
            });
            return false;
        }
    });
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        initView();
        setData();
    }

    private void initView() {
        //创建新闻页数据库
        DbManager.DaoConfig daoConfig = XUtilDbRead.getDaoConfig();
        dbManagerRead = x.getDb(daoConfig);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        vp = (ViewPager) findViewById(R.id.view_pager);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        iv = (ImageView) findViewById(R.id.read_iv_error);
    }

    private void setData() {
        progressWheel.setProgress(0.0f);
        progressWheel.setBarColor(Color.BLUE);
        progressWheel.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float v) {
                if (v == 0) {
                    progressWheel.setProgress(1.0f);
                } else if (v == 1) {
                    progressWheel.setProgress(0.0f);
                }
            }
        });
        onReadNet();
    }

    String str;

    public void onReadNet() {
        String url = getIntent().getStringExtra("Api_url");
        final String name = getIntent().getStringExtra("TitleNews");
        LogUtils.i("ReadActivity", name);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            public void onError(Call call, Exception e, int i) {
                handler.sendEmptyMessageDelayed(0,3000);
            }

            public void onResponse(String s, int i) {
                listRead = NetJson.ReadNews(s);
                progressWheel.stopSpinning();
                ttt = listRead.get(0).block_title;
                nextUrl = listRead.get(0).next_url;
                ima = listRead.get(0).pages.get(0).bgimage_url;
                count = listRead.get(0).articles.size() / 6;
                if (sharedPreferences.getBoolean("isFirst", true)) {
                    insert(listRead, listRead.get(0).block_title, ima);
                    LogUtils.i("ReadActivity", "第一次插入数据成功");
                } else {
                    try {
                        if (dbManagerRead.selector(SQLIteReadTable.class).where(
                                "block_title", "=", name).findAll() != null &&
                                dbManagerRead.selector(SQLIteReadTable.class).where(
                                        "block_title", "=", name).findAll().size() != 0) {
                            str = dbManagerRead.selector(SQLIteReadTable.class).where(
                                    "block_title", "=", name).findAll().get(0).getBlock_title();
                        } else {
                            str = "";
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (str.equals(name)) {
                        delete();
                        LogUtils.i("ReadActivity", "删除数据成功");
                        insert(listRead, listRead.get(0).block_title, ima);
                        LogUtils.i("ReadActivity", "插入新数据成功");
                    } else {
                        insert(listRead, listRead.get(0).block_title, ima);
                        LogUtils.i("ReadActivity", "插入没插过的数据成功");
                    }
                }
                for (int n = 0; n < count; n++) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", n);
                    ReadFragment fragment = new ReadFragment();
                    fragment.setArguments(bundle);
                    ReadActivity.this.list.add(fragment);
                    adapter = new ReadPagerFragmentAdapter(getSupportFragmentManager(), ReadActivity.this.list);
                    vp.setAdapter(adapter);
                }
                vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == adapter.getCount() - 2) {
                            onNetMore();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        });
    }

    public void onNetMore() {

        OkHttpUtils.get().url(nextUrl).build().execute(new StringCallback() {
            public void onError(Call call, Exception e, int i) {

            }

            public void onResponse(String s, int i) {
                listMore = NetJson.ReadNews(s);
                nextUrl = nextUrl.equals(listMore.get(0).next_url) ? "" : listMore.get(0).next_url;
                insert(listMore, ttt, ima);
                int newCount = listMore.get(0).articles.size() / 6;
                if (isFirst) {
                    allCount = allCount + count;
                }
                isFirst = false;
                List<BaseFragment> list = new ArrayList<>();
                for (int j = 0; j < newCount; j++) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", allCount + j);
                    ReadFragment fragment = new ReadFragment();
                    fragment.setArguments(bundle);
                    Log.i("TAG", "onResponse: count" + (allCount + j));
                    list.add(fragment);
                }
                allCount = allCount + newCount;
                adapter.addList(list);
            }
        });
    }


    private void insert(List<ReadBean> listRead, String title, String ima) {
        SQLIteReadTable table = new SQLIteReadTable();
        try {
            table.setBlock_title(title);
            table.setBgimage_url(ima);
            for (int j = 0; j < listRead.get(0).articles.size(); j++) {
                table.setAuther_name(listRead.get(0).articles.get(j).auther_name);
                table.setTitle(listRead.get(0).articles.get(j).title);
                table.setDate(listRead.get(0).articles.get(j).date);
                table.setWeburl(listRead.get(0).articles.get(j).weburl);
                for (int x = 0; x < listRead.get(0).articles.get(j).media.size(); x++) {
                    table.setUrl(listRead.get(0).articles.get(j).media.get(x).url);
                }
                dbManagerRead.save(table);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        try {
            dbManagerRead.delete(SQLIteReadTable.class,
                    WhereBuilder.b("block_title", "=", getIntent().getStringExtra("TitleNews")));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
        Log.i("ReadActivity", "状态位改变，不是第一次进入");
    }

}