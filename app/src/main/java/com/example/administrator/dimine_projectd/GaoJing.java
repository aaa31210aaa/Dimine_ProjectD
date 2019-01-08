package com.example.administrator.dimine_projectd;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.GjFragmentAdapter;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import entity.Alarm;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ThreadUtil;

public class GaoJing extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String TAG = "GaoJing";
    private LinearLayout gaojing_have_nomessage;
    private ImageView gj_back;
    private ImageView gj_back_top; //返回listview顶部

    private String url;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String user_token;
    private ArrayList<Alarm> gj_list;
    private GjFragmentAdapter adapter;

    private int mPage = 1;
    private int total; //总页数
    private boolean a = true;
    private boolean b = true;
    private Dialog dialog;

    //修改后的
    private BGARefreshLayout gj_refresh;
    private ListView gj_listview;
    private static final int LOADING_REFRESH = 3000;
    private static final int LOADING_MORE = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gao_jing);

        initView();
        setOnClick();
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void dispatchMessage(Message msg) {
//            super.dispatchMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    adapter.notifyDataSetChanged();
//                    ShowToast.showShort(GaoJing.this, R.string.refresh);
//                    break;
//                case 1:
//                    dialog.dismiss();
//                    b = true;
//                    ShowToast.showShort(GaoJing.this, R.string.loading_more_message);
//                    mPage++;
//                    break;
//                case 2:
//                    break;
//                default:
//                    break;
//
//            }
//        }
//    };

    @Override
    protected void initView() {
        gaojing_have_nomessage = (LinearLayout) findViewById(R.id.gaojing_have_nomessage);
        gj_listview = (ListView) findViewById(R.id.gj_listview);
        gj_back = (ImageView) findViewById(R.id.gj_back);
        gj_back_top = (ImageView) findViewById(R.id.gj_back_top);

        gj_refresh = (BGARefreshLayout) findViewById(R.id.gj_refresh);
        MyRefreshStyle(gj_refresh);


        url = MyHttpUtils.ApiAlarmList();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        editor.commit();
        mOkhttp(mPage);
//        LoadMore();
//        gj_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
    }

    //请求数据
    private void mOkhttp(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("alarmtype", "GJLB002")
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(GaoJing.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("rows");
                            total = Integer.parseInt(jsonObject.getString("total"));

                            if (jsonArray.length() == 0) {
                                gaojing_have_nomessage.setVisibility(View.VISIBLE);
                            } else {
                                gaojing_have_nomessage.setVisibility(View.GONE);
                                //赋值
                                gj_list = new ArrayList<Alarm>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Alarm alarm = new Alarm();
                                    alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
                                    alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
                                    alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
                                    alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("memo"));
                                    gj_list.add(alarm);
                                }
                                adapter = new GjFragmentAdapter(GaoJing.this, gj_list);
                                gj_listview.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //加载更多
    private void LoadMore(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("alarmtype", "GJLB002")
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        gj_refresh.endLoadingMore();
                        ShowToast.showShort(GaoJing.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("rows");
                            total = Integer.parseInt(jsonObject.getString("total"));

                            //赋值
                            gj_list = new ArrayList<Alarm>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Alarm alarm = new Alarm();
                                alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
                                alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
                                alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
                                alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("memo"));
                                gj_list.add(alarm);
                            }
                            adapter.addMoreData(gj_list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    //滑动到底部加载更多
//    private void LoadMore() {
//        gj_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem == 0) {
//                    View firstVisibleItemView = gj_listview.getChildAt(0);
//                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        // Log.d("ListView", "<----滚动到顶部----->");
//                    }
//                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    View lastVisibleItemView = gj_listview.getChildAt(gj_listview.getChildCount() - 1);
//                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == gj_listview.getHeight()) {
//                        if (mPage > total) {
//                            if (a) {
//                                ShowToast.showShort(GaoJing.this, R.string.no_more_message);
//                                a = false;
//                            }
//                        } else {
//                            if (b) {
//                                b = false;
//                                OkHttpUtils
//                                        .get()
//                                        .url(url)
//                                        .addParams("access_token", user_token)
//                                        .addParams("alarmtype", "GJLB002")
//                                        .addParams("page", mPage + "")
//                                        .addParams("rows", "10")
//                                        .build()
//                                        .execute(new StringCallback() {
//                                            @Override
//                                            public void onError(Request request, Exception e) {
//                                                e.printStackTrace();
//                                                ShowToast.showShort(GaoJing.this, R.string.network_error);
//                                            }
//
//                                            @Override
//                                            public void onResponse(String response) {
//                                                Log.e(TAG,response);
//                                                try {
//                                                    JSONObject jsonObject = new JSONObject(response);
//                                                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
//
//                                                    //赋值
//                                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                                        Alarm alarm = new Alarm();
//                                                        alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
//                                                        alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
//                                                        alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
//                                                        alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("memo"));
//                                                        gj_list.add(alarm);
//                                                    }
//                                                    adapter.MyUpdate(gj_list);
//                                                    mPage = mPage + 1;
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                            }
//                                        });
//                                dialog = DialogUtil.createLoadingDialog(GaoJing.this, R.string.loading);
//                                handler.sendEmptyMessageDelayed(1, 1000);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//    }

    @Override
    protected void setOnClick() {
        gj_back.setOnClickListener(this);
        gj_back_top.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gj_back:
                finish();
                break;
            case R.id.gj_back_top:
                gj_listview.setSelection(0);
                ShowToast.showShort(this, R.string.back_top);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                gj_list.clear();
                mOkhttp(1);
                adapter.addNewData(gj_list);
                refreshLayout.endRefreshing();
                mPage = 1;
                ShowToast.showShort(GaoJing.this, R.string.refresh);
            }
        }, LOADING_REFRESH);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        mPage++;
        if (mPage > total) {
            refreshLayout.endLoadingMore();
            ShowToast.showShort(GaoJing.this, R.string.no_more_message);
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

//    //刷新
//    @Override
//    public void onRefresh() {
//        Thread mthread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    Message message = handler.obtainMessage();
//                    message.what = 0;
//                    handler.sendMessage(message);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        mthread.start();
//    }
}
