package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import org.json.JSONObject;

import java.util.ArrayList;

import utils.ViewHolder;

public class ProductionGridView2Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<JSONObject> tv2_list;
    private ArrayList<JSONObject> iv2_list;

    public ProductionGridView2Adapter(Context context, ArrayList<JSONObject> tv2_list, ArrayList<JSONObject> iv2_list) {
        this.context = context;
        this.tv2_list = tv2_list;
        this.iv2_list = iv2_list;
    }

    @Override
    public int getCount() {
        return tv2_list.size();
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
        JSONObject jsonObject = tv2_list.get(position);
        TextView tv_item = ViewHolder.get(convertView, R.id.tv_item);
        ImageView iv_item = ViewHolder.get(convertView, R.id.iv_item);
        tv_item.setText(jsonObject.optString("gtv"));
        iv_item.setBackgroundResource(R.drawable.u21);

        return convertView;
    }
}
