package fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.dimine_projectd.ProductionDetail;
import com.example.administrator.dimine_projectd.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.PlanGridView1Adapter;
import entity.PlanWeekly;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProductionFragment";
    private View view;
    private MyGridView gridview1;
    private MyGridView gridview2;
    private MyGridView gridview3;
    private MyGridView gridview4;
    private Intent intent;
    private String url;
    private String byGx_url;
    private String bz_url;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    //    private String[] procid;
    private String json1 = "{'success':true,'titles':['标题1','标题2','标题3'],'cells':{'标题1':[{'content':'测试1','url':'aaaa'}],'标题2':[{'content':'测试4','url':'aaaa'},{'content':'测试5','url':'aaaa'},{'content':'测试6','url':'aaaa'}],'标题3':[{'content':'测试7','url':'aaaa'},{'content':'测试8','url':'aaaa'}]}}";
    private String content;
    private String img_url;
    private ArrayList<String> flag_list;
    private LinearLayout production_focous; //获取焦点的
    private String language;

    public ProductionFragment() {
        // Required empty public constructor
    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    intent = new Intent(getActivity(), ProductionDetail.class);
                    startActivity(intent);
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_production, null);
            initView();
            setOnclick();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        production_focous.setFocusable(true);
        production_focous.setFocusableInTouchMode(true);
        production_focous.requestFocus();
    }

    protected void initView() {
        production_focous = (LinearLayout) view.findViewById(R.id.production_focous);
        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage",null);

        url = MyHttpUtils.ApiProduction();
        byGx_url = MyHttpUtils.ApiProductionByGx();
        bz_url = MyHttpUtils.ApiProductionByUser();
        mOkhttp();
    }


    private void mOkhttp(){
        final LinearLayout production_big_ll = (LinearLayout) view.findViewById(R.id.production_big_ll);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("identification",language)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(getActivity(),"请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("ProductionFragment", response);
                        try {
                            JSONObject jsb = new JSONObject(response);
                            JSONObject jsb2 = jsb.getJSONObject("cells");
                            final JSONArray jsonArray = jsb.getJSONArray("titles");
                            flag_list = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONArray jsonArray2 = jsb2.getJSONArray(jsonArray.getString(i));
                                LinearLayout layout = new LinearLayout(getActivity());
                                LinearLayout grid_layout = new LinearLayout(getActivity());
                                layout.setGravity(Gravity.CENTER);
                                layout.setPadding(10, 10, 10, 10);
                                LinearLayout.LayoutParams my_img = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams my_text = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                                ArrayList<PlanWeekly> tv_list = new ArrayList<PlanWeekly>();
                                ArrayList<PlanWeekly> iv_list = new ArrayList<PlanWeekly>();

                                for (int j = 0; j < jsonArray2.length(); j++) {
//                                    procid[j] = jsonArray2.optJSONObject(j).getString("procid");
//                                    Log.e("ProductionFragment", procid.length + "----" + jsonArray2.length());
                                    PlanWeekly pz = new PlanWeekly();
                                    pz.setPlanname(jsonArray2.optJSONObject(j).getString("proctname"));
                                    flag_list.add(jsonArray2.optJSONObject(j).getString("flag"));
                                    tv_list.add(pz);
                                }

                                PlanGridView1Adapter adapter = new PlanGridView1Adapter(getActivity(), tv_list, iv_list);
                                myGridView.setAdapter(adapter);


                                layout.addView(img, my_img);
                                layout.addView(text, my_text);
                                grid_layout.addView(myGridView, my_gridview);
                                //动态添加GridView
                                production_big_ll.addView(layout);
                                production_big_ll.addView(grid_layout);

                                myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                                        try {
//                                            OkHttpUtils
//                                                    .get()
//                                                    .url(bz_url)
//                                                    .addParams("access_token", user_token)
//                                                    .addParams("procid", jsonArray2.optJSONObject(position).getString("procid"))
//                                                    .build()
//                                                    .execute(new StringCallback() {
//                                                        @Override
//                                                        public void onError(Request request, Exception e) {
//
//                                                        }
//
//                                                        @Override
//                                                        public void onResponse(String response) {
//                                                            try {
//                                                                Log.e("ProductionFragment", response);
//
//
//
//
////                                                                if (jsonArray.length()>1){
////                                                                    intent.putExtra("pro_titlename",jsonArray.getString(position));
////                                                                }else{
////                                                                    intent.putExtra("pro_titlename",jsonArray.getString(0));
////                                                                }
//
//
//                                                            } catch (JSONException e) {
//                                                                e.printStackTrace();
//                                                            }
//
//                                                        }
//                                                    });
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
                                        if (flag_list.get(position).equals("1")){
                                            try {
                                                intent = new Intent(getActivity(), ProductionDetail.class);
                                                intent.putExtra("prod_name", jsonArray2.optJSONObject(position).getString("proctname"));
                                                intent.putExtra("clickid", jsonArray2.optJSONObject(position).getString("procid"));
                                                startActivity(intent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            ShowToast.showShort(getActivity(),"该工序没有配置指标,不能维护台账记录");
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
