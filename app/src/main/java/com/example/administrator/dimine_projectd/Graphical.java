package com.example.administrator.dimine_projectd;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
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

import utils.BaseActivity;
import utils.DialogUtil;
import utils.MyHttpUtils;
import utils.MyYAxisValueFormatter;
import utils.ShowToast;

public class Graphical extends BaseActivity implements GestureDetector.OnGestureListener, OnChartValueSelectedListener {
    private static final String TAG = "Graphical";
    private ImageView graphical_back;
    private LineChart mlineChartView;   //折线图
    protected BarChart mBarChart;   //柱形图
    private TextView graphical_title; //标题
    private TextView graphical_procname;
    private Spinner graphical_sp;
    private ArrayAdapter<String> adapter;

    private Intent intent;

    private String procid;
    private String techid;
    private String procnames;
    private String titlename;
    private ArrayList<String> str_lit;   //listview中间属性的集合
    private ArrayList<String> str2_list; //targetid集合
    private String first_targetid; //第一次进入的targetid

    private String select_sign; //选择的标志
    private String m_date; //选择后的日期
    private String week_date; //周报传过来的日期

//    private Typeface mTf;

    //    private static int count = 30;
    private int maxCount = 50;
    //
    private static final int[] mColor = {
            Color.rgb(255, 0, 0), Color.rgb(255, 128, 0), Color.rgb(0, 255, 0),
            Color.rgb(0, 255, 255), Color.rgb(0, 0, 255)
    };

    private static int[] zxt_color = new int[]{};

    //    private static final int[] mColor = new int[7];
    private float val; //实际折线图与柱形图数据


    private float arr[] = new float[]{};
    private float arr2[] = new float[]{};

    private int dq_year;//本年
    private int dq_month;//本月
    private int dq_day;//当天
    private int day_of_month; //当月总共的天数
    private int first = 1; //第一天

    private ArrayList<String> x_index; //日报横坐标
    private String mStr;
    private String unit;//单位

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;

    private int x_num = 30; //横坐标总长度

    private ArrayList<String> dwcres_list; //实际值
    private ArrayList<String> mpres_list; //预计值

    private Dialog dialog;
    private boolean a = false;

    private JSONArray select_arr;
    private ArrayList<String> select_dwcres; //选择属性后请求的实际数据
    private ArrayList<String> select_mpres; //选择属性后请求的预计数据

    private JSONArray mjsonArray;
    private LinearLayout linechart_ll;
    private LinearLayout barchart_ll;
    private ImageView graphical_switching;
    private boolean myBit = true;
    private JSONArray select_jsonArray;
    private String language;

    private LineDataSet mLineDataSet;
    private LineDataSet mLineDataSet2;
//    private CombinedChart combined_chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_graphical);
        initView();
        setOnClick();
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    // 设置折线图的数据
                    LineData mLineData = makeLineData(x_num, select_mpres, select_dwcres);
                    setChartStyle(mlineChartView, mLineData);
                    //设置X轴最后的位置的显示
//                    mBarChart.moveViewToX(x_index.size());
                    //设置柱形图的数据
                    setData(x_num, select_mpres, select_dwcres);
                    //设置X轴最后的位置的显示
                    mBarChart.moveViewToX(x_index.size() * 2 + 5);
                    dialog.dismiss();
                    break;
                case 1:
                    // 设置折线图的数据
                    LineData mLineData2 = makeLineData(mjsonArray.length(), select_mpres, select_dwcres);
                    setChartStyle(mlineChartView, mLineData2);

                    //设置柱形图的数据
                    setData(mjsonArray.length(), select_mpres, select_dwcres);
                    dialog.dismiss();
                    break;

                //获取日报信息
                case 2:
                    mOkhttpDay();
                    break;
                //获取周报信息
                case 3:
                    mOkhttpWeek();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void initView() {
        graphical_title = (TextView) findViewById(R.id.graphical_title);
        graphical_back = (ImageView) findViewById(R.id.graphical_back);
        mlineChartView = (LineChart) findViewById(R.id.spread_line_chart);
        mBarChart = (BarChart) findViewById(R.id.spread_bar_chart);
        graphical_procname = (TextView) findViewById(R.id.graphical_procname);
        graphical_sp = (Spinner) findViewById(R.id.graphical_sp);
//      combined_chart = (CombinedChart) findViewById(R.id.combined_chart);
        linechart_ll = (LinearLayout) findViewById(R.id.linechart_ll);
        barchart_ll = (LinearLayout) findViewById(R.id.barchart_ll);
        graphical_switching = (ImageView) findViewById(R.id.graphical_switching);


        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage", null);

        /**
         * 接收传过来的值
         */
        intent = this.getIntent();
        titlename = intent.getStringExtra("titlename");
        procnames = intent.getStringExtra("procnames");
        procid = intent.getStringExtra("procid");
        techid = intent.getStringExtra("techid");
        week_date = intent.getStringExtra("m_date");


        str_lit = intent.getStringArrayListExtra("map");
        str2_list = intent.getStringArrayListExtra("map2");

        select_sign = intent.getStringExtra("select_sign");

        m_date = intent.getStringExtra("date");
        if (str_lit != null) {
            first_targetid = str2_list.get(0);
        }

        //截取单位
//        int start = mStr.indexOf("(")+ 1;
//        int end = mStr.indexOf(")");
//        unit = mStr.substring(start, end);
//        ShowToast.showShort(this, unit);


        //给Spinner赋值
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_lit);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphical_sp.setAdapter(adapter);

        //初始化图表赋值
        if (select_sign.equals("Day")) {
            graphical_title.setText(titlename);
            graphical_procname.setText(procnames);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(2);
                }
            }).start();

        } else if (select_sign.equals("Week")) {
            graphical_title.setText(titlename);
            graphical_procname.setText(procnames);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(3);
                }
            }).start();

        }


        graphical_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (select_sign.equals("Day")) {
                    selectDayReport(position);
                } else if (select_sign.equals("Week")) {
                    selectWeekReport(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        combined_chart.setDescription("");
//        combined_chart.setBackgroundColor(Color.WHITE);
//        combined_chart.setDrawGridBackground(false);
//        combined_chart.setDrawBarShadow(false);
//
//        // draw bars behind lines
//        combined_chart.setDrawOrder(new CombinedChart.DrawOrder[]{
//                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
//        });
//
//        YAxis rightAxis = combined_chart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//
//        YAxis leftAxis = combined_chart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//
//        XAxis xAxis = combined_chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//
//        //设置视图横坐标
//        CombinedData data = new CombinedData(mMonths);
//
//        data.setData(generateLineData());
//        data.setData(generateBarData());
//
//        combined_chart.setData(data);
//        combined_chart.invalidate();
    }

    //选择哪种属性的日报
    public void selectDayReport(int index) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("techid", techid)
                .addParams("targetid", str2_list.get(index))
                .addParams("pdate", m_date)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            select_jsonArray = jsonObject.getJSONArray("data");
                            select_dwcres = new ArrayList<String>();
                            select_mpres = new ArrayList<String>();
                            for (int i = 0; i < select_jsonArray.length(); i++) {
                                select_dwcres.add(select_jsonArray.optJSONObject(i).getString("dwcres"));
                                select_mpres.add(select_jsonArray.optJSONObject(i).getString("mpres"));
                            }
                            if (a) {
                                dialog = DialogUtil.createLoadingDialog(Graphical.this, "请稍后");
                                handler.sendEmptyMessageDelayed(0, 1500);
                            }
                            a = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //选择哪种属性的周报
    public void selectWeekReport(int index) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("techid", techid)
                .addParams("targetid", str2_list.get(index))
                .addParams("yearmonth", week_date)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(Graphical.this, "请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            select_jsonArray = jsonObject.getJSONArray("data");
                            select_dwcres = new ArrayList<String>();
                            select_mpres = new ArrayList<String>();
                            for (int i = 0; i < select_jsonArray.length(); i++) {
                                select_dwcres.add(select_jsonArray.optJSONObject(i).getString("dwcres"));
                                select_mpres.add(select_jsonArray.optJSONObject(i).getString("mpres"));
                                x_index.add(select_jsonArray.optJSONObject(i).getString("weekcount"));
                            }

                            if (a) {
                                dialog = DialogUtil.createLoadingDialog(Graphical.this, "请稍后");
                                handler.sendEmptyMessageDelayed(1, 1500);
                            }
                            a = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


//    private float getRandom(float range, float startsfrom) {
//        return (float) (Math.random() * range) + startsfrom;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.combined, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.actionToggleLineValues: {
//                for (IDataSet set : mChart.getData().getDataSets()) {
//                    if (set instanceof LineDataSet)
//                        set.setDrawValues(!set.isDrawValuesEnabled());
//                }
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleBarValues: {
//                for (IDataSet set : mChart.getData().getDataSets()) {
//                    if (set instanceof BarDataSet)
//                        set.setDrawValues(!set.isDrawValuesEnabled());
//                }
//                mChart.invalidate();
//                break;
//            }
//        }
//        return true;
//    }


    /*
       * 将时间转换为时间戳
       */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
       * 将时间戳转换为时间
       */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    //请求日报数据
    private void mOkhttpDay() {
        url = MyHttpUtils.ApiDataByRpt();
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("techid", techid)
                .addParams("targetid", first_targetid)
                .addParams("pdate", m_date)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            x_index = new ArrayList<String>();
                            dwcres_list = new ArrayList<String>();
                            mpres_list = new ArrayList<String>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                dwcres_list.add(jsonArray.optJSONObject(j).getString("dwcres"));
                                mpres_list.add(jsonArray.optJSONObject(j).getString("mpres"));
                                x_index.add(jsonArray.optJSONObject(j).getString("pdate"));
                            }


                            Calendar calendar = Calendar.getInstance();
                            dq_year = calendar.get(Calendar.YEAR);
                            dq_month = calendar.get(Calendar.MONTH) + 1;

//                            day_of_month = getDays(dq_year, dq_month);
//                            zxt_color = new int[x_index.size()];
                            arr = new float[x_index.size()];
                            arr2 = new float[x_index.size()];

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        graphical_back = (ImageView) findViewById(R.id.graphical_back);
                        //折现
                        mlineChartView = (LineChart) findViewById(R.id.spread_line_chart);

                        MyMarkerView mv = new MyMarkerView(Graphical.this, R.layout.custom_marker_view);
                        mlineChartView.setMarkerView(mv);


                        // 设置折线图的数据
                        LineData mLineData = makeLineData(x_index.size(), mpres_list, dwcres_list);
                        setChartStyle(mlineChartView, mLineData);

                        //设置柱形图的数据
                        setData(x_index.size(), mpres_list, dwcres_list);
                        //设置X轴最后的位置的显示
                        mBarChart.moveViewToX(x_index.size() * 2 + 5);
                    }
                });
    }


    //周报请求
    private void mOkhttpWeek() {
        url = MyHttpUtils.ApiMonthWeekByRpt();
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .addParams("techid", techid)
                .addParams("targetid", first_targetid)
                .addParams("yearmonth", week_date)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(Graphical.this, "请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            mjsonArray = jsonObject.getJSONArray("data");

                            dwcres_list = new ArrayList<String>();
                            mpres_list = new ArrayList<String>();
                            x_index = new ArrayList<String>();
                            for (int i = 0; i < mjsonArray.length(); i++) {
                                dwcres_list.add(mjsonArray.optJSONObject(i).getString("dwcres"));
                                mpres_list.add(mjsonArray.optJSONObject(i).getString("mpres"));
                                x_index.add(mjsonArray.optJSONObject(i).getString("weekcount"));
                            }

//                            day_of_month = getDays(dq_year, dq_month);
//                            zxt_color = new int[mjsonArray.length()];
                            arr = new float[mjsonArray.length()];
                            arr2 = new float[mjsonArray.length()];

                            graphical_back = (ImageView) findViewById(R.id.graphical_back);
                            //折现
                            mlineChartView = (LineChart) findViewById(R.id.spread_line_chart);

                            MyMarkerView mv = new MyMarkerView(Graphical.this, R.layout.custom_marker_view);
                            mlineChartView.setMarkerView(mv);

                            // 设置折线图的数据
                            LineData mLineData = makeLineData(mjsonArray.length(), mpres_list, dwcres_list);
                            setChartStyle(mlineChartView, mLineData);
                            //设置柱形图的数据
                            setData(mjsonArray.length(), mpres_list, dwcres_list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    //判断闰年
    private boolean isLeap(int year) {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))
            return true;
        else
            return false;
    }

    //返回当月天数
    private int getDays(int year, int month) {
        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }


    /**
     * 柱形图数据
     *
     * @param count
     */
    private void setData(int count, ArrayList<String> mpres, ArrayList<String> dwcres) {
        //柱形
        mBarChart.setOnChartValueSelectedListener(Graphical.this);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setDescription("");
        mBarChart.setDescriptionTextSize(15);
        mBarChart.animateX(2000);
//        mBarChart.setHighlightPerDragEnabled(false);
        //点击某个柱形高亮
        mBarChart.setHighlightPerTapEnabled(false);

        //计划柱形图数据
        for (int j = 0; j < count; j++) {
            arr2[j] = Float.parseFloat(mpres.get(j));
        }

        //实际柱形图数据
        for (int i = 0; i < count; i++) {
            arr[i] = Float.parseFloat(dwcres.get(i));
        }

//        mBarChart.setDescription("");
        //设置最多有多少项
//      mBarChart.setMaxVisibleValueCount(30);
        //设置缩放是否在XY轴同时进行
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);
//        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        //标注字体位置设置
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setSpaceBetweenLabels(1);

        YAxisValueFormatter custom = new MyYAxisValueFormatter();
        //设置左侧数值
        YAxis leftAxis = mBarChart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        //设置左侧显示几个数值
        leftAxis.setLabelCount(6, false);
        //将左侧数值设置到边框外
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setTextColor(Color.BLACK);


        //设置右侧数值
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(6, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f);
        rightAxis.setTextColor(Color.BLACK);
        //设置底部标注
        Legend mLegend = mBarChart.getLegend();
        mLegend.setTextColor(Color.BLACK);
        mLegend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
//        mLegend.setForm(Legend.LegendForm.SQUARE);
//        mLegend.setFormSize(35.0f);//字体大小

//        mLegend.setFormSize(9f);
//        mLegend.setTextSize(11f);
//        mLegend.setXEntrySpace(4f);
//        mLegend.setExtra(mColor, new String[]{"超高告警",
//                "超高预警", "正常", "超低预警", "超低告警"});
//        mLegend.setCustom(mColor, new String[]{"超高告警",
//                "超高预警", "正常", "超低预警", "超低告警"});


        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(x_index.get(i));
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

        //赋值柱形图颜色取值范围
        for (int i = 0; i < count; i++) {
//            if (arr[i] > 90) {
//                zxt_color[i] = Color.rgb(255, 0, 0);
//            } else if (arr[i] <= 90 && arr[i] > 70) {
//                zxt_color[i] = Color.rgb(255, 128, 0);
//            } else if (arr[i] <= 70 && arr[i] > 50) {
//                zxt_color[i] = Color.rgb(0, 255, 0);
//            } else if (arr[i] <= 50 && arr[i] > 30) {
//                zxt_color[i] = Color.rgb(0, 255, 255);
//            } else {
//                zxt_color[i] = Color.rgb(0, 0, 255);
//            }
            yVals1.add(new BarEntry(arr[i], i));
            yVals2.add(new BarEntry(arr2[i], i));
        }
        BarDataSet set1;
        BarDataSet set2;
        if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
//            set1.setYVals(yVals1);
//            mBarChart.getData().setXVals(xVals);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();


            if (language.equals("en")){
                set1 = new BarDataSet(yVals1, "Actual");
            }else{
                set1 = new BarDataSet(yVals1, "实际");
            }

            set1.setBarSpacePercent(15f);
            //设置多种颜色;
//            set1.setColors(zxt_color);
            set1.setColor(Color.rgb(86, 171, 228));
            if (language.equals("en")) {
                set2 = new BarDataSet(yVals2, "Estimate");
            } else {
                set2 = new BarDataSet(yVals2, "预计");
            }
            set2.setBarSpacePercent(15f);
            set2.setColor(Color.GREEN);
            set1.setBarSpacePercent(20f);
            set2.setBarSpacePercent(20f);

            set1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    value = (float) (Math.round(value * 100)) / 100;
                    String s = value + "";
                    return s;
                }
            });

            set2.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    value = (float) (Math.round(value * 100)) / 100;
                    String s = value + "";
                    return s;
                }
            });

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            mBarChart.setData(data);
        } else {
            if (language.equals("en")){
                set1 = new BarDataSet(yVals1, "Actual");
            }else{
                set1 = new BarDataSet(yVals1, "实际");
            }
            set1.setBarSpacePercent(15f);
            //设置多种颜色;
//            set1.setColors(zxt_color);
            set1.setColor(Color.rgb(86, 171, 228));
            set1.setValueTextColor(Color.BLACK);
            if (language.equals("en")) {
                set2 = new BarDataSet(yVals2, "Estimate");
            } else {
                set2 = new BarDataSet(yVals2, "预计");
            }

            set2.setBarSpacePercent(15f);
            set2.setColor(Color.GREEN);
            set2.setValueTextColor(Color.BLACK);
            set1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    value = (float) (Math.round(value * 100)) / 100;
                    String s = value + "";
                    return s;
                }
            });

            set2.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    value = (float) (Math.round(value * 100)) / 100;
                    String s = value + "";
                    return s;
                }
            });


            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            BarData data = new BarData(xVals, dataSets);
//            data.setGroupSpace(6);
            data.setValueTextSize(10f);
            mBarChart.setData(data);
        }

//        mBarChart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mlineChartView.onTouchEvent(event);
//                return false;
//            }
//        });


        mBarChart.setVisibleXRangeMaximum((float) 19);
//        mBarChart.setVisibleXRangeMaximum(14);

        mBarChart.invalidate();
    }


    /**
     * 设置折线图的样式
     *
     * @param mLineChart
     * @param lineData
     */
    private void setChartStyle(final LineChart mLineChart, LineData lineData) {
        YAxisValueFormatter custom = new MyYAxisValueFormatter();
        YAxis leftAxis = mLineChart.getAxisLeft();
        //设置左侧显示几个数值
        leftAxis.setLabelCount(6, false);
        //将左侧数值设置到边框外
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setTextColor(Color.BLACK);


        //设置右侧数值
        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(6, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f);
        rightAxis.setTextColor(Color.BLACK);

        //是否在折线图上添加边框
        mLineChart.setDrawBorders(false);

        //数据描述
        mLineChart.setDescription("");
//        mLineChart.setDescriptionColor(Color.WHITE);
//        mLineChart.setDescriptionTextSize(15);
        //如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart.setNoDataTextDescription("无数据内容。");


        XAxis xAxis = mLineChart.getXAxis();
        //是否绘制纵向的线
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
//        xAxis.setSpaceBetweenLabels(2);
        xAxis.resetLabelsToSkip();   //将自动计算坐标相隔多少


        // 是否绘制背景颜色。
        // 如果mLineChart.setDrawGridBackground(false)，
        // 那么mLineChart.setGridBackgroundColor(Color.CYAN)将失效;
        mLineChart.setDrawGridBackground(false);
        // 触摸
        mLineChart.setTouchEnabled(true);
        // 拖拽
        mLineChart.setDragEnabled(true);
        // 缩放
        mLineChart.setScaleEnabled(true);

        // 设置x,y轴的数据
        mLineChart.setData(lineData);

        //将X轴上的字放到底部
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // 设置比例图标示，就是那个一组y的value的
        Legend mLegend = mLineChart.getLegend();
//        mLegend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        mLegend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        mLegend.setForm(Legend.LegendForm.LINE);//样式
        mLegend.setFormSize(35.0f);//字体大小
        mLegend.setTextColor(Color.BLACK);
        // 沿x轴动画，时间。
        mLineChart.animateX(2000);
        mLineChart.getData().notifyDataChanged();;
        mLineChart.notifyDataSetChanged();
//        mLineChart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mBarChart.onTouchEvent(event);
//                return false;
//            }
//        });

        //设置X轴显示位置
        mlineChartView.moveViewToX(x_index.size());
        //设置视窗显示最多多少个点  需要在设置数据之后设置
        mLineChart.setVisibleXRangeMaximum(6);
        mLineChart.invalidate();
    }


    /**
     * 定义折线图数据点的数量
     *
     * @param count
     * @return
     */
    private LineData makeLineData(int count, ArrayList<String> mpres_list, ArrayList<String> dwcres_list) {
        ArrayList<String> line_x = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            //x轴显示的数据
            line_x.add(x_index.get(i));
        }

        //预计线y轴的数据
        ArrayList<Entry> line_y = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float yuji_val = Float.parseFloat(mpres_list.get(i));
            Entry entry = new Entry(yuji_val, i);
            line_y.add(entry);
        }
        //实际线y轴数据
        ArrayList<Entry> line_y2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float shiji_val = Float.parseFloat(dwcres_list.get(i));
            Entry entry = new Entry(shiji_val, i);
            line_y2.add(entry);
        }

        //y轴数据集
        if (language.equals("en")){
            mLineDataSet = new LineDataSet(line_y, "Estimate");
        }else{
            mLineDataSet = new LineDataSet(line_y, "预计");
        }
        if (language.equals("en")){
            mLineDataSet2 = new LineDataSet(line_y2, "Actual");
        }else{
            mLineDataSet2 = new LineDataSet(line_y2, "实际");
        }

        // 用y轴的集合来设置参数
        // 线宽
        //是否显示点上的数值
        mLineDataSet.setDrawValues(true);
        mLineDataSet2.setDrawValues(true);
        mLineDataSet.setLineWidth(1.0f);
        mLineDataSet2.setLineWidth(1.0f);
        // 显示的圆形大小
        mLineDataSet.setCircleRadius(3.0f);
        mLineDataSet2.setCircleRadius(3.0f);
        // 折线的颜色
        mLineDataSet.setColor(Color.GREEN);
        mLineDataSet2.setColor(Color.rgb(86, 171, 228));
        // 圆球的颜色
        mLineDataSet.setCircleColor(Color.GREEN);
        mLineDataSet2.setCircleColor(Color.rgb(86, 171, 228));
        mLineDataSet.setDrawHighlightIndicators(false);
        mLineDataSet2.setDrawHighlightIndicators(false);
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
        mLineDataSet.setDrawHighlightIndicators(false);
        mLineDataSet2.setDrawHighlightIndicators(false);
        // 按击后，十字交叉线的颜色
//        mLineDataSet.setHighLightColor(Color.CYAN);
//        mLineDataSet2.setHighLightColor(Color.RED);
        // 设置这项上显示的数据点的字体大小。
        mLineDataSet.setValueTextSize(10.0f);
        mLineDataSet2.setValueTextSize(10.0f);
        mLineDataSet.setValueTextColor(Color.BLACK);
        mLineDataSet2.setValueTextColor(Color.BLACK);
        // mLineDataSet.setDrawCircleHole(true);

        // 改变折线样式，用曲线。
         mLineDataSet.setDrawCubic(true);
        mLineDataSet2.setDrawCubic(true);
        // 曲线的平滑度，值越大越平滑。
        // mLineDataSet.setCubicIntensity(0.2f);

        // 填充曲线下方的区域，红色，半透明。
//         mLineDataSet.setDrawFilled(true);
//         mLineDataSet.setFillAlpha(80);
//         mLineDataSet.setFillColor(Color.RED);

        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
        mLineDataSet.setCircleColorHole(Color.GREEN);
        mLineDataSet2.setCircleColorHole(Color.rgb(86, 171, 228));
        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        mLineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                value = (float) (Math.round(value * 100)) / 100;
                String s = value + "";
                return s;
            }
        });

        mLineDataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                value = (float) (Math.round(value * 100)) / 100;
                String s = value + "";
                return s;
            }
        });

        ArrayList<ILineDataSet> mLineDataSets = new ArrayList<ILineDataSet>();
        mLineDataSets.add(mLineDataSet);
        mLineDataSets.add(mLineDataSet2);
        LineData mLineData = new LineData(line_x, mLineDataSets);

        return mLineData;

    }


    @Override
    protected void setOnClick() {
        graphical_back.setOnClickListener(this);
        graphical_switching.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.graphical_back:
                finish();
                break;
            case R.id.graphical_switching:
                if (myBit) {
                    linechart_ll.setVisibility(View.GONE);
                    barchart_ll.setVisibility(View.VISIBLE);
                    myBit = false;
                } else {
                    linechart_ll.setVisibility(View.VISIBLE);
                    barchart_ll.setVisibility(View.GONE);
                    myBit = true;
                }
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return false;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
