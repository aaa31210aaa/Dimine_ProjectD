package com.example.administrator.dimine_projectd;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.MyDbsyAdapter;
import entity.MyDbsy;
import entity.PlanContent;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.MyHttpUtils;
import utils.ShowToast;

public class MyApprovalActivity extends BaseActivity {
    private static final String TAG = "MyApprovalActivity";
    private ImageView my_dbsy_back;
    private String url;
    private String qs_url; //签收url
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String user_token;
    private TextView dbsy_tv_name;//第一列列名
    private ArrayList<MyDbsy> dbsy_list; //第一列list
    private ArrayList<Map<String, String>> dbsy2_list; //第二列list;
    private int height_px;
    private TextView tv;//动态添加的textview
    private LinearLayout dbsy_title_ll;

    private int width;//屏幕的宽
    private int height; //屏幕的高
    private ListView dbsy_lv_goodname; //左边listview
    private ListView dbsy_good_info;// 右边listview
    private NoScrollHorizontalScrollView dbsy_sv_title;//不可滑动的顶部左侧的ScrollView
    private LinkedHorizontalScrollView dbsy_good_detail;//底部左侧的ScrollView

    private Map<String, String> map;
    private String[] arr_title = {"操作", "流程名单", "任务名称", "任务发起人", "当前节点", "任务创建日期"};
    private String[] arr_content = {"mobilenodeaction", "processname", "ywname", "appusername", "nameWithoutCascade", "createTime"};

    private String json = "{\"total\":1,\"cells\":[{\"errorMessage\":\"\",\"tenantId\":null,\"createTime\":\"2016-09-18  09:29:21\",\"parentTaskIdWithoutCascade\":null,\"formKey\":null,\"assigneeWithoutCascade\":\"0b6ed5ad3b774971b422272b20193b0a\",\"param\":null,\"query\":null,\"userid\":null,\"executionId\":null,\"delegationStateString\":null,\"taskDefinitionKeyWithoutCascade\":\"xmbjszgcs\",\"dueDateWithoutCascade\":null,\"ownerWithoutCascade\":null,\"mobilenodeaction\":null,\"mobileinfouri\":\"/mobile/ypdetail/completenodyTask.action\",\"nameWithoutCascade\":\"项目部技术总工程师\",\"id\":\"1010046\",\"revision\":null,\"processInstanceId\":\"1010039\",\"appusername\":null,\"bussinesskey\":\"dee311ffdd654be4a251a71bc84282e6\",\"processDefinitionId\":\"PM_NJHSPLC:3:950016\",\"priorityWithoutCascade\":\"50\",\"pager\":null,\"suspensionState\":null,\"uri\":\"/webpage/biz/pm/yearplan/process/main.jsp\",\"nodeaction\":\"/webpage/biz/pm/yearplan/completenodyTask.action\",\"processname\":\"年计划审批流程\",\"categoryWithoutCascade\":null,\"descriptionWithoutCascade\":null,\"applyUserId\":\"${applyUserId}\",\"ywname\":\"2012年开拓计划审批\",\"success\":true},{\"errorMessage\":\"\",\"tenantId\":null,\"createTime\":\"2016-09-18  09:29:19\",\"parentTaskIdWithoutCascade\":null,\"formKey\":null,\"assigneeWithoutCascade\":null,\"param\":null,\"query\":null,\"userid\":null,\"executionId\":null,\"delegationStateString\":null,\"taskDefinitionKeyWithoutCascade\":\"xmbjszgcs\",\"dueDateWithoutCascade\":null,\"ownerWithoutCascade\":null,\"mobilenodeaction\":null,\"mobileinfouri\":\"/mobile/ypdetail/completenodyTask.action\",\"nameWithoutCascade\":\"项目部技术总工程师\",\"id\":\"1010037\",\"revision\":null,\"processInstanceId\":\"1010030\",\"appusername\":null,\"bussinesskey\":\"b0432b2a93914ea2951b6aca51c17e5f\",\"processDefinitionId\":\"PM_NJHSPLC:3:950016\",\"priorityWithoutCascade\":\"50\",\"pager\":null,\"suspensionState\":null,\"uri\":\"/webpage/biz/pm/yearplan/process/main.jsp\",\"nodeaction\":\"/webpage/biz/pm/yearplan/completenodyTask.action\",\"processname\":\"年计划审批流程\",\"categoryWithoutCascade\":null,\"descriptionWithoutCascade\":null,\"applyUserId\":\"${applyUserId}\",\"ywname\":\"2013年开拓计划审批\",\"success\":true},{\"errorMessage\":\"\",\"tenantId\":null,\"createTime\":\"2016-09-18  09:29:16\",\"parentTaskIdWithoutCascade\":null,\"formKey\":null,\"assigneeWithoutCascade\":\"0b6ed5ad3b774971b422272b20193b0a\",\"param\":null,\"query\":null,\"userid\":null,\"executionId\":null,\"delegationStateString\":null,\"taskDefinitionKeyWithoutCascade\":\"xmbjszgcs\",\"dueDateWithoutCascade\":null,\"ownerWithoutCascade\":null,\"mobilenodeaction\":null,\"mobileinfouri\":\"/mobile/ypdetail/completenodyTask.action\",\"nameWithoutCascade\":\"项目部技术总工程师\",\"id\":\"1010028\",\"revision\":null,\"processInstanceId\":\"1010021\",\"appusername\":null,\"bussinesskey\":\"9d4775114a534c5381eb66d42083ad2b\",\"processDefinitionId\":\"PM_NJHSPLC:3:950016\",\"priorityWithoutCascade\":\"50\",\"pager\":null,\"suspensionState\":null,\"uri\":\"/webpage/biz/pm/yearplan/process/main.jsp\",\"nodeaction\":\"/webpage/biz/pm/yearplan/completenodyTask.action\",\"processname\":\"年计划审批流程\",\"categoryWithoutCascade\":null,\"descriptionWithoutCascade\":null,\"applyUserId\":\"${applyUserId}\",\"ywname\":\"2015年开拓计划审批\",\"success\":true}]}";

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    private Dialog dialog;
    private PlanContent mContent;
    private MyDbsy dbsy;
    private MyDbsy myDbsy;
//    private DbsyLvNameAdapter adapter;
//    private DbsyLvInfoAdapter adapter2;

    private int mPage = 1; //页数
    private final static int time_out = 15000; //超时
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private int total;
    private boolean a = true;
    private boolean b = true;
    private Intent intent;
    private String transact_url; //办理url
    private String taskId;
    private String taskDefinitionkey;
    private String bussinessid;
    private String processInstanceId;

    //参数的集合
    private ArrayList<String> mlist;
    private ArrayList<String> taskid_list;
    private ArrayList<String> bussinessid_list;
    private ArrayList<String> processInstanceId_list;
    private TextView tv_name;
    private RelativeLayout dbsy_have_message;
    private RelativeLayout dbsy_have_nomessage;
    private int records;



    private static final int code = 10; //传给审批界面的请求码


    //修改后
    private ListView dbsy_listview;
    private ArrayList<MyDbsy> mDbsylist;
    private MyDbsyAdapter adapter;
    private String language;

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismiss();
                    b = true;
                    adapter.notifyDataSetChanged();
                    ShowToast.showShort(MyApprovalActivity.this, R.string.loading_more_message);
                    mPage++;
                    break;
                case 1:
                    dialog.dismiss();
                    ShowToast.showShort(MyApprovalActivity.this, R.string.network_error);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dai_ban_shi_yi);
        initView();
        setOnClick();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
//        OkHttpUtils
//                .get()
//                .url(url)
//                .addParams("access_token", user_token)
//                .addParams("page", "1")
//                .addParams("rows", "10")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        e.printStackTrace();
//                        ShowToast.showShort(MyApprovalActivity.this,R.string.network_error);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            dbsy_list.clear();
//                            dbsy2_list.clear();
//
//                            mlist.clear();
//                            taskid_list.clear();
//                            bussinessid_list.clear();
//                            processInstanceId_list.clear();
//
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
//                            total = Integer.parseInt(jsonObject.getString("total"));
//                            Log.d("aaaa", jsonArray.toString());
//
//                            for (int j = 0; j < jsonArray.length(); j++) {
//                                dbsy = new MyDbsy();
//                                if (jsonArray.optJSONObject(j).getString("assigneeWithoutCascade").equals("null") || jsonArray.optJSONObject(j).getString("assigneeWithoutCascade").equals("")) {
//                                    dbsy.setOne_content("签收");
//                                } else {
//                                    dbsy.setOne_content("办理");
//                                }
//                                dbsy_list.add(dbsy);
//
//                                //将参数添加到集合中
//                                mlist.add(jsonArray.optJSONObject(j).getString("mobilenodeaction"));
//                                taskid_list.add(jsonArray.optJSONObject(j).getString("id"));
//                                bussinessid_list.add(jsonArray.optJSONObject(j).getString("bussinesskey"));
//                                processInstanceId_list.add(jsonArray.optJSONObject(j).getString("processInstanceId"));
//
//
//                                dbsy_lv_goodname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                                        tv_name = (TextView) view.findViewById(R.id.tv_name);
//                                        if (tv_name.getText().toString().equals("签收")) {
//                                            new Thread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    OkHttpUtils
//                                                            .get()
//                                                            .url(qs_url)
//                                                            .addParams("access_token", user_token)
//                                                            .addParams("taskId", taskid_list.get(position))
//                                                            .build()
//                                                            .execute(new StringCallback() {
//                                                                @Override
//                                                                public void onError(Request request, Exception e) {
//                                                                    e.printStackTrace();
//                                                                }
//
//                                                                @Override
//                                                                public void onResponse(String response) {
//                                                                    Log.d(TAG, response);
//                                                                    tv_name.setText("办理");
//                                                                    try {
//                                                                        JSONObject result = new JSONObject(response);
//                                                                        String message = result.getString("errormessage");
//                                                                        ShowToast.showShort(MyApprovalActivity.this, message);
//                                                                    } catch (JSONException e) {
//                                                                        e.printStackTrace();
//                                                                    }
//                                                                }
//                                                            });
//                                                }
//                                            }).start();
//                                        } else {
//                                            if (mlist.get(position) == null || mlist.get(position).equals("null") || mlist.get(position).equals("")) {
//                                                ShowToast.showShort(MyApprovalActivity.this, R.string.mine_no_development);
//                                            } else {
//                                                transact_url = MyHttpUtils.ApiBl() + mlist.get(position);
//
//                                                Log.d(TAG, position + "");
//                                                intent = new Intent(MyApprovalActivity.this, TransactDbsy.class);
//                                                intent.putExtra("transact_url", transact_url);
//                                                intent.putExtra("taskId", taskid_list.get(position));
//                                                intent.putExtra("bussinessid", bussinessid_list.get(position));
//                                                intent.putExtra("processInstanceId", processInstanceId_list.get(position));
//                                                startActivityForResult(intent, code);
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//
//                            for (int k = 0; k < jsonArray.length(); k++) {
//                                try {
//                                    map = new HashMap<String, String>();
//                                    for (int p = 1; p < arr_title.length; p++) {
//                                        if (jsonArray.optJSONObject(k).getString(arr_content[p]).equals("null") || jsonArray.optJSONObject(k).getString(arr_content[p]).equals("")) {
//                                            map.put(arr_title[p], "");
//                                        } else {
//                                            map.put(arr_title[p], jsonArray.optJSONObject(k).getString(arr_content[p]));
//                                        }
//                                    }
//                                    dbsy2_list.add(map);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            adapter.uPdate(dbsy_list);
//                            adapter2.mUpData(dbsy2_list);
//                            combination(dbsy_lv_goodname, dbsy_good_info, dbsy_sv_title, dbsy_good_detail);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }


//                    }
//                });

        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("page", "1")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyApprovalActivity.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        mDbsylist.clear();
                        mlist.clear();
                        taskid_list.clear();
                        bussinessid_list.clear();
                        processInstanceId_list.clear();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            total = Integer.parseInt(jsonObject.getString("total"));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                //将参数添加到集合中
                                mlist.add(jsonArray.optJSONObject(i).getString("mobilenodeaction"));
                                taskid_list.add(jsonArray.optJSONObject(i).getString("id"));
                                bussinessid_list.add(jsonArray.optJSONObject(i).getString("bussinesskey"));
                                processInstanceId_list.add(jsonArray.optJSONObject(i).getString("processInstanceId"));

                                //赋值
                                myDbsy = new MyDbsy();
                                myDbsy.setAssigneeWithoutCascade(jsonArray.optJSONObject(i).getString("assigneeWithoutCascade"));
                                myDbsy.setContent1(jsonArray.optJSONObject(i).getString(arr_content[0]));
                                myDbsy.setContent2(jsonArray.optJSONObject(i).getString(arr_content[1]));
                                myDbsy.setContent3(jsonArray.optJSONObject(i).getString(arr_content[2]));
                                myDbsy.setContent4(jsonArray.optJSONObject(i).getString(arr_content[3]));
                                myDbsy.setContent5(jsonArray.optJSONObject(i).getString(arr_content[4]));
                                myDbsy.setContent6(jsonArray.optJSONObject(i).getString(arr_content[5]));
                                mDbsylist.add(myDbsy);
                            }
                            adapter.uPdate(mDbsylist);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

        }
    }

    @Override
    protected void initView() {
        my_dbsy_back = (ImageView) findViewById(R.id.my_dbsy_back);
//        dbsy_tv_name = (TextView) findViewById(R.id.dbsy_tv_name);
//        dbsy_title_ll = (LinearLayout) findViewById(R.id.dbsy_title_ll);
//        dbsy_lv_goodname = (ListView) findViewById(R.id.dbsy_lv_goodname);
//        dbsy_good_info = (ListView) findViewById(R.id.dbsy_good_info);
//        dbsy_sv_title = (NoScrollHorizontalScrollView) findViewById(R.id.dbsy_sv_title);
//        dbsy_good_detail = (LinkedHorizontalScrollView) findViewById(R.id.dbsy_good_detail);
//
//        dbsy_have_message = (RelativeLayout) findViewById(R.id.dbsy_have_message);
        dbsy_have_nomessage = (RelativeLayout) findViewById(R.id.dbsy_have_nomessage);
        //新修改的
        dbsy_listview = (ListView) findViewById(R.id.dbsy_listview);


        url = MyHttpUtils.ApiDbsy();
        qs_url = MyHttpUtils.ApiClaim();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        language = sp.getString("mylanguage",null);
        editor.commit();


        // 获得屏幕宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        height_px = dip2px(this, 70);
        mOkhttp();
//        combination(dbsy_lv_goodname, dbsy_good_info, dbsy_sv_title, dbsy_good_detail);
        mScoll();
    }

    //滑动监听
    private void mScoll() {
        dbsy_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = dbsy_listview.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                         Log.d("ListView", "<----滚动到顶部----->");
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = dbsy_listview.getChildAt(dbsy_listview.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == dbsy_listview.getHeight()) {
                        if (mPage > total) {
                            if (a) {
                                ShowToast.showShort(MyApprovalActivity.this, R.string.nomore);
                                a = false;
                            }
                        } else {
                            if (b) {
                                b = false;
                                AddMore();
                            }
                        }
                    }
                }
            }
        });
    }


    //调用接口数据
    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("page", mPage + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        ShowToast.showShort(MyApprovalActivity.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
//                        mlist = new ArrayList<String>();
//                        taskid_list = new ArrayList<String>();
//                        bussinessid_list = new ArrayList<String>();
//                        processInstanceId_list = new ArrayList<String>();
//
//                        try {
//                            jsonObject = new JSONObject(response);
//                            jsonArray = jsonObject.getJSONArray("cells");
//                            total = Integer.parseInt(jsonObject.getString("total"));
//
//                            if (jsonArray.length() == 0) {
//                                dbsy_have_message.setVisibility(View.GONE);
//                                dbsy_have_nomessage.setVisibility(View.VISIBLE);
//                            } else {
//                                dbsy_have_message.setVisibility(View.VISIBLE);
//                                dbsy_have_nomessage.setVisibility(View.GONE);
//                                //动态添加列名
//                                dbsy_tv_name.setText(arr_title[0]);
//                                dbsy_tv_name.setTextSize(13);
//                                dbsy2_list = new ArrayList<Map<String, String>>();
//                                for (int i = 1; i < arr_title.length; i++) {
//                                    tv = new TextView(MyApprovalActivity.this);
//                                    tv.setText(arr_title[i]);
//                                    tv.setLayoutParams(new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    tv.setTextSize(13);
//                                    tv.setGravity(Gravity.CENTER);
//                                    tv.setBackgroundResource(R.color.blue_deep);
//                                    tv.setTextColor(Color.WHITE);
//                                    dbsy_title_ll.addView(tv);
//                                }
//                                //填充第一个listview的内容
//                                dbsy_list = new ArrayList<MyDbsy>();
//                                addOneList();
//
//                                adapter = new DbsyLvNameAdapter(MyApprovalActivity.this, dbsy_list);
//                                dbsy_lv_goodname.setAdapter(adapter);
//
//                                //填充第二个listview
//                                addTowList();
//                                adapter2 = new DbsyLvInfoAdapter(MyApprovalActivity.this, dbsy2_list, arr_title.length, width, height, arr_title, height_px);
//                                dbsy_good_info.setAdapter(adapter2);
//                                mPage = mPage + 1;
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("cells");
                            total = Integer.parseInt(jsonObject.getString("total"));
                            records = Integer.parseInt(jsonObject.getString("records"));

                            if (records > 0){
                                mDbsylist = new ArrayList<MyDbsy>();
                                mlist = new ArrayList<String>();
                                taskid_list = new ArrayList<String>();
                                bussinessid_list = new ArrayList<String>();
                                processInstanceId_list = new ArrayList<String>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //将参数添加到集合中
                                    mlist.add(jsonArray.optJSONObject(i).getString("mobilenodeaction"));
                                    taskid_list.add(jsonArray.optJSONObject(i).getString("id"));
                                    bussinessid_list.add(jsonArray.optJSONObject(i).getString("bussinesskey"));
                                    processInstanceId_list.add(jsonArray.optJSONObject(i).getString("processInstanceId"));

                                    //赋值
                                    myDbsy = new MyDbsy();
                                    myDbsy.setAssigneeWithoutCascade(jsonArray.optJSONObject(i).getString("assigneeWithoutCascade"));
                                    myDbsy.setContent1(jsonArray.optJSONObject(i).getString(arr_content[0]));
                                    myDbsy.setContent2(jsonArray.optJSONObject(i).getString(arr_content[1]));
                                    myDbsy.setContent3(jsonArray.optJSONObject(i).getString(arr_content[2]));
                                    myDbsy.setContent4(jsonArray.optJSONObject(i).getString(arr_content[3]));
                                    myDbsy.setContent5(jsonArray.optJSONObject(i).getString(arr_content[4]));
                                    myDbsy.setContent6(jsonArray.optJSONObject(i).getString(arr_content[5]));
                                    mDbsylist.add(myDbsy);
                                }
                                adapter = new MyDbsyAdapter(MyApprovalActivity.this, mDbsylist, mlist, taskid_list, bussinessid_list, processInstanceId_list,language);
                                dbsy_listview.setAdapter(adapter);
                                mPage = mPage + 1;
                            }else{
                                dbsy_have_nomessage.setVisibility(View.VISIBLE);
                                dbsy_listview.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    //上拉加载更多
    private void AddMore() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("page", mPage + "")
                .addParams("rows", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(1, time_out);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("cells");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //将参数添加到集合中
                                mlist.add(jsonArray.optJSONObject(i).getString("mobilenodeaction"));
                                taskid_list.add(jsonArray.optJSONObject(i).getString("id"));
                                bussinessid_list.add(jsonArray.optJSONObject(i).getString("bussinesskey"));
                                processInstanceId_list.add(jsonArray.optJSONObject(i).getString("processInstanceId"));

                                //赋值
                                myDbsy = new MyDbsy();
                                myDbsy.setAssigneeWithoutCascade(jsonArray.optJSONObject(i).getString("assigneeWithoutCascade"));
                                myDbsy.setContent1(jsonArray.optJSONObject(i).getString(arr_content[0]));
                                myDbsy.setContent2(jsonArray.optJSONObject(i).getString(arr_content[1]));
                                myDbsy.setContent3(jsonArray.optJSONObject(i).getString(arr_content[2]));
                                myDbsy.setContent4(jsonArray.optJSONObject(i).getString(arr_content[3]));
                                myDbsy.setContent5(jsonArray.optJSONObject(i).getString(arr_content[4]));
                                myDbsy.setContent6(jsonArray.optJSONObject(i).getString(arr_content[5]));
                                mDbsylist.add(myDbsy);
                            }
                            dialog = DialogUtil.createLoadingDialog(MyApprovalActivity.this, R.string.loading);
                            handler.sendEmptyMessageDelayed(0, 1500);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    //填充第一列list
    private void addOneList() {
        try {
            for (int j = 0; j < jsonArray.length(); j++) {
                dbsy = new MyDbsy();
                if (jsonArray.optJSONObject(j).getString("assigneeWithoutCascade").equals("null") || jsonArray.optJSONObject(j).getString("assigneeWithoutCascade").equals("")) {
                    dbsy.setOne_content("签收");
                } else {
                    dbsy.setOne_content("办理");
                }
                dbsy_list.add(dbsy);
                //将参数添加到集合中
                mlist.add(jsonArray.optJSONObject(j).getString("mobilenodeaction"));
                taskid_list.add(jsonArray.optJSONObject(j).getString("id"));
                bussinessid_list.add(jsonArray.optJSONObject(j).getString("bussinesskey"));
                processInstanceId_list.add(jsonArray.optJSONObject(j).getString("processInstanceId"));


                dbsy_lv_goodname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        tv_name = (TextView) view.findViewById(R.id.tv_name);
                        if (tv_name.getText().toString().equals("签收")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    OkHttpUtils
                                            .get()
                                            .url(qs_url)
                                            .addParams("access_token", user_token)
                                            .addParams("taskId", taskid_list.get(position))
                                            .build()
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Request request, Exception e) {
                                                    e.printStackTrace();
                                                    ShowToast.showShort(MyApprovalActivity.this, "请检查网络");
                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d(TAG, response);
                                                    tv_name.setText("办理");
                                                    try {
                                                        JSONObject result = new JSONObject(response);
                                                        String message = result.getString("errormessage");
                                                        ShowToast.showShort(MyApprovalActivity.this, message);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }).start();
                        } else {
                            if (mlist.get(position) == null || mlist.get(position).equals("null") || mlist.get(position).equals("")) {
                                ShowToast.showShort(MyApprovalActivity.this, "暂未开放");
                            } else {
                                transact_url = MyHttpUtils.ApiBl() + mlist.get(position);

                                Log.d(TAG, position + "");
                                intent = new Intent(MyApprovalActivity.this, TransactDbsy.class);
                                intent.putExtra("transact_url", transact_url);
                                intent.putExtra("taskId", taskid_list.get(position));
                                intent.putExtra("bussinessid", bussinessid_list.get(position));
                                intent.putExtra("processInstanceId", processInstanceId_list.get(position));
                                startActivity(intent);
                            }
                        }


                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //填充第二列list
    private void addTowList() {
        for (int k = 0; k < jsonArray.length(); k++) {
            try {
                map = new HashMap<String, String>();
                for (int p = 1; p < arr_title.length; p++) {
                    if (jsonArray.optJSONObject(k).getString(arr_content[p]).equals("null") || jsonArray.optJSONObject(k).getString(arr_content[p]).equals("")) {
                        map.put(arr_title[p], "");
                    } else {
                        map.put(arr_title[p], jsonArray.optJSONObject(k).getString(arr_content[p]));
                    }
                }
                dbsy2_list.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void combination(final ListView lvName, final ListView lvDetail, final HorizontalScrollView title, LinkedHorizontalScrollView content) {
        /**
         * 左右滑动同步
         */
        content.setMyScrollChangeListener(new LinkedHorizontalScrollView.LinkScrollChangeListener() {
            @Override
            public void onscroll(LinkedHorizontalScrollView view, int x, int y, int oldx, int oldy) {
                title.scrollTo(x, y);
            }
        });

        /**
         * 上下滑动同步
         */
        // 禁止快速滑动
        lvName.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        lvDetail.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        //左侧ListView滚动时，控制右侧ListView滚动
        lvName.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //这两个enable标志位是为了避免死循环
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isRightListEnabled = false;
                    isLeftListEnabled = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isRightListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View child = view.getChildAt(0);
                if (child != null && isLeftListEnabled) {
                    lvDetail.setSelectionFromTop(firstVisibleItem, child.getTop());
                }

                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = dbsy_lv_goodname.getChildAt(dbsy_lv_goodname.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == dbsy_lv_goodname.getHeight()) {
                        if (mPage > total) {
                            if (a) {
                                ShowToast.showShort(MyApprovalActivity.this, "无更多数据加载");
                                a = false;
                            }
                        } else {
                            if (b) {
                                b = false;
                                OkHttpUtils
                                        .get()
                                        .url(url)
                                        .addParams("access_token", user_token)
                                        .addParams("page", mPage + "")
                                        .addParams("rows", "10")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Request request, Exception e) {
                                                e.printStackTrace();
                                                handler.sendEmptyMessageDelayed(1, time_out);
                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    jsonObject = new JSONObject(response);
                                                    jsonArray = jsonObject.getJSONArray("cells");

                                                    //加载更多第一个listview的内容
                                                    for (int j = 0; j < jsonArray.length(); j++) {
                                                        dbsy = new MyDbsy();
                                                        if (jsonArray.optJSONObject(j).getString("assigneeWithoutCascade").equals("null") || jsonArray.optJSONObject(j).getString("assigneeWithoutCascade").equals("")) {
                                                            dbsy.setOne_content("签收");
                                                        } else {
                                                            dbsy.setOne_content("办理");
                                                        }
                                                        dbsy_list.add(dbsy);
                                                        //加载更多后需添加到list集合中
                                                        mlist.add(jsonArray.optJSONObject(j).getString("mobilenodeaction"));
                                                        taskid_list.add(jsonArray.optJSONObject(j).getString("id"));
                                                        bussinessid_list.add(jsonArray.optJSONObject(j).getString("bussinesskey"));
                                                        processInstanceId_list.add(jsonArray.optJSONObject(j).getString("processInstanceId"));

                                                    }

                                                    //填充第二个listview
                                                    for (int k = 0; k < jsonArray.length(); k++) {
                                                        map = new HashMap<String, String>();
                                                        for (int p = 1; p < arr_title.length; p++) {
                                                            if (jsonArray.optJSONObject(k).getString(arr_content[p]).equals("null") || jsonArray.optJSONObject(k).getString(arr_content[p]).equals("")) {
                                                                map.put(arr_title[p], "");
                                                            } else {
                                                                map.put(arr_title[p], jsonArray.optJSONObject(k).getString(arr_content[p]));
                                                            }
                                                        }
                                                        dbsy2_list.add(map);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                dialog = DialogUtil.createLoadingDialog(MyApprovalActivity.this, "正在加载");
                                handler.sendEmptyMessageDelayed(0, 1500);
                            }

                        }


                    }
                }
            }
        });

        //右侧ListView滚动时，控制左侧ListView滚动
        lvDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLeftListEnabled = false;
                    isRightListEnabled = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isLeftListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isRightListEnabled) {
                    lvName.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });
    }


    @Override
    protected void setOnClick() {
        my_dbsy_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_dbsy_back:
                finish();
                break;
        }
    }
}
