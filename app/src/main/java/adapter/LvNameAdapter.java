package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.PlanContent;

public class LvNameAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlanContent> plan1_list;


    public LvNameAdapter(Context context, ArrayList<PlanContent> plan1_list) {
        this.context = context;
        this.plan1_list = plan1_list;
    }

    @Override
    public int getCount() {
        return plan1_list.size();
    }

    @Override
    public Object getItem(int position) {
        return plan1_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_good_name, null);
            holder.tv_goodname = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlanContent mContent = plan1_list.get(position);
        holder.tv_goodname.setText(mContent.getOne_content());
        return convertView;
    }

    class ViewHolder {
        TextView tv_goodname;
    }
}
