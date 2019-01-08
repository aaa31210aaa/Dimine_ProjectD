package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Info;

public class MyApprovalListViewAdapter extends BaseAdapter {
    private ArrayList<Info> wkj_list;
    private Context context;
    private Map<Integer, Boolean> isSelected;

    public MyApprovalListViewAdapter(ArrayList<Info> wkj_list, Context context) {
        this.wkj_list = wkj_list;
        this.context = context;

        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < wkj_list.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return wkj_list.size();
    }

    @Override
    public Object getItem(int position) {
        return wkj_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.my_approval_listview_item, null);
            holder.weekly_kt_jj_listview_item_fd = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_fd);
            holder.weekly_kt_jj_listview_item_zjhjc = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_zjhjc);
            holder.weekly_kt_jj_listview_item_zjhgcl = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_zjhgcl);
            holder.weekly_kt_jj_listview_item_xl = (ImageView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_xl);
            holder.weekly_kt_jj_listview_item_zd = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_zd);
            holder.weekly_kt_jj_listview_item_zb = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_zb);
            holder.weekly_kt_jj_listview_item_jhgcl = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_jhgcl);
            holder.weekly_kt_jj_listview_item_dont = (TextView) convertView.findViewById(R.id.weekly_kt_jj_listview_item_dont);
            holder.weekly_kt_jj_listview_item_detail = (LinearLayout) convertView.findViewById(R.id.weekly_kt_jj_listview_item_detail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final int pos = position;

        /**
         * 下拉图标
         */
        holder.weekly_kt_jj_listview_item_xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.weekly_kt_jj_listview_item_detail.getVisibility() == View.GONE) {
                    isSelected.put(pos, true);
                    holder.weekly_kt_jj_listview_item_detail.setVisibility(View.VISIBLE);

                } else {
                    isSelected.put(pos, false);
                    holder.weekly_kt_jj_listview_item_detail.setVisibility(View.GONE);
                }
            }
        });
        if (isSelected.get(pos)) {
            holder.weekly_kt_jj_listview_item_detail.setVisibility(View.VISIBLE);
        } else {
            holder.weekly_kt_jj_listview_item_detail.setVisibility(View.GONE);
        }
        holder.weekly_kt_jj_listview_item_fd.setText(wkj_list.get(position).getFd());
        holder.weekly_kt_jj_listview_item_zjhjc.setText(wkj_list.get(position).getJjjc());
        holder.weekly_kt_jj_listview_item_zjhgcl.setText(wkj_list.get(position).getJjjc());
        holder.weekly_kt_jj_listview_item_zd.setText(wkj_list.get(position).getFd());
        holder.weekly_kt_jj_listview_item_zb.setText(wkj_list.get(position).getBc());
        holder.weekly_kt_jj_listview_item_jhgcl.setText(wkj_list.get(position).getJjjc());
        holder.weekly_kt_jj_listview_item_dont.setText(wkj_list.get(position).getJjjc());
        return convertView;
    }


    private class ViewHolder {
        private TextView weekly_kt_jj_listview_item_fd;
        private TextView weekly_kt_jj_listview_item_zjhjc;
        private TextView weekly_kt_jj_listview_item_zjhgcl;
        private ImageView weekly_kt_jj_listview_item_xl;
        private TextView weekly_kt_jj_listview_item_zd;
        private TextView weekly_kt_jj_listview_item_zb;
        private TextView weekly_kt_jj_listview_item_jhgcl;
        private TextView weekly_kt_jj_listview_item_dont;
        private LinearLayout weekly_kt_jj_listview_item_detail;
    }

}
