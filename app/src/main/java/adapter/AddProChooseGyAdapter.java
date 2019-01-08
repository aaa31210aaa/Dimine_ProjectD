package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.ChooseTech;
import utils.ViewHolder;

/**
 * 选择工艺适配器
 */
public class AddProChooseGyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ChooseTech> tech_list;

    public AddProChooseGyAdapter(Context context, ArrayList<ChooseTech> tech_list) {
        this.context = context;
        this.tech_list = tech_list;
    }

    @Override
    public int getCount() {
        return tech_list.size();
    }

    @Override
    public Object getItem(int position) {
        return tech_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.add_pro_choose_bc_item, null);
        }
        TextView add_pro_choose_bc_item_tv = ViewHolder.get(convertView, R.id.add_pro_choose_bc_item_tv);
        ChooseTech tech = tech_list.get(position);
        add_pro_choose_bc_item_tv.setText(tech.getTechname());
        return convertView;
    }
}
