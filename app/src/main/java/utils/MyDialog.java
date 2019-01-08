package utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class MyDialog extends Dialog {

    public MyDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
    }
     
}