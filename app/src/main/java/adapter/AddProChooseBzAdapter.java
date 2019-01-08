package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.ChooseBanZu;
import utils.ViewHolder;

/**
 * 选择班组适配器
 */
public class AddProChooseBzAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChooseBanZu> bz_list;

    public AddProChooseBzAdapter(Context context, ArrayList<ChooseBanZu> bz_list) {
        this.context = context;
        this.bz_list = bz_list;
    }

    @Override
    public int getCount() {
        return bz_list.size();
    }

    @Override
    public Object getItem(int position) {
        return bz_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.add_pro_choose_bc_item, null);
        }
        TextView add_pro_choose_bc_item_tv = ViewHolder.get(convertView, R.id.add_pro_choose_bc_item_tv);
        ChooseBanZu banZu = bz_list.get(position);
        add_pro_choose_bc_item_tv.setText(banZu.getTeamname());
        return convertView;
    }
}
