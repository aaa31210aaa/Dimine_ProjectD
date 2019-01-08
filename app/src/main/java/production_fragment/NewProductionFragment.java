package production_fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.NewProductionJl;
import com.example.administrator.dimine_projectd.R;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import utils.DialogUtil;
import utils.MyHttpUtils;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewProductionFragment";
    private View view;
    private View view_bc;
    private View view_sb;
    private View view_gy;

    private Intent intent;
    private TextView new_production_etv_bz; // 班组
    private TextView new_production_tv_bc; //班次
    private ImageView new_production_img_bc;
    private TextView new_production_date; //日期
    private TextView new_production_jl;  //作业地点
    private TextView new_production_device; //设备
    private TextView new_production_gy;  //工艺
    private EditText new_production_beizhu; //备注

    private ImageView new_production_jl_img;
    private ImageView new_production_submit;
    private boolean cdj = true;

    //    {"success":true,"errormessage":"","cells":[{"serialno":"1","targetname":"指标一","datatype":"SJLX002","hqdatatype":"HQSJLX001","isedit":"1","isevent":"0","ismust":"1","talgorithm":"","targetunitname":"万元"},{"serialno":"2","targetname":"指标二","datatype":"SJLX002","hqdatatype":"HQSJLX001","isedit":"1","isevent":"0","ismust":"1","talgorithm":"column_2=column_1*column_3","targetunitname":"万元"},{"serialno":"3","targetname":"指标三","datatype":"SJLX003","hqdatatype":"HQSJLX001","isedit":"1","isevent":"0","ismust":"1","talgorithm":"","targetunitname":"万元"},{"serialno":"4","targetname":"指标四","datatype":"SJLX001","hqdatatype":"HQSJLX001","isedit":"1","isevent":"0","ismust":"1","talgorithm":"","targetunitname":"万元"}]}
    private String json = "{'success':true,'errormessage':'','titles':['实际进尺','计划进尺','计划工程量'],'cells':[]}";
    private String json2 = " {\"success\":true,\"errormessage\":\"\",\"cells\":[{\"serialno\":\"1\",\"targetname\":\"指标一\",\"datatype\":\"SJLX002\",\"hqdatatype\":\"HQSJLX001\",\"isedit\":\"1\",\"isevent\":\"1\",\"ismust\":\"1\",\"talgorithm\":\"\",\"targetunitname\":\"万元\"},{\"serialno\":\"2\",\"targetname\":\"指标二\",\"datatype\":\"SJLX002\",\"hqdatatype\":\"HQSJLX001\",\"isedit\":\"1\",\"isevent\":\"1\",\"ismust\":\"1\",\"talgorithm\":\"column_2=column_1*column_3\",\"targetunitname\":\"万元\"},{\"serialno\":\"3\",\"targetname\":\"指标三\",\"datatype\":\"SJLX003\",\"hqdatatype\":\"HQSJLX001\",\"isedit\":\"1\",\"isevent\":\"0\",\"ismust\":\"1\",\"talgorithm\":\"\",\"targetunitname\":\"万元\"},{\"serialno\":\"4\",\"targetname\":\"指标四\",\"datatype\":\"SJLX001\",\"hqdatatype\":\"HQSJLX001\",\"isedit\":\"0\",\"isevent\":\"0\",\"ismust\":\"1\",\"talgorithm\":\"\",\"targetunitname\":\"万元\"}]}";

    private String gy_json = "{\"success\":true,\"errormessage\":\"\",\"cells\":[{\"techid\":\"BC001\",\"techname\":\"工艺1\"},{\"techid\":\"BC002\",\"techname\":\"工艺2\"},{\"techid\":\"BC003\",\"techname\":\"工艺3\"}]}";

    private Calendar calendar;
    private static final int RESULT_OK = -1;
    private static final int A = 10; //传给进路选择的请求码
    private String url;
    private String bc_url;
    private String sb_url;
    private String gy_url;
    private String zydd_url;
    private String add_url;

    private Gson gson;
    private String jsonStr;

    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String[] dt_text;

    private int width;//屏幕宽度
    private int height;//屏幕高度

    private String[] num;

    private Map<String, Object> map = new HashMap<>();
    private ArrayList<String> add_list = new ArrayList<String>();

    private PopupWindow popupWindow_bc;  //班次的弹窗
    private PopupWindow popupWindow_sb;  //设备的弹窗
    private PopupWindow popupWindow_gy;  //工艺的弹窗

    private String classid;  //班次id
    private String clickid;   //工序id
    private String techid; //工艺id

    private String teamgroupid; //班组id
    private String projectid; //作业地点id
    private String devid; //设备id

    private JSONArray gx_jsonArray;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Context context;
    private EditText editText;
    private LinearLayout new_production_zdy_ll;

    private Dialog dh_dialog;

    private String str;

    public NewProductionFragment() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    getActivity().recreate();
                    handler.sendEmptyMessageDelayed(1, 2000);
                    break;
                case 1:
                    dh_dialog.dismiss();
                    ShowToast.showShort(context, "新增完成");
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            view = View.inflate(getActivity(), R.layout.fragment_new_production, null);
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
        x.view().inject(getActivity());
        context = getActivity();

        calendar = Calendar.getInstance();
        new_production_etv_bz = (TextView) view.findViewById(R.id.new_production_etv_bz);
        new_production_tv_bc = (TextView) view.findViewById(R.id.new_production_tv_bc);
//        new_production_img_bc = (ImageView) view.findViewById(R.id.new_production_img_bc);
        new_production_date = (TextView) view.findViewById(R.id.new_production_date);
        int sj_month = calendar.get(Calendar.MONTH) + 1;
        new_production_date.setText(calendar.get(Calendar.YEAR) + "-" + sj_month + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        new_production_jl = (TextView) view.findViewById(R.id.new_production_jl);
//        new_production_jl_img = (ImageView) view.findViewById(R.id.new_production_jl_img);
        new_production_submit = (ImageView) view.findViewById(R.id.new_production_submit);

        new_production_device = (TextView) view.findViewById(R.id.new_production_device);
        new_production_gy = (TextView) view.findViewById(R.id.new_production_gy);

        new_production_beizhu = (EditText) view.findViewById(R.id.new_production_beizhu);


        // 获得屏幕分辨率
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


//        new_production_juejin = (EditText) view.findViewById(R.id.new_production_juejin);
//        new_production_jjjc = (EditText) view.findViewById(R.id.new_production_jjjc);

//        new_production_juejin.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
//        new_production_jjjc.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        view_bc = View.inflate(getActivity(), R.layout.new_production_bc_item, null);
        view_sb = View.inflate(getActivity(), R.layout.new_production_sb_item, null);
        view_gy = View.inflate(getActivity(), R.layout.new_production_gy_item, null);

        //班次选择
//        new_production_bc_item_morning = (TextView) view_bc.findViewById(R.id.new_production_bc_item_morning);
//        new_production_bc_item_noon = (TextView) view_bc.findViewById(R.id.new_production_bc_item_noon);
//        new_production_bc_item_night = (TextView) view_bc.findViewById(R.id.new_production_bc_item_night);


        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);

        Intent intent = getActivity().getIntent();
        clickid = intent.getStringExtra("clickid");
        String teamname = intent.getStringExtra("teamname");
        teamgroupid = intent.getStringExtra("teamgroupid");

        new_production_etv_bz.setText(teamname);

        Log.d("NewProductionFragment", clickid);
        url = MyHttpUtils.ApiProductionByGx();
        bc_url = MyHttpUtils.ApiProductionByBc();
        sb_url = MyHttpUtils.ApiProductionDeviceByTeam();
        gy_url = MyHttpUtils.ApiSelectGyByGxAndProject();
        zydd_url = MyHttpUtils.ApiSelectProjectByGx();
        add_url = MyHttpUtils.ApiAddProductionBill();


        new_production_zdy_ll = (LinearLayout) view.findViewById(R.id.new_production_zdy_ll);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(url)
                        .addParams("access_token", user_token)
                        .addParams("procid", clickid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                                ShowToast.showShort(getActivity(),"请检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("NewProductionFragment", response);
                                try {
                                    JSONObject jsb = new JSONObject(response);
                                    gx_jsonArray = jsb.getJSONArray("cells");
                                    dt_text = new String[gx_jsonArray.length()];
                                    num = new String[gx_jsonArray.length()];


                                    for (int i = 0; i < gx_jsonArray.length(); i++) {
                                        num[i] = gx_jsonArray.optJSONObject(i).getString("serialno");
//                                LinearLayout layout = new LinearLayout(getActivity());
//                                layout.setOrientation(LinearLayout.HORIZONTAL);
//                                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        final int dt_tag = i;
                                        //创建文本框
                                        editText = new EditText(getActivity());
                                        editText.setId(i);
                                        editText.setHint(gx_jsonArray.optJSONObject(i).getString("targetname") + "              单位(" + gx_jsonArray.optJSONObject(i).getString("targetunitname") + ")")
                                        ;

                                        editText.setHintTextColor(Color.rgb(211, 211, 211));
                                        editText.setBackgroundResource(R.drawable.editext_bk);
                                        editText.setPadding(50, 50, 50, 50);
                                        editText.setLayoutParams(new LinearLayout.LayoutParams(width, 150));
                                        if (gx_jsonArray.optJSONObject(i).getString("datatype").equals("SJLX002")) {
                                            editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                                        }
                                        editText.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }


                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            }
                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                dt_text[dt_tag] = editText.getText().toString();
                                                map.put("column_" + num[dt_tag], dt_text[dt_tag]);
                                                add_list.add(dt_text[dt_tag]);
//                                      Log.d("ggggg", map.get(num[dt_tag]));
                                            }


                                        });

                                        new_production_zdy_ll.addView(editText);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        }).start();


//        try {
//            JSONObject jsb = new JSONObject(json);
//            JSONArray jsonArray = jsb.getJSONArray("titles");
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                //自定义文本框
//                EditText editText = new EditText(getActivity());
//                editText.setWidth(width);
//                editText.setHeight(100);
//                editText.setHint(jsonArray.getString(i));
//                editText.setBackgroundResource(R.drawable.tab_horizontal_item_d);
//                editText.setPadding(50, 50, 50, 50);
////                editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//
//                new_production_zdy_ll.addView(editText);
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        /**
         * 动态加载班次视图
         */

        /**
         * 新建popupwindow对象
         */
        popupWindow_bc = new PopupWindow(view_bc, -2, -2);
        /**
         * 给popupwindow设置焦点
         */
        popupWindow_bc.setFocusable(true);
        /**
         * 设置popupwindow点击外部消失
         */
        popupWindow_bc.setOutsideTouchable(true);
        popupWindow_bc.setBackgroundDrawable(new BitmapDrawable(getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        popupWindow_bc.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        new Thread(new Runnable() {
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
                                ShowToast.showShort(getActivity(),"请检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                LinearLayout production_bc_item_ll = (LinearLayout) view_bc.findViewById(R.id.production_bc_item_ll);
                                LinearLayout bc_layout;
                                Log.d("NewProductionFragment", response);
                                try {
                                    JSONObject jsb = new JSONObject(response);
                                    final JSONArray jsonArray = jsb.getJSONArray("cells");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        final int pop_id = i;
                                        bc_layout = new LinearLayout(getActivity());
                                        bc_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        bc_layout.setBackgroundResource(R.drawable.pop_ybs);
                                        final TextView textView = new TextView(getActivity());
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(width / 2 - 40, 100));
                                        textView.setText(jsonArray.optJSONObject(i).getString("classname"));
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupWindow_bc.dismiss();
                                                try {
                                                    new_production_tv_bc.setText(jsonArray.optJSONObject(pop_id).getString("classname"));
                                                    classid = jsonArray.optJSONObject(pop_id).getString("classid");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        ImageView imageView = new ImageView(getActivity());
                                        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                                        imageView.setPadding(5, 0, 5, 0);
                                        imageView.setBackgroundColor(Color.BLACK);
                                        bc_layout.addView(textView);
                                        production_bc_item_ll.addView(bc_layout);
                                        production_bc_item_ll.addView(imageView);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

            }
        }).start();


        /**
         * 动态加载设备
         */

        popupWindow_sb = new PopupWindow(view_sb, -2, -2);

        /**
         * 给popupwindow设置焦点
         */
        popupWindow_sb.setFocusable(true);
        /**
         * 设置popupwindow点击外部消失
         */
        popupWindow_sb.setOutsideTouchable(true);
        popupWindow_sb.setBackgroundDrawable(new BitmapDrawable(getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        popupWindow_sb.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(sb_url)
                        .addParams("access_token", user_token)
                        .addParams("procid", clickid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                                ShowToast.showShort(getActivity(),"请检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("NewProductionFragment", response);
                                LinearLayout production_sb_item_ll = (LinearLayout) view_sb.findViewById(R.id.production_sb_item_ll);
                                LinearLayout sb_layout;
                                try {
                                    JSONObject jsb = new JSONObject(response);
                                    final JSONArray jsonArray = jsb.getJSONArray("cells");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        final int pop_sb_id = i;
                                        sb_layout = new LinearLayout(getActivity());
                                        sb_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        sb_layout.setBackgroundResource(R.drawable.pop_ybs);
                                        final TextView textView = new TextView(getActivity());
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(width / 2 - 40, 100));
                                        textView.setText(jsonArray.optJSONObject(i).getString("devname"));
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupWindow_sb.dismiss();
                                                try {
                                                    new_production_device.setText(jsonArray.optJSONObject(pop_sb_id).getString("devname"));
                                                    devid = jsonArray.optJSONObject(pop_sb_id).getString("devid");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        ImageView imageView = new ImageView(getActivity());
                                        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                                        imageView.setPadding(5, 0, 5, 0);
                                        imageView.setBackgroundColor(Color.BLACK);
                                        sb_layout.addView(textView);
                                        production_sb_item_ll.addView(sb_layout);
                                        production_sb_item_ll.addView(imageView);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();


    }

    protected void setOnclick() {
        new_production_tv_bc.setOnClickListener(this);
        new_production_date.setOnClickListener(this);
        new_production_jl.setOnClickListener(this);
        new_production_submit.setOnClickListener(this);
        new_production_device.setOnClickListener(this);
        new_production_gy.setOnClickListener(this);
    }


//    void postReq() {
//        OkHttpClient client = new OkHttpClient();
//        gson = new Gson();
//        jsonStr = gson.toJson(map);
//        map.put("access_token", user_token);
//        map.put("reportdate", new_production_date.getText().toString());
//        map.put("classes", classid);
//        map.put("teamgroupid", teamgroupid);
//        map.put("projectid", projectid);
//        map.put("procid", clickid);
//        map.put("devid", devid);
//        map.put("techid", techid);
//
//        RequestBody body = RequestBody.create(JSON, jsonStr);
//        okhttp3.Request request = new Request.Builder().url(add_url).post(body).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("Tooooo", response + "");
//            }
//        });
//    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            //班次点击事件
            case R.id.new_production_tv_bc:
                /**
                 * 在某个视图的下面显示
                 */
                popupWindow_bc.showAsDropDown(v, 0, 0);
                break;

            case R.id.new_production_date:

                final DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        new_production_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;

            case R.id.new_production_jl:
                Log.d("NewProductionFragment", new_production_date.getText().toString() + "------" + classid + "------" + clickid + "-----" + teamgroupid);
                if (classid == null) {
                    ShowToast.showShort(getActivity(), "请先选择班次");
                } else {
                    intent = new Intent(getActivity(), NewProductionJl.class);
                    intent.putExtra("classid", classid);
                    intent.putExtra("reportdate", new_production_date.getText().toString());
                    intent.putExtra("clickid", clickid);
                    intent.putExtra("teamgroupid", teamgroupid);
                    startActivityForResult(intent, A);
                }

//                OkHttpUtils
//                        .get()
//                        .url(zydd_url)
//                        .addParams("access_token", user_token)
//                        .addParams("reportdate", new_production_date.getText().toString())
//                        .addParams("classes", classid)
//                        .addParams("procid",clickid)
//                        .addParams("teamgroupid",teamgroupid)
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Request request, Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("NewProductionFragment", response);
//                                if (classid == null){
//                                    ShowToast.showShort(getActivity(),"请先选择班次");
//                                }else{
//                                    intent = new Intent(getActivity(), NewProductionJl.class);
//                                    startActivityForResult(intent, A);
//                                }
//                            }
//                        });


                break;
            case R.id.new_production_device:
                popupWindow_sb.showAsDropDown(v, 0, 0);
                break;
            case R.id.new_production_gy:
                if (projectid == null) {
                    ShowToast.showShort(getActivity(), "请先选择作业地点");
                } else {
                    popupWindow_gy.showAsDropDown(v, 0, 0);
                }

//                OkHttpUtils
//                            .get()
//                            .url("http://192.168.5.228:9697/DMMES/mobile/productionbill/addProductionBill.action")
//                            .addParams("access_token", user_token)
////                            .addParams("str", gson.toJson(map))
//                            .build()
//                            .execute(new Callback() {
//                                @Override
//                                public Object parseNetworkResponse(Response response) throws IOException {
//                                    return null;
//                                }
//
//                                @Override
//                                public void onError(Request request, Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                @Override
//                                public void onResponse(Object response) {
//                                    Log.d("NewProductionFragment", response + "");
//                                }
//                            });


                break;

            //这里点击完成提交
            case R.id.new_production_submit:
                if (classid == null) {
                    ShowToast.showShort(getActivity(), "未选择班次");
                } else if (projectid == null) {
                    ShowToast.showShort(getActivity(), "未选择作业地点");
                } else if (devid == null) {
                    ShowToast.showShort(getActivity(), "未选择设备");
                } else if (techid == null) {
                    ShowToast.showShort(getActivity(), "未选择工艺");
                } else {
                    for (int j = 0; j < gx_jsonArray.length(); j++) {
                        if (dt_text[j] == null) {
                            ShowToast.showShort(getActivity(), "有未填写的指标内容");
                            return;
                        }
                    }


                    map.put("reportdate", new_production_date.getText().toString());
                    map.put("classes", classid);
                    map.put("teamgroupid", teamgroupid);
                    map.put("projectid", projectid);
                    map.put("procid", clickid);
                    map.put("devid", devid);
                    map.put("techid", techid);
                    gson = new Gson();
                    jsonStr = gson.toJson(map);

                    try {
                        jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

//                    try {
//                        str = URLEncoder.encode(str, "UTF-8");
//                        Log.d("aaaa", str);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }


                    Log.d("new_token", user_token);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpUtils
                                    .get()
                                    .url(add_url)
                                    .addParams("access_token", user_token)
                                    .addParams("str", jsonStr)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            e.printStackTrace();
                                            ShowToast.showShort(getActivity(),"请检查网络");
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("NewProductionFragment", response);
                                            dh_dialog = DialogUtil.createLoadingDialog(context, "请稍后");
                                            new_production_beizhu.setText("");
                                            new_production_beizhu.setHint("备注");
                                            handler.sendEmptyMessageDelayed(0, 1000);


                                        }
                                    });
                        }
                    }).start();

                }
                break;
        }
    }

    private void Updata() {
        new_production_tv_bc.setText(null);
        new_production_tv_bc.setHint("班次");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String back = bundle.getString("back");
            projectid = bundle.getString("selectid");
            new_production_jl.setText(back);
        }

        /**
         * 动态加载工艺
         */
        popupWindow_gy = new PopupWindow(view_gy, -2, -2);

        /**
         * 给popupwindow设置焦点
         */
        popupWindow_gy.setFocusable(true);
        /**
         * 设置popupwindow点击外部消失
         */
        popupWindow_gy.setOutsideTouchable(true);
        popupWindow_gy.setBackgroundDrawable(new BitmapDrawable(getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        popupWindow_gy.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
                        ShowToast.showShort(getActivity(), "请先选择作业地点");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("NewProductionFragment", response);
                        LinearLayout production_gy_item_ll = (LinearLayout) view_gy.findViewById(R.id.production_gy_item_ll);
                        LinearLayout gy_layout;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final int pop_id = i;
                                gy_layout = new LinearLayout(getActivity());
                                gy_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                gy_layout.setBackgroundResource(R.drawable.pop_ybs);
                                TextView textView = new TextView(getActivity());
                                textView.setLayoutParams(new LinearLayout.LayoutParams(width / 2 - 40, 100));
                                textView.setText(jsonArray.optJSONObject(i).getString("techname"));
                                textView.setGravity(Gravity.CENTER);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindow_gy.dismiss();
                                        try {
                                            new_production_gy.setText(jsonArray.optJSONObject(pop_id).getString("techname"));
                                            techid = jsonArray.optJSONObject(pop_id).getString("techid");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                ImageView imageView = new ImageView(getActivity());
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                                imageView.setPadding(5, 0, 5, 0);
                                imageView.setBackgroundColor(Color.BLACK);
                                gy_layout.addView(textView);
                                production_gy_item_ll.addView(gy_layout);
                                production_gy_item_ll.addView(imageView);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }
}
