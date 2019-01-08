package com.example.administrator.dimine_projectd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import utils.BaseActivity;

public class NotificationDetail extends BaseActivity {
    private ImageView notification_detail_back;
    private LinearLayout notification_wb;
    private InputMethodManager imm;
    private TextView notification_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        notification_detail_back = (ImageView) findViewById(R.id.notification_detail_back);
        notification_wb = (LinearLayout) findViewById(R.id.notification_wb);
        notification_content = (TextView) findViewById(R.id.notification_content);

        /**
         * 接收传过来的值
         */
        Intent intent = this.getIntent();
        notification_content.setText(intent.getStringExtra("content"));

    }


    @Override
    protected void setOnClick() {
        notification_detail_back.setOnClickListener(this);
        notification_wb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_detail_back:
                finish();
                break;
        }
    }
}
