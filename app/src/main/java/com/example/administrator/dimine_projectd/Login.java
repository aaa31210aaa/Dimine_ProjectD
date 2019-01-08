package com.example.administrator.dimine_projectd;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import utils.BaseActivity;
import utils.DialogUtil;
import utils.MyHttpUtils;
import utils.ShowToast;

public class Login extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Login";
    private TextView login_title_tv;
    private ClearEditText login_editext_account;
    private ClearEditText login_editext_pwd;
    private CheckBox login_save_account;
    private RadioButton personal;
    private RadioButton admin;
    private RadioGroup login_radiogroup;
    private Button login_submit;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor;
    private Dialog dialog = null;
    private final static int send_time = 3000;
    private final static int time_out = 15000; //超时
    private Intent intent;
    private JSONObject param;
    private String error;


    private String type; //账号权限类型


    private static String url = "";
    private Thread mythread;
    private boolean stopThread = false; //子线程开启状态
    private OkHttpClient mclient;
    private String token;
    private String username;
    //人员地区
    private String deptname;
    //用户名字
    private String name;
    //用户密码
    private String user_password;
    //用户职位
    private String position;
    private String head_img;
    private String company;
    private Button change_language;//切换语言
    private boolean a = false;

    private Dialog mDialog; //切换语言的dialog

    private String account_password_null;

    private String language;


    /**
     * 是否退出的的标识
     */
    private static boolean isExit = false;

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    dialog.dismiss();
                    intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                    ShowToast.showShort(Login.this, R.string.login_success);
                    break;
                case 2:
                    break;
                case 3:
                    dialog.dismiss();
                    ShowToast.showShort(Login.this, R.string.login_timeout);
                    break;
                case 4:
                    isExit = false;
                    break;
                case 5:
                    dialog.dismiss();
                    switchLanguage("en");
                    //更新语言后，destroy当前页面，重新绘制
                    finish();
                    Intent it_english = new Intent(Login.this, Login.class);
                    startActivity(it_english);
                    break;
                case 6:
                    dialog.dismiss();
                    switchLanguage("zh");
                    //更新语言后，destroy当前页面，重新绘制
                    finish();
                    Intent it_chinese = new Intent(Login.this, Login.class);
                    startActivity(it_chinese);
                    break;

            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.exit);
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(4, send_time);
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setTintColor(R.color.blue_deep);
//            tintManager.setStatusBarTintResource(R.color.blue_deep);//通知栏所需颜色
//        }
        initView();
        setOnClick();

    }

//    @TargetApi(19)
//    private void setTranslucentStatus( boolean on){
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }



    @Override
    protected void initView() {
        //调用登陆接口
        url = MyHttpUtils.ApiLogin();
        login_title_tv = (TextView) findViewById(R.id.login_title_tv);

        TextPaint tp = login_title_tv.getPaint();
        tp.setFakeBoldText(true);
        login_editext_account = (ClearEditText) findViewById(R.id.login_editext_account);
        login_editext_pwd = (ClearEditText) findViewById(R.id.login_editext_pwd);
        login_save_account = (CheckBox) findViewById(R.id.login_save_account);
        login_radiogroup = (RadioGroup) findViewById(R.id.login_radiogroup);

        change_language = (Button) findViewById(R.id.change_language);


//        personal = (RadioButton) findViewById(R.id.personal);
//        admin = (RadioButton) findViewById(R.id.admin);

        login_submit = (Button) findViewById(R.id.login_submit);

        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();

        //判断记住密码多选框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            //设置默认是记住账号状态
            login_save_account.setChecked(true);
            login_editext_account.setText(sp.getString("USER_NAME", ""));
        }

//        if (sp.getBoolean("PISCHECK", true)) {
//            personal.setChecked(true);
//            admin.setChecked(false);
//        } else {
//            admin.setChecked(true);
//            personal.setChecked(false);
//        }


        login_save_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (login_save_account.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });

        login_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == personal.getId()) {
                    sp.edit().putBoolean("PISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("PISCHECK", false).commit();
                }

            }
        });
    }


    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(send_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("loginname", login_editext_account.getText().toString())
                    .addParams("loginpwd", login_editext_pwd.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d("timeout", e + "");
                            handler.sendEmptyMessageDelayed(3, time_out);
                        }

                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("Login", response);
                                param = new JSONObject(response);
                                String success = param.getString("success");
                                error = param.getString("errormessage");

//                                editor.putString("company", deptname);
//                                editor.putString("position", username);

                                if (success.equals("true")) {
                                    token = param.getString("access_token");
                                    editor = sp.edit();
                                    editor.putString("user_token", token);

                                    user_password = login_editext_pwd.getText().toString();
                                    name = param.getString("username");
//                                    position = param.getString("username");
                                    position = "";
                                    company = param.getString("deptname");

                                    editor.putString("userpassword", user_password);
                                    editor.putString("name", name);
                                    editor.putString("position", position);
                                    editor.putString("company", company);
                                    editor.commit();
                                    if (a) {
                                        if (login_save_account.isChecked()) {
                                            editor = sp.edit();
                                            editor.putString("USER_NAME", login_editext_account.getText().toString());
                                            editor.commit();
                                        }
                                        Message message = handler.obtainMessage();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                    } else {
//                                        if (admin.isChecked()) {
//                                            ShowToast.showShort(Login.this, "你没有权限登录");
//                                        } else if (personal.isChecked()) {
                                        if (login_save_account.isChecked()) {
                                            editor = sp.edit();
                                            editor.putString("USER_NAME", login_editext_account.getText().toString());
                                            editor.commit();
                                        }
                                        Message message = handler.obtainMessage();
                                        message.what = 1;
                                        handler.sendMessage(message);
//                                        }
                                    }


                                } else {
                                    dialog.dismiss();
                                    ShowToast.showShort(Login.this, error);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread = true;
    }

    @Override
    protected void setOnClick() {
//        personal.setOnClickListener(this);
//        admin.setOnClickListener(this);
        login_submit.setOnClickListener(this);
        change_language.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.personal:
//                break;
//            case R.id.admin:
//                break;
            case R.id.login_submit:
                if (login_editext_account.getText().toString().equals("") || login_editext_pwd.getText().toString().equals("")) {
                    ShowToast.showShort(this, R.string.account_password_null);
                } else {
                    language = login_submit.getText().toString();
                    if (language.equals("登录")) {
                        editor = sp.edit();
                        editor.putString("mylanguage", "zh");
                        editor.commit();
                    } else {
                        editor = sp.edit();
                        editor.putString("mylanguage", "en");
                        editor.commit();
                    }
                    //启动子线程
                    dialog = DialogUtil.createLoadingDialog(this, R.string.login_loading);
                    new Thread(mRunable).start();
                }
//                try {
//                    param = new JSONObject(json);
//                    String code = param.getString("code");
//                    type = param.getString("admin");
//                    account = param.getString("account");
//                    head_img = param.getString("head_img");
//                    company = param.getString("company");
//                    position = param.getString("position");
//                    editor.putString("name", account);
//                    editor.putString("img", head_img);
//                    editor.putString("company", company);
//                    editor.putString("position", position);
//                    editor.commit();
//                    Log.d("gggg", type + "---------" + head_img);
//                    if (code.equals("0")) {
//                        if (type.toString().equals("true")) {
//                            if (login_save_account.isChecked()) {
//                                editor = sp.edit();
//                                editor.putString("USER_NAME", login_editext_account.getText().toString());
//                                editor.commit();
//                            }
//                            dialog = DialogUtil.createLoadingDialog(this, "请稍后");
//                            handler.sendEmptyMessageDelayed(1, send_time);
//                        } else {
//                            if (admin.isChecked()) {
//                                ShowToast.showShort(Login.this, "你没有权限登录");
//                            } else if (personal.isChecked()) {
//                                if (login_save_account.isChecked()) {
//                                    editor = sp.edit();
//                                    editor.putString("USER_NAME", login_editext_account.getText().toString());
//                                    editor.commit();
//                                }
//                                dialog = DialogUtil.createLoadingDialog(this, "请稍后");
//                                handler.sendEmptyMessageDelayed(1, send_time);
//                            }
//                        }
//                    } else {
//                        ShowToast.showShort(Login.this, "账号或者密码错误");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                //登陆网络连接
//                RequestParams params = new RequestParams("url");
//                params.addQueryStringParameter("loginname", login_editext_account.getText().toString());
//                params.addQueryStringParameter("loginpwd", login_editext_pwd.getText().toString());
//
//                x.http().get(params, new Callback.CommonCallback<String>() {
//                            @Override
//                            public void onSuccess(String result) {
//                                if (type.toString().equals("true")) {
//                                    if (login_save_account.isChecked()) {
//                                        editor = sp.edit();
//                                        editor.putString("USER_NAME", login_editext_account.getText().toString());
//                                        editor.commit();
//                                    }
//                                } else {
//                                    if (admin.isChecked()) {
//                                        ShowToast.showShort(Login.this, "你没有权限登录");
//                                    } else if (personal.isChecked()) {
//                                        if (login_save_account.isChecked()) {
//                                            editor = sp.edit();
//                                            editor.putString("USER_NAME", login_editext_account.getText().toString());
//                                            editor.commit();
//                                        }
//                                        dialog = DialogUtil.createLoadingDialog(Login.this, "请稍后");
//                                        handler.sendEmptyMessageDelayed(1, send_time);
//                                    }
//                                }
//
//                                try {
//                                    JSONObject param = new JSONObject(result);
//                                    Log.d("ppp", param + "");
//                                    String code = param.getString("success");
//                                    err = param.getString("errormessage");
//                                    if (code.equals("true")) {
//                                        dialog = DialogUtil.createLoadingDialog(Login.this, "请稍后");
//                                        handler.sendEmptyMessageDelayed(1, send_time);
//                                    } else {
//                                        dialog = DialogUtil.createLoadingDialog(Login.this, "请稍后");
//                                        handler.sendEmptyMessageDelayed(2, send_time);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onError(Throwable ex, boolean isOnCallback) {
//                                dialog = DialogUtil.createLoadingDialog(Login.this, "请稍后");
//                                handler.sendEmptyMessageDelayed(3, send_time);
//                                Log.d("kkk", ex + "");
//                            }
//
//                            @Override
//                            public void onCancelled(CancelledException cex) {
//
//                            }
//
//                            @Override
//                            public void onFinished() {
//
//                            }
//                        }
//
//                );
                break;

            //选择语言
            case R.id.change_language:
                if (mDialog == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_select_language, null);
                    TextView english = (TextView) layout.findViewById(R.id.select_english);
                    TextView chinese = (TextView) layout.findViewById(R.id.select_chinese);
                    mDialog = new Dialog(Login.this, R.style.Custom_Dialog_Theme);
                    mDialog.setCanceledOnTouchOutside(true);
                    english.setOnClickListener(Login.this);
                    chinese.setOnClickListener(Login.this);
                    mDialog.setContentView(layout);
                }
                mDialog.show();
                break;

            case R.id.select_english:
                mDialog.dismiss();
                //切换为英文
                dialog = DialogUtil.createLoadingDialog(this, R.string.switching);
                handler.sendEmptyMessageDelayed(5, 1500);
                break;
            case R.id.select_chinese:
                mDialog.dismiss();
                //切换为中文
                dialog = DialogUtil.createLoadingDialog(this, R.string.switching);
                handler.sendEmptyMessageDelayed(6, 1500);
                break;
            default:
                break;

        }
    }
}
