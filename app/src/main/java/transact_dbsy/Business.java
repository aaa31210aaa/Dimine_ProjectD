package transact_dbsy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.administrator.dimine_projectd.R;

public class Business extends Fragment implements View.OnClickListener{
    private static final String TAG = "Business";
    private View view;
    private Spinner business_spinner;

    public Business() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_business, null);
            initView();
            setOnclick();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView(){
        business_spinner = (Spinner) view.findViewById(R.id.business_spinner);
    }

    private void setOnclick(){

    }

    @Override
    public void onClick(View v) {

    }
}
