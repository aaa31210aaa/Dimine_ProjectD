package com.example.administrator.dimine_projectd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import transact_dbsy.Business;
import transact_dbsy.HisApproval;
import utils.BaseActivity;

public class TransactDbsy extends BaseActivity {
    private static final String TAG = "TransactDbsy";
    private ImageView transact_dbsy_back;
//    private TabLayout transact_dbsy_tablayout;
//    private ViewPager transact_dbsy_viewpager;
    private String[] mTitles_zh = new String[]{"审批历史", "业务信息"};
    private String[] mTitles_en = new String[]{"History Approval", "Business Information"};
    private String language;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SmartTabLayout transact_dbsy_viewpagertab;
    private ViewPager transact_dbsy_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transact_dbsy);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        transact_dbsy_back = (ImageView) findViewById(R.id.transact_dbsy_back);
//        transact_dbsy_tablayout = (TabLayout) findViewById(R.id.transact_dbsy_tablayout);
//        transact_dbsy_viewpager = (ViewPager) findViewById(R.id.transact_dbsy_viewpager);

        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        language = sp.getString("mylanguage", null);
        editor.commit();

        //创建两个fragment
//        transact_dbsy_viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                if (position == 0) {
//                    return new HisApproval();
//                }
//                return new Business();
//            }
//
//            @Override
//            public int getCount() {
//                if (language.equals("zh")) {
//                    return mTitles_zh.length;
//                } else {
//                    return mTitles_en.length;
//                }
//
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                if (language.equals("zh")) {
//                    return mTitles_zh[position];
//                } else {
//                    return mTitles_en[position];
//                }
//
//            }
//        });
//        transact_dbsy_tablayout.setupWithViewPager(transact_dbsy_viewpager);


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.plan_tab_week, HisApproval.class)
                .add(R.string.plan_tab_month, Business.class)
                .create());

        transact_dbsy_viewpager = (ViewPager)findViewById(R.id.transact_dbsy_viewpager);
        transact_dbsy_viewpager.setAdapter(adapter);

        transact_dbsy_viewpagertab = (SmartTabLayout)findViewById(R.id.transact_dbsy_viewpagertab);
        transact_dbsy_viewpagertab.setViewPager(transact_dbsy_viewpager);


    }

    @Override
    protected void setOnClick() {
        transact_dbsy_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transact_dbsy_back:
                finish();
                break;
        }
    }
}
