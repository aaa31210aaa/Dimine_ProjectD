package com.example.administrator.dimine_projectd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.NotificationListViewAdapter;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import entity.Notification;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ThreadUtil;

public class MyNotification extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = "MyNotification";
    private ImageView my_notification_back;
    private ListView my_notification_listview;
    private Calendar calendar;
    private Intent intent;
    private String url = "";
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private NotificationListViewAdapter adapter;
    //内容数组
    private String[] myContent;

    private int mPage = 1;
    private int total; //实际页数
    private boolean a = true;
    private boolean b = true;
    private ArrayList<Notification> no_list;

    private BGARefreshLayout notification_refresh;
    private static final int LOADING_REFRESH = 3000;
    private static final int LOADING_MORE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        my_notification_back = (ImageView) findViewById(R.id.my_notification_back);
        my_notification_listview = (ListView) findViewById(R.id.my_notification_listview);

        notification_refresh = (BGARefreshLayout) findViewById(R.id.notification_refresh);
        MyRefreshStyle(notification_refresh);


        url = MyHttpUtils.ApiNotification();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        editor.commit();
//        new Thread(mRunnable).start();
        mOkhttp(mPage);


        my_notification_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ShowToast.showShort(MyNotification.this, "这是第" + (position + 1) + "条通知");
                intent = new Intent(MyNotification.this, NotificationDetail.class);
                intent.putExtra("content", myContent[position]);
                startActivity(intent);
            }
        });
    }


    private void mOkhttp(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        notification_refresh.endRefreshing();
                        ShowToast.showShort(MyNotification.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, response);
                            JSONObject param = new JSONObject(response);
                            JSONArray jsonArray = param.getJSONArray("rows");

                            no_list = new ArrayList<>();
                            myContent = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Notification tz = new Notification();
                                tz.setTitle(jsonArray.optJSONObject(i).getString("messagetitle"));
                                tz.setContent(jsonArray.optJSONObject(i).getString("shorttile"));
                                tz.setCreatedate(jsonArray.optJSONObject(i).getString("mestime"));
                                myContent[i] = jsonArray.optJSONObject(i).getString("mescontent");
                                no_list.add(tz);
                            }
                            adapter = new NotificationListViewAdapter(no_list, MyNotification.this);
                            my_notification_listview.setAdapter(adapter);
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
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        notification_refresh.endLoadingMore();
                        ShowToast.showShort(MyNotification.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d(TAG, response);
                            JSONObject param = new JSONObject(response);
                            JSONArray jsonArray = param.getJSONArray("rows");

                            no_list = new ArrayList<>();
                            myContent = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Notification tz = new Notification();
                                tz.setTitle(jsonArray.optJSONObject(i).getString("messagetitle"));
                                tz.setContent(jsonArray.optJSONObject(i).getString("shorttile"));
                                tz.setCreatedate(jsonArray.optJSONObject(i).getString("mestime"));
                                myContent[i] = jsonArray.optJSONObject(i).getString("mescontent");
                                no_list.add(tz);
                            }
                            adapter.addMoreData(no_list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        my_notification_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_notification_back:
                finish();
                break;
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                no_list.clear();
                mOkhttp(1);
                adapter.addNewData(no_list);
                refreshLayout.endRefreshing();
                mPage = 1;
                ShowToast.showShort(MyNotification.this, R.string.refresh);
            }
        },LOADING_REFRESH);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        mPage++;
        if (mPage > total) {
            refreshLayout.endLoadingMore();
            ShowToast.showShort(MyNotification.this, R.string.no_more_message);
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
