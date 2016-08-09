package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zuoyupeng.zaker.R;

public class LoginActivity extends Activity {

    Button btnback;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setData();
    }

    private void initView() {
        btnback = (Button) findViewById(R.id.login_title_back);
    }

    private void setData() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
