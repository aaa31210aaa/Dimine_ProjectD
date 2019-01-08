package production_fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.AddProChooseBc;
import com.example.administrator.dimine_projectd.AddProChooseBz;
import com.example.administrator.dimine_projectd.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.HistoryListViewAdapter;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ThreadUtil;

public class ProductionHistory extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = "ProductionHistory";
    private ImageView his_back;
    private ListView production_his_listview;
    private TextView production_his_bz; //班组
    private ImageView his_delete_img;
    private TextView production_his_bc; //班次
    private ImageView his_delete_img2;
    private TextView production_his_start_date; //起始日期
    private TextView production_his_end_date; //结束日期

    private String teamname;//班组名
    private String teamgroupid = ""; //班组id
    private String procid = "";//工序id


    private Intent intent;
    private static final int code_bz = 1;
    private static final int code_bc = 2;
    private Bundle bundle;
    private String his_bc_id = "";
    private Calendar calendar;
    private int start_time;
    private int end_time;

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;

    private SimpleDateFormat simpleDateFormat;

    private TextView tv; //动态添加的textview

    private int width;//屏幕的宽
    private int height; //屏幕的高
    private HistoryListViewAdapter adapter;
    private Map<String, String> map;
    private Dialog dialog;

    private boolean startfirst = true;
    private boolean endfirst = true;
    private int startYear;
    private int startMonth;
    private int startDay;

    private int endYear;
    private int endMonth;
    private int endDay;


    private boolean isfirst = true;

    private boolean a = true;
    private boolean b = true;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String language;

    //修改的内容
    //刷新
    private BGARefreshLayout his_refreshLayout;
    private ArrayList<String> tv_title;
    private ArrayList<Map<String, String>> tv_content;
    private Map<String, String> contents;
    private String titile;
    private String content;

    private int mPage = 1; //页数
    private int mRows = 10; //一页多少条
    private int total; //总共多少条数据
    private float num_page; //页数
    private int my_numPage;

    private LinearLayout production_his_have_nomessage;
    private static final int LOADING_REFRESH = 3000;
    private static final int LOADING_MORE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_history);
        initView();
        setOnClick();
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void dispatchMessage(Message msg) {
//            super.dispatchMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    production_his_listview.setAdapter(null);
//                    his_list.clear();
//                    his_biaoti.removeAllViews();
//                    mOkhttp();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


    @Override
    protected void initView() {
        his_back = (ImageView) findViewById(R.id.his_back);
        production_his_listview = (ListView) findViewById(R.id.production_his_listview);
        production_his_bz = (TextView) findViewById(R.id.production_his_bz);
        his_delete_img = (ImageView) findViewById(R.id.his_delete_img);
        production_his_bc = (TextView) findViewById(R.id.production_his_bc);
        his_delete_img2 = (ImageView) findViewById(R.id.his_delete_img2);
        production_his_start_date = (TextView) findViewById(R.id.production_his_start_date);
        production_his_end_date = (TextView) findViewById(R.id.production_his_end_date);
        production_his_have_nomessage = (LinearLayout) findViewById(R.id.production_his_have_nomessage);

        //刷新的设置
        his_refreshLayout = (BGARefreshLayout) findViewById(R.id.his_refreshLayout);
        // 为BGARefreshLayout设置代理
        his_refreshLayout.setDelegate(this);
        // 设置刷新风格
        BGAMoocStyleRefreshViewHolder refreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        refreshViewHolder.setOriginalImage(R.drawable.refresh_ks);
        refreshViewHolder.setUltimateColor(R.color.refresh_img_ks);
        // 设置下拉刷新和上拉加载更多的风格
        his_refreshLayout.setRefreshViewHolder(refreshViewHolder);

        //获取首选项保存的内容
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage", null);
        calendar = Calendar.getInstance();
        intent = getIntent();

//        teamname = intent.getStringExtra("his_teamname");
//
//        if (intent.getStringExtra("his_teamgroupid") == null) {
//            teamgroupid = "";
//        } else {
//            teamgroupid = intent.getStringExtra("his_teamgroupid");
//        }
        procid = intent.getStringExtra("his_procid");

        url = MyHttpUtils.ApiDisplayTargetByGx();


        //设置初始日期
        int sj_month = calendar.get(Calendar.MONTH) + 1;
        production_his_start_date.setText(calendar.get(Calendar.YEAR) + "-" + sj_month + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        production_his_end_date.setText(calendar.get(Calendar.YEAR) + "-" + sj_month + "-" + calendar.get(Calendar.DAY_OF_MONTH));

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start_date = simpleDateFormat.parse(production_his_start_date.getText().toString());
            Date end_date = simpleDateFormat.parse(production_his_end_date.getText().toString());
            start_time = (int) start_date.getTime();
            end_time = (int) end_date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 获得屏幕宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        mOkhttp(mPage);
    }

    private void mOkhttp(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("teamgroupid", teamgroupid)
                .addParams("classes", his_bc_id)
                .addParams("page", page + "")
                .addParams("rows", mRows + "")
                .addParams("startdate", production_his_start_date.getText().toString())
                .addParams("enddate", production_his_end_date.getText().toString())
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(ProductionHistory.this, "查询失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response + "-----------" + language);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("title");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("content");
                            total = Integer.parseInt(jsonObject.getString("total"));

                            if (jsonArray.length() == 0 || jsonArray2.length() == 0) {
                                production_his_have_nomessage.setVisibility(View.VISIBLE);
                                his_refreshLayout.setVisibility(View.GONE);
                            } else {
                                production_his_have_nomessage.setVisibility(View.GONE);
                                his_refreshLayout.setVisibility(View.VISIBLE);


                                tv_title = new ArrayList<String>();
                                tv_content = new ArrayList<Map<String, String>>();

                                //算出总共多少页
                                if (total != 0) {
                                    num_page = (total + 10 - 1) / 10;
                                }

                                //赋值title
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    titile = jsonArray.getString(i);
                                    tv_title.add(titile);
                                }

                                //填充内容
                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    contents = new HashMap<String, String>();
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        content = jsonArray2.optJSONObject(j).getString(jsonArray.getString(k));
                                        contents.put(jsonArray.getString(k), content);
                                    }
                                    tv_content.add(contents);
                                }
                                adapter = new HistoryListViewAdapter(ProductionHistory.this, tv_title, tv_content);
                                production_his_listview.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //加载更多内容
    private void LoadMore(int page) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("teamgroupid", teamgroupid)
                .addParams("classes", his_bc_id)
                .addParams("page", page + "")
                .addParams("rows", mRows + "")
                .addParams("startdate", production_his_start_date.getText().toString())
                .addParams("enddate", production_his_end_date.getText().toString())
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        his_refreshLayout.endLoadingMore();
                        ShowToast.showShort(ProductionHistory.this, R.string.query_failed);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("title");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("content");
                            tv_title = new ArrayList<String>();
                            tv_content = new ArrayList<Map<String, String>>();

                            //填充内容
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                contents = new HashMap<String, String>();
                                for (int k = 0; k < jsonArray.length(); k++) {
                                    content = jsonArray2.optJSONObject(j).getString(jsonArray.getString(k));
                                    contents.put(jsonArray.getString(k), content);
                                }
                                tv_content.add(contents);
                            }
                            adapter.addMoreData(tv_title, tv_content);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }




    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {
        ThreadUtil.runInUIThread(new Runnable() {
            @Override
            public void run() {
                tv_title.clear();
                tv_content.clear();
                mOkhttp(1);
                refreshLayout.endRefreshing();
                adapter.addNewData(tv_title, tv_content);
                mPage = 1;
                ShowToast.showShort(ProductionHistory.this, R.string.refresh);
            }
        }, LOADING_REFRESH);

    }

    //加载更多
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        mPage++;
        if (mPage > num_page) {
            refreshLayout.endLoadingMore();
            ShowToast.showShort(ProductionHistory.this, R.string.no_more_message);
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


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismiss();
                    b = true;
                    ShowToast.showShort(ProductionHistory.this, "加载更多数据");
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code_bz) {
            if (data != null) {
                bundle = data.getExtras();
                String his_bz_back = bundle.getString("bz_back");
                teamgroupid = bundle.getString("bz_selectid");
                production_his_bz.setText(his_bz_back);
                production_his_listview.setAdapter(null);
                mPage = 1;
                mOkhttp(mPage);
            }
        }

        if (requestCode == code_bc) {
            if (data != null) {
                bundle = data.getExtras();
                String his_bc_back = bundle.getString("back");
                his_bc_id = bundle.getString("selectid");
                production_his_bc.setText(his_bc_back);
                production_his_listview.setAdapter(null);
                mPage = 1;
                mOkhttp(mPage);
            }
        }
    }

    @Override
    protected void setOnClick() {
        his_back.setOnClickListener(this);
        production_his_bz.setOnClickListener(this);
        production_his_bc.setOnClickListener(this);
        production_his_start_date.setOnClickListener(this);
        production_his_end_date.setOnClickListener(this);
        his_delete_img.setOnClickListener(this);
        his_delete_img2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.his_back:
                finish();
                break;
            case R.id.production_his_bz:
                intent = new Intent(this, AddProChooseBz.class);
                intent.putExtra("myprocid", procid);
                startActivityForResult(intent, code_bz);
                break;
            case R.id.his_delete_img:
                production_his_bz.setText("");
                teamgroupid = "";
                production_his_listview.setAdapter(null);
//                his_list.clear();
                mPage = 1;
                mOkhttp(mPage);
                break;

            case R.id.production_his_bc:
                intent = new Intent(this, AddProChooseBc.class);
                startActivityForResult(intent, code_bc);
                break;
            case R.id.his_delete_img2:
                production_his_bc.setText("");
                his_bc_id = "";
                production_his_listview.setAdapter(null);
//                his_list.clear();
                mPage = 1;
                mOkhttp(mPage);
                break;

            case R.id.production_his_start_date:
                if (startfirst) {
                    DatePickerDialog start_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            production_his_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            try {
                                Date start_date = simpleDateFormat.parse(production_his_start_date.getText().toString());
                                start_time = (int) start_date.getTime();
                                mPage = 1;
                                mOkhttp(mPage);
                                startYear = year;
                                startMonth = monthOfYear;
                                startDay = dayOfMonth;
                                startfirst = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    start_dialog.show();
                } else {
                    DatePickerDialog start_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            production_his_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            try {
                                Date start_date = simpleDateFormat.parse(production_his_start_date.getText().toString());
                                start_time = (int) start_date.getTime();
                                production_his_listview.setAdapter(null);
                                mPage = 1;
                                mOkhttp(mPage);
                                startYear = year;
                                startMonth = monthOfYear;
                                startDay = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, startYear, startMonth, startDay);
                    start_dialog.show();
                }
                break;


            case R.id.production_his_end_date:
                if (endfirst) {
                    DatePickerDialog end_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            production_his_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date end_date = simpleDateFormat.parse(production_his_end_date.getText().toString());
                                end_time = (int) end_date.getTime();
                                production_his_listview.setAdapter(null);
                                mPage = 1;
                                mOkhttp(mPage);
                                endYear = year;
                                endMonth = monthOfYear;
                                endDay = dayOfMonth;
                                endfirst = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    end_dialog.show();
                } else {
                    DatePickerDialog end_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            production_his_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                            try {
//                                Date end_date = simpleDateFormat.parse(production_his_end_date.getText().toString());
//                                end_time = (int) end_date.getTime();
                            production_his_listview.setAdapter(null);
                            mPage = 1;
                            mOkhttp(mPage);
                            endYear = year;
                            endMonth = monthOfYear;
                            endDay = dayOfMonth;
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }, endYear, endMonth, endDay);
                    end_dialog.show();
                }

                break;


        }
    }


}
