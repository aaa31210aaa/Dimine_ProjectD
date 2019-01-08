package adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.PlanWeekly;
import utils.ViewHolder;

public class PlanMonthlyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlanWeekly> plan_tv_list;
    private ArrayList<PlanWeekly> plan_iv_list;

    public PlanMonthlyAdapter(Context context, ArrayList<PlanWeekly> plan_tv_list, ArrayList<PlanWeekly> plan_iv_list) {
        this.context = context;
        this.plan_tv_list = plan_tv_list;
        this.plan_iv_list = plan_iv_list;
    }

    @Override
    public int getCount() {
        return plan_tv_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.plan_grid_item, null);
        }
        PlanWeekly pz = plan_tv_list.get(position);
        TextView plan_tv_item = ViewHolder.get(convertView, R.id.plan_tv_item);
        ImageView plan_iv_item = ViewHolder.get(convertView, R.id.plan_iv_item);
        plan_tv_item.setText(pz.getPlanname());
        plan_iv_item.setBackgroundResource(R.drawable.ks);

        return convertView;
    }
}
