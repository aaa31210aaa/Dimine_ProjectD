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

public class GjFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Alarm> gj_list;

    public GjFragmentAdapter(Context context, ArrayList<Alarm> gj_list) {
        this.context = context;
        this.gj_list = gj_list;
    }

    @Override
    public int getCount() {
        return gj_list.size();
    }

    @Override
    public Object getItem(int position) {
        return gj_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gj_listview_item, null);
        }
        TextView gj_gjmc = ViewHolder.get(convertView, R.id.gj_gjmc);
        TextView gj_gjlb = ViewHolder.get(convertView, R.id.gj_gjlb);
        TextView gj_gjnr = ViewHolder.get(convertView, R.id.gj_gjnr);
        TextView gj_gjsj = ViewHolder.get(convertView, R.id.gj_gjsj);

        Alarm alarm = gj_list.get(position);
        gj_gjmc.setText(alarm.getAlarmtitle());
        gj_gjlb.setText(alarm.getAlarmlevel());
        gj_gjsj.setText(alarm.getAddtime());
        gj_gjnr.setText(alarm.getHandlecontent());
        return convertView;
    }

    public void MyUpdate(ArrayList<Alarm> list){
        this.gj_list = list;
        notifyDataSetChanged();
    }

    /**
     * 刷新
     */
    public void addNewData(ArrayList<Alarm> list) {
        if (list != null) {
            gj_list.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合
     */
    public void addMoreData(ArrayList<Alarm> list) {
        if (list != null) {
            gj_list.addAll(list);
            notifyDataSetChanged();
        }
    }

}
