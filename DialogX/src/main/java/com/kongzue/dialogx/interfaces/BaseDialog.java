package com.kongzue.dialogx.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.impl.ActivityLifecycleImpl;
import com.kongzue.dialogx.util.TextInfo;

import java.lang.ref.WeakReference;

import static com.kongzue.dialogx.DialogX.DEBUGMODE;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 14:10
 */
public class BaseDialog {
    
    private static WeakReference<FrameLayout> rootFrameLayout;
    private static WeakReference<Activity> contextWeakReference;
    
    public static void init(Context context) {
        ActivityLifecycleImpl.init(context, new ActivityLifecycleImpl.onActivityResumeCallBack() {
            @Override
            public void getActivity(Activity activity) {
                try {
                    contextWeakReference = new WeakReference<>(activity);
                    rootFrameLayout = new WeakReference<>((FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content));
                } catch (Exception e) {
                    error("DialogX.init: 初始化异常，找不到Activity的根布局");
                }
            }
        });
    }
    
    public static void log(Object o) {
        if (DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    public static void error(Object o) {
        if (DEBUGMODE) Log.e(">>>", o.toString());
    }
    
    public static void show(final View view) {
        if (rootFrameLayout == null || view == null || rootFrameLayout.get() == null) return;
        view.setTag(rootFrameLayout.get());
        rootFrameLayout.get().post(new Runnable() {
            @Override
            public void run() {
                rootFrameLayout.get().addView(view);
            }
        });
    }
    
    public static void show(Activity activity, final View view) {
        if (activity == null || view == null) return;
        final ViewGroup activityRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (activityRootView == null) return;
        view.setTag(activityRootView);
        activityRootView.post(new Runnable() {
            @Override
            public void run() {
                activityRootView.addView(view);
            }
        });
    }
    
    public static void dismiss(View dialogView) {
        if (rootFrameLayout == null || dialogView == null) return;
        if (dialogView.getTag() == null || !(dialogView.getTag() instanceof ViewGroup)) {
            rootFrameLayout.get().removeView(dialogView);
        } else {
            ((ViewGroup) dialogView.getTag()).removeView(dialogView);
        }
    }
    
    public static Context getContext() {
        if (contextWeakReference == null) return null;
        return contextWeakReference.get();
    }
    
    public static void cleanContext() {
        contextWeakReference.clear();
        contextWeakReference = null;
        System.gc();
    }
    
    protected boolean cancelable = true;
    protected OnBackPressedListener onBackPressedListener;
    protected boolean isShow;
    protected DialogXStyle style;
    protected DialogX.THEME theme;
    protected boolean autoShowInputKeyboard;
    protected int backgroundColor = -1;
    
    public BaseDialog() {
        cancelable = DialogX.cancelable;
        style = DialogX.globalStyle;
        theme = DialogX.globalTheme;
        autoShowInputKeyboard = DialogX.autoShowInputKeyboard;
    }
    
    public View createView(int layoutId) {
        return LayoutInflater.from(getContext()).inflate(layoutId, null);
    }
    
    public boolean isShow() {
        return isShow;
    }
    
    public DialogXStyle getStyle() {
        return style;
    }
    
    public DialogX.THEME getTheme() {
        return theme;
    }
    
    protected void useTextInfo(TextView textView, TextInfo textInfo) {
        if (textInfo == null) return;
        if (textView == null) return;
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textInfo.getFontSize());
        }
        if (textInfo.getFontColor() != 1) {
            textView.setTextColor(textInfo.getFontColor());
        }
        if (textInfo.getGravity() != -1) {
            textView.setGravity(textInfo.getGravity());
        }
        textView.getPaint().setFakeBoldText(textInfo.isBold());
    }
    
    protected void showText(TextView textView, CharSequence text) {
        if (textView == null) return;
        if (text == null) {
            textView.setVisibility(View.GONE);
            textView.setText("");
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }
    
    protected View createHorizontalSplitView(int color) {
        View splitView = new View(getContext());
        splitView.setBackgroundColor(color);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        splitView.setLayoutParams(lp);
        return splitView;
    }
    
    protected View createVerticalSplitView(int color, int height) {
        View splitView = new View(getContext());
        splitView.setBackgroundColor(color);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1, dip2px(height));
        splitView.setLayoutParams(lp);
        return splitView;
    }
    
    public static boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || "null".equals(s) || "(null)".equals(s)) {
            return true;
        }
        return false;
    }
    
    public static boolean isNull(CharSequence c) {
        String s = String.valueOf(c);
        if (c == null || s.trim().isEmpty() || "null".equals(s) || "(null)".equals(s)) {
            return true;
        }
        return false;
    }
    
    public Resources getResources() {
        if (getContext() == null) return Resources.getSystem();
        return getContext().getResources();
    }
    
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public boolean isLightTheme() {
        return theme == DialogX.THEME.LIGHT;
    }
    
    public static FrameLayout getRootFrameLayout() {
        if (rootFrameLayout == null) return null;
        return rootFrameLayout.get();
    }
    
    public void tintColor(View view, int color) {
        view.setBackgroundTintList(ColorStateList.valueOf(color));
    }
    
    protected void beforeShow() {
        if (getContext() == null) {
            error("DialogX 未初始化。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
        }
        if (style.styleVer != DialogXStyle.styleVer) {
            error("DialogX 所引用的 Style 不符合当前适用版本：" + DialogXStyle.styleVer + " 引入的 Style(" + style.getClass().getSimpleName() + ") 版本" + style.styleVer);
        }
    }
    
    protected String getString(int titleResId) {
        if (getContext() == null) {
            error("DialogX 未初始化。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return null;
        }
        return getContext().getString(titleResId);
    }
    
    protected int getColor(int backgroundRes) {
        if (getContext() == null) {
            error("DialogX 未初始化。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return Color.BLACK;
        }
        return getContext().getResources().getColor(backgroundRes);
    }
}
