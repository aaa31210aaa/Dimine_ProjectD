package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.SelectProject;
import utils.ViewHolder;

public class NewProductionJlListviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SelectProject> jl_list;

    public NewProductionJlListviewAdapter(Context context, ArrayList<SelectProject> jl_list) {
        this.context = context;
        this.jl_list = jl_list;
    }

    @Override
    public int getCount() {
        return jl_list.size();
    }

    @Override
    public Object getItem(int position) {
        return jl_list.get(position);
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
        SelectProject selectProject = jl_list.get(position);
        add_pro_choose_bc_item_tv.setText(selectProject.getProjectname());

        return convertView;
    }
}
