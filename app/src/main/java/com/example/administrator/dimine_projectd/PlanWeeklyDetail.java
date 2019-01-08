package com.example.administrator.dimine_projectd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import adapter.PlanDetailAdapter;
import utils.MyHttpUtils;
import utils.ShowToast;

public class PlanWeeklyDetail extends Activity implements View.OnClickListener {
    private static final String TAG = "PlanWeeklyDetail";
    private ImageView plan_weekly_detail_back;
    private TextView plan_weekly_detail_title; //顶部标题

    private RelativeLayout weekly_plan_detail_nomessage; //无数据内容


    private String[] arr_titles;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;
    private Context context;
    private String titlename; //标题名字
    private String proctname;
    private String procid;


    private boolean a = true;

    private LayoutInflater mInflater;

    private HVListView mListView;

    private JSONArray jsonArray_title; //列名
    private JSONArray jsonArray2;

    private String item1Content;  //第一列的列名

    private Map<String, String> map;
    private ArrayList<Map<String, String>> planDtailList;

    private PlanDetailAdapter adapter;

    private TextView mitem1;
    private View item_view;
    private int width;//屏幕的宽
    private int height; //屏幕的高
    private String[] arr_one; //第一列的内容


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_weekly_detail);
        initView();
        setOnClick();
    }

    protected void initView() {
        item_view = View.inflate(this, R.layout.item, null);
        plan_weekly_detail_title = (TextView) findViewById(R.id.plan_weekly_detail_title);
        plan_weekly_detail_back = (ImageView) findViewById(R.id.plan_weekly_detail_back);
        item_view = View.inflate(this, R.layout.item, null);
        mitem1 = (TextView) item_view.findViewById(R.id.item1);

        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        weekly_plan_detail_nomessage = (RelativeLayout) findViewById(R.id.weekly_plan_detail_nomessage);
        url = MyHttpUtils.ApiPlanMonthlyDetail();


        // 获得屏幕宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        Intent intent = getIntent();
        titlename = intent.getStringExtra("titlename");
        proctname = intent.getStringExtra("proctname");
        procid = intent.getStringExtra("myprocid");
        plan_weekly_detail_title.setText(titlename + "-" + proctname);

        thread.start();
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("access_token", user_token)
                    .addParams("procid", procid)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                            ShowToast.showShort(PlanWeeklyDetail.this,"请检查网络");
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonArray2 = jsonObject.getJSONArray("cells");
                                jsonArray_title = jsonObject.getJSONArray("title");
                                arr_one = new String[jsonArray2.length()];
                                //设置第一列列名
                                Log.d("aaaaa", mitem1.getText().toString());
                                arr_titles = new String[jsonArray_title.length()];
                                LinearLayout head_ll = (LinearLayout) findViewById(R.id.head);
                                mListView = (HVListView) findViewById(android.R.id.list);
                                //设置列头
                                mListView.mListHead = (LinearLayout) findViewById(R.id.head);



                                mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                                mListView.setLayoutParams(new LinearLayout.LayoutParams(350 * arr_titles.length, ViewGroup.LayoutParams.MATCH_PARENT));
                                planDtailList = new ArrayList<Map<String, String>>();

                                //填充列名
                                for (int i = 1; i < jsonArray_title.length(); i++) {
                                    TextView my_text = new TextView(PlanWeeklyDetail.this);
                                    my_text.setLayoutParams(new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.MATCH_PARENT));
                                    my_text.setText(jsonArray_title.getString(i));
                                    my_text.setTextSize(20);
                                    if (i % 2 == 0) {
                                        my_text.setBackgroundColor(Color.BLUE);
                                        my_text.setTextColor(Color.WHITE);
                                    }
                                    my_text.setGravity(Gravity.CENTER);
                                    head_ll.addView(my_text);
                                }


//                                填充内容
                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    arr_one[j] = jsonArray2.optJSONObject(j).getString(jsonArray_title.getString(0));
                                    map = new HashMap<String, String>();

                                    for (int k = 0; k < jsonArray_title.length(); k++) {
                                        map.put(jsonArray_title.getString(k), jsonArray2.optJSONObject(j).getString(jsonArray_title.getString(k)));
                                    }
                                    planDtailList.add(map);
                                }
//                                adapter = new PlanDetailAdapter(planDtailList, PlanWeeklyDetail.this, jsonArray_title.length(), width, height, jsonArray_title);
                                //设置数据
//                                mListView.setAdapter(adapter);
                                mListView.setAdapter(new DataAdapter(planDtailList));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    });


//    private class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return planDtailList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return planDtailList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            MyViewHoder holder = null;
//            if (convertView == null) {
//                holder = new MyViewHoder();
//                convertView = holder.toAddView();
//                convertView.setTag(holder);
//            } else {
//                holder = (MyViewHoder) convertView.getTag();
//            }
//            for (int i = 0; i < jsonArray_title.length(); i++) {
//                try {
//                    ((TextView) holder.layout.getChildAt(i)).setText(planDtailList.get(position).get(jsonArray_title.getString(i)));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            //校正（处理同时上下和左右滚动出现错位情况）
//            View child = ((ViewGroup) convertView).getChildAt(1);
//            int head = mListView.getHeadScrollX();
//            if (child.getScrollX() != head) {
//                child.scrollTo(mListView.getHeadScrollX(), 0);
//            }
//
//            return convertView;
//        }
//
//
//        private class MyViewHoder {
//            LinearLayout layout = new LinearLayout(context);
//
//            public LinearLayout toAddView() {
//                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layout.setLayoutParams(lp);
//                for (int i = 0; i < jsonArray_title.length(); i++) {
//                    TextView view = new TextView(context);
//                    view.setTextSize(16);
//                    view.setGravity(Gravity.CENTER);
//                    view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//                    layout.addView(view);
//                }
//                return layout;
//            }
//
//        }
//
//    }


    private class DataAdapter extends BaseAdapter {
        private LinearLayout head_ll;
        private ArrayList<Map<String, String>> planDtailList;

        public DataAdapter(ArrayList<Map<String, String>> planDtailList) {
            this.planDtailList = planDtailList;
        }

        @Override
        public int getCount() {
            return planDtailList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item, null);
            }

//            for (int j = 0; j < jsonArray2.length(); j++) {
//                ((TextView) convertView.findViewById(R.id.item1)).setText(arr_one[j]);
//            }


            head_ll = (LinearLayout) convertView.findViewById(R.id.head);

            Map<String, String> mContent = planDtailList.get(position);
            for (int i = 0; i < arr_titles.length; i++) {
//                tv_arr[i].setText("数据" + position + "行" + (i + 2) + "列");
                TextView my_text = new TextView(PlanWeeklyDetail.this);
                my_text.setLayoutParams(new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.MATCH_PARENT));
                try {
                    my_text.setText(mContent.get(jsonArray_title.getString(i + 1)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                my_text.setTextSize(20);
                my_text.setGravity(Gravity.CENTER);
                head_ll.addView(my_text);
            }

            //校正（处理同时上下和左右滚动出现错位情况）
            View child = ((ViewGroup) convertView).getChildAt(1);
            int head = mListView.getHeadScrollX();
            if (child.getScrollX() != head) {
                child.scrollTo(mListView.getHeadScrollX(), 0);
            }
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return planDtailList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    protected void setOnClick() {
        plan_weekly_detail_back.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plan_weekly_detail_back:
                finish();
                break;
        }
    }
}
