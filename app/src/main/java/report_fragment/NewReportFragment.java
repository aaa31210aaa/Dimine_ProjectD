package report_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.dimine_projectd.Graphical;
import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.TestAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewReportFragment extends Fragment {
    private View view;
    private Intent intent;
    private ListView new_report_listview;
    private ArrayList<String> mytargetname;
    private ArrayList<String> mytargetid;
    private ArrayList<String> procid;
    private ArrayList<String> techid;
    private ArrayList<String> procnames;
    private String titlename;
    private String date;


    private String sign; //标志

    public NewReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        //onCreateView中获取
        final Map<String, ArrayList<String>> map = new HashMap<>();
        final Map<String, ArrayList<String>> map2 = new HashMap<>();
        procnames = arguments.getStringArrayList("myprocnames");
        procid = arguments.getStringArrayList("myprocid");
        techid = arguments.getStringArrayList("mytechid");
        titlename = arguments.getString("mytitlename");
        sign = arguments.getString("sign");
        date = arguments.getString("mydate");

        for (String s : procnames) {
            map.put(s, arguments.getStringArrayList(s));
        }

        for (String s2 : procid) {
            map2.put(s2, arguments.getStringArrayList(s2));
        }

        view = View.inflate(getActivity(), R.layout.fragment_new_report,null);
        new_report_listview = (ListView) view.findViewById(R.id.new_report_listview);
        new_report_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getActivity(), Graphical.class);
                ArrayList<String> str_list = map.get(procnames.get(position));
                ArrayList<String> str2_list = map2.get(procid.get(position));

                intent.putStringArrayListExtra("map", str_list);
                intent.putStringArrayListExtra("map2",str2_list);

                intent.putExtra("procid", procid.get(position));
                intent.putExtra("techid", techid.get(position));
                intent.putExtra("procnames", procnames.get(position));
                intent.putExtra("titlename", titlename);
                intent.putExtra("select_sign", sign);
                intent.putExtra("m_date",date);
                startActivity(intent);

            }
        });
        TestAdapter adapter = new TestAdapter(getActivity(), procnames, map);
        adapter.notifyDataSetChanged();
        new_report_listview.setAdapter(adapter);

        return view;
    }

    public static NewReportFragment newInstance(ArrayList<String> procnames, Map<String, ArrayList<String>> map, Map<String, ArrayList<String>> map2, ArrayList<String> targetname_list, ArrayList<String> targetid_list, ArrayList<String> procid_list, ArrayList<String> techid_list, String title_name, String sign, String date){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("myprocnames", procnames);
        bundle.putStringArrayList("mytargetname", targetname_list);
        bundle.putStringArrayList("mytargetid", targetid_list);
        bundle.putStringArrayList("myprocid", procid_list);
        bundle.putStringArrayList("mytechid", techid_list);
        bundle.putString("mytitlename", title_name);
        bundle.putString("sign", sign);
        bundle.putString("mydate",date);

        for (String s : procnames) {
            bundle.putStringArrayList(s, map.get(s));
        }

        for (String s2 : procid_list) {
            bundle.putStringArrayList(s2, map2.get(s2));
        }


        NewReportFragment fragment = new NewReportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
