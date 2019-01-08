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

import adapter.AddProChooseBzAdapter;
import entity.ChooseBanZu;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class AddProChooseBz extends BaseActivity {
    private static final String TAG = "AddProChooseBz";
    private ImageView choose_bz_back;
    private ListView addpro_choose_bz_list;
    private String bz_url;
    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ArrayList<ChooseBanZu> bz_list;
    private String teamgroupid; //班组id
    private String procid; //工序id



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pro_choose_bz);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        choose_bz_back = (ImageView) findViewById(R.id.choose_bz_back);
        addpro_choose_bz_list = (ListView) findViewById(R.id.addpro_choose_bz_list);

        bz_url = MyHttpUtils.ApiProductionByUser();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);

        Intent intent = getIntent();

        procid = intent.getStringExtra("myprocid");
        mOkhttp();
    }


    private void mOkhttp(){
        OkHttpUtils
                .get()
                .url(bz_url)
                .addParams("access_token", user_token)
                .addParams("procid",procid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(AddProChooseBz.this,R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            bz_list = new ArrayList<ChooseBanZu>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ChooseBanZu banZu = new ChooseBanZu();
                                banZu.setTeamname(jsonArray.optJSONObject(i).getString("teamname"));
                                bz_list.add(banZu);

                                addpro_choose_bz_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView add_pro_choose_bc_item_tv = (TextView) view.findViewById(R.id.add_pro_choose_bc_item_tv);
                                        Intent intent = new Intent();
                                        String bz_back = add_pro_choose_bc_item_tv.getText().toString();
                                        try {
                                            teamgroupid = jsonArray.optJSONObject(position).getString("teamgroupid");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra("bz_back", bz_back);
                                        intent.putExtra("bz_selectid", teamgroupid);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                            AddProChooseBzAdapter adapter = new AddProChooseBzAdapter(AddProChooseBz.this, bz_list);
                            addpro_choose_bz_list.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        choose_bz_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_bz_back:
                finish();
                break;
        }
    }
}
