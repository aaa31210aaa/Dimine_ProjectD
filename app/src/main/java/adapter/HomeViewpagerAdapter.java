package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeViewpagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager fm;
    private List<String> taglist;


    public HomeViewpagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragmentList) {
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

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        //得到缓存的fragment
//        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//        //得到tag
//        String fragmentTag = fragment.getTag();
//        FragmentTransaction ft = fm.beginTransaction();
//        //移除旧的fragment
//        ft.remove(fragment);
//        //换成新的fragment
//        fragment = mFragmentList.get(position);
//        //添加新fragment时必须用前面获得的tag
//        ft.add(container.getId(), fragment, fragmentTag);
//        ft.attach(fragment);
//        ft.commit();
//        return fragment;
//    }

    public void refresh(ArrayList<Fragment> fragments){
        this.mFragmentList = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}
