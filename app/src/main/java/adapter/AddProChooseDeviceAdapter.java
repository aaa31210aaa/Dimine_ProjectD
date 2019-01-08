package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.ChooseDevice;
import utils.ViewHolder;

/**
 * 选择设备适配器
 */
public class AddProChooseDeviceAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ChooseDevice> device_list;

    public AddProChooseDeviceAdapter(Context context, ArrayList<ChooseDevice> device_list) {
        this.context = context;
        this.device_list = device_list;
    }

    @Override
    public int getCount() {
        return device_list.size();
    }

    @Override
    public Object getItem(int position) {
        return device_list.get(position);
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
        ChooseDevice device = device_list.get(position);
        add_pro_choose_bc_item_tv.setText(device.getDevname());

        return convertView;
    }
}
