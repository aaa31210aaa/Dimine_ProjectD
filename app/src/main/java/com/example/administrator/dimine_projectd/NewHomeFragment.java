package com.example.administrator.dimine_projectd;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.TestAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewHomeFragment extends Fragment {
    private View view;
    private ListView test_listview;
    private Intent intent;
    private ArrayList<String> procid;
    private ArrayList<String> techid;
    private ArrayList<String> procnames;
    private String titlename;
    private ArrayList<String> indexno;
    private String msign; //标志
    private String mdate; //传到图形界面的日期
    private TextView fragment_home_date;
    private View home_view;
    private Map<String, ArrayList<String>> my_map;
    private Map<String, ArrayList<String>> my_map2;

    private TestAdapter adapter;
    private int fetch;


    public NewHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }


        Bundle arguments = getArguments();

        //onCreateView中获取
        my_map = new HashMap<>();
        my_map2 = new HashMap<>();
        procnames = arguments.getStringArrayList("myprocnames");
        procid = arguments.getStringArrayList("myprocid");
        techid = arguments.getStringArrayList("mytechid");
        titlename = arguments.getString("mytitlename");
        indexno = arguments.getStringArrayList("myindexno");
        msign = arguments.getString("sign");
        mdate = arguments.getString("mydate");

        home_view = View.inflate(getActivity(), R.layout.fragment_home, null);
        fragment_home_date = (TextView) home_view.findViewById(R.id.fragment_home_date);

        for (String s : procnames) {
            my_map.put(s, arguments.getStringArrayList(s));
        }

        for (String s2 : procid) {
            my_map2.put(s2, arguments.getStringArrayList(s2));
        }
//        Log.d("opoppop", my_map + "---" + my_map2 + "---" + procnames + "---" + procid + "---" + techid + "---" + titlename);

        view = inflater.inflate(R.layout.fragment_test, container, false);
        test_listview = (ListView) view.findViewById(R.id.test_listview);

        test_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getActivity(), Graphical.class);
                //将id集合传到图表界面
                ArrayList<String> str_list = my_map.get(procnames.get(position));
                ArrayList<String> str2_list = my_map2.get(procid.get(position));

                intent.putStringArrayListExtra("map", str_list);
                intent.putStringArrayListExtra("map2", str2_list);

                intent.putExtra("procid", procid.get(position));
                intent.putExtra("techid", techid.get(position));
                intent.putExtra("procnames", procnames.get(position));
                intent.putExtra("titlename", titlename);
                intent.putExtra("select_sign", msign);
                intent.putExtra("date", mdate);
                startActivity(intent);
            }
        });
        adapter = new TestAdapter(getActivity(), procnames, my_map);
        test_listview.setAdapter(adapter);
        return view;
    }

    //新方法
    public static NewHomeFragment newInstance(ArrayList<String> procnames, Map<String, ArrayList<String>> map, Map<String, ArrayList<String>> map2, ArrayList<String> targetname_list, ArrayList<String> targetid_list, ArrayList<String> procid_list, ArrayList<String> techid_list, String title_name, String sign, String date) {
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("myprocnames", procnames);
        bundle.putStringArrayList("mytargetname", targetname_list);
        bundle.putStringArrayList("mytargetid", targetid_list);
        bundle.putStringArrayList("myprocid", procid_list);
        bundle.putStringArrayList("mytechid", techid_list);
        bundle.putString("mytitlename", title_name);
        bundle.putString("sign", sign);
        bundle.putString("mydate", date);

        Log.e("tag", map.toString() + "-----" + map2.toString());

        for (String s : procnames) {
            bundle.putStringArrayList(s, map.get(s));
        }

        for (String s2 : procid_list) {
            bundle.putStringArrayList(s2, map2.get(s2));
        }

//      Log.d("popopop", procnames + "--" + targetname_list + "--" + targetid_list + "--" + procid_list + "--" + map + "--" + map2);

        NewHomeFragment fragment = new NewHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
