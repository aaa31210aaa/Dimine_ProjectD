package adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;
import java.util.Map;

import utils.ViewHolder;

public class HistoryListViewAdapter extends BaseAdapter {
//    private ArrayList<Map<String, String>> his_list;
//    private Context context;
//    private int width;
//    private int height;
//    private int title_length;
//    private JSONArray jsonArray;
//
//
//    public HistoryListViewAdapter(ArrayList<Map<String, String>> his_list, Context context, int width, int height, int title_length, JSONArray jsonArray) {
//        this.his_list = his_list;
//        this.context = context;
//        this.width = width;
//        this.height = height;
//        this.title_length = title_length;
//        this.jsonArray = jsonArray;
//    }
//
//    @Override
//    public int getCount() {
//        return his_list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return his_list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public void notify(ArrayList<Map<String, String>> list) {
//        this.his_list = list;
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        MyViewHoder holder = null;
//        if (convertView == null) {
//            holder = new MyViewHoder();
//            convertView = holder.toAddView();
//            convertView.setTag(holder);
//        } else {
//            holder = (MyViewHoder) convertView.getTag();
//        }
//
//        Map<String, String> hisTitle = his_list.get(position);
//        for (int i = 0; i < title_length; i++) {
//            try {
//                ((TextView) holder.layout.getChildAt(i)).setText(hisTitle.get(jsonArray.getString(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return convertView;
//
//    }
//
//    private class MyViewHoder {
//        LinearLayout layout = new LinearLayout(context);
//
//        public LinearLayout toAddView() {
////            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
////                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            layout.setLayoutParams(lp);
//            for (int i = 0; i < title_length; i++) {
//                TextView view = new TextView(context);
//                view.setTextSize(18);
//                view.setGravity(Gravity.CENTER);
//                view.setPadding(10, 10, 10, 10);
//                view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
//                layout.addView(view);
//            }
//            return layout;
//        }
//
//    }

    private Context context;
    private ArrayList<String> textViewsList_title;
    private ArrayList<Map<String, String>> textViewsList_content;

    public HistoryListViewAdapter(Context context, ArrayList<String> textViewsList_title, ArrayList<Map<String, String>> textViewsList_content) {
        this.context = context;
        this.textViewsList_title = textViewsList_title;
        this.textViewsList_content = textViewsList_content;
    }

    @Override
    public int getCount() {
        return textViewsList_content.size();
    }

    @Override
    public Object getItem(int position) {
        return textViewsList_content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.production_history_item, null);
        }
        LinearLayout title_ll = ViewHolder.get(convertView, R.id.production_his_title);
        LinearLayout content_ll = ViewHolder.get(convertView, R.id.production_his_content);
        title_ll.removeAllViews();
        content_ll.removeAllViews();

        for (int i = 0; i < textViewsList_title.size(); i++) {
            TextView text_title = new TextView(context);
            LinearLayout.LayoutParams layoutParams_title = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text_title.setLayoutParams(layoutParams_title);
            text_title.setText(textViewsList_title.get(i) + "：");
            text_title.setGravity(Gravity.CENTER);
            text_title.setTextSize(13);
            text_title.setBackgroundResource(R.drawable.editext_bk);
            title_ll.addView(text_title);

            TextView text_content = new TextView(context);
            LinearLayout.LayoutParams layoutParams_content = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text_content.setLayoutParams(layoutParams_content);
            text_content.setText(textViewsList_content.get(position).get(textViewsList_title.get(i)));
            text_content.setGravity(Gravity.CENTER);
            text_content.setTextSize(13);
//            text_content.setBackgroundResource(R.drawable.editext_bk);
            content_ll.addView(text_content);
        }


        return convertView;
    }


    /**
     * 刷新
     */
    public void addNewData(ArrayList<String> datas_title, ArrayList<Map<String, String>> datas_content) {
        if (datas_title != null && datas_content != null) {
            textViewsList_title.addAll(0, datas_title);
            textViewsList_content.addAll(0, datas_content);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合
     */
    public void addMoreData(ArrayList<String> datas_title, ArrayList<Map<String, String>> datas_content) {

        if (datas_title != null && datas_content != null) {
            textViewsList_content.addAll(datas_content);
            notifyDataSetChanged();
        }
    }

    public void DataNotify(ArrayList<String> datas_title, ArrayList<Map<String, String>> datas_content) {
        this.textViewsList_title = datas_title;
        this.textViewsList_content = datas_content;
        notifyDataSetChanged();
    }


}
