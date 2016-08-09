package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.zuoyupeng.zaker.R;

public class WebUrlActivity extends Activity {

    WebView wv;
    Button btnBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_url);
        initView();
        setData();
    }

    private void initView() {
        wv = (WebView) findViewById(R.id.web_url_wv);
        btnBack = (Button) findViewById(R.id.web_title_btn_back);
    }

    private void setData() {
        final String url = getIntent().getStringExtra("weburl");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wv.loadUrl(url);
        wv.getSettings().setJavaScriptEnabled(true);//执行java脚本
        wv.getSettings().setAllowFileAccess(true);//访问文件
        wv.getSettings().setBlockNetworkImage(false);//加载图片
        wv.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(getIntent().getStringExtra("weburl"));
                return true;
            }
        });
    }
}
