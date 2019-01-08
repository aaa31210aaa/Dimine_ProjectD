package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.util.ArrayList;

import entity.CommentEntity;
import utils.ViewHolder;

public class MyPllbAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CommentEntity> commList;

    public MyPllbAdapter(Context context, ArrayList<CommentEntity> commList) {
        this.context = context;
        this.commList = commList;
    }

    @Override
    public int getCount() {
        return commList.size();
    }

    @Override
    public Object getItem(int position) {
        return commList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_comment, null);
        }
        CommentEntity commentEntity = commList.get(position);
        ImageView iv_user_photo = ViewHolder.get(convertView, R.id.iv_user_photo);
        TextView tv_user_name = ViewHolder.get(convertView, R.id.tv_user_name);
        TextView comment_time = ViewHolder.get(convertView, R.id.comment_time);
        TextView tv_user_comment = ViewHolder.get(convertView, R.id.tv_user_comment);

        iv_user_photo.setBackgroundResource(R.drawable.ks);
        tv_user_name.setText(commentEntity.getUserName());
        comment_time.setText(commentEntity.getCommentTime());
        tv_user_comment.setText(commentEntity.getCommentContent());

        return convertView;
    }

    public void uPdate(ArrayList<CommentEntity> list) {
        this.commList = list;
        notifyDataSetChanged();
    }

    /**
     * 刷新
     */
    public void addNewData(ArrayList<CommentEntity> list) {
        if (list != null) {
            commList.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合
     */
    public void addMoreData(ArrayList<CommentEntity> list) {
        if (list != null) {
            commList.addAll(list);
            notifyDataSetChanged();
        }
    }
}
