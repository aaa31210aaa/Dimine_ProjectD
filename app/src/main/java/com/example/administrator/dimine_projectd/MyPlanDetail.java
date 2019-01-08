package com.example.administrator.dimine_projectd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.LvInfoAdapter;
import adapter.LvNameAdapter;
import adapter.MyPlanDetailAdapter;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import entity.PlanContent;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ThreadUtil;

public class MyPlanDetail extends BaseActivity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = "MyPlanDetail";
    private NoScrollHorizontalScrollView sv_normalgoods_title;//不可滑动的顶部左侧的ScrollView
    private LinkedHorizontalScrollView sv_normalgoods_detail;//底部左侧的ScrollView
    private ListView lv_normalgoodname;//底部左侧的ListView
    private ListView lv_normalgood_info;//底部右侧的ListView

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    private LvNameAdapter mLvNormalNameAdapter;
    private LvInfoAdapter mLvNormalInfoAdapter;

    private ImageView my_plan_test_back; //返回
    private TextView my_plan_test_title; //顶部标题


    private TextView tv_name;//第一列列名
    private LinearLayout sv_title_ll; //装后几列列名的容器
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;
    private String procid;
    private String techid;
    //    private String titlename; //标题名字
//    private String proctname;
    private String str;

    private TextView tv; //动态添加的textview
    private int width;//屏幕的宽
    private int height; //屏幕的高
    private Map<String, String> map;
    private ArrayList<PlanContent> plan1_list; //第一列list
    private ArrayList<Map<String, String>> plan2_list; //第二列list;
    private int height_px;

    private Intent intent;
    private ArrayList<String> detaiId_list; //详情id集合
    private String commtype; //传给评论列表的类型
    private String myDetailId; //详情id
    private int mPage = 1; //页数
    private int total; //实际页数
    private String language;
    private static final int LOADING_REFRESH = 3000;
    private static final int LOADING_MORE = 2000;


    //修改后的
    private BGARefreshLayout plan_refresh; //有信息时候的布局
    private RelativeLayout plan_have_nomessage; //没有信息时候的布局

    private ListView plan_detail_listview;
    private ArrayList<String> tv_title;

    private ArrayList<Map<String, String>> tv_content;
    private Map<String, String> contents;

    private MyPlanDetailAdapter adapter;
    private String titile;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan_test);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage", null);

        // 获得屏幕宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        height_px = dip2px(this, 70);

        Intent intent = getIntent();
//        titlename = intent.getStringExtra("titlename");
//        proctname = intent.getStringExtra("proctname");
        procid = intent.getStringExtra("myprocid");
        techid = intent.getStringExtra("mytechid");
        str = intent.getStringExtra("str");

        my_plan_test_back = (ImageView) findViewById(R.id.my_plan_test_back);
        my_plan_test_title = (TextView) findViewById(R.id.my_plan_test_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        plan_have_nomessage = (RelativeLayout) findViewById(R.id.plan_have_nomessage);

        //设置刷新的样式
        plan_refresh = (BGARefreshLayout) findViewById(R.id.plan_refresh);
        MyRefreshStyle(plan_refresh);


//        sv_normalgoods_title = (NoScrollHorizontalScrollView) findViewById(R.id.sv_title);
//        sv_normalgoods_detail = (LinkedHorizontalScrollView) findViewById(R.id.sv_good_detail);
//        lv_normalgoodname = (ListView) findViewById(R.id.lv_goodname);
//        lv_normalgood_info = (ListView) findViewById(R.id.lv_good_info);

//        sv_title_ll = (LinearLayout) findViewById(R.id.sv_title_ll);


        //修改后
        plan_detail_listview = (ListView) findViewById(R.id.plan_detail_listview);


        //设置顶部标题
        if (str.equals("month")) {
            my_plan_test_title.setText(R.string.plan_tab_month);
            if (language.equals("en")) {
                myDetailId = "month plan detail id";
            } else {
                myDetailId = "月计划详情ID";
            }
            url = MyHttpUtils.ApiPlanMonthlyDetail();
            commtype = "PLLX002";
        } else {
            my_plan_test_title.setText(R.string.plan_tab_week);
            if (language.equals("en")) {
                myDetailId = "week plan detail id";
            } else {
                myDetailId = "周计划详情ID";
            }
            url = MyHttpUtils.ApiPlanWeeklyDetail();
            commtype = "PLLX003";
        }
        mOkhttp(mPage);
    }

    @Override
    protected void setOnClick() {
        my_plan_test_back.setOnClickListener(this);
    }


    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void mOkhttp(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("techid", techid)
                .addParams("page", page + "")
                .addParams("rows", "10")
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyPlanDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("title");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("cells");


                            String flag = jsonObject.getString("flag");
                            detaiId_list = new ArrayList<String>();

                            tv_title = new ArrayList<String>();
                            tv_content = new ArrayList<Map<String, String>>();

                            if (flag.equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    titile = jsonArray.getString(i);
                                    tv_title.add(titile);
                                }

                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    detaiId_list.add(jsonArray2.optJSONObject(j).getString(myDetailId));
                                    contents = new HashMap<String, String>();
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        content = jsonArray2.optJSONObject(j).getString(jsonArray.getString(k));
                                        contents.put(jsonArray.getString(k), content);
                                    }
                                    tv_content.add(contents);
                                }
                                adapter = new MyPlanDetailAdapter(MyPlanDetail.this, tv_title, tv_content);
                                plan_detail_listview.setAdapter(adapter);

                                plan_detail_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        intent = new Intent(MyPlanDetail.this, MyPllb.class);
                                        intent.putExtra("detaiId", detaiId_list.get(position));
                                        intent.putExtra("selectCommtype", commtype);
                                        startActivity(intent);
                                    }
                                });


                            } else {
                                plan_refresh.setVisibility(View.GONE);
                                plan_have_nomessage.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void LoadMore(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("techid", techid)
                .addParams("page", page + "")
                .addParams("rows", "10")
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyPlanDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("title");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("cells");
                            String flag = jsonObject.getString("flag");
                            detaiId_list = new ArrayList<String>();
                            tv_title = new ArrayList<String>();
                            tv_content = new ArrayList<Map<String, String>>();

                            if (flag.equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    titile = jsonArray.getString(i);
                                    tv_title.add(titile);
                                }

                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    detaiId_list.add(jsonArray2.optJSONObject(j).getString(myDetailId));
                                    contents = new HashMap<String, String>();
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        content = jsonArray2.optJSONObject(j).getString(jsonArray.getString(k));
                                        contents.put(jsonArray.getString(k), content);
                                    }
                                    tv_content.add(contents);
                                }
                                adapter = new MyPlanDetailAdapter(MyPlanDetail.this, tv_title, tv_content);
                                plan_detail_listview.setAdapter(adapter);

                                plan_detail_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        intent = new Intent(MyPlanDetail.this, MyPllb.class);
                                        intent.putExtra("detaiId", detaiId_list.get(position));
                                        intent.putExtra("selectCommtype", commtype);
                                        startActivity(intent);
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_plan_test_back:
                finish();
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                mOkhttp(1);
                refreshLayout.endRefreshing();
                adapter.addNewData(tv_title, tv_content);
                mPage = 1;
                ShowToast.showShort(MyPlanDetail.this, R.string.refresh);
            }
        }, LOADING_REFRESH);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        mPage++;
        if (mPage > total) {
            refreshLayout.endLoadingMore();
            ShowToast.showShort(MyPlanDetail.this, R.string.no_more_message);
            return false;
        }
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                LoadMore(mPage);
                refreshLayout.endLoadingMore();
            }
        }, LOADING_MORE);


        return true;
    }
}
