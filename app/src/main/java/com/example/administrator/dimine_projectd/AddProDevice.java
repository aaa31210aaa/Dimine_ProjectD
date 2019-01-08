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

import adapter.AddProChooseDeviceAdapter;
import entity.ChooseDevice;
import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class AddProDevice extends BaseActivity {
    private static final String TAG= "AddProDevice";
    private ImageView addpro_choose_device_back;
    private ListView addpro_choose_device_list;

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String device_url;
    private String clickid;   //班组id
    private String devid; //设备id
    private ArrayList<ChooseDevice> device_list;
    private String teamgroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initView();
        setOnClick();
    }

    @Override
    protected void initView() {
        addpro_choose_device_back = (ImageView) findViewById(R.id.addpro_choose_device_back);
        addpro_choose_device_list = (ListView) findViewById(R.id.addpro_choose_device_list);

        device_url = MyHttpUtils.ApiProductionDeviceByTeam();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        Intent intent = getIntent();
        clickid = intent.getStringExtra("clickid");
        teamgroupId = intent.getStringExtra("myteamgroupid");

        mOkhttp();
    }

    private void mOkhttp(){
        OkHttpUtils
                .get()
                .url(device_url)
                .addParams("access_token", user_token)
                .addParams("teamgroupid", teamgroupId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ShowToast.showShort(AddProDevice.this,"请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            device_list = new ArrayList<ChooseDevice>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                ChooseDevice device = new ChooseDevice();
                                device.setDevname(jsonArray.optJSONObject(i).getString("devname"));
                                device_list.add(device);

                                addpro_choose_device_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView add_pro_choose_bc_item_tv = (TextView) view.findViewById(R.id.add_pro_choose_bc_item_tv);
                                        Intent intent = new Intent();
                                        String device_back = add_pro_choose_bc_item_tv.getText().toString();
                                        try {
                                            devid = jsonArray.optJSONObject(position).getString("devid");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        intent.putExtra("device_back", device_back);
                                        intent.putExtra("device_selectid", devid);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                            AddProChooseDeviceAdapter adapter = new AddProChooseDeviceAdapter(AddProDevice.this,device_list);
                            addpro_choose_device_list.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        addpro_choose_device_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addpro_choose_device_back:
                finish();
                break;
        }
    }
}
