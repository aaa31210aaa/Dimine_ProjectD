package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DayReportViewpagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager fm;
    private List<String> taglist;

    public DayReportViewpagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragmentList) {
        super(fm);
        this.fm = fm;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        int hashCode = mFragmentList.get(position).hashCode();
        return hashCode;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void DrRefresh(ArrayList<Fragment> fragments) {
        this.mFragmentList = fragments;
        notifyDataSetChanged();
    }
}
