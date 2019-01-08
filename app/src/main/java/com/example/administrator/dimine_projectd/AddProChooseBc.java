package com.example.administrator.dimine_projectd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.AddProChooseBcAdapter;
import entity.ChooseBanCi;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class AddProChooseBc extends BaseActivity {
    private static final String TAG = "ProChooseBc";
    private ListView addpro_choose_bc_list;
    private String bc_url;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ArrayList<ChooseBanCi> bc_list;
    private String classid;  //作业地点id
    private ImageView choose_bc_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pro_choose_bc);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        addpro_choose_bc_list = (ListView) findViewById(R.id.addpro_choose_bc_list);
        choose_bc_back = (ImageView) findViewById(R.id.choose_bc_back);
        bc_url = MyHttpUtils.ApiProductionByBc();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        thread.start();


    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            OkHttpUtils
                    .get()
                    .url(bc_url)
                    .addParams("access_token", user_token)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                            ShowToast.showShort(AddProChooseBc.this,R.string.network_error);
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                final JSONArray jsonArray = jsonObject.getJSONArray("cells");

                                bc_list = new ArrayList<ChooseBanCi>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ChooseBanCi banCi = new ChooseBanCi();
                                    banCi.setClassname(jsonArray.optJSONObject(i).getString("classname"));
                                    bc_list.add(banCi);

                                    addpro_choose_bc_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView add_pro_choose_bc_item_tv = (TextView) view.findViewById(R.id.add_pro_choose_bc_item_tv);
                                            Intent intent = new Intent();
                                            String back = add_pro_choose_bc_item_tv.getText().toString();
                                            try {
                                                classid = jsonArray.optJSONObject(position).getString("classid");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            intent.putExtra("back", back);
                                            intent.putExtra("selectid", classid);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });

                                }


                                AddProChooseBcAdapter adapter = new AddProChooseBcAdapter(AddProChooseBc.this, bc_list);
                                addpro_choose_bc_list.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    });


    @Override
    protected void setOnClick() {
        choose_bc_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_bc_back:
                finish();
                break;
        }
    }
}
