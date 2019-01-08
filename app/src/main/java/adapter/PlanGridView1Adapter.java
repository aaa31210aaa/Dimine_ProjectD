package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.R;

import java.io.FileInputStream;
import java.util.ArrayList;

import entity.PlanWeekly;
import utils.ViewHolder;

public class PlanGridView1Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlanWeekly> tv_list;
    private ArrayList<PlanWeekly> iv_list;
    private FileInputStream fis;

    public PlanGridView1Adapter(Context context, ArrayList<PlanWeekly> tv_list, ArrayList<PlanWeekly> iv_list) {
        this.context = context;
        this.tv_list = tv_list;
        this.iv_list = iv_list;
    }

    @Override
    public int getCount() {
        return tv_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.production_grid_item, null);
        }

        PlanWeekly pz = tv_list.get(position);
        TextView tv_item = ViewHolder.get(convertView, R.id.tv_item);
        ImageView iv_item = ViewHolder.get(convertView, R.id.iv_item);
        tv_item.setText(pz.getPlanname());
        iv_item.setBackgroundResource(R.drawable.ks);
        


//        JSONObject jsonObject = tv_list.get(position);
//        JSONObject jsonObject2 = iv_list.get(position);
//        TextView tv_item = ViewHolder.get(convertView, R.id.tv_item);
//        ImageView iv_item = ViewHolder.get(convertView, R.id.iv_item);
//        tv_item.setText(jsonObject.optString("gtv"));
//        String img = jsonObject2.optString("giv");
//        Log.d("zzz", img);


//        try {
//            fis = new FileInputStream(img);
//            Bitmap bm = BitmapFactory.decodeStream(fis);
//            iv_item.setImageBitmap(bm);
//        } catch (FileNotFoundException e) {
//            Log.d("zzz", e + "");
//            e.printStackTrace();
//        }
//        Log.d("hhh", "" + jsonObject + position);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
