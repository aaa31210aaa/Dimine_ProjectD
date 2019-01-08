package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.Info;
import utils.ViewHolder;

public class ProductionHistoryListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Info> his_list;


    public ProductionHistoryListViewAdapter(Context context, ArrayList<Info> his_list) {
        this.context = context;
        this.his_list = his_list;
    }

    @Override
    public int getCount() {
        return his_list.size();
    }

    @Override
    public Object getItem(int position) {
        return his_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.new_production_history_listview_item, null);
        }
        TextView jl_item_tv = ViewHolder.get(convertView, R.id.jl_item_tv);
        Info info = his_list.get(position);
        jl_item_tv.setText(info.getFd() + position + "#进路");

        return convertView;
    }
}
