package adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;
import java.util.Map;

public class TestAdapter extends BaseAdapter {
    private Context context;
    private Map<String, ArrayList<String>> lists;
    private ArrayList<String> procnames;
    private int fetch;

    public TestAdapter(Context context, ArrayList<String> procnames, Map<String, ArrayList<String>> data) {
        this.context = context;
        this.lists = data;
        this.procnames = procnames;
//        this.fetch = fetch;
    }

    @Override
    public int getCount() {
        return procnames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String procname = procnames.get(position);
        ArrayList<String> item = lists.get(procname);//获得单个item中间ui

        int count = item.size();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.test_listview_item, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.title1);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.test_listview_item_ll);
            for (int i = 0; i < count; i++) {
                TextView text = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                text.setLayoutParams(layoutParams);
                text.setText(item.get(i));
                text.setGravity(Gravity.CENTER);
                text.setTextSize(13);
                holder.layout.addView(text);//动态生成view
            }
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();


        holder.title1.setText(procname);//左侧ui
        LinearLayout layout = holder.layout;

//        for (int i = 0; i < count; i++) {
//         ((TextView) layout.getChildAt(i)).setText(item.get(i));//设置内容
//        }

        Log.e("Toooo", "--item--" + item.size());
        holder.img.setBackgroundResource(R.drawable.u72);
        return convertView;
    }


    private class ViewHolder {
        private TextView title1;
        private LinearLayout layout;
        private ImageView img;

    }
}
