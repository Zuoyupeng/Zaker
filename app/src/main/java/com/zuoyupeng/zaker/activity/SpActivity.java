package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoyupeng.zaker.R;
import com.zuoyupeng.zaker.adapter.SpAdapter;
import com.zuoyupeng.zaker.bean.SpBean;
import com.zuoyupeng.zaker.json.NetJson;

import java.util.List;

import okhttp3.Call;

public class SpActivity extends Activity {

    Button btnBack;
    ListView lv;
    SpringView springView;
    List<SpBean> list;
    List<SpBean.ArticlesI> listA;
    ImageView ivHeader,ivTitle;
    SpAdapter adapter;
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
        setContentView(R.layout.activity_sp);
        initView();
        setData();
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
        onSpNet();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                onSpNetAgain();
            }

            @Override
            public void onLoadmore() {
            }
        });
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.sp_title_btn_back);
        lv = (ListView) findViewById(R.id.sp_lv);
        springView = (SpringView) findViewById(R.id.sp_sv);
        ivTitle = (ImageView) findViewById(R.id.sp_title_iv);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        iv = (ImageView) findViewById(R.id.sp_iv_error);
        ivHeader = new ImageView(this);
        ivHeader.setScaleType(ImageView.ScaleType.FIT_XY);
        String img = getIntent().getStringExtra("promotion_img");
        final String weburl = getIntent().getStringExtra("weburl");
        Glide.with(this).load(img).into(ivHeader);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,600);
        ivHeader.setLayoutParams(params);
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpActivity.this,WebUrlActivity.class);
                intent.putExtra("weburl",weburl);
                startActivity(intent);
            }
        });
    }

    public void onSpNet(){
        String url = getIntent().getStringExtra("api_url");
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handler.sendEmptyMessageDelayed(0,3000);
            }

            @Override
            public void onResponse(String s, int i) {
                list = NetJson.spJson(s);
                Glide.with(SpActivity.this).load(list.get(0).bgimage_url).into(ivTitle);
                ivTitle.setScaleType(ImageView.ScaleType.FIT_XY);
                listA = list.get(0).articles;
                adapter = new SpAdapter(listA,SpActivity.this);
                lv.setAdapter(adapter);
                lv.addHeaderView(ivHeader);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(SpActivity.this,WebUrlActivity.class);
                        intent.putExtra("weburl",listA.get(i-1).weburl);
                        startActivity(intent);
                    }
                });
                adapter.setNotifyList(listA);
                springView.onFinishFreshAndLoad();
                progressWheel.stopSpinning();
            }
        });
    }

    public void onSpNetAgain(){
        String url = getIntent().getStringExtra("api_url");
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                list = NetJson.spJson(s);
                adapter.setNotifyList(listA);
                springView.onFinishFreshAndLoad();
            }
        });
    }
}
