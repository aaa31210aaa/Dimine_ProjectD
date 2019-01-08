package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.MyProduction;
import utils.ViewHolder;

public class ProductionGridView1Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<MyProduction> tv_list;
    private ArrayList<MyProduction> iv_list;

    public ProductionGridView1Adapter(Context context, ArrayList<MyProduction> tv_list, ArrayList<MyProduction> iv_list) {
        this.context = context;
        this.tv_list = tv_list;
        this.iv_list = iv_list;
    }

    @Override
    public int getCount() {
        return tv_list.size();
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
        MyProduction mpro = tv_list.get(position);
        TextView tv_item = ViewHolder.get(convertView, R.id.tv_item);
        ImageView iv_item = ViewHolder.get(convertView, R.id.iv_item);
        Log.d("hhh", "" + mpro + position);
        tv_item.setText(mpro.getProctname());
        iv_item.setBackgroundResource(R.drawable.u23);


        return convertView;
    }
}
