package report_fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.NewHomeFragment;
import com.example.administrator.dimine_projectd.R;
import com.example.administrator.dimine_projectd.ViewPagerIndicator;
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
import java.util.List;
import java.util.Map;

import adapter.DayReportViewpagerAdapter;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayReportFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DayReportFragment";

    private Intent intent;
    private View view;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;
    private String detail_url;

    private ArrayList<Fragment> mTabContents = new ArrayList<Fragment>();
    private DayReportViewpagerAdapter mAdapter;
    private ViewPager mViewPager;
    private JSONArray title; //标题
    private List<String> mDatas;
    private ViewPagerIndicator mIndicator;

    private LinearLayout day_report_message; //有数据时
    private LinearLayout report_have_nomessage; //没有数据时

    private ImageView fragment_day_report_date_before; //前一天
    private ImageView fragment_day_report_date_next; //后一天
    private TextView fragment_day_report_date; //日期
    private SimpleDateFormat formatter;
    private boolean first = true;
    private int myYear;
    private int myMonth;
    private int myDay;

    private String sign = "Day"; //标志

    private NewHomeFragment fragment;

    public DayReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_day_report, null);
            initView();
            setOnclick();
            //设置Tab上的标题
            initDatas();
            mViewPager.setAdapter(mAdapter);
            //设置关联的ViewPager
            mIndicator.setViewPager(mViewPager, 0);
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    protected void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.day_report_id_vp);
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.day_report_id_indicator);

        day_report_message = (LinearLayout) view.findViewById(R.id.day_report_message);
        report_have_nomessage = (LinearLayout) view.findViewById(R.id.report_have_nomessage);

        fragment_day_report_date_before = (ImageView) view.findViewById(R.id.fragment_day_report_date_before);
        fragment_day_report_date_next = (ImageView) view.findViewById(R.id.fragment_day_report_date_next);
        fragment_day_report_date = (TextView) view.findViewById(R.id.fragment_day_report_date);


        //按钮点击效果
        fragment_day_report_date_before.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改为按下时的背景图片
                    v.setBackgroundResource(R.drawable.reduce_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //改为抬起时的图片
                    v.setBackgroundResource(R.drawable.reduce);
                }
                return false;
            }
        });

        fragment_day_report_date_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改为按下时的背景图片
                    v.setBackgroundResource(R.drawable.add_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //改为抬起时的图片
                    v.setBackgroundResource(R.drawable.add);
                }
                return false;
            }
        });


        formatter = new SimpleDateFormat("yyyy-MM-dd");
        fragment_day_report_date.setText(formatter.format(new Date()));

        url = MyHttpUtils.ApiDayReport();
        detail_url = MyHttpUtils.ApiPlanMonthlyDetail();

        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
    }

    private void initDatas() {
        Myjson2obj(fragment_day_report_date.getText().toString());
        mAdapter = new DayReportViewpagerAdapter(getFragmentManager(), mTabContents) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
    }


    private void Myjson2obj(String date) {
        final String json = "{\"success\":true,\"errormessage\":\"\",\"title\":[\"钻探\",\"采切\",\"开拓\",\"运输\"],\"cells\":{\"钻探\":[{\"procid\":\"2003\",\"procname\":\"支护\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"锚杆支护\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2003\",\"procname\":\"掘进\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"进尺\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2003\",\"procname\":\"运搬(出渣)\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"进尺\",\"dwcres\":\"3.0000\"}]},{\"procid\":\"2003\",\"procname\":\"放矿\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"放矿量(m³)\",\"dwcres\":\"50.0000\"}]},{\"procid\":\"2003\",\"procname\":\"运输\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"方量(t)\",\"dwcres\":\"150.0000\"}]},{\"procid\":\"2004\",\"procname\":\"安装\",\"techid\":\"11\",\"indexno\":\"1\",\"data\":[{\"targetid\":\"1023\",\"targetname\":\"井筒装备\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2005\",\"procname\":\"穿孔\",\"techid\":\"11\",\"indexno\":\"2\",\"data\":[{\"targetid\":\"1029\",\"targetname\":\"穿孔量(m)\",\"dwcres\":\"200.0000\"}]}],\"采切\":[{\"procid\":\"2001\",\"procname\":\"掘进\",\"techid\":\"22\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1002\",\"targetname\":\"进尺(m)\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2002\",\"procname\":\"运搬(出碴)\",\"techid\":\"22\",\"indexno\":\"1\",\"data\":[{\"targetid\":\"1002\",\"targetname\":\"进尺(m)\",\"dwcres\":\"3.0000\"}]}],\"开拓\":[{\"procid\":\"2001\",\"procname\":\"掘进\",\"techid\":\"21\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1002\",\"targetname\":\"进尺(m)\",\"dwcres\":\"0.0000\"},{\"targetid\":\"1003\",\"targetname\":\"方量(m3)\",\"dwcres\":\"0.0000\"}]}],\"运输\":[{\"procid\":\"2008\",\"procname\":\"放矿\",\"techid\":\"24\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1038\",\"targetname\":\"放矿量(m3)\",\"dwcres\":\"50.0000\"}]},{\"procid\":\"2009\",\"procname\":\"运输\",\"techid\":\"24\",\"indexno\":\"1\",\"data\":[{\"targetid\":\"1042\",\"targetname\":\"方量(t)\",\"dwcres\":\"100.0000\"}]}]}}";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("pdate", date)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(getActivity(), "请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject result = new JSONObject(response);//转换为JSONObject
                            title = result.getJSONArray("title");
                            JSONObject jsonObject = result.getJSONObject("cells");//获取JSONArray
                            if (title.length() == 0 || jsonObject.length() == 0) {
                                day_report_message.setVisibility(View.GONE);
                                report_have_nomessage.setVisibility(View.VISIBLE);
                            } else {
                                day_report_message.setVisibility(View.VISIBLE);
                                report_have_nomessage.setVisibility(View.GONE);
                                //设置标题
                                mDatas = new ArrayList<>();
                                for (int j = 0; j < title.length(); j++) {
                                    mDatas.add(title.getString(j));
                                }
                                mIndicator.setTabItemTitles(mDatas);


                                ArrayList<Fragment> fragments = new ArrayList<Fragment>();
                                for (int i = 0; i < title.length(); i++) {
                                    //fragment的个数
                                    ArrayList<String> targetid_list = new ArrayList<String>();
                                    ArrayList<String> targetname_list = new ArrayList<String>();
                                    ArrayList<String> procid_list = new ArrayList<String>();
                                    ArrayList<String> techid_list = new ArrayList<String>();

                                    String title_name = title.getString(i);
                                    //cells里面的jsonArray
                                    JSONArray jsonArray = jsonObject.getJSONArray(title.getString(i));

                                    Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                                    Map<String, ArrayList<String>> map2 = new HashMap<String, ArrayList<String>>();
                                    ArrayList<String> procnames = new ArrayList<>();//ui左侧变量存放左侧变量
                                    for (int j = 0; j < jsonArray.length(); j++) {  //单个fragment中item的个数
                                        ArrayList<String> list = new ArrayList<String>();
                                        ArrayList<String> list2 = new ArrayList<String>();

//                                      JSONObject item = jsonArray.getJSONObject(j);
                                        procid_list.add(jsonArray.optJSONObject(j).getString("procid"));
                                        techid_list.add(jsonArray.optJSONObject(j).getString("techid"));

                                        String procname = jsonArray.optJSONObject(j).getString("procname");//ui左侧变量
                                        String procid = jsonArray.optJSONObject(j).getString("procid"); //ui左侧变量的id
                                        procnames.add(procname);
                                        JSONArray data = jsonArray.optJSONObject(j).getJSONArray("data");//ui中间变量
                                        int datacount = data.length();
                                        for (int m = 0; m < datacount; m++) {//单个fragment中单个item中间变量的个数
                                            JSONObject d = data.getJSONObject(m);
                                            list.add(d.getString("targetname") + ":" + d.getString("dwcres"));
                                            list2.add(d.getString("targetid"));
                                            targetid_list.add(d.getString("targetid"));
                                            targetname_list.add(d.getString("targetname"));
                                        }
                                        map.put(procname, list);//生成ui的变量   procname作为键   中间变量集合作为值
                                        map2.put(procid, list2); //ui左侧变量的id作为键   tagetid集合作为值
                                    }
                                    fragment = NewHomeFragment.newInstance(procnames, map, map2, targetname_list, targetid_list, procid_list, techid_list, title_name, sign, fragment_day_report_date.getText().toString());
                                    fragments.add(fragment);
                                    mIndicator.highLightTextView(0);
                                }
                                mTabContents = fragments;
                            }
                            mAdapter.DrRefresh(mTabContents);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
    }

    Calendar calendar = Calendar.getInstance();
    //点击次数
    private long num = 0;

    //计算时间
    private String mtime(long num) {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mdate = fragment_day_report_date.getText().toString();
        long millionSeconds = 0;
        try {
            millionSeconds = formatter.parse(mdate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date curDate = new Date(millionSeconds + (num * 24 * 3600 * 1000));//获取当前时间
        String str = formatter.format(curDate);

        Log.d(TAG, millionSeconds + "......." + num);
        return str;
    }

    protected void setOnclick() {
        fragment_day_report_date_before.setOnClickListener(this);
        fragment_day_report_date_next.setOnClickListener(this);
        fragment_day_report_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前一天
            case R.id.fragment_day_report_date_before:
                try {
                    num--;
                    fragment_day_report_date.setText(mtime(num));
                    num = 0;
//                    mTabContents.clear();
                    mAdapter.notifyDataSetChanged();
                    Myjson2obj(fragment_day_report_date.getText().toString());
                    Date date = formatter.parse(fragment_day_report_date.getText().toString());
                    calendar.setTime(date);
                    myYear = calendar.get(Calendar.YEAR);
                    myMonth = calendar.get(Calendar.MONTH);
                    myDay = calendar.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            //下一天
            case R.id.fragment_day_report_date_next:
                try {
                    num++;
                    fragment_day_report_date.setText(mtime(num));
//                Log.d(TAG, fragment_home_date.getText().toString());
                    num = 0;
//                    mTabContents.clear();
                    mAdapter.notifyDataSetChanged();
                    Myjson2obj(fragment_day_report_date.getText().toString());
                    Date date = formatter.parse(fragment_day_report_date.getText().toString());
                    calendar.setTime(date);
                    myYear = calendar.get(Calendar.YEAR);
                    myMonth = calendar.get(Calendar.MONTH);
                    myDay = calendar.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            //日期
            case R.id.fragment_day_report_date:
                //第一次进入
                if (first) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker arg0, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    try {
                                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = formatter.parse(select_time);
                                        String select_date = formatter.format(date);
                                        // TODO Auto-generated method stub
                                        fragment_day_report_date.setText(select_date);
//                                        mTabContents.clear();
                                        mAdapter.notifyDataSetChanged();
                                        Myjson2obj(fragment_day_report_date.getText().toString());
                                        myYear = year;
                                        myMonth = monthOfYear;
                                        myDay = dayOfMonth;
                                        first = false;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                } else {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker arg0, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    try {
                                        String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = formatter.parse(select_time);
                                        String select_date = formatter.format(date);
                                        // TODO Auto-generated method stub
                                        fragment_day_report_date.setText(select_date);
//                                        mTabContents.clear();
                                        mAdapter.notifyDataSetChanged();
                                        Myjson2obj(fragment_day_report_date.getText().toString());
                                        myYear = year;
                                        myMonth = monthOfYear;
                                        myDay = dayOfMonth;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, myYear, myMonth, myDay);
                    dialog.show();
                }

                break;
        }
    }
}
