package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.Notification;
import utils.ViewHolder;

public class NotificationListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Notification> no_list;

    public NotificationListViewAdapter(ArrayList<Notification> no_list, Context context) {
        this.no_list = no_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return no_list.size();
    }

    @Override
    public Object getItem(int position) {
        return no_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.my_notification_listview_item, null);
        }
        TextView notification_listview_item_title = ViewHolder.get(convertView, R.id.notification_listview_item_title);
        TextView notification_listview_item_date = ViewHolder.get(convertView, R.id.notification_listview_item_date);
        TextView notification_listview_item_content = ViewHolder.get(convertView, R.id.notification_listview_item_content);

        Notification tz = no_list.get(position);
        notification_listview_item_title.setText(tz.getTitle());
        notification_listview_item_date.setText(tz.getCreatedate());
        notification_listview_item_content.setText(tz.getContent());
        return convertView;
    }


    /**
     * 刷新
     */
    public void addNewData(ArrayList<Notification> list) {
        if (list != null) {
            no_list.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合
     */
    public void addMoreData(ArrayList<Notification> list) {
        if (list != null) {
            no_list.addAll(list);
            notifyDataSetChanged();
        }
    }
}
