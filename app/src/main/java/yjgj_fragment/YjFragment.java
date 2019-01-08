package yjgj_fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.dimine_projectd.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.YjFragmentAdapter;
import entity.Alarm;
import listener.SwpipeListViewOnScrollListener;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class YjFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "YjFragment";
    private String url;
    private View view;
    private ListView yj_listview;
    private SwipeRefreshLayout yj_freshlayout;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String user_token;
    private ArrayList<Alarm> yj_list;

    public YjFragment() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    ShowToast.showShort(getActivity(), "刷新完成");
                    yj_freshlayout.setRefreshing(false);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_yj, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    protected void initView() {
        yj_listview = (ListView) view.findViewById(R.id.yj_listview);
        yj_freshlayout = (SwipeRefreshLayout) view.findViewById(R.id.yj_freshlayout);
        yj_listview.setOnScrollListener(new SwpipeListViewOnScrollListener(yj_freshlayout));

        yj_freshlayout.setOnRefreshListener(this);
        yj_freshlayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        url = MyHttpUtils.ApiAlarmList();
        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        editor.commit();


        thread.start();
    }


    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("access_token", user_token)
                    .addParams("alarmtype", "GJLB001")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("cells");
                                yj_list = new ArrayList<Alarm>();
                                for (int i = 0 ; i<jsonArray.length();i++){
                                    Alarm alarm = new Alarm();
                                    alarm.setAlarmtitle(jsonArray.optJSONObject(i).getString("alarmtitle"));
                                    alarm.setAlarmlevel(jsonArray.optJSONObject(i).getString("alarmlevel"));
                                    alarm.setAddtime(jsonArray.optJSONObject(i).getString("addtime"));
                                    alarm.setHandlecontent(jsonArray.optJSONObject(i).getString("handlecontent"));
                                    yj_list.add(alarm);
                                }
                                YjFragmentAdapter adapter = new YjFragmentAdapter(getActivity(),yj_list);
                                yj_listview.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    });


    //刷新
    @Override
    public void onRefresh() {
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mthread.start();
    }



}
