package com.github.noyeecao2008.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;
public class ScreenUtil {
    // https://blog.csdn.net/m0_56231540/article/details/124622124
    public static double getPhysicsScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        return point.x;
    }

}
