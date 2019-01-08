package utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.administrator.dimine_projectd.R;

import org.xutils.x;

import java.util.Locale;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener{
    public Context context;

    @Override
    public Resources getResources() {
        // 保持字体不受系统字体大小影响
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 点击隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 透明状态栏
     */
    public void statusBar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        context = this;
//        PushAgent.getInstance(context).onAppStart();

        //初始化PreferenceUtil
        AppSharedPreferences.getInstance(this);
        //根据上次的语言设置，重新设置语言
        switchLanguage(AppSharedPreferences.getString("language", "zh"));
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();


    /**
     * 绑定点击事件
     */
    protected abstract void setOnClick();


    /**
     * <切换语言>
     *
     * @param language
     * @see [类、类#方法、类#成员]
     */
    protected void switchLanguage(String language) {
        // 设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en")) {
            config.locale = Locale.ENGLISH;
        } else {
            // 简体中文
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);

        // 保存设置语言的类型
        AppSharedPreferences.commitString("language", language);
    }

    /**
     * 刷新样式
     */
    protected void MyRefreshStyle(BGARefreshLayout refreshLayout) {
        refreshLayout.setDelegate((BGARefreshLayout.BGARefreshLayoutDelegate) this);
        BGAMoocStyleRefreshViewHolder refreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        refreshViewHolder.setOriginalImage(R.drawable.refresh_ks);
        refreshViewHolder.setUltimateColor(R.color.refresh_img_ks);
        // 设置下拉刷新和上拉加载更多的风格
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    }


}
