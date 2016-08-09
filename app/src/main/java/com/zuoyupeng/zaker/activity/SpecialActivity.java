package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.adapter.SpecialAdapter;
import com.zuoyupeng.zaker.bean.SpecialBean;
import com.zuoyupeng.zaker.json.NetJson;
import com.zuoyupeng.zaker.utils.LogUtils;
import com.zuoyupeng.zaker.utils.NetWorkUtils;

import java.util.List;

import okhttp3.Call;

public class SpecialActivity extends Activity {

    Button btnBack;
    List<SpecialBean> listS;
    List<SpecialBean.TopicItem> list;
    ListView lv;
    SpringView springView;
    SpecialAdapter adapter;
    ProgressWheel progressWheel;
    ImageView iv;

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
        setContentView(R.layout.activity_special);
        initView();
        setData();
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.special_title_btn_back);
        lv = (ListView) findViewById(R.id.special_lv);
        springView = (SpringView) findViewById(R.id.special_sv);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        iv = (ImageView) findViewById(R.id.special_iv_error);
    }

    private void setData() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        onSpecialNet();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                onSpecialNet();
            }

            @Override
            public void onLoadmore() {
                nextNet();
            }
        });
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
    }

    public void onSpecialNet(){
        OkHttpUtils.get().url(NetWorkUtils.NET_ZHUANTI).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handler.sendEmptyMessageDelayed(0,3000);
            }

            @Override
            public void onResponse(String s, int i) {
                listS = NetJson.specialJson(s);
                list = listS.get(0).list.get(0).topic_list;
                onSpecialNetMore();
            }
        });
    }

    public void onSpecialNetMore(){
        OkHttpUtils.get().url(NetWorkUtils.NET_ZHUANTI).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                List<SpecialBean> listMore = NetJson.specialJson(s);
                list.addAll(listMore.get(0).list.get(1).topic_list);
                adapter = new SpecialAdapter(list,SpecialActivity.this);
                lv.setAdapter(adapter);
                adapter.setNotifyList(list);
                springView.onFinishFreshAndLoad();
                progressWheel.stopSpinning();
            }
        });
    }

    public void nextNet(){
        OkHttpUtils.get().url(listS.get(0).next_url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                List<SpecialBean> list1 = NetJson.specialJson(s);
                list.addAll( list1.get(0).list.get(0).topic_list);
                adapter.setNotifyList(SpecialActivity.this.list);
                springView.onFinishFreshAndLoad();
            }
        });
    }
}
