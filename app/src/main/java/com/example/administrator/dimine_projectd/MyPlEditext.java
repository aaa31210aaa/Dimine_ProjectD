package com.example.administrator.dimine_projectd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class MyPlEditext extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MyPlEditext";
    private ImageView pl_edtext_back;
    private TextView my_pl_send;
    private EditText my_pl_editext;

    private String url;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private Intent intent;
    private String mCommentType; //评论类型
    private String mId; //业务id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pl_editext);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        pl_edtext_back = (ImageView) findViewById(R.id.pl_edtext_back);
        my_pl_send = (TextView) findViewById(R.id.my_pl_send);
        my_pl_editext = (EditText) findViewById(R.id.my_pl_editext);

        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        url = MyHttpUtils.ApiAddComment();

        intent = getIntent();
        mCommentType = intent.getStringExtra("myCommentType");
        mId = intent.getStringExtra("myid");

    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("commtype", mCommentType)
                .addParams("busiid", mId)
                .addParams("commmemo", my_pl_editext.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyPlEditext.this,"请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response + "---" + mCommentType + "---" + mId + "---" + my_pl_editext.getText().toString());
                        ShowToast.showShort(MyPlEditext.this, "评论成功");

                    }
                });
    }


    @Override
    protected void setOnClick() {
        pl_edtext_back.setOnClickListener(this);
        my_pl_send.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pl_edtext_back:
                finish();
                break;
            //发送评论内容
            case R.id.my_pl_send:
                if (my_pl_editext.getText().toString().trim().length() == 0) {
                    ShowToast.showShort(this, "内容不能为空。");
                } else {
                    mOkhttp();
//                    intent.putExtra("back","评论成功");
                    setResult(RESULT_OK, intent);
                    finish();
                }


                break;
        }
    }
}
