package com.example.administrator.dimine_projectd;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.YjFragmentAdapter;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import entity.Alarm;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ThreadUtil;

public class YuJing extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String TAG = "YuJing";

    private RelativeLayout yujing_have_nomessage;
    private ImageView yj_back;
    private ImageView yj_back_top; //返回listview顶部

    private String url;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String user_token;
    private ArrayList<Alarm> yj_list;
    private YjFragmentAdapter adapter;
    private int mPage = 1;
    private int total; //总页数
    private boolean a = true;
    private boolean b = true;
    private Dialog dialog;

    //修改后的刷新
    private BGARefreshLayout yj_refresh;
    private ListView yj_listview;
    private static final int LOADING_REFRESH = 3000;
    private static final int LOADING_MORE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu_jing);
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
//                    ShowToast.showShort(YuJing.this, R.string.refresh);
//                    break;
//                case 1:
//                    dialog.dismiss();
//                    b = true;
//                    ShowToast.showShort(YuJing.this, R.string.loading_more_message);
//                    mPage++;
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


    @Override
    protected void initView() {
        yujing_have_nomessage = (RelativeLayout) findViewById(R.id.yujing_have_nomessage);
        yj_listview = (ListView) findViewById(R.id.yj_listview);
        yj_back = (ImageView) findViewById(R.id.yj_back);
        yj_back_top = (ImageView) findViewById(R.id.yj_back_top);

        yj_refresh = (BGARefreshLayout) findViewById(R.id.yj_refresh);
        //设置刷新样式
        MyRefreshStyle(yj_refresh);


        url = MyHttpUtils.ApiAlarmList();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        editor.commit();

        mOkhttp(mPage);
//        LoadMore();
    }


    @Override
    protected void setOnClick() {
        yj_back.setOnClickListener(this);
        yj_back_top.setOnClickListener(this);
    }

    //请求数据
    private void mOkhttp(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("alarmtype", "GJLB001")
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(YuJing.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("rows");
                            total = Integer.parseInt(jsonObject.getString("total"));

                            if (jsonArray.length() == 0) {
                                yujing_have_nomessage.setVisibility(View.VISIBLE);
                            } else {
                                yujing_have_nomessage.setVisibility(View.GONE);

                                //赋值
                                yj_list = new ArrayList<Alarm>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Alarm alarm = new Alarm();
                                    alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
                                    alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
                                    alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
                                    alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("memo"));
                                    yj_list.add(alarm);
                                }
                                adapter = new YjFragmentAdapter(YuJing.this, yj_list);
                                yj_listview.setAdapter(adapter);
//                                mPage = mPage + 1;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //滑动到底部加载更多
    private void LoadDataMore(int page){
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("alarmtype", "GJLB001")
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        yj_refresh.endLoadingMore();
                        ShowToast.showShort(YuJing.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("rows");
                            total = Integer.parseInt(jsonObject.getString("total"));

                            //赋值
                            yj_list = new ArrayList<Alarm>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Alarm alarm = new Alarm();
                                alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
                                alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
                                alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
                                alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("memo"));
                                yj_list.add(alarm);
                            }
                            adapter.addMoreData(yj_list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }



//    private void LoadMore() {
//        yj_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem == 0) {
//                    View firstVisibleItemView = yj_listview.getChildAt(0);
//                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        // Log.d("ListView", "<----滚动到顶部----->");
//                    }
//                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    View lastVisibleItemView = yj_listview.getChildAt(yj_listview.getChildCount() - 1);
//                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == yj_listview.getHeight()) {
//                        if (mPage > total) {
//                            if (a) {
//                                ShowToast.showShort(YuJing.this, R.string.no_more_message);
//                                a = false;
//                            }
//                        } else {
//                            if (b) {
//                                b = false;
//                                OkHttpUtils
//                                        .get()
//                                        .url(url)
//                                        .addParams("access_token", user_token)
//                                        .addParams("alarmtype", "GJLB001")
//                                        .addParams("page", mPage + "")
//                                        .addParams("rows", "10")
//                                        .build()
//                                        .execute(new StringCallback() {
//                                            @Override
//                                            public void onError(Request request, Exception e) {
//                                                e.printStackTrace();
//                                                ShowToast.showShort(YuJing.this, R.string.network_error);
//                                            }
//
//                                            @Override
//                                            public void onResponse(String response) {
//                                                Log.e(TAG, response);
//                                                try {
//                                                    JSONObject jsonObject = new JSONObject(response);
//                                                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
//
//                                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                                        Alarm alarm = new Alarm();
//                                                        alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
//                                                        alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
//                                                        alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
//                                                        alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("memo"));
//                                                        yj_list.add(alarm);
//                                                    }
////                                                    adapter.refresh(yj_list);
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                            }
//                                        });
//                                dialog = DialogUtil.createLoadingDialog(YuJing.this, R.string.loading);
//                                handler.sendEmptyMessageDelayed(1, 1000);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yj_back:
                finish();
                break;
            case R.id.yj_back_top:
                yj_listview.setSelection(0);
                ShowToast.showShort(this, R.string.back_top);
                break;
        }
    }

    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                yj_list.clear();
                mOkhttp(1);
                adapter.addNewData(yj_list);
                refreshLayout.endRefreshing();
                mPage = 1;
                ShowToast.showShort(YuJing.this, R.string.refresh);
            }
        }, LOADING_REFRESH);
    }

    //加载更多
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        mPage++;
        if (mPage > total) {
            refreshLayout.endLoadingMore();
            ShowToast.showShort(YuJing.this, R.string.no_more_message);
            return false;
        }
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                LoadDataMore(mPage);
                refreshLayout.endLoadingMore();
            }
        }, LOADING_MORE);

        return true;
    }

}
