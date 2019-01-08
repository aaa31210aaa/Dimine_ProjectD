package dm_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.dimine_projectd.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DmDeviceFragment extends Fragment {
    private static final String TAG = "DmDeviceFragment";
    private View view;


    public DmDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_dm_device_fragment, null);
            initView();
            setOnClick();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView(){

    }

    private void setOnClick(){

    }


}
