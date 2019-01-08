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

public class PlanDetailAdapter extends BaseAdapter {
    private ArrayList<Map<String, String>> planDtailList;
    private Context context;
    private int title_length;
    private int width;
    private int height;
    private JSONArray jsonArray;

    public PlanDetailAdapter(ArrayList<Map<String, String>> planDtailList, Context context, int title_length, int width, int height, JSONArray jsonArray) {
        this.planDtailList = planDtailList;
        this.context = context;
        this.title_length = title_length;
        this.width = width;
        this.height = height;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return planDtailList.size();
    }

    @Override
    public Object getItem(int position) {
        return planDtailList.get(position);
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

        Map<String, String> mContent = planDtailList.get(position);

        for (int i = 0; i < title_length; i++) {
            try {
                ((TextView) holder.layout.getChildAt(i)).setText(mContent.get(jsonArray.getString(i)));
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
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lp);
            for (int i = 0; i < title_length; i++) {
                TextView view = new TextView(context);
                view.setTextSize(16);
                view.setGravity(Gravity.CENTER);
                view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                layout.addView(view);
            }
            return layout;
        }

    }
}
