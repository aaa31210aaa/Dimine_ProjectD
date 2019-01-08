package devicemanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.dimine_projectd.R;
import com.example.administrator.dimine_projectd.ViewPagerCompat;

import java.util.ArrayList;
import java.util.List;

import dm_fragment.DmDeviceFragment;
import dm_fragment.DmDevicePlan;
import dm_fragment.DmDeviceReport;
import utils.BaseActivity;

public class DeviceManagement extends BaseActivity {
    private static final String TAG = "DeviceManagement";
    private LinearLayout dm_ll_device;
    private LinearLayout dm_ll_plan;
    private LinearLayout dm_ll_report;
    private ViewPagerCompat dm_viewpager;

    /**
     * 所有fragment的集合
     **/
    private List<Fragment> fragments = new ArrayList<Fragment>();
    /**
     * 适配器
     **/
    private MyPagerAdapter adapter;
    /**
     * 当前所选中的栏目 默认首页(0-设备 1-计划 2-报表)
     **/
    private int current = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);
        initView();
        setOnClick();
        setPager();
    }

    @Override
    protected void initView() {
        dm_ll_device = (LinearLayout) findViewById(R.id.dm_ll_device);
        dm_ll_plan = (LinearLayout) findViewById(R.id.dm_ll_plan);
        dm_ll_report = (LinearLayout) findViewById(R.id.dm_ll_report);
        dm_viewpager = (ViewPagerCompat) findViewById(R.id.dm_viewpager);

        dm_viewpager.setViewTouchMode(true);
        dm_viewpager.setOffscreenPageLimit(1);
        dm_ll_device.setSelected(true);
    }

    @Override
    protected void setOnClick() {
        dm_ll_device.setOnClickListener(this);
        dm_ll_plan.setOnClickListener(this);
        dm_ll_report.setOnClickListener(this);
    }

    /**
     * 跳转某个fragment
     *
     * @param which
     */
    private void setCurrent(int which) {
        current = which;
        dm_viewpager.setCurrentItem(current, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dm_ll_device:
                if (current == 0) break;
                setCurrent(0);
                dm_ll_device.setSelected(true);
                dm_ll_plan.setSelected(false);
                dm_ll_report.setSelected(false);
                break;
            case R.id.dm_ll_plan:
                if (current == 1) break;
                setCurrent(1);
                dm_ll_device.setSelected(false);
                dm_ll_plan.setSelected(true);
                dm_ll_report.setSelected(false);
                break;
            case R.id.dm_ll_report:
                if (current == 2) break;
                setCurrent(2);
                dm_ll_device.setSelected(false);
                dm_ll_plan.setSelected(false);
                dm_ll_report.setSelected(true);
                break;
        }
    }

    /**
     * 设置viewpager的选项
     */
    private void setPager() {
        fragments.add(new DmDeviceFragment());
        fragments.add(new DmDevicePlan());
        fragments.add(new DmDeviceReport());
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        dm_viewpager.setAdapter(adapter);
        dm_viewpager.setCurrentItem(0);
    }


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
