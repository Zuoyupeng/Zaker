package com.zuoyupeng.zaker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.bean.NewsTab;
import com.zuoyupeng.zaker.dao.SQLIteSearchTable;
import com.zuoyupeng.zaker.dao.SQLIteTable;
import com.zuoyupeng.zaker.dao.XUtilDb;
import com.zuoyupeng.zaker.dao.XUtilDbSearch;
import com.zuoyupeng.zaker.fragment.BaseFragment;
import com.zuoyupeng.zaker.fragment.HotFragment;
import com.zuoyupeng.zaker.fragment.LocFragment;
import com.zuoyupeng.zaker.fragment.PlayFragment;
import com.zuoyupeng.zaker.fragment.SubscribeFragment;
import com.zuoyupeng.zaker.fragment.UserFragment;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import okhttp3.Call;

public class MainActivity extends FragmentActivity {

    SubscribeFragment subscribeFragment;
    HotFragment hotFragment;
    LocFragment locFragment;
    PlayFragment playFragment;
    UserFragment userFragment;
    Button btnSearch, btnUser;
    RadioGroup rg;
    RadioButton rbSubscribe, rbHot, rbLoc, rbPlay, rbUser;
    FrameLayout fm;
    private static boolean isExit = false;// 定义一个变量，来标识是否退出
    DbManager dbManager, dbManagerSearch;//数据库
    List<NewsTab> listCha;
    SharedPreferences sharedPreferences;

    private static Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            isExit = false;
            return false;
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.activity_main);
        initView();
        setData();
    }

    private void initView() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        btnSearch = (Button) findViewById(R.id.title_search);
        btnUser = (Button) findViewById(R.id.title_user);
        rg = (RadioGroup) findViewById(R.id.bottom_rg);
        rbSubscribe = (RadioButton) findViewById(R.id.bottom_rb_subscribe);
        rbHot = (RadioButton) findViewById(R.id.bottom_rb_hot);
        rbLoc = (RadioButton) findViewById(R.id.bottom_rb_loc);
        rbPlay = (RadioButton) findViewById(R.id.bottom_rb_play);
        rbUser = (RadioButton) findViewById(R.id.bottom_rb_user);
        fm = (FrameLayout) findViewById(R.id.main_fl);
        //创建主界面数据库
        DbManager.DaoConfig daoConfig = XUtilDb.getDaoConfig();
        dbManager = x.getDb(daoConfig);
        //创建搜索页数据库
        DbManager.DaoConfig daoConfigSearch = XUtilDbSearch.getDaoConfig();
        dbManagerSearch = x.getDb(daoConfigSearch);
        //fragment替换
        subscribeFragment = new SubscribeFragment();
        hotFragment = new HotFragment();
        locFragment = new LocFragment();
        playFragment = new PlayFragment();
        userFragment = new UserFragment();
        switchFragment(subscribeFragment);
    }

    private void setData() {
        //搜索按钮点击事件
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        //用户名按钮点击事件
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });
        //底部按钮点击事件
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.bottom_rb_subscribe:
                        switchFragment(subscribeFragment);
                        break;
                    case R.id.bottom_rb_hot:
                        switchFragment(hotFragment);
                        break;
                    case R.id.bottom_rb_loc:
                        switchFragment(locFragment);
                        break;
                    case R.id.bottom_rb_play:
                        switchFragment(playFragment);
                        break;
                    case R.id.bottom_rb_user:
                        switchFragment(userFragment);
                        break;
                }
            }
        });
        //判断是否是第一次进入主界面
        if (sharedPreferences.getBoolean("isFirst", true)) {
            LogUtils.i("MainActivity", "第一次进入主界面");
            onChannelNet();
        }
    }

    /**
     * 网络请求并解析
     */
    public void onChannelNet() {
        OkHttpUtils.get().url(NetWorkUtils.NET_CHANNEL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                LogUtils.i("MainActivity","网络请求失败");
            }

            @Override
            public void onResponse(String s, int i) {
                LogUtils.i("MainActivity","网络请求成功");
                listCha = NetJson.searchNews(s);
                insertS();//插入数据到搜索页数据库
            }
        });
    }

    /**
     * 调用系统返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 两次点击推出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            this.finish();
        }
    }

    /**
     * 查询数据库，得到主界面数据库集合
     */
    private void query() {
        try {
            dbManager.findAll(SQLIteTable.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据库，插入到搜索页集合
     */
    public void insertS() {
        SQLIteSearchTable table = new SQLIteSearchTable();
        try {
            for (int i = 0; i < listCha.size(); i++) {
                table.setTitleChannel(listCha.get(i).titleCha);
                table.setList_icon(listCha.get(i).list_icon);
                dbManagerSearch.save(table);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
        LogUtils.i("MainActivity", "状态位改变，不是第一次进入");
    }

    protected void onResume() {
        super.onResume();
        LogUtils.i("MainActivity", "获取焦点，状态位为"+NetWorkUtils.REFRESH);
        if(NetWorkUtils.REFRESH == 1){
            handler.sendEmptyMessage(0);
            LogUtils.i("MainActivity", "发送handler消息");
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            query();
            rbSubscribe.setChecked(true);
            subscribeFragment = new SubscribeFragment();
            switchFragment(subscribeFragment);
            return false;
        }
    });

    protected void onPause() {
        super.onPause();
        NetWorkUtils.REFRESH = 0;
        LogUtils.i("MainActivity", "失去焦点，状态位变为0");
    }

    /**
     * 替换主界面fragment
     */
    public void switchFragment(BaseFragment bm) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fl, bm)
                .commit();
    }
}
