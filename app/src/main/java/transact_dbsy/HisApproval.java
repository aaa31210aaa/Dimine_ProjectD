package transact_dbsy;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.administrator.dimine_projectd.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import utils.DialogUtil;
import utils.ShowToast;

public class HisApproval extends Fragment implements View.OnClickListener {
    private static final String TAG = "HisApproval";
    private View view;
    private Spinner his_approval_spinner; // 下拉控件
    private EditText his_approval_edtv;  // 审批意见
    private Button his_approval_submit;  //提交按钮
    private String[] arr;


    private String user_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String url;
    private String url_submit;
    private String taskId;
    private String bussinessid;
    private String processInstanceId;
    private String keys;
    private String types;
    private String value;
    private String m;
    private String back;
    private Dialog dialog;
    private Intent intent;
    private String message;


    public HisApproval() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_his_approval, null);
            initView();
            setOnclick();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    ShowToast.showShort(getActivity(), back);
                    break;
                case 1:
                    dialog.dismiss();
                    ShowToast.showShort(getActivity(), R.string.approval_success);
                    intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                    break;
                default:

                    break;
            }
        }
    };


    private void initView() {
        his_approval_spinner = (Spinner) view.findViewById(R.id.his_approval_spinner);
        his_approval_submit = (Button) view.findViewById(R.id.his_approval_submit);
        his_approval_edtv = (EditText) view.findViewById(R.id.his_approval_edtv);
        Intent intent = getActivity().getIntent();
        url = intent.getStringExtra("transact_url");


        taskId = intent.getStringExtra("taskId");
        bussinessid = intent.getStringExtra("bussinessid");
        processInstanceId = intent.getStringExtra("processInstanceId");
        keys = "nodepass" + "," + "comments";
        types = "B" + "," + "S";
        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        editor.commit();

        mSpinner();
    }


    //初始化数据
//    private Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            OkHttpUtils
//                    .get()
//                    .url(url)
//                    .addParams("access_token", user_token)
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Request request, Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d(TAG, response);
//
//                        }
//                    });
//        }
//    });


    //监听下拉控件选择的哪一个
    private void mSpinner() {
        his_approval_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arr = getResources().getStringArray(R.array.languages);

                if (arr[position].equals("同意") || arr[position].equals("agree")) {
                    m = "true";
                } else {
                    m = "false";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOnclick() {
        his_approval_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.his_approval_submit:
                if (his_approval_edtv.getText().toString().equals("")) {
                    ShowToast.showShort(getActivity(), R.string.not_filling_opinion);
                } else {
                    mOkhttp();
                }

                break;
        }
    }

    private void mOkhttp() {
        value = m + "," + his_approval_edtv.getText().toString();
        Log.e(TAG, value + "---" + user_token + "---" + keys + "---" + types + "---" + taskId + "---" + bussinessid + "---" + processInstanceId);

        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("variable.keys", keys)
                .addParams("variable.values", value)
                .addParams("variable.types", types)
                .addParams("taskId", taskId)
                .addParams("bussinessid", bussinessid)
                .addParams("processInstanceId", processInstanceId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            message = jsonObject.getString("errormessage");
//                            Log.e(TAG, message);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.approving);
                        handler.sendEmptyMessageDelayed(1, 1500);
                    }
                });

    }
}
