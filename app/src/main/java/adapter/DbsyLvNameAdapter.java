package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.MyDbsy;

public class DbsyLvNameAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MyDbsy> dbsy_list;

    public DbsyLvNameAdapter(Context context, ArrayList<MyDbsy> dbsy_list) {
        this.context = context;
        this.dbsy_list = dbsy_list;
    }

    public void setList(ArrayList<MyDbsy> dbsy_list) {
        this.dbsy_list = dbsy_list;
    }

    @Override
    public int getCount() {
        return dbsy_list.size();
    }

    @Override
    public Object getItem(int position) {
        return dbsy_list.get(position);
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
        MyDbsy dbsy = dbsy_list.get(position);
        holder.tv_goodname.setText(dbsy.getOne_content());
        return convertView;
    }

    public void uPdate(ArrayList<MyDbsy> dbsy_list){
        this.dbsy_list = dbsy_list;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tv_goodname;
    }
}
