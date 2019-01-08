package entity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * 弹窗内部子类项（绘制标题和图标）
 */
public class ActionItem {
    // 定义图片对象
    public Drawable mDrawable;
    // 定义文本对象
    public CharSequence mTitle;

    public ActionItem(Drawable drawable, CharSequence title) {
        this.mDrawable = drawable;
        this.mTitle = title;
    }

    public ActionItem(CharSequence title) {
        this.mDrawable = null;
        this.mTitle = title;
    }

    public ActionItem(Context context, int titleId, int drawableId) {
        this.mTitle = context.getResources().getText(titleId);
        this.mDrawable = ContextCompat.getDrawable(context,drawableId);
    }

    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.mTitle = title;
        this.mDrawable = ContextCompat.getDrawable(context,drawableId);
    }

    public void setItemTv(CharSequence tv) {
        mTitle = tv;
    }
}
