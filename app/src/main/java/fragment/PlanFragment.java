package fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.dimine_projectd.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import plan_fragment.MonthlyPlanFragment;
import plan_fragment.WeeklyPlanFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {
    private static final String TAG = "PlanFragment";
    private View view;
//    private TabLayout plan_tablayout;
//    private ViewPager plan_viewpager;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String language;
    private String[] mTitles;

    private SmartTabLayout plan_viewpagertab;
    private ViewPager plan_viewpager;


    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_plan, null);
            initView();

        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

//    protected void initView() {
//        plan_tablayout = (TabLayout) view.findViewById(R.id.plan_tablayout);
//        plan_viewpager = (ViewPager) view.findViewById(R.id.plan_viewpager);
//
//        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
//        editor = sp.edit();
//        language = sp.getString("mylanguage", null);
//
//        if (language.equals("en")){
//            mTitles =new String[]{"Weekly plan", "Monthly plan"};
//        }else{
//            mTitles =new String[]{"周计划", "月计划"};
//        }
//
//
//        plan_viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                if (position == 0) {
//                    return new WeeklyPlanFragment();
//                }
//                return new MonthlyPlanFragment();
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
//        plan_tablayout.setupWithViewPager(plan_viewpager);
//    }


    private void initView(){
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add(R.string.plan_tab_week, WeeklyPlanFragment.class)
                .add(R.string.plan_tab_month, MonthlyPlanFragment.class)
                .create());

        plan_viewpager = (ViewPager) view.findViewById(R.id.plan_viewpager);
        plan_viewpager.setAdapter(adapter);

        plan_viewpagertab = (SmartTabLayout) view.findViewById(R.id.plan_viewpagertab);
        plan_viewpagertab.setViewPager(plan_viewpager);
    }
}
