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

import adapter.AddProChooseGyAdapter;
import entity.ChooseTech;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class AddProTech extends BaseActivity {
    private static final String TAG = "AddProTech";
    private ImageView addpro_choose_tech_back;
    private ListView addpro_choose_tech_list;

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String gy_url; // 工艺接口地址

    private String clickid;   //工序id
    private String projectid; //作业地点id
    private String techid; //工艺id
    private ArrayList<ChooseTech> tech_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pro_tech);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        addpro_choose_tech_back = (ImageView) findViewById(R.id.addpro_choose_tech_back);
        addpro_choose_tech_list = (ListView) findViewById(R.id.addpro_choose_tech_list);


        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);

        gy_url = MyHttpUtils.ApiSelectGyByGxAndProject();
        Intent intent = getIntent();
        clickid = intent.getStringExtra("clickid");
        projectid = intent.getStringExtra("projectid");

        thread.start();
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            OkHttpUtils
                    .get()
                    .url(gy_url)
                    .addParams("access_token", user_token)
                    .addParams("procid", clickid)
                    .addParams("projectid", projectid)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                            ShowToast.showShort(AddProTech.this,"请检查网络");
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                final JSONArray jsonArray = jsonObject.getJSONArray("cells");
                                tech_list= new ArrayList<ChooseTech>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ChooseTech tech = new ChooseTech();
                                    tech.setTechname(jsonArray.optJSONObject(i).getString("techname"));
                                    tech_list.add(tech);

                                    addpro_choose_tech_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView add_pro_choose_bc_item_tv = (TextView) view.findViewById(R.id.add_pro_choose_bc_item_tv);
                                            Intent intent = new Intent();
                                            String tech_back = add_pro_choose_bc_item_tv.getText().toString();
                                            try {
                                                techid = jsonArray.optJSONObject(position).getString("techid");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            intent.putExtra("tech_back", tech_back);
                                            intent.putExtra("techid", techid);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });

                                }
                                AddProChooseGyAdapter adapter = new AddProChooseGyAdapter(AddProTech.this,tech_list);
                                addpro_choose_tech_list.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

        }
    });

    @Override
    protected void setOnClick() {
        addpro_choose_tech_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addpro_choose_tech_back:
                finish();
                break;
        }
    }
}
