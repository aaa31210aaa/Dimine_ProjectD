package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.PlanWeekly;

public class PlanWeeklyDetailAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<PlanWeekly> pwdl_list;

    public PlanWeeklyDetailAdapter(Context context, ArrayList<PlanWeekly> pwdl_list) {
        this.context = context;
        this.pwdl_list = pwdl_list;
    }

    @Override
    public int getCount() {
        return pwdl_list.size();
    }

    @Override
    public Object getItem(int position) {
        return pwdl_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.activity_plan_weekly_detail,null);
        }





        return convertView;
    }
}
