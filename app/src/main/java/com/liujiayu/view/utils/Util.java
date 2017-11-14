package com.liujiayu.view.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liujiayu on 2017/9/27.
 */

public class Util {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}