package com.example.administrator.dimine_projectd;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fragment.HomeFragment;
import fragment.MineFragment;
import fragment.PlanFragment;
import fragment.ProductionFragment;
import fragment.ReportFormFragment;
import myinterface.MyRedPoint;
import utils.BaseActivity;
import utils.ShowToast;

public class MainActivity extends BaseActivity implements View.OnClickListener, MyRedPoint {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private FragmentTabHost mTabHost;

    /**
     * 布局填充器
     */
//    private LayoutInflater mLayoutInflater;
    /**
     * Fragment数组界面
     */
//    private Class mFragmentArray[] = {HomeFragment.class, PlanFragment.class,
//            ProductionFragment.class, ReportFormFragment.class, MineFragment.class};
    /**
     * 存放图片数组
     */
//    private int mImageArray[] = {R.drawable.tab_home_btn,
//            R.drawable.tab_plan_btn, R.drawable.tab_production_btn,
//            R.drawable.tab_reportform_btn, R.drawable.tab_mine_btn};
    /**
     * 选项卡文字
     */
//    private String mTextArray[] = {"首页", "计划", "生产", "报表", "我的"};
    /**
     * 是否退出的的标识
     */
    private static boolean isExit = false;

    /**
     * 主界面的viewpager
     **/
    private ViewPagerCompat mPager;
    /**
     * 所有fragment的集合
     **/
    private List<Fragment> fragments = new ArrayList<Fragment>();
    /**
     * 适配器
     **/
    private MyPagerAdapter adapter;

    /**
     * 当前所选中的栏目 默认首页(0-首页 1-计划 2-生产 3-报表 4-我)
     **/
    private int current = 0;

    private LinearLayout ll_home;
    private LinearLayout ll_plan;
    private LinearLayout ll_production;
    private LinearLayout ll_report;
    private LinearLayout ll_mine;

    private ImageView main_redpoint;

    private TextView tv1;

//    private ImageView img_home;
//    private ImageView img_plan;
//    private ImageView img_production;
//    private ImageView img_report;
//    private ImageView img_mine;

    private int[] main_num = new int[4];

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setOnClick();
        setPager();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


        } else {
            // Nothing need to be done here
        }
    }

    @Override
    protected void initView() {
        tv1 = (TextView) findViewById(R.id.tv1);
//        mLayoutInflater = LayoutInflater.from(this);
//        // 找到TabHost
//        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//        // 得到fragment的个数
//        final int count = mFragmentArray.length;
//        for (int i = 0; i < count; i++) {
//            // 给每个Tab按钮设置图标、文字和内容
//            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
//                    .setIndicator(getTabItemView(i));
//            // 将Tab按钮添加进Tab选项卡中
//            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
//        }
        mPager = (ViewPagerCompat) findViewById(R.id.viewpager);
        //禁止viewpager滑动
        mPager.setViewTouchMode(true);
        mPager.setOffscreenPageLimit(4);

        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_plan = (LinearLayout) findViewById(R.id.ll_plan);
        ll_production = (LinearLayout) findViewById(R.id.ll_production);
        ll_report = (LinearLayout) findViewById(R.id.ll_report);
        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);

//        img_home = (ImageView) findViewById(R.id.img_home);
//        img_plan = (ImageView) findViewById(R.id.img_plan);
//        img_production = (ImageView) findViewById(R.id.img_production);
//        img_report = (ImageView) findViewById(R.id.img_report);
//        img_mine = (ImageView) findViewById(R.id.img_mine);


        ll_home.setSelected(true);
        verifyStoragePermissions(this);

        for (int i = 0; i < main_num.length; i++) {
            int news_num = getRandomNum(5);
            main_num[i] = news_num;
        }


//        BadgeView badgeView_img_home = new BadgeView(this);
//        badgeView_img_home.setTargetView(img_home);
//        badgeView_img_home.setBadgeCount(main_num[0]);
//
//
//        BadgeView badgeView_img_plan = new BadgeView(this);
//        badgeView_img_plan.setTargetView(img_home);
//        badgeView_img_plan.setBadgeCount(main_num[1]);
    }

    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
    }

    //添加权限
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * 设置viewpager的选项
     */
    private void setPager() {
        fragments.add(new HomeFragment());
        fragments.add(new PlanFragment());
        fragments.add(new ProductionFragment());
        fragments.add(new ReportFormFragment());
        fragments.add(new MineFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(0);
    }


    /**
     * 跳转某个fragment
     *
     * @param which
     */
    private void setCurrent(int which) {
        current = which;
        mPager.setCurrentItem(current, false);
    }

    @Override
    protected void setOnClick() {
        ll_home.setOnClickListener(this);
        ll_plan.setOnClickListener(this);
        ll_production.setOnClickListener(this);
        ll_report.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                if (current == 0) break;
                setCurrent(0);
                ll_home.setSelected(true);
                ll_plan.setSelected(false);
                ll_production.setSelected(false);
                ll_report.setSelected(false);
                ll_mine.setSelected(false);
                break;
            case R.id.ll_plan:
                if (current == 1) break;
                setCurrent(1);
                ll_home.setSelected(false);
                ll_plan.setSelected(true);
                ll_production.setSelected(false);
                ll_report.setSelected(false);
                ll_mine.setSelected(false);
                break;
            case R.id.ll_production:
                if (current == 2) break;
                setCurrent(2);
                ll_home.setSelected(false);
                ll_plan.setSelected(false);
                ll_production.setSelected(true);
                ll_report.setSelected(false);
                ll_mine.setSelected(false);
                break;
            case R.id.ll_report:
                if (current == 3) break;
                setCurrent(3);
                ll_home.setSelected(false);
                ll_plan.setSelected(false);
                ll_production.setSelected(false);
                ll_report.setSelected(true);
                ll_mine.setSelected(false);
                break;
            case R.id.ll_mine:
                if (current == 4) break;
                setCurrent(4);
                ll_home.setSelected(false);
                ll_plan.setSelected(false);
                ll_production.setSelected(false);
                ll_report.setSelected(false);
                ll_mine.setSelected(true);
                break;
        }
    }

    @Override
    public void settext(String str) {

    }

    @Override
    public void setVisible(String vb) {
        if (vb.equals("false")) {
            main_redpoint.setVisibility(View.GONE);
        }
    }


    /**
     * 主界面viewpager适配器
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
        }

    }

}
