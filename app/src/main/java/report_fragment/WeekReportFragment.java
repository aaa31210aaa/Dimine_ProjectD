package report_fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.administrator.dimine_projectd.R;
import com.example.administrator.dimine_projectd.ViewPagerIndicator;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.HomeViewpagerAdapter;
import utils.MyDate;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekReportFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WeekReportFragment";

    private Intent intent;
    private View view;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;

    private ArrayList<Fragment> mTabContents = new ArrayList<Fragment>();
    private HomeViewpagerAdapter mAdapter;
    private ViewPager mViewPager;
    private JSONArray title; //标题
    private List<String> mDatas;
    private ViewPagerIndicator mIndicator;

    private LinearLayout week_report_message; //有数据时
    private RelativeLayout week_report_have_nomessage; //没有数据时

    private Button week_report_date; //年月选择
    private Spinner week_report_sp; //周选择
    private Calendar localCalendar;
    private SimpleDateFormat formatter;
    private NewReportFragment fragment;


    private String sign = "Week"; //标志

    public WeekReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_week_report, null);
            initView();
            setOnclick();
            //设置Tab上的标题
            initDatas();
            mViewPager.setAdapter(mAdapter);
            //设置关联的ViewPager
            mIndicator.setViewPager(mViewPager, 0);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    protected void initView() {
        week_report_message = (LinearLayout) view.findViewById(R.id.week_report_message);
        week_report_have_nomessage = (RelativeLayout) view.findViewById(R.id.week_report_have_nomessage);
        week_report_date = (Button) view.findViewById(R.id.week_report_date);
        week_report_sp = (Spinner) view.findViewById(R.id.week_report_sp);

        localCalendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("yyyy-MM");


        url = MyHttpUtils.ApiWeekrpt();
        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);

        mViewPager = (ViewPager) view.findViewById(R.id.week_report_id_vp);
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.week_report_id_indicator);
        mSpinner();
    }


    private void initDatas() {
        int sj_month = localCalendar.get(Calendar.MONTH) + 1;
        week_report_date.setText(localCalendar.get(Calendar.YEAR) + "-" + sj_month);
        mAdapter = new HomeViewpagerAdapter(getFragmentManager(), mTabContents) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
        json2obj(week_report_date.getText().toString(), "1");
    }

    //下拉框
    private void mSpinner() {
        week_report_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTabContents.clear();
                mAdapter.notifyDataSetChanged();
                json2obj(week_report_date.getText().toString(), (position + 1) + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void json2obj(String yyvalue, String weekcounts) {
        final String json = "{\"success\":true,\"errormessage\":\"\",\"title\":[\"钻探\",\"采切\",\"开拓\",\"运输\"],\"cells\":{\"钻探\":[{\"procid\":\"2003\",\"procname\":\"支护\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"锚杆支护\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2003\",\"procname\":\"掘进\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"进尺\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2003\",\"procname\":\"运搬(出渣)\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"进尺\",\"dwcres\":\"3.0000\"}]},{\"procid\":\"2003\",\"procname\":\"放矿\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"放矿量(m³)\",\"dwcres\":\"50.0000\"}]},{\"procid\":\"2003\",\"procname\":\"运输\",\"techid\":\"11\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1014\",\"targetname\":\"方量(t)\",\"dwcres\":\"150.0000\"}]},{\"procid\":\"2004\",\"procname\":\"安装\",\"techid\":\"11\",\"indexno\":\"1\",\"data\":[{\"targetid\":\"1023\",\"targetname\":\"井筒装备\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2005\",\"procname\":\"穿孔\",\"techid\":\"11\",\"indexno\":\"2\",\"data\":[{\"targetid\":\"1029\",\"targetname\":\"穿孔量(m)\",\"dwcres\":\"200.0000\"}]}],\"采切\":[{\"procid\":\"2001\",\"procname\":\"掘进\",\"techid\":\"22\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1002\",\"targetname\":\"进尺(m)\",\"dwcres\":\"2.0000\"}]},{\"procid\":\"2002\",\"procname\":\"运搬(出碴)\",\"techid\":\"22\",\"indexno\":\"1\",\"data\":[{\"targetid\":\"1002\",\"targetname\":\"进尺(m)\",\"dwcres\":\"3.0000\"}]}],\"开拓\":[{\"procid\":\"2001\",\"procname\":\"掘进\",\"techid\":\"21\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1002\",\"targetname\":\"进尺(m)\",\"dwcres\":\"0.0000\"},{\"targetid\":\"1003\",\"targetname\":\"方量(m3)\",\"dwcres\":\"0.0000\"}]}],\"运输\":[{\"procid\":\"2008\",\"procname\":\"放矿\",\"techid\":\"24\",\"indexno\":\"0\",\"data\":[{\"targetid\":\"1038\",\"targetname\":\"放矿量(m3)\",\"dwcres\":\"50.0000\"}]},{\"procid\":\"2009\",\"procname\":\"运输\",\"techid\":\"24\",\"indexno\":\"1\",\"data\":[{\"targetid\":\"1042\",\"targetname\":\"方量(t)\",\"dwcres\":\"100.0000\"}]}]}}";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("yyvalue", yyvalue)
                .addParams("weekcounts", weekcounts)
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
                            JSONObject cells = result.getJSONObject("cells");//获取JSONArray
                            int count = title.length();

                            if (title.length() == 0 || cells.length() == 0) {
                                week_report_message.setVisibility(View.GONE);
                                week_report_have_nomessage.setVisibility(View.VISIBLE);
                            } else {
                                week_report_message.setVisibility(View.VISIBLE);
                                week_report_have_nomessage.setVisibility(View.GONE);

                                //设置标题
                                mDatas = new ArrayList<>();
                                for (int j = 0; j < title.length(); j++) {
                                    mDatas.add(title.getString(j));
                                }
                                mIndicator.setTabItemTitles(mDatas);


                                for (int i = 0; i < count; i++) {
                                    //fragment的个数
                                    ArrayList<String> targetid_list = new ArrayList<String>();
                                    ArrayList<String> targetname_list = new ArrayList<String>();
                                    ArrayList<String> procid_list = new ArrayList<String>();
                                    ArrayList<String> techid_list = new ArrayList<String>();
                                    ArrayList<String> title_list = new ArrayList<String>();

                                    String title_name = title.getString(i);

                                    for (int p = 0; p < count; p++) {
                                        title_list.add(title.getString(i));
                                    }
                                    JSONArray cell = cells.getJSONArray(title.getString(i));
                                    int cellcount = cell.length();
                                    Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

                                    Map<String, ArrayList<String>> map2 = new HashMap<String, ArrayList<String>>();

                                    ArrayList<String> procnames = new ArrayList<>();//ui左侧变量存放左侧变量
                                    for (int j = 0; j < cellcount; j++) {  //单个fragment中item的个数
                                        ArrayList<String> list = new ArrayList<String>();
                                        ArrayList<String> list2 = new ArrayList<String>();

                                        JSONObject item = cell.getJSONObject(j);
                                        procid_list.add(item.getString("procid"));
                                        techid_list.add(item.getString("techid"));

                                        String procname = item.getString("procname");//ui左侧变量
                                        String procid = item.getString("procid"); //ui左侧变量的id
                                        procnames.add(procname);
                                        JSONArray data = item.getJSONArray("data");//ui中间变量
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
                                    fragment = NewReportFragment.newInstance(procnames, map, map2, targetname_list, targetid_list, procid_list, techid_list, title_name, sign, week_report_date.getText().toString());
                                    mTabContents.add(fragment);
                                    mIndicator.highLightTextView(0);
                                }
                                mAdapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }


//    private String cutString(String str) {
//        String a = str.substring(1, 2);
//        return a;
//    }

    protected void setOnclick() {
        week_report_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击年月选择
            case R.id.week_report_date:
                new YearPickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        localCalendar.set(Calendar.YEAR, year);
                        localCalendar.set(Calendar.MONTH, monthOfYear);
                        week_report_date.setText(MyDate.clanderTodatetime(localCalendar, "yyyy-MM"));
                        mTabContents.clear();
                        json2obj(week_report_date.getText().toString(), (week_report_sp.getSelectedItemPosition() + 1) + "");
                    }
                }, localCalendar.get(Calendar.YEAR), localCalendar.get(Calendar.MONTH), localCalendar.get(Calendar.DATE)).show();
                break;
        }
    }

    //重写日期选择器
    public class YearPickerDialog extends DatePickerDialog {
        public YearPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            //        this.setTitle(year + "年" + (monthOfYear + 1) + "月");
            ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }

        public YearPickerDialog(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
            super(context, theme, listener, year, monthOfYear, dayOfMonth);

//        this.setTitle(year + "年" + (monthOfYear + 1) + "月");
            ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            this.setTitle(year + "年" + month + "月");
        }
    }

}
