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

public class ReportGridView1Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlanWeekly> report_tv_list;
    private ArrayList<PlanWeekly> report_iv_list;

    public ReportGridView1Adapter(Context context, ArrayList<PlanWeekly> report_tv_list, ArrayList<PlanWeekly> report_iv_list) {
        this.context = context;
        this.report_tv_list = report_tv_list;
        this.report_iv_list = report_iv_list;
    }

    @Override
    public int getCount() {
        return report_tv_list.size();
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
            convertView = View.inflate(context, R.layout.production_grid_item, null);
        }
        PlanWeekly pz = report_tv_list.get(position);
        TextView report_tv_item = ViewHolder.get(convertView, R.id.tv_item);
        ImageView report_iv_item = ViewHolder.get(convertView, R.id.iv_item);
        report_tv_item.setText(pz.getPlanname());
        report_iv_item.setBackgroundResource(R.drawable.ks);
        return convertView;
    }
}
