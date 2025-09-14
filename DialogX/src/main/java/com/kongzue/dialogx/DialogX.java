package com.kongzue.dialogx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.DialogListBuilder;
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
    
    public static final String ERROR_INIT_TIPS = "DialogX.init: 初始化异常，context 为 null 或未初始化，详情请查看 https://github.com/kongzue/DialogX/wiki";
    
    //开启日志
    public static boolean DEBUGMODE = true;
    
    //全局主题风格
    public static DialogXStyle globalStyle = MaterialStyle.style();
    
    //全局对话框明暗风格
    public static DialogX.THEME globalTheme = DialogX.THEME.LIGHT;
    
    //TipDialog 和 WaitDialog 明暗风格，不设置则默认根据 globalTheme 定义
    public static DialogX.THEME tipTheme;
    
    //DialogX 实现模式（实验性功能）
    public static IMPL_MODE implIMPLMode = IMPL_MODE.VIEW;
    
    //对话框最大宽度（像素）
    public static int dialogMaxWidth;
    
    //对话框最大高度（像素）
    public static int dialogMaxHeight;
    
    //对话框最小宽度（像素）
    public static int dialogMinWidth;
    
    //对话框最小高度（像素）
    public static int dialogMinHeight;
    
    //是否允许 InputDialog 自动弹出键盘
    public static boolean autoShowInputKeyboard = true;
    
    //同时只显示一个 PopTip
    public static boolean onlyOnePopTip = false;
    
    //同时只显示一个 PopNotification
    public static boolean onlyOnePopNotification = true;
    
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
    
    //默认对话框背景颜色（值为 ColorInt，为 null 不生效）
    public static Integer backgroundColor = null;
    
    //默认 TipDialog 和 WaitDialog 背景颜色（值为 ColorInt，为 null 不生效）
    public static Integer tipBackgroundColor = null;
    
    /**
     * 重写 TipDialog 和 WaitDialog 进度动画颜色，
     * 注意此属性为覆盖性质，即设置此值将替换提示框原本的进度动画的颜色，包括亮暗色切换的颜色变化也将被替代
     * （值为 ColorInt，为 null 不生效）
     */
    public static Integer tipProgressColor = null;
    
    //默认对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
    public static boolean cancelable = true;
    
    //默认提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
    public static boolean cancelableTipDialog = false;
    
    //默认取消按钮文本文字，影响 BottomDialog
    public static String cancelButtonText;
    
    //默认 PopTip 文本样式
    public static TextInfo popTextInfo;
    
    //默认启动对话框动画时长
    public static long enterAnimDuration = -1;
    
    //默认关闭对话框动画时长
    public static long exitAnimDuration = -1;
    
    //全局 Dialog 生命周期监听器
    public static DialogLifecycleCallback<BaseDialog> dialogLifeCycleListener;
    
    //是否自动在主线程执行
    public static boolean autoRunOnUIThread = true;
    
    //使用振动反馈
    public static boolean useHaptic = true;
    
    /**
     * 声明：若 Activity 已使用沉浸式适配请开启（已废弃）
     * <p>
     * 请注意，若你没有使用沉浸式适配，请关闭此选项，此选项将影响对话框布局是否允许延伸至导航栏背后显示
     */
    @Deprecated
    public static boolean useActivityLayoutTranslationNavigationBar = false;
    
    /**
     * 设置 BottomDialog 导航栏背景颜色
     * 彩蛋：a_man 私人定制款属性
     */
    public static int bottomDialogNavbarColor = Color.TRANSPARENT;
    
    //触摸滑动触发阈值，影响 BottomDialog、FullScreenDialog 下滑关闭触发距离，单位：像素
    public static int touchSlideTriggerThreshold = dip2px(35);
    
    //Window 模式使用全局悬浮窗，需要 SYSTEM_ALERT_WINDOW 权限
    public static boolean globalHoverWindow = false;
    
    //部分插屏广告 SDK 可能出现背景黑屏的问题，在这里配置需要 DialogX 屏蔽的 Activity 的包名以屏蔽对该 activity 的支持：
    public static String[] unsupportedActivitiesPackageNames = new String[]{
            "com.bytedance.sdk.openadsdk.stub.activity",
            "com.mobile.auth.gatewayauth",
            "com.google.android.gms.ads"
    };

    public static int defaultMessageDialogBackgroundRadius = -1;

    public static int defaultBottomDialogBackgroundRadius = -1;

    public static int defaultFullScreenDialogBackgroundRadius = -1;

    public static int defaultWaitAndTipDialogBackgroundRadius = -1;

    public static int defaultPopMenuBackgroundRadius = -1;

    public static int defaultPopTipBackgroundRadius = -1;

    public static int defaultPopNotificationBackgroundRadius = -1;

    //开启沉浸式适配
    public static boolean enableImmersiveMode = true;

    //沉浸式忽略左右的非安全区
    public static boolean ignoreUnsafeInsetsHorizontal = false;

    public enum THEME {
        LIGHT, DARK, AUTO
    }
    
    public enum IMPL_MODE {
        VIEW, WINDOW, DIALOG_FRAGMENT, FLOATING_ACTIVITY
    }
    
    public static void init(Context context) {
        if (context == null) {
            error(ERROR_INIT_TIPS);
            return;
        }
        BaseDialog.init(context);
    }
    
    public static void error(Object o) {
        if (DEBUGMODE) Log.e(">>>", o.toString());
    }
    
    private static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public static DialogListBuilder showDialogList(BaseDialog... dialogs) {
        return DialogListBuilder.create(dialogs).show();
    }

    // 默认消息对话框标题文本
    public static CharSequence defaultMessageDialogTitleText;

    // 等待提示框默认文本
    public static CharSequence defaultWaitDialogWaitingText;

    // 成功提示框默认文本
    public static CharSequence defaultTipDialogSuccessText;

    // 错误提示框默认文本
    public static CharSequence defaultTipDialogErrorText;

    // 警告提示框默认文本
    public static CharSequence defaultTipDialogWarningText;

    // 销毁对话框时自动回收内存
    public static boolean autoGC;
}
