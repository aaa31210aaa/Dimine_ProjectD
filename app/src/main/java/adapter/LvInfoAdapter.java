package adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

public class LvInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Map<String, String>> plan2_list;
    private int title_length;
    private int width;
    private int height;
    private JSONArray jsonArray_title;
    private int height_px;

    public LvInfoAdapter(Context context, ArrayList<Map<String, String>> plan2_list, int title_length, int width, int height, JSONArray jsonArray_title, int height_px) {
        this.context = context;
        this.plan2_list = plan2_list;
        this.title_length = title_length;
        this.width = width;
        this.height = height;
        this.jsonArray_title = jsonArray_title;
        this.height_px = height_px;
    }

    @Override
    public int getCount() {
        return plan2_list.size();
    }

    @Override
    public Object getItem(int position) {
        return plan2_list.get(position);
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
        Map<String, String> mContent = plan2_list.get(position);

        for (int i = 1; i < title_length; i++) {
            try {
                ((TextView) holder.layout.getChildAt(i - 1)).setText(mContent.get(jsonArray_title.getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
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

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_good_info, null);
//            holder.tv_barcode = (TextView) convertView.findViewById(R.id.tv_barcode);
//            holder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
//            holder.tv_spec = (TextView) convertView.findViewById(R.id.tv_spec);
//            holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
//            holder.tv_supplyer = (TextView) convertView.findViewById(R.id.tv_supplyer);
//            holder.tv_sale_money = (TextView) convertView.findViewById(R.id.tv_sale_money);
//            holder.tv_income_money = (TextView) convertView.findViewById(R.id.tv_income_money);
//            holder.tv_keep = (TextView) convertView.findViewById(R.id.tv_keep);
//            holder.tv_intime = (TextView) convertView.findViewById(R.id.tv_intime);
//            holder.tv_online = (ImageView) convertView.findViewById(R.id.iv_online);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if (position < 10) {
//            holder.tv_barcode.setText("1001200660" + position);
//        } else {
//            holder.tv_barcode.setText("100120066" + position);
//        }
//        holder.tv_category.setText("类型" + position);
//        holder.tv_spec.setText("规格" + position);
//        holder.tv_unit.setText("个");
//        holder.tv_supplyer.setText("供应商" + position);
//        holder.tv_sale_money.setText("价格" + position);
//        holder.tv_keep.setText("1年");
//        holder.tv_intime.setText("2016-03-21");
//        holder.tv_income_money.setText("进货价" + position);
//
//        return convertView;
//    }

//    class ViewHolder {
//        TextView tv_barcode;
//        TextView tv_category;
//        TextView tv_spec;
//        TextView tv_unit;
//        TextView tv_supplyer;
//        TextView tv_sale_money;
//        TextView tv_income_money;
//        TextView tv_keep;
//        TextView tv_intime;
//        ImageView tv_online;
//
//    }
}
