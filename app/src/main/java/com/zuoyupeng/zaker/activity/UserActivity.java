package com.zuoyupeng.zaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuoyupeng.zaker.R;

public class UserActivity extends Activity {

    LinearLayout llLogin;
    Button btnBack;
    TextView tvSpecial;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        setData();
    }

    private void initView() {
        llLogin = (LinearLayout) findViewById(R.id.user_ll_login);
        btnBack = (Button) findViewById(R.id.user_title_btn_back);
        tvSpecial = (TextView) findViewById(R.id.user_special);
    }

    private void setData() {
        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,SpecialActivity.class);
                startActivity(intent);
            }
        });
    }
}
