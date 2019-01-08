package plan_fragment;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.MyGridView;
import com.example.administrator.dimine_projectd.MyPlanDetail;
import com.example.administrator.dimine_projectd.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.PlanGridView1Adapter;
import entity.PlanWeekly;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyPlanFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WeeklyPlanFragment";
    private View view;
    private Intent intent;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String url;
    private String detail_url;


//    private String[] procid;

    private PlanGridView1Adapter adapter;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private HashMap<String, String> map = new HashMap<String, String>();
    private String language;

    public WeeklyPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_weekly_plan, null);
            initView();
            setOnclick();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    protected void initView() {
        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage", null);
        //周计划月计划外部布局接口
        url = MyHttpUtils.ApiPlanWeeklyMonth();

        detail_url = MyHttpUtils.ApiPlanWeeklyDetail();
        mOkhttp();
    }

    private void mOkhttp() {
        final LinearLayout weekly_plan_big_ll = (LinearLayout) view.findViewById(R.id.weekly_plan_big_ll);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("identification", language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(getActivity(), R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("WeeklyPlanFragment", response);
                        try {
                            JSONObject jsb = new JSONObject(response);
                            JSONObject jsb2 = jsb.getJSONObject("cells");
                            final JSONArray jsonArray = jsb.getJSONArray("title");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONArray jsonArray2 = jsb2.getJSONArray(jsonArray.getString(i));
                                LinearLayout layout = new LinearLayout(getActivity());
                                LinearLayout grid_layout = new LinearLayout(getActivity());
                                layout.setGravity(Gravity.CENTER_VERTICAL);
                                layout.setPadding(10, 10, 10, 10);
                                LinearLayout.LayoutParams my_img = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams my_text = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams my_gridview = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                my_text.setMargins(20, 0, 0, 0);
                                //实例化
                                TextView img = new TextView(getActivity());
                                TextView text = new TextView(getActivity());
                                MyGridView myGridView = new MyGridView(getActivity());

                                //设置布局
                                img.setLayoutParams(my_img);
                                text.setLayoutParams(my_text);
                                myGridView.setLayoutParams(my_gridview);

                                //赋值
                                img.setBackgroundResource(R.drawable.u18_line);
                                text.setText(jsonArray.getString(i));
                                img.setWidth(8);
                                img.setHeight(60);
                                text.setTextSize(13);
                                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_deep));

                                if (jsonArray2.length() >= 3) {
                                    myGridView.setNumColumns(3);
                                } else {
                                    myGridView.setNumColumns(3);
                                }


                                //去掉滚动条
                                myGridView.setVerticalScrollBarEnabled(false);
                                ArrayList<PlanWeekly> plan_tv_list = new ArrayList<PlanWeekly>();
                                ArrayList<PlanWeekly> plan_iv_list = new ArrayList<PlanWeekly>();

                                for (int j = 0; j < jsonArray2.length(); j++) {
                                    PlanWeekly pz = new PlanWeekly();
                                    pz.setPlanname(jsonArray2.optJSONObject(j).getString("procname"));
                                    plan_tv_list.add(pz);
                                }
                                adapter = new PlanGridView1Adapter(getActivity(), plan_tv_list, plan_iv_list);
                                myGridView.setAdapter(adapter);


                                layout.addView(img, my_img);
                                layout.addView(text, my_text);
                                grid_layout.addView(myGridView, my_gridview);
                                //动态添加GridView
                                weekly_plan_big_ll.addView(layout);
                                weekly_plan_big_ll.addView(grid_layout);
                                myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        final TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
                                        try {
                                            OkHttpUtils
                                                    .get()
                                                    .url(detail_url)
                                                    .addParams("access_token", user_token)
                                                    .addParams("procid", jsonArray2.optJSONObject(position).getString("procid"))
                                                    .addParams("techid", jsonArray2.optJSONObject(position).getString("techid"))
                                                    .build()
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onError(Request request, Exception e) {
                                                            e.printStackTrace();
                                                            ShowToast.showShort(getActivity(), R.string.network_error);
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            Log.d("WeeklyPlanFragment", response);
                                                            try {
                                                                intent = new Intent(getActivity(), MyPlanDetail.class);
//                                                                    if (jsonArray.length() > 1) {
//                                                                        intent.putExtra("titlename", map.get(tv_item.getText().toString()));
//                                                                    } else {
//                                                                        intent.putExtra("titlename", jsonArray.getString(0));
//                                                                    }
                                                                String myprocid = jsonArray2.optJSONObject(position).getString("procid");
                                                                String mytechid = jsonArray2.optJSONObject(position).getString("techid");
                                                                String str = "weekly";


//                                                                    intent.putExtra("proctname", jsonArray2.optJSONObject(position).getString("procname"));
//                                                                  Log.d("WeeklyPlanFragment", jsonArray.getString(position) + "----" + jsonArray2.optJSONObject(position).getString("procname") + "-----" + jsonArray2.optJSONObject(position).getString("procid"));
                                                                intent.putExtra("str", str);
                                                                intent.putExtra("myprocid", myprocid);
                                                                intent.putExtra("mytechid", mytechid);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    protected void setOnclick() {

    }

    @Override
    public void onClick(View v) {

    }
}
