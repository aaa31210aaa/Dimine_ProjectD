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
public class DmDeviceReport extends Fragment {


    public DmDeviceReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dm_device_report, container, false);
    }

}
