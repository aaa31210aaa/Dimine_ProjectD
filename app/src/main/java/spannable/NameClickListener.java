package spannable;

import android.text.SpannableString;
import android.widget.Toast;

import myinterface.ISpanClick;
import utils.MyApplication;

/**
 * 点赞和评论中人名的点击事件
 */
public class NameClickListener implements ISpanClick {
    private SpannableString userName;
    private String userId;

    public NameClickListener(SpannableString name, String userid) {
        this.userName = name;
        this.userId = userid;
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(MyApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
    }
}
