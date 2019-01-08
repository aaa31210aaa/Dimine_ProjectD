package com.example.administrator.dimine_projectd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import utils.BaseActivity;
import utils.MyHttpUtils;
import utils.ShowToast;

public class NewProductionJl extends BaseActivity {
    private static final String TAG = "NewProductionJl";
    private ImageView new_production_jl_back;
    private ListView new_production_jl_listview;
    private AutoCompleteTextView new_production_jl_etSearch;
    private String user_token;
    private String zydd_url;
    private String classid;
    private String reportdate;
    private String clickid;
    private String teamgroupid;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String projectid;

    private ArrayAdapter<String> adapter;
    private String[] data; // listview里面的内容
    private HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_production_jl);
        initView();
        setOnClick();
    }


    @Override
    protected void initView() {
        new_production_jl_back = (ImageView) findViewById(R.id.new_production_jl_back);
        new_production_jl_listview = (ListView) findViewById(R.id.new_production_jl_listview);
        new_production_jl_etSearch = (AutoCompleteTextView) findViewById(R.id.new_production_jl_etSearch);


        zydd_url = MyHttpUtils.ApiSelectProjectByGx();

        //获得一些跳转传过来的数据
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        Intent intent = getIntent();
        classid = intent.getStringExtra("classid");
        reportdate = intent.getStringExtra("reportdate");
        clickid = intent.getStringExtra("my_clickid");
        teamgroupid = intent.getStringExtra("teamgroupid");
        mOkhttp();
    }


    //视图初始化接口连接
    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(zydd_url)
                .addParams("access_token", user_token)
                .addParams("reportdate", reportdate)
                .addParams("classes", classid)
                .addParams("procid", clickid)
                .addParams("teamgroupid", teamgroupid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(NewProductionJl.this,"请检查网络");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("NewProductionJl", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            data = new String[jsonArray.length()];
//                            ArrayList<SelectProject> jl_list = new ArrayList<SelectProject>();
                            for (int i = 0; i < jsonArray.length(); i++) {
//                                SelectProject selectProject = new SelectProject();
//                                selectProject.setProjectname(jsonArray.optJSONObject(i).getString("projectname"));
//                                jl_list.add(selectProject);
                                map.put(jsonArray.optJSONObject(i).getString("projectname"), jsonArray.optJSONObject(i).getString("projectid"));
                                data[i] = jsonArray.optJSONObject(i).getString("projectname");

                                new_production_jl_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView add_pro_choose_bc_item_tv = (TextView) view.findViewById(R.id.add_pro_choose_bc_item_tv);
                                        Intent intent = new Intent();
                                        String back = add_pro_choose_bc_item_tv.getText().toString();
                                        try {
                                            projectid = jsonArray.optJSONObject(position).getString("projectid");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra("project_back", back);
                                        intent.putExtra("project_selectid", map.get(add_pro_choose_bc_item_tv.getText().toString()));
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }

                            Log.d("ttttt", map + "");

//                            NewProductionJlListviewAdapter adapter = new NewProductionJlListviewAdapter(NewProductionJl.this, jl_list);
//                            new_production_jl_listview.setAdapter(adapter);
                            adapter = new ArrayAdapter<String>(NewProductionJl.this, R.layout.add_pro_choose_bc_item, R.id.add_pro_choose_bc_item_tv, data);
                            new_production_jl_etSearch.setAdapter(adapter);
                            new_production_jl_listview.setAdapter(adapter);

                            //当输入框的文字改变时执行以下方法
                            new_production_jl_etSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapter.getFilter().filter(s);

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        ArrayList<SelectProject> jl_list = new ArrayList<SelectProject>();
//                        SelectProject selectProject = new SelectProject();
//                        for (int i = 0; i < 15; i++) {
//                            selectProject.setProjectname("-120分段");
//                            jl_list.add(selectProject);
//                        }
//
//
//
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void setOnClick() {
        new_production_jl_back.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_production_jl_back:
                finish();
                break;
        }
    }

}
