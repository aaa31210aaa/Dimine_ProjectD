package fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.administrator.dimine_projectd.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import report_fragment.DayReportFragment;
import report_fragment.WeekReportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFormFragment extends Fragment {
    private static final String TAG = "ReportFormFragment";
    private View view;
    private GridView gridview1;
    private Intent intent;
    private String json1 = "{'code':'0','gridview1_tv':[{'gtv':'测试1'},{'gtv':'测试2'},{'gtv':'测试3'},{'gtv':'测试4'},{'gtv':'测试5'},{'gtv':'测试6'}],'gridview1_iv':[{'giv':'R.drawable.u23'},{'giv':'R.drawable.u23'},{'giv':R.drawable.u23'}]}";
    private String json2 = "{'code':'0','gridview2_tv':[{'gtv':'测试1'}],'gridview2_iv':[{'giv':R.drawable.u21}]}";

    private String json3 = "{\"success\":true,\"errormessage\":\"\",\"title\":[\"生产统计\",\"设备运行统计\",\"影响因素统计\",\"选厂统计\"],\"cells\":{\"生产统计\":[{\"procid\":\"0de05a34c26d4fff93fc38ea31eef222\",\"procname\":\"掘进\"},{\"procid\":\"ebb6f1cc667a42479df612319cdff9df\",\"procname\":\"破碎\"}],\"设备运行统计\":[],\"影响因素统计\":[],\"选厂统计\":[]}}";

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String language;
    private String url;
    private String detail_url;

    private String[] mTitles;


//    private TabLayout report_tablayout;
//    private ViewPager report_viewpager;

    private SmartTabLayout report_viewpagertab;
    private ViewPager report_viewpager;

    public ReportFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_report_form, null);
            initView();
            setOnclick();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    protected void initView() {
//        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
//        editor = sp.edit();
//        user_token = sp.getString("user_token", null);

//        url = MyHttpUtils.ApiDayReport();
//        detail_url = MyHttpUtils.ApiPlanMonthlyDetail();

//        mOkhttp();
//        report_tablayout = (TabLayout) view.findViewById(R.id.report_tablayout);
//        report_viewpager = (ViewPager) view.findViewById(R.id.report_viewpager);
//
//        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
//        editor = sp.edit();
//        language = sp.getString("mylanguage", null);
//
//        if (language.equals("en")){
//            mTitles = new String[]{"Daily report", "Weekly report"};
//        }else{
//            mTitles = new String[]{"日 报", "周 报"};
//        }
//
//        report_viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                if (position == 0) {
//                    return new DayReportFragment();
//                }
//                return new WeekReportFragment();
//            }
//
//            @Override
//            public int getCount() {
//                return mTitles.length;
//            }
//
//            public CharSequence getPageTitle(int position) {
//                return mTitles[position];
//            }
//        });
//        report_tablayout.setupWithViewPager(report_viewpager);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add(R.string.report_day, DayReportFragment.class)
                .add(R.string.report_week, WeekReportFragment.class)
                .create());

        report_viewpager = (ViewPager) view.findViewById(R.id.report_viewpager);
        report_viewpager.setAdapter(adapter);

        report_viewpagertab = (SmartTabLayout) view.findViewById(R.id.report_viewpagertab);
        report_viewpagertab.setViewPager(report_viewpager);

    }


    protected void setOnclick() {

    }
}
