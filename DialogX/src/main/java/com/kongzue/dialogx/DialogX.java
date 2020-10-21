package com.kongzue.dialogx;

import android.content.Context;
import android.util.Log;

import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.style.MaterialStyle;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/21 17:07
 */
public class DialogX {
    
    public static boolean DEBUGMODE = true;
    
    public static DialogXStyle globalStyle = MaterialStyle.style();
    public static DialogX.THEME globalTheme = DialogX.THEME.LIGHT;
    public static int dialogMaxWidth;
    public static boolean autoShowInputKeyboard = true;
    public static boolean onlyOnePopTip = true;
    
    public enum THEME {
        LIGHT, DARK
    }
    
    public static void init(Context context) {
        if (context == null) {
            error("DialogX.init: 初始化异常，context 为 null");
            return;
        }
        BaseDialog.init(context);
    }
    
    public static void log(Object o) {
        if (DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    public static void error(Object o) {
        if (DEBUGMODE) Log.e(">>>", o.toString());
    }
}
