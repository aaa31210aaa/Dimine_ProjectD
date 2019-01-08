package com.example.administrator.dimine_projectd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Welcome extends Activity {
    private Intent intent;
    private int send_time = 3000;

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(Welcome.this, Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(1, send_time);
    }
}
