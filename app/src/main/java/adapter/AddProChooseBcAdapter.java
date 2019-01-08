package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.ChooseBanCi;
import utils.ViewHolder;

public class AddProChooseBcAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChooseBanCi> bc_list;

    public AddProChooseBcAdapter(Context context, ArrayList<ChooseBanCi> bc_list) {
        this.context = context;
        this.bc_list = bc_list;
    }

    @Override
    public int getCount() {
        return bc_list.size();
    }

    @Override
    public Object getItem(int position) {
        return bc_list.get(position);
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
        ChooseBanCi banCi = bc_list.get(position);
        add_pro_choose_bc_item_tv.setText(banCi.getClassname());

        return convertView;
    }
}
