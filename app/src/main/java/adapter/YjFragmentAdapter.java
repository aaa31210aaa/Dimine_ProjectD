package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.Alarm;
import utils.ViewHolder;

public class YjFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Alarm> yj_list;

    public YjFragmentAdapter(Context context, ArrayList<Alarm> yj_list) {
        this.context = context;
        this.yj_list = yj_list;
    }

    @Override
    public int getCount() {
        return yj_list.size();
    }

    @Override
    public Object getItem(int position) {
        return yj_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.yj_listview_item, null);
        }
        TextView yj_yjmc = ViewHolder.get(convertView, R.id.yj_yjmc);
        TextView yj_lb = ViewHolder.get(convertView, R.id.yj_lb);
        TextView yj_nr = ViewHolder.get(convertView, R.id.yj_nr);
        TextView yj_yjsj = ViewHolder.get(convertView, R.id.yj_yjsj);
        Alarm alarm = yj_list.get(position);
        yj_yjmc.setText(alarm.getAlarmtitle());
        yj_lb.setText(alarm.getAlarmlevel());
        yj_nr.setText(alarm.getHandlecontent());
        yj_yjsj.setText(alarm.getAddtime());
        return convertView;
    }

    /**
     * 刷新
     */
    public void addNewData(ArrayList<Alarm> list) {
        if (list != null) {
            yj_list.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合
     */
    public void addMoreData(ArrayList<Alarm> list) {
        if (list != null) {
            yj_list.addAll(list);
            notifyDataSetChanged();
        }
    }



}

