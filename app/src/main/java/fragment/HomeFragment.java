package fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.GaoJing;
import com.example.administrator.dimine_projectd.MainActivity;
import com.example.administrator.dimine_projectd.MyApprovalActivity;
import com.example.administrator.dimine_projectd.MyNotification;
import com.example.administrator.dimine_projectd.NewHomeFragment;
import com.example.administrator.dimine_projectd.R;
import com.example.administrator.dimine_projectd.ViewPagerIndicator;
import com.example.administrator.dimine_projectd.YuJing;
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
import java.util.Random;

import adapter.HomeViewpagerAdapter;
import myinterface.MyRedPoint;
import utils.MyHttpUtils;
import utils.ShowToast;

public class HomeFragment extends Fragment implements View.OnClickListener {
    public static final MainActivity MAIN_AACTIVITY = new MainActivity();
    private static final String TAG = "HomeFragment";
    private View view;
    private LinearLayout home_dbsy;
    private LinearLayout home_yj;
    private LinearLayout home_gj;
    private LinearLayout home_tzgg;


    private Intent intent;
    public MyRedPoint mr;
    private int myYear;
    private int myMonth;
    private int myDay;

    private int[] show_num = new int[4];

    private ArrayList<Fragment> mTabContents = new ArrayList<Fragment>();
    private HomeViewpagerAdapter mAdapter;
    private ViewPager mViewPager;
    private JSONArray title; //标题
    private List<String> mDatas;
    private ViewPagerIndicator mIndicator;

    private ImageView home_img_dbsy;
    private ImageView home_img_yjgj;
    private ImageView home_img_pllb;
    private ImageView home_img_tzgg;

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;
    private LinearLayout home_message; //有数据时
    private LinearLayout home_have_nomessage; //没有数据时
    private String sign = "Day"; //标志

    private ImageButton fragment_home_date_before; //前一天
    private ImageButton fragment_home_date_next;  //后一天
    private TextView fragment_home_date;        //日期
    private SimpleDateFormat formatter;
    private NewHomeFragment fragment;
    private boolean first = true;
    //屏幕宽高
    private int width;
    private int height;


    public HomeFragment() {
        // Required empty public constr
    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    handler.sendEmptyMessageDelayed(1, 10000);
                    break;
            }
        }
    };


    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_home, null);
            initView();
            setOnclick();
            //设置Tab上的标题
            initDatas();
            mAdapter.notifyDataSetChanged();
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


    private void initDatas() {
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
        json2obj(fragment_home_date.getText().toString());
    }


    protected void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;


        home_dbsy = (LinearLayout) view.findViewById(R.id.home_dbsy);
        home_yj = (LinearLayout) view.findViewById(R.id.home_yj);
        home_tzgg = (LinearLayout) view.findViewById(R.id.home_tzgg);
        home_gj = (LinearLayout) view.findViewById(R.id.home_gj);

        //设置宽度为屏幕的四分之一
        home_dbsy.setLayoutParams(new LinearLayout.LayoutParams(width/4, ViewGroup.LayoutParams.MATCH_PARENT));
        home_yj.setLayoutParams(new LinearLayout.LayoutParams(width/4, ViewGroup.LayoutParams.MATCH_PARENT));
        home_tzgg.setLayoutParams(new LinearLayout.LayoutParams(width/4, ViewGroup.LayoutParams.MATCH_PARENT));
        home_gj.setLayoutParams(new LinearLayout.LayoutParams(width/4, ViewGroup.LayoutParams.MATCH_PARENT));
        

        mViewPager = (ViewPager) view.findViewById(R.id.id_vp);
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.id_indicator);

        home_img_dbsy = (ImageView) view.findViewById(R.id.home_img_dbsy);
        home_img_yjgj = (ImageView) view.findViewById(R.id.home_img_yjgj);
        home_img_pllb = (ImageView) view.findViewById(R.id.home_img_pllb);
        home_img_tzgg = (ImageView) view.findViewById(R.id.home_img_tzgg);

        home_message = (LinearLayout) view.findViewById(R.id.home_message);
        home_have_nomessage = (LinearLayout) view.findViewById(R.id.home_have_nomessage);

        fragment_home_date_before = (ImageButton) view.findViewById(R.id.fragment_home_date_before);
        fragment_home_date_next = (ImageButton) view.findViewById(R.id.fragment_home_date_next);
        fragment_home_date = (TextView) view.findViewById(R.id.fragment_home_date);

        fragment_home_date_before.setOnTouchListener(new View.OnTouchListener() {
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

        fragment_home_date_next.setOnTouchListener(new View.OnTouchListener() {
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
        fragment_home_date.setText(formatter.format(new Date()));


        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        url = MyHttpUtils.ApiDayReport();

        for (int i = 0; i < show_num.length; i++) {
            int news_num = getRandomNum(5);
            show_num[i] = news_num;
        }


        //显示小圆点
//        BadgeView badgeView_dbsy = new BadgeView(getActivity());
//        badgeView_dbsy.setTargetView(home_img_dbsy);
//        badgeView_dbsy.setBadgeCount(show_num[0]);
//        if (ShowRedPoint(show_num[0]) == true) {
//            badgeView_dbsy.setVisibility(View.VISIBLE);
//        } else {
//            badgeView_dbsy.setVisibility(View.GONE);
//        }
//
//        BadgeView badgeView_yjgj = new BadgeView(getActivity());
//        badgeView_yjgj.setTargetView(home_img_yjgj);
//        badgeView_yjgj.setBadgeCount(show_num[1]);
//
//        if (ShowRedPoint(show_num[1]) == true) {
//            badgeView_yjgj.setVisibility(View.VISIBLE);
//        } else {
//            badgeView_yjgj.setVisibility(View.GONE);
//        }
//
//        BadgeView badgeView_tzgg = new BadgeView(getActivity());
//        badgeView_tzgg.setTargetView(home_img_tzgg);
//        badgeView_tzgg.setBadgeCount(show_num[2]);
//
//        if (ShowRedPoint(show_num[2]) == true) {
//            badgeView_tzgg.setVisibility(View.VISIBLE);
//        } else {
//            badgeView_tzgg.setVisibility(View.GONE);
//        }
//
//        BadgeView badgeView_pllb = new BadgeView(getActivity());
//        badgeView_pllb.setTargetView(home_img_pllb);
//        badgeView_pllb.setBadgeCount(show_num[3]);
//
//        if (ShowRedPoint(show_num[3]) == true) {
//            badgeView_pllb.setVisibility(View.VISIBLE);
//        } else {
//            badgeView_pllb.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void json2obj(String date) {
        mAdapter.notifyDataSetChanged();
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
                        ShowToast.showShort(getActivity(), R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject result = new JSONObject(response);//转换为JSONObject
                            title = result.getJSONArray("title");
                            JSONObject jsonObject = result.getJSONObject("cells");//获取JSONArray
                            if (title.length() == 0 || jsonObject.length() == 0) {

                                home_message.setVisibility(View.GONE);
                                home_have_nomessage.setVisibility(View.VISIBLE);
                            } else {
                                home_message.setVisibility(View.VISIBLE);
                                home_have_nomessage.setVisibility(View.GONE);
                                //设置标题
                                mDatas = new ArrayList<>();
                                for (int j = 0; j < title.length(); j++) {
                                    mDatas.add(title.getString(j));
                                }

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
                                        map.put(procname, list);//生成ui的变量  procname作为键   中间变量集合作为值
                                        map2.put(procid, list2); //ui左侧变量的id作为键   tagetid集合作为值
                                    }
                                    fragment = NewHomeFragment.newInstance(procnames, map, map2, targetname_list, targetid_list, procid_list, techid_list, title_name, sign, fragment_home_date.getText().toString());
                                    fragments.add(fragment);
                                    mIndicator.setTabItemTitles(mDatas);
                                    mIndicator.highLightTextView(0);
                                }
                                mTabContents = fragments;
                            }
                            mAdapter.refresh(mTabContents);
                            mIndicator.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
    }


    private boolean ShowRedPoint(int num) {
        if (num == 0) {
            return false;
        }
        return true;
    }


    protected void setOnclick() {
        home_dbsy.setOnClickListener(this);
        home_yj.setOnClickListener(this);
        home_gj.setOnClickListener(this);
        home_tzgg.setOnClickListener(this);
//        home_pllb.setOnClickListener(this);

        fragment_home_date_before.setOnClickListener(this);
        fragment_home_date_next.setOnClickListener(this);
        fragment_home_date.setOnClickListener(this);
    }


    Calendar calendar = Calendar.getInstance();
    //点击次数
    private long num = 0;

    //计算时间
    private String mtime(long num) {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mdate = fragment_home_date.getText().toString();
        long millionSeconds = 0;
        try {
            millionSeconds = formatter.parse(mdate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date curDate = new Date(millionSeconds + (num * 24 * 3600 * 1000));//获取当前时间
        String str = formatter.format(curDate);

//        Log.e(TAG, millionSeconds + "......." + num);
        return str;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_dbsy:
                intent = new Intent(getActivity(), MyApprovalActivity.class);
                startActivity(intent);
                break;
            case R.id.home_yj:
                intent = new Intent(getActivity(), YuJing.class);
                startActivity(intent);
                break;
            case R.id.home_gj:
                intent = new Intent(getActivity(), GaoJing.class);
                startActivity(intent);
                break;
            case R.id.home_tzgg:
                intent = new Intent(getActivity(), MyNotification.class);
                startActivity(intent);
                break;

            //上一天
            case R.id.fragment_home_date_before:
                try {
                    num--;
                    fragment_home_date.setText(mtime(num));
                    num = 0;
                    mAdapter.notifyDataSetChanged();
                    json2obj(fragment_home_date.getText().toString());
                    Date date = formatter.parse(fragment_home_date.getText().toString());
                    calendar.setTime(date);
                    myYear = calendar.get(Calendar.YEAR);
                    myMonth = calendar.get(Calendar.MONTH);
                    myDay = calendar.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            //下一天
            case R.id.fragment_home_date_next:
                try {
                    num++;
                    fragment_home_date.setText(mtime(num));
//                  Log.e(TAG, fragment_home_date.getText().toString());
                    num = 0;
                    mAdapter.notifyDataSetChanged();
                    json2obj(fragment_home_date.getText().toString());

                    Date date = formatter.parse(fragment_home_date.getText().toString());
                    calendar.setTime(date);
                    myYear = calendar.get(Calendar.YEAR);
                    myMonth = calendar.get(Calendar.MONTH);
                    myDay = calendar.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            //选择天数
            case R.id.fragment_home_date:
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
                                        fragment_home_date.setText(select_date);
//                                        mTabContents.clear();
                                        mAdapter.notifyDataSetChanged();
                                        json2obj(fragment_home_date.getText().toString());
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
                                        fragment_home_date.setText(select_date);
//                                        mTabContents.clear();
                                        mAdapter.notifyDataSetChanged();
                                        json2obj(fragment_home_date.getText().toString());
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
