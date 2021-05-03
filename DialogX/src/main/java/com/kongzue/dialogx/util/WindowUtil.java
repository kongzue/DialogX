package com.kongzue.dialogx.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;

import static android.view.WindowManager.LayoutParams.*;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/4/29 16:02
 */
public class WindowUtil {
    
    public static void show(Activity activity, View dialogView, boolean touchEnable) {
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.type = TYPE_APPLICATION_ATTACHED_DIALOG;
        layoutParams.flags = FLAG_FULLSCREEN |
                FLAG_TRANSLUCENT_STATUS |
                FLAG_TRANSLUCENT_NAVIGATION |
                FLAG_LAYOUT_IN_SCREEN
        ;
        if (!touchEnable) {
            layoutParams.flags = layoutParams.flags | FLAG_NOT_FOCUSABLE;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            layoutParams.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        
        manager.addView(dialogView, layoutParams);
    }
    
    public static void dismiss(View dialogView) {
        BaseDialog baseDialog = (BaseDialog) dialogView.getTag();
        if (baseDialog != null && baseDialog.getActivity() != null) {
            WindowManager manager = (WindowManager) baseDialog.getActivity().getSystemService(Context.WINDOW_SERVICE);
            manager.removeViewImmediate(dialogView);
        }
    }
}
