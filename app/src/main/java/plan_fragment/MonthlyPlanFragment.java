package plan_fragment;


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
import java.util.Map;

import adapter.PlanMonthlyAdapter;
import entity.PlanWeekly;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyPlanFragment extends Fragment {
    private static final String TAG = "MonthlyPlanFragment";
    private View view;
    private Map test_grid = new HashMap();
    //   private String[][] test_grid ={{"测试1","测试2","测试3"},{"测试1","测试2","测试3"},{"测试1","测试2","测试3","测试4"}};
    private String json3 = "{\"success\":true,\"errormessage\":\"\",\"cells\":[{\"planname\":\"\",\"deptname\":\"大冶\",\"abc\":\"计划名称\"},{\"planname\":\"\",\"deptname\":\"大冶\",\"abc\":\"所属项目部\"},{\"planname\":\"\",\"deptname\":\"大冶\",\"abc\":\"工序\"}]}";


    private String json2 = "{'success':true,'titles':['标题1','标题2'],'cells':{'标题1':[{'content':'测试1','url':'aaaa'},{'content':'测试2','url':'aaaa'},{'content':'测试3','url':'aaaa'}],'标题2':[{'content':'测试4','url':'aaaa'},{'content':'测试5','url':'aaaa'},{'content':'测试6','url':'aaaa'}]}}";
    private String content;
    private String img_url;

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;
    private String detail_url;
    private PlanMonthlyAdapter adapter;
    private Intent intent;
    private String language;

    public MonthlyPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_monthly_plan, null);
            initView();

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
        url = MyHttpUtils.ApiPlanWeeklyMonth();
        detail_url = MyHttpUtils.ApiPlanMonthlyDetail();
//        Iterator<String> keys = test_grid.keySet().iterator();
        mOkhttp();
    }

    private void mOkhttp() {
        final LinearLayout big_ll = (LinearLayout) view.findViewById(R.id.big_ll);
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
                                 Log.d(TAG, response);
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
                                         //添加适配器
                                         adapter = new PlanMonthlyAdapter(getActivity(), plan_tv_list, plan_iv_list);
                                         myGridView.setAdapter(adapter);

                                         layout.addView(img, my_img);
                                         layout.addView(text, my_text);
                                         grid_layout.addView(myGridView, my_gridview);
                                         //动态添加GridView
                                         big_ll.addView(layout);
                                         big_ll.addView(grid_layout);

                                         myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                               @Override
                                                                               public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                                            try {
//                                                OkHttpUtils
//                                                        .get()
//                                                        .url(detail_url)
//                                                        .addParams("access_token", user_token)
//                                                        .addParams("procid", jsonArray2.optJSONObject(position).getString("procid"))
//                                                        .build()
//                                                        .execute(new StringCallback() {
//                                                            @Override
//                                                            public void onError(Request request, Exception e) {
//                                                                e.printStackTrace();
//                                                            }
//
//                                                            @Override
//                                                            public void onResponse(String response) {
//                                                                Log.d("MonthlyPlanFragment", response);
//                                                                try {
//                                                                    intent = new Intent(getActivity(), PlanWeeklyDetail.class);
//                                                                    if (jsonArray.length() > 1) {
//                                                                        intent.putExtra("titlename", jsonArray.getString(position));
//                                                                    } else {
//                                                                        intent.putExtra("titlename", jsonArray.getString(0));
//                                                                    }
//                                                                    intent.putExtra("proctname", jsonArray2.optJSONObject(position).getString("procname"));
////                                                                    Log.d("WeeklyPlanFragment", jsonArray.getString(position) + "----" + jsonArray2.optJSONObject(position).getString("procname") + "-----" + jsonArray2.optJSONObject(position).getString("procid"));
//                                                                    startActivity(intent);
//                                                                } catch (JSONException e) {
//                                                                    e.printStackTrace();
//                                                                }
//
//
//                                                            }
//                                                        });
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
                                                                                   try {
                                                                                       intent = new Intent(getActivity(), MyPlanDetail.class);
//                                                                                           if (jsonArray.length() > 1) {
//                                                                                               intent.putExtra("titlename", jsonArray.getString(position));
//                                                                                           } else {
//                                                                                               intent.putExtra("titlename", jsonArray.getString(0));
//                                                                                           }
                                                                                       String myprocid = jsonArray2.optJSONObject(position).getString("procid");
                                                                                       String mytechid = jsonArray2.optJSONObject(position).getString("techid");

                                                                                       String str = "month";
//                                                                                           intent.putExtra("proctname", jsonArray2.optJSONObject(position).getString("procname"));
                                                                                       intent.putExtra("str", str);
                                                                                       intent.putExtra("myprocid", myprocid);
                                                                                       intent.putExtra("mytechid", mytechid);
                                                                                       startActivity(intent);
                                                                                   } catch (JSONException e) {
                                                                                       e.printStackTrace();
                                                                                   }
                                                                               }
                                                                           }

                                         );
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                );
    }


}
