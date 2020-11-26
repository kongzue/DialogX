package com.kongzue.dialogx;

import android.content.Context;
import android.util.Log;

import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.dialogx.util.TextInfo;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/21 17:07
 */
public class DialogX {
    
    //开启日志
    public static boolean DEBUGMODE = true;
    
    //全局主题风格
    public static DialogXStyle globalStyle = MaterialStyle.style();
    
    //全局对话框明暗风格
    public static DialogX.THEME globalTheme = DialogX.THEME.LIGHT;
    
    //TipDialog 和 WaitDialog 明暗风格，不设置则默认根据 globalTheme 定义
    public static DialogX.THEME tipTheme;
    
    //对话框最大宽度（像素）
    public static int dialogMaxWidth;
    
    //是否允许 InputDialog 自动弹出键盘
    public static boolean autoShowInputKeyboard = true;
    
    //同时只显示一个 PopTip
    public static boolean onlyOnePopTip = true;
    
    //默认按钮文字样式
    public static TextInfo buttonTextInfo;
    
    //默认确定按钮文字样式
    public static TextInfo okButtonTextInfo;
    
    //默认标题文字样式
    public static TextInfo titleTextInfo;
    
    //默认内容文字样式
    public static TextInfo messageTextInfo;
    
    //默认 WaitDialog 和 TipDialog 文字样式
    public static TextInfo tipTextInfo;
    
    //默认输入框文字样式
    public static InputInfo inputInfo;
    
    //默认底部菜单、对话框的标题文字样式
    public static TextInfo menuTitleInfo;
    
    //默认底部菜单文本样式
    public static TextInfo menuTextInfo;
    
    //默认对话框背景颜色（值为 ColorInt，为-1不生效）
    public static int backgroundColor = -1;
    
    //默认 TipDialog 和 WaitDialog 背景颜色（值为 ColorInt，为-1不生效）
    public static int tipBackgroundColor = -1;
    
    /**
     * 重写 TipDialog 和 WaitDialog 进度动画颜色，
     * 注意此属性为覆盖性质，即设置此值将替换提示框原本的进度动画的颜色，包括亮暗色切换的颜色变化也将被替代
     * （值为 ColorInt，为-1不生效）
     */
    public static int tipProgressColor = -1;
    
    //默认对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
    public static boolean cancelable = true;
    
    //默认提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
    public static boolean cancelableTipDialog = false;
    
    //默认取消按钮文本文字，影响 BottomDialog
    public static String cancelButtonText ;
    
    //默认 PopTip 文本样式
    public static TextInfo popTextInfo;
    
    //全局 Dialog 生命周期监听器
    public static DialogLifecycleCallback<BaseDialog> dialogLifeCycleListener;
    
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
