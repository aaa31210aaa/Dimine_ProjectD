package com.example.administrator.dimine_projectd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class ModifyPassword extends BaseActivity {
    private ImageView modify_password_back;
    private EditText old_password;
    private EditText new_password;
    private EditText confirm_password;
    private Button modify_password_submit;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String user_old_password;
    private String url;
    private String user_token;
    private static final String TAG = "ModifyPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        modify_password_back = (ImageView) findViewById(R.id.modify_password_back);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        modify_password_submit = (Button) findViewById(R.id.modify_password_submit);
        url = MyHttpUtils.ApiPassWordChange();

        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_old_password = sp.getString("userpassword", null);
        user_token = sp.getString("user_token", null);
    }

    @Override
    protected void setOnClick() {
        modify_password_back.setOnClickListener(this);
        modify_password_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_password_back:
                finish();
                break;
            case R.id.modify_password_submit:
                if (old_password.getText().toString().equals("") || new_password.getText().toString().equals("") || confirm_password.getText().toString().equals("")) {
                    ShowToast.showShort(this, R.string.modify_password_nonull);
                    return;
                }
                if (!new_password.getText().toString().equals(confirm_password.getText().toString())) {
                    ShowToast.showShort(this, R.string.modify_password_atypism);
                    return;
                }

                OkHttpUtils
                        .get()
                        .url(url)
                        .addParams("access_token", user_token)
                        .addParams("loginpwd", old_password.getText().toString())
                        .addParams("newLoginpwd", new_password.getText().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                                ShowToast.showShort(ModifyPassword.this,"请检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("ModifyPassword", response);
                                try {
                                    JSONObject param = new JSONObject(response);
                                    String success = param.getString("success");
                                    String errormessage = param.getString("errormessage");
                                    if (success.equals("true")) {
                                        ShowToast.showShort(ModifyPassword.this, "修改成功");
                                        finish();
                                    } else {
                                        ShowToast.showShort(ModifyPassword.this, errormessage);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


//                if (old_password.getText().toString().equals(user_old_password)) {
//                    if (isPassword(new_password.getText().toString()) == true) {
//                        if (new_password.getText().toString().equals(confirm_password.getText().toString())) {
//                            ShowToast.showShort(this, "修改完成");
//                            finish();
//                        } else {
//                            ShowToast.showShort(this, "两次输入的密码不同");
//                        }
//                    } else {
//                        ShowToast.showShort(this, "您输入的新密码格式不正确");
//                    }
//                } else {
//                    ShowToast.showShort(this, "原密码不正确");
//                }
                break;
        }
    }

    private static boolean isPassword(String password) {
        Pattern p2 = Pattern.compile("^(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,}$");
        Matcher m2 = p2.matcher(password);
        return m2.matches();
    }

}
