package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;
import com.example.administrator.dimine_projectd.TransactDbsy;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entity.MyDbsy;
import utils.MyHttpUtils;
import utils.ShowToast;
import utils.ViewHolder;

public class MyDbsyAdapter extends BaseAdapter {
    private static final String TAG = "MyDbsyAdapter";
    private Context context;
    private ArrayList<MyDbsy> mDbsylist;
    private ArrayList<String> mlist;
    private ArrayList<String> taskid_list;
    private ArrayList<String> bussinessid_list;
    private ArrayList<String> processInstanceId_list;

    private String transact_url; //办理url
    private String qs_url; //签收url
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String language;

    private String user_token;
    private Intent intent;


    private String[] arr_title_zh = {"操作", "流程名单", "任务名称", "任务发起人", "当前节点", "任务创建日期"};
    private String[] arr_title_en ={"Operation","Process List","Task Name","Task Sponsor","Current Node","Task Creation Date"};


    public MyDbsyAdapter(Context context, ArrayList<MyDbsy> mDbsylist, ArrayList<String> mlist, ArrayList<String> taskid_list, ArrayList<String> bussinessid_list, ArrayList<String> processInstanceId_list,String language) {
        this.context = context;
        this.mDbsylist = mDbsylist;
        this.mlist = mlist;
        this.taskid_list = taskid_list;
        this.bussinessid_list = bussinessid_list;
        this.processInstanceId_list = processInstanceId_list;
        this.language = language;
    }

    @Override
    public int getCount() {
        return mDbsylist.size();
    }

    @Override
    public Object getItem(int position) {
        return mDbsylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.dbsy_listview_item, null);
        }
        TextView title1 = ViewHolder.get(convertView, R.id.dbsy_listview_item_title1);
        TextView title2 = ViewHolder.get(convertView, R.id.dbsy_listview_item_title2);
        TextView title3 = ViewHolder.get(convertView, R.id.dbsy_listview_item_title3);
        TextView title4 = ViewHolder.get(convertView, R.id.dbsy_listview_item_title4);
        TextView title5 = ViewHolder.get(convertView, R.id.dbsy_listview_item_title5);
        TextView title6 = ViewHolder.get(convertView, R.id.dbsy_listview_item_title6);
        final TextView tv = ViewHolder.get(convertView, R.id.dbsy_listview_item_operation_tv);

        qs_url = MyHttpUtils.ApiClaim();
        sp = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        editor = sp.edit();
        user_token = sp.getString("user_token", null);
        editor.commit();

        MyDbsy myDbsy = mDbsylist.get(position);

        if (myDbsy.getAssigneeWithoutCascade().equals("null")) {
            tv.setText(R.string.signed_for);
        } else {
            tv.setText(R.string.transact);
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getText().toString().equals("Signed")||tv.getText().toString().equals("签收")) {
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
                                    ShowToast.showShort(context, R.string.network_error);
                                }

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.e(TAG, response);
                                        tv.setText(R.string.transact);
                                        JSONObject result = new JSONObject(response);
                                        String message = result.getString("errormessage");
                                        ShowToast.showShort(context, message);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    if (mlist.get(position) == null || mlist.get(position).equals("null") || mlist.get(position).equals("")) {
                        ShowToast.showShort(context, R.string.yet_open);
                    } else {
                        transact_url = MyHttpUtils.ApiBl() + mlist.get(position);
                        intent = new Intent(context, TransactDbsy.class);
                        intent.putExtra("transact_url", transact_url);
                        intent.putExtra("taskId", taskid_list.get(position));
                        intent.putExtra("bussinessid", bussinessid_list.get(position));
                        intent.putExtra("processInstanceId", processInstanceId_list.get(position));
                        context.startActivity(intent);
                    }
                }
            }
        });

        if (language.equals("zh")){
            title1.setText(arr_title_zh[0]);
            title2.setText(arr_title_zh[1] + "： " + myDbsy.getContent2());
            title3.setText(arr_title_zh[2] + "： " + myDbsy.getContent3());
            title4.setText(arr_title_zh[3] + "： " + myDbsy.getContent4());
            title5.setText(arr_title_zh[4] + "： " + myDbsy.getContent5());
            title6.setText(arr_title_zh[5] + "： " + myDbsy.getContent6());
        }else{
            title1.setText(arr_title_en[0]);
            title2.setText(arr_title_en[1] + "： " + myDbsy.getContent2());
            title3.setText(arr_title_en[2] + "： " + myDbsy.getContent3());
            title4.setText(arr_title_en[3] + "： " + myDbsy.getContent4());
            title5.setText(arr_title_en[4] + "： " + myDbsy.getContent5());
            title6.setText(arr_title_en[5] + "： " + myDbsy.getContent6());
        }

        return convertView;
    }

    public void uPdate(ArrayList<MyDbsy> list) {
        this.mDbsylist = list;
        notifyDataSetChanged();
    }


}
