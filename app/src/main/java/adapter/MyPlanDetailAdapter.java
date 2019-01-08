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

public class MyPlanDetailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> textViewsList_title;
    private ArrayList<Map<String,String>> textViewsList_content;


    public MyPlanDetailAdapter(Context context, ArrayList<String> textViewsList_title, ArrayList<Map<String, String>> textViewsList_content) {
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
            convertView = View.inflate(context, R.layout.plan_detail_item, null);
        }
        LinearLayout plan_detail_title = ViewHolder.get(convertView, R.id.plan_detail_title);
        LinearLayout plan_detail_content = ViewHolder.get(convertView, R.id.plan_detail_content);
        plan_detail_title.removeAllViews();
        plan_detail_content.removeAllViews();

        for (int i = 0; i < textViewsList_title.size(); i++) {
            TextView text_title = new TextView(context);
            LinearLayout.LayoutParams layoutParams_title = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text_title.setLayoutParams(layoutParams_title);
            text_title.setText(textViewsList_title.get(i)+"：");
            text_title.setGravity(Gravity.CENTER);
            text_title.setTextSize(13);
            text_title.setBackgroundResource(R.drawable.editext_bk);
            plan_detail_title.addView(text_title);

            TextView text_content = new TextView(context);
            LinearLayout.LayoutParams layoutParams_content = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text_content.setLayoutParams(layoutParams_content);
            text_content.setText(textViewsList_content.get(position).get(textViewsList_title.get(i)));
            text_content.setGravity(Gravity.CENTER);
            text_content.setTextSize(13);
//            text_content.setBackgroundResource(R.drawable.editext_bk);
            plan_detail_content.addView(text_content);
        }

        return convertView;
    }

    /**
     * 刷新
     */
    public void addNewData(ArrayList<String> title_list,ArrayList<Map<String,String>> content_list) {
        if (title_list != null && content_list != null) {
            textViewsList_title.addAll(0, title_list);
            textViewsList_content.addAll(0, content_list);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合
     */
    public void addMoreData(ArrayList<String> title_list,ArrayList<Map<String,String>> content_list) {
        if (title_list != null && content_list != null) {
            textViewsList_content.addAll(content_list);
            notifyDataSetChanged();
        }
    }

}
