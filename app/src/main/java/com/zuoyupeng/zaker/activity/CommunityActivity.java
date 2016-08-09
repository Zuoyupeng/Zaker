package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.adapter.CommAdapter;
import com.zuoyupeng.zaker.bean.Commbean;
import com.zuoyupeng.zaker.json.NetJson;

import java.util.List;

import okhttp3.Call;

public class CommunityActivity extends Activity {

    SpringView springView;
    ProgressWheel progressWheel;
    ListView lv;
    List<Commbean.PostsItem> listP;
    List<Commbean> list;
    CommAdapter adapter;
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
        setContentView(R.layout.activity_community);
        initView();
        setData();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.comm_lv);
        springView = (SpringView) findViewById(R.id.comm_sv);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        iv = (ImageView) findViewById(R.id.community_iv_error);
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
        onCommNet();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                onCommNet();
            }

            @Override
            public void onLoadmore() {
                onCommNetMore();
            }
        });
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
    }

    public void onCommNet(){
        String url = getIntent().getStringExtra("api_url");
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handler.sendEmptyMessageDelayed(0,3000);
            }

            @Override
            public void onResponse(String s, int i) {
                list = NetJson.commJson(s);
                listP = list.get(i).posts;
                adapter = new CommAdapter(listP,CommunityActivity.this);
                lv.setAdapter(adapter);
                adapter.setNotifyList(listP);
                springView.onFinishFreshAndLoad();
                progressWheel.stopSpinning();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(CommunityActivity.this,
                                WebUrlActivity.class);
                        intent.putExtra("weburl",listP.get(i).weburl);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void onCommNetMore(){
        OkHttpUtils.get().url(list.get(0).next_url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                list = NetJson.commJson(s);
                listP.addAll(list.get(i).posts);
                adapter.setNotifyList(listP);
                springView.onFinishFreshAndLoad();
                progressWheel.stopSpinning();
            }
        });
    }
}
