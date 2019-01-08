package com.example.administrator.dimine_projectd;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.MyPllbAdapter;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import entity.CommentEntity;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ThreadUtil;

public class MyPllb extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = "MyPllb";
    private ImageView pllb_back;
    private ImageView btn_main_send; //评论

    private String url;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ArrayList<CommentEntity> commList;

    private Intent intent;
    private String id; //详情内容id
    private String commtype; //评论类型
    private MyPllbAdapter adapter;

    private int mPage = 1; //页数
    private int total; //实际页数
    private Dialog dialog;
    private boolean a = true;
    private boolean b = true;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private String language;

    private static final int A = 10;

    private BGARefreshLayout pllb_refresh;
    private ListView lv_comments;
    private static final int LOADING_REFRESH = 3000;
    private static final int LOADING_MORE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pllb);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        pllb_back = (ImageView) findViewById(R.id.pllb_back);
        btn_main_send = (ImageView) findViewById(R.id.btn_main_send);
        lv_comments = (ListView) findViewById(R.id.lv_comments);
        pllb_refresh = (BGARefreshLayout) findViewById(R.id.pllb_refresh);
        MyRefreshStyle(pllb_refresh);

        url = MyHttpUtils.ApiCommentList();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage", null);

        intent = getIntent();
        id = intent.getStringExtra("detaiId");
        commtype = intent.getStringExtra("selectCommtype");

//        ShowToast.showShort(this, commtype);
//        commList = new ArrayList<CommentEntity>();
//        for (int i = 0; i < arr.length; i++) {
//            CommentEntity commentEntity = new CommentEntity();
//            commentEntity.setUserName(arr[i]);
//            commentEntity.setCommentTime("2016-09-28");
//            commentEntity.setCommentContent(arr2[i]);
//            commList.add(commentEntity);
//        }
//        MyPllbAdapter adapter = new MyPllbAdapter(this, commList);
//        lv_comments.setAdapter(adapter);
        mOkhttp(mPage);
    }


    private void mOkhttp(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("commtype", commtype)
                .addParams("busiid", id)
                .addParams("page", page + "")
                .addParams("rows", "10")
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyPllb.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("rows");
                            total = Integer.parseInt(jsonObject.getString("total"));
                            commList = new ArrayList<CommentEntity>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CommentEntity commentEntity = new CommentEntity();
                                commentEntity.setUserName(jsonArray.optJSONObject(i).getString("username"));
                                commentEntity.setCommentTime(jsonArray.optJSONObject(i).getString("commtime"));
                                commentEntity.setCommentContent(jsonArray.optJSONObject(i).getString("commmemo"));
                                commList.add(commentEntity);
                            }
                            adapter = new MyPllbAdapter(MyPllb.this, commList);
                            lv_comments.setAdapter(adapter);
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
                .addParams("commtype", commtype)
                .addParams("busiid", id)
                .addParams("page", page + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyPllb.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("rows");
                            total = Integer.parseInt(jsonObject.getString("total"));
                            commList = new ArrayList<CommentEntity>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CommentEntity commentEntity = new CommentEntity();
                                commentEntity.setUserName(jsonArray.optJSONObject(i).getString("username"));
                                commentEntity.setCommentTime(jsonArray.optJSONObject(i).getString("commtime"));
                                commentEntity.setCommentContent(jsonArray.optJSONObject(i).getString("commmemo"));
                                commList.add(commentEntity);
                            }
                            adapter.addMoreData(commList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setOnClick() {
        pllb_back.setOnClickListener(this);
        btn_main_send.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pllb_back:
                finish();
                break;
            case R.id.btn_main_send:
                intent = new Intent(this, MyPlEditext.class);
                intent.putExtra("myCommentType", commtype);
                intent.putExtra("myid", id);
                startActivityForResult(intent, A);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("access_token", user_token)
                    .addParams("commtype", commtype)
                    .addParams("busiid", id)
                    .addParams("page", "1")
                    .addParams("rows", "10")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                            ShowToast.showShort(MyPllb.this, R.string.network_error);
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("rows");
                                commList = new ArrayList<CommentEntity>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CommentEntity commentEntity = new CommentEntity();
                                    commentEntity.setUserName(jsonArray.optJSONObject(i).getString("username"));
                                    commentEntity.setCommentTime(jsonArray.optJSONObject(i).getString("commtime"));
                                    commentEntity.setCommentContent(jsonArray.optJSONObject(i).getString("commmemo"));
                                    commList.add(commentEntity);
                                }
                                adapter.uPdate(commList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                commList.clear();
                mOkhttp(1);
                adapter.addNewData(commList);
                refreshLayout.endRefreshing();
                mPage = 1;
                ShowToast.showShort(MyPllb.this, R.string.refresh);
            }
        }, LOADING_REFRESH);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        mPage++;
        if (mPage > total) {
            refreshLayout.endLoadingMore();
            ShowToast.showShort(MyPllb.this, R.string.no_more_message);
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
