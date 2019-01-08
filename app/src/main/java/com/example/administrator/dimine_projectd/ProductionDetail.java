package com.example.administrator.dimine_projectd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import production_fragment.ProductionHistory;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.MyHttpUtils;
import utils.ShowToast;

public class ProductionDetail extends BaseActivity {
    private static final String TAG = "ProductionDetail";
    private ImageView production_detail_back;
    private TextView add_production_title;
    private TextView add_production_history; //历史台账
    private TextView add_production_date; //选择日期
    private TextView add_production_jl; //作业地点选择
    private TextView add_production_device; //选择设备
    private TextView add_production_gy;  //选择工艺
    private EditText add_production_beizhu;//备注
    private Button add_production_submit; //提交按钮


    private Intent intent;

//    private TabLayout production_detail_tablayout;
//    private MyViewPager production_detail_viewpager;


    private TextView add_production_etv_bz; // 班组
    private TextView add_production_tv_bc; //班次

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    //标题名
    private String pro_titlename;
    private String prod_name;

    private String teamgroupid = ""; //班组id
    private String clickid;   //工序id
    private String classid; //班次id
    private String projectid; //作业地点id
    private String deviceid; //设备id
    private String techid; //工艺id
    private String tag; //指标id
    private String tag_value; //指标的值

    private String teamname; //班组名
    private int sj_month;

    private String url;
    private String add_url;
    private String getTarget_url;
    private Calendar calendar;
    private JSONArray gx_jsonArray;
    private String[] dt_text;
    private String[] num;
    //    private EditText editText; //动态创建的ediText
    private int width;//屏幕宽度
    private int height;//屏幕高度
    private Map<String, String> map = new HashMap<>();
    private ArrayList<String> add_list = new ArrayList<String>();
    private LinearLayout add_production_zdy_ll;
    private static final int BZ = 9; //传给班组的请求码
    private static final int A = 10; //传给选择班次的请求码
    private static final int B = 11; //传给选择作业地点的请求码
    private static final int C = 12; //传给选择设备的请求码
    private static final int D = 13; //传给选择工艺的请求码
    private Bundle bundle;
    //    private String str_all; //全部的公式
    private String str_result; //公式的结果
    private String str_num; //公式算法
    private String newStr = ""; //填写编辑框替换值后的公式
    private String gs_str;

    private Gson gson;
    private String jsonStr;
    private Dialog my_dialog;
    private String language;
    private ArrayList<String> dw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_detail);
        initView();
        setOnClick();


    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    my_dialog.dismiss();
                    ShowToast.showShort(ProductionDetail.this, "新增完成");
                    CleanDate();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        production_detail_back = (ImageView) findViewById(R.id.production_detail_back);
        add_production_title = (TextView) findViewById(R.id.add_production_title);
        add_production_history = (TextView) findViewById(R.id.add_production_history);
        add_production_etv_bz = (TextView) findViewById(R.id.add_production_etv_bz);
        add_production_zdy_ll = (LinearLayout) findViewById(R.id.add_production_zdy_ll);
        add_production_date = (TextView) findViewById(R.id.add_production_date);
        add_production_tv_bc = (TextView) findViewById(R.id.add_production_tv_bc);
        add_production_jl = (TextView) findViewById(R.id.add_production_jl);
        add_production_device = (TextView) findViewById(R.id.add_production_device);
        add_production_gy = (TextView) findViewById(R.id.add_production_gy);
        add_production_beizhu = (EditText) findViewById(R.id.add_production_beizhu);
        add_production_submit = (Button) findViewById(R.id.add_production_submit);


        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage", null);
        calendar = Calendar.getInstance();


        Intent intent = getIntent();
        clickid = intent.getStringExtra("clickid");
        pro_titlename = intent.getStringExtra("pro_titlename");
        prod_name = intent.getStringExtra("prod_name");
//        teamname = intent.getStringExtra("teamname");
//        teamgroupid = intent.getStringExtra("teamgroupid");

        url = MyHttpUtils.ApiProductionByGx();
        add_url = MyHttpUtils.ApiAddProductionBill();
        getTarget_url = MyHttpUtils.ApiGetTargetValue();


        // 获得屏幕分辨率
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //设置标题名字
//        add_production_title.setText(pro_titlename + "-" + prod_name);
        add_production_title.setText(prod_name);
//        add_production_etv_bz.setText(teamname);

        //设置初始日期
        sj_month = calendar.get(Calendar.MONTH) + 1;
        add_production_date.setText(calendar.get(Calendar.YEAR) + "-" + sj_month + "-" + calendar.get(Calendar.DAY_OF_MONTH));

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        }).start();
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", clickid)
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(ProductionDetail.this, "请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("NewProductionFragment", response);
                        try {
                            JSONObject jsb = new JSONObject(response);
                            gx_jsonArray = jsb.getJSONArray("cells");
                            dt_text = new String[gx_jsonArray.length()];
                            num = new String[gx_jsonArray.length()];
                            final Map<String, String> data = new HashMap<String, String>();
                            dw = new ArrayList<String>();
                            for (int i = 0; i < gx_jsonArray.length(); i++) {
                                num[i] = gx_jsonArray.optJSONObject(i).getString("serialno");
                                final String str_all = gx_jsonArray.optJSONObject(i).getString("talgorithm");
                                String ismust = gx_jsonArray.optJSONObject(i).getString("ismust");
                                final int dt_tag = i;
                                dw.add(gx_jsonArray.optJSONObject(i).getString("targetunitname"));
                                //创建文本框
                                final EditText editText = new EditText(ProductionDetail.this);
                                editText.setHint(gx_jsonArray.optJSONObject(i).getString("targetname") + "                                      " + "(" + dw.get(i) + ")");
                                editText.setTextSize(16);
                                editText.setHintTextColor(Color.rgb(211, 211, 211));
                                editText.setBackgroundResource(R.drawable.editext_bk);
                                editText.setPadding(50, 0, 0, 0);
                                editText.setTag(ismust);
                                editText.setLayoutParams(new LinearLayout.LayoutParams(width, 120));

//                                if (gx_jsonArray.optJSONObject(i).getString("datatype").equals("SJLX002")) {
//                                    editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//                                }
                                add_production_zdy_ll.addView(editText);

//                                //监听编辑框
//                                editText.addTextChangedListener(new TextWatcher() {
//                                    @Override
//                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                    }
//
//
//                                    @Override
//                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
////                                        map.put("column_" + num[dt_tag], TextUtils.isEmpty(editText.getText().toString()) ? "" : editText.getText().toString());
////                                        if (map.get("column_" + num[dt_tag]) == null || map.get("column_" + num[dt_tag]).equals("")){
////
////                                        }
////                                        if (str_all != null || str_all != "") {
////                                            for (int i = 1; i <= gx_jsonArray.length(); i++) {
////                                                gs_str = str_all.replace("column_" + i, data.get("column_" + i));
////                                            }
////                                            Log.e(TAG, gs_str);
////
////                                        } else {
////                                            data.put((String) editText.getTag(), editText.getText().toString());
////                                        }
//                                    }
//
//                                    @Override
//                                    public void afterTextChanged(Editable s) {
//
//                                    }
//
//                                });


                                //判断某个指标中有没有公式

                                //

                                //编辑框监听
                                final String[] colModel = str_all.split("=");
                                if (str_all.contains("column_" + i)) {
                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                            String newStr = colModel[1].replaceAll("column_" + (dt_tag + 1), editText.getText().toString());
//                                            Log.e(TAG, newStr + "---" + editText.getText().toString());
                                        }


                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //通过作业地点工艺获取指标值
    private void mGetTargetValues(String zyddId, String gyId) {
        OkHttpUtils
                .get()
                .url(getTarget_url)
                .addParams("access_token", user_token)
                .addParams("procid", clickid)
                .addParams("projectid", zyddId)
                .addParams("techid", gyId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(ProductionDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tag = jsonArray.optJSONObject(i).getString("id");
                                tag_value = jsonArray.optJSONObject(i).getString("value");
                                //选择完作业地点或者工艺时自动获取部分指标内容
                                ((EditText) add_production_zdy_ll.getChildAt(Integer.parseInt(tag))).setText(tag_value + "(" + dw.get(i) + ")");
                                ((EditText) add_production_zdy_ll.getChildAt(Integer.parseInt(tag))).setTextColor(Color.BLACK);
                                if (tag_value.equals("") || tag_value == null) {
                                    ((EditText) add_production_zdy_ll.getChildAt(Integer.parseInt(tag))).setEnabled(true);
                                } else {
                                    ((EditText) add_production_zdy_ll.getChildAt(Integer.parseInt(tag))).setEnabled(false);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //返回的班组内容
        if (requestCode == BZ) {
            if (data != null) {
                bundle = data.getExtras();
                String bz_back = bundle.getString("bz_back");
                teamgroupid = bundle.getString("bz_selectid");
                add_production_etv_bz.setText(bz_back);
                add_production_device.setText(null);
                deviceid = null;
            }
        }
        //返回的班次内容
        else if (requestCode == A) {
            if (data != null) {
                bundle = data.getExtras();
                String back = bundle.getString("back");
                add_production_tv_bc.setText(back);
                classid = bundle.getString("selectid");

            }
        }
        //返回的作业地点内容
        else if (requestCode == B) {
            if (data != null) {
                bundle = data.getExtras();
                String project_back = bundle.getString("project_back");
                projectid = bundle.getString("project_selectid");
                add_production_jl.setText(project_back);
                add_production_gy.setText(null);
                techid = null;
                mGetTargetValues(projectid, techid);
            }
        }
        //返回的设备内容
        else if (requestCode == C) {
            if (data != null) {
                bundle = data.getExtras();
                String device_back = bundle.getString("device_back");
                deviceid = bundle.getString("device_selectid");
                add_production_device.setText(device_back);
            }
        }
        //返回的工艺内容
        else if (requestCode == D) {
            if (data != null) {
                bundle = data.getExtras();
                String tech_back = bundle.getString("tech_back");
                techid = bundle.getString("techid");
                add_production_gy.setText(tech_back);
                mGetTargetValues(projectid, techid);
            }
        }
    }

    @Override
    protected void setOnClick() {
        production_detail_back.setOnClickListener(this);
        add_production_etv_bz.setOnClickListener(this);
        add_production_date.setOnClickListener(this);
        add_production_tv_bc.setOnClickListener(this);
        add_production_jl.setOnClickListener(this);
        add_production_device.setOnClickListener(this);
        add_production_gy.setOnClickListener(this);
        add_production_submit.setOnClickListener(this);
        add_production_history.setOnClickListener(this);
    }

    //清空数据
    private void CleanDate() {
        add_production_tv_bc.setText(null);
        classid = null;
        add_production_jl.setText(null);
        projectid = null;
        add_production_device.setText(null);
        deviceid = null;
        add_production_gy.setText(null);
        techid = null;
        add_production_etv_bz.setText(null);
        teamgroupid = null;
        add_production_beizhu.setText(null);
        add_production_date.setText(calendar.get(Calendar.YEAR) + "-" + sj_month + "-" + calendar.get(Calendar.DAY_OF_MONTH));

        for (int i = 0; i < gx_jsonArray.length(); i++) {
            ((TextView) add_production_zdy_ll.getChildAt(i)).setText(null);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.production_detail_back:
                finish();
                break;
            //班组选择
            case R.id.add_production_etv_bz:
                intent = new Intent(this, AddProChooseBz.class);
                intent.putExtra("myprocid", clickid);
                startActivityForResult(intent, BZ);
                break;

            //班次选择
            case R.id.add_production_tv_bc:
                intent = new Intent(this, AddProChooseBc.class);
                startActivityForResult(intent, A);
                break;


            // 日期按钮
            case R.id.add_production_date:
                DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        add_production_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;

            //作业地点选择
            case R.id.add_production_jl:
                if (classid == null) {
                    ShowToast.showShort(this, R.string.select_first_class);
                } else {
                    intent = new Intent(this, NewProductionJl.class);
                    intent.putExtra("classid", classid);
                    intent.putExtra("reportdate", add_production_date.getText().toString());
                    intent.putExtra("my_clickid", clickid);
                    intent.putExtra("teamgroupid", teamgroupid);
                    startActivityForResult(intent, B);
                }
                break;

            //设备选择
            case R.id.add_production_device:
//                if (teamgroupid ==null){
//                    ShowToast.showShort(this, R.string.select_first_teamgroup);
//                }else{
                intent = new Intent(this, AddProDevice.class);
                intent.putExtra("myteamgroupid", teamgroupid);
                startActivityForResult(intent, C);
//                }
                break;


            //工艺选择
            case R.id.add_production_gy:
                if (projectid == null) {
                    ShowToast.showShort(this, R.string.select_first_job_location);
                } else {
                    intent = new Intent(this, AddProTech.class);
                    intent.putExtra("projectid", projectid);
                    intent.putExtra("clickid", clickid);
                    startActivityForResult(intent, D);
                }
                break;

            case R.id.add_production_submit:
                if (teamgroupid == null) {
                    ShowToast.showShort(this, R.string.noselect_teamgroup_yet);
                } else if (classid == null) {
                    ShowToast.showShort(this, R.string.noselect_class_yet);
                } else if (projectid == null) {
                    ShowToast.showShort(this, R.string.noselect_job_location_yet);
//                } else if (deviceid == null) {
//                    ShowToast.showShort(this, "未选择设备");
                } else if (techid == null) {
                    ShowToast.showShort(this, R.string.noselect_tech_yet);
                } else {
                    for (int j = 1; j < gx_jsonArray.length() + 1; j++) {
                        if (((TextView) add_production_zdy_ll.getChildAt(j - 1)).getTag().toString().equals("1") && ((TextView) add_production_zdy_ll.getChildAt(j - 1)).getText().toString().equals("")) {
                            ShowToast.showShort(this, R.string.no_index_content);
                            return;
                        } else {
                            map.put("column_" + j, ((TextView) add_production_zdy_ll.getChildAt(j - 1)).getText().toString());
                        }
                    }
                    map.put("reportdate", add_production_date.getText().toString());
                    map.put("memo", add_production_beizhu.getText().toString());
                    map.put("classes", classid);
                    map.put("teamgroupid", teamgroupid);
                    map.put("projectid", projectid);
                    map.put("procid", clickid);
                    map.put("devid", deviceid);
                    map.put("techid", techid);

                    gson = new Gson();
                    jsonStr = gson.toJson(map);

                    try {
                        jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpUtils
                                    .get()
                                    .url(add_url)
                                    .addParams("access_token", user_token)
                                    .addParams("procid", clickid)
                                    .addParams("str", jsonStr)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            e.printStackTrace();
                                            ShowToast.showShort(ProductionDetail.this, R.string.network_error);
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            Log.e(TAG, response);
                                            my_dialog = DialogUtil.createLoadingDialog(ProductionDetail.this, R.string.login_loading);
                                            handler.sendEmptyMessageDelayed(0, 2000);

                                        }
                                    });
                        }
                    }).start();
                }
                break;

            //历史台账
            case R.id.add_production_history:
                intent = new Intent(this, ProductionHistory.class);
                intent.putExtra("his_procid", clickid);
                startActivity(intent);
                break;
        }
    }
}
