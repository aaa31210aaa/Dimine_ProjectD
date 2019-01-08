package adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class DbsyLvInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Map<String, String>> dbsy2_list;
    private int title_length;
    private int width;
    private int height;
    private String[] arr_title;
    private int height_px;

    public DbsyLvInfoAdapter(Context context, ArrayList<Map<String, String>> dbsy2_list, int title_length, int width, int height, String[] arr_title, int height_px) {
        this.context = context;
        this.dbsy2_list = dbsy2_list;
        this.title_length = title_length;
        this.width = width;
        this.height = height;
        this.arr_title = arr_title;
        this.height_px = height_px;
    }

//    public void addItem(Map map) {
//        dbsy2_list.add(map);
//        notifyDataSetChanged();
//    }


    @Override
    public int getCount() {
        return dbsy2_list.size();
    }

    @Override
    public Object getItem(int position) {
        return dbsy2_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHoder holder = null;
        if (convertView == null) {
            holder = new MyViewHoder();
            convertView = holder.toAddView();
            convertView.setTag(holder);
        } else {
            holder = (MyViewHoder) convertView.getTag();
        }

        Map<String, String> mContent = dbsy2_list.get(position);
        for (int i = 1; i < title_length; i++) {
            ((TextView) holder.layout.getChildAt(i - 1)).setText(mContent.get(arr_title[i]));
        }
        return convertView;
    }

    public void mUpData(ArrayList<Map<String, String>> dbsy2_list){
        this.dbsy2_list = dbsy2_list;
        notifyDataSetChanged();
    }


    private class MyViewHoder {
        LinearLayout layout = new LinearLayout(context);

        public LinearLayout toAddView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, height_px);
            layout.setLayoutParams(lp);


            for (int i = 1; i < title_length; i++) {
                TextView view = new TextView(context);
                view.setTextSize(16);
                view.setGravity(Gravity.CENTER);
                view.setLayoutParams(new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                layout.addView(view);
            }
            return layout;
        }
    }
}
