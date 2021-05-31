package com.kongzue.dialogx.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatDelegate;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.impl.ActivityLifecycleImpl;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.WindowUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kongzue.dialogx.DialogX.DEBUGMODE;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 14:10
 */
public abstract class BaseDialog {
    
    private static WeakReference<FrameLayout> rootFrameLayout;
    private static WeakReference<Activity> contextWeakReference;
    protected WeakReference<Activity> ownActivity;
    private static List<BaseDialog> runningDialogList;
    private WeakReference<View> dialogView;
    
    public static void init(Context context) {
        if (context instanceof Activity) {
            initActivityContext((Activity) context);
        }
        ActivityLifecycleImpl.init(context, new ActivityLifecycleImpl.onActivityResumeCallBack() {
            @Override
            public void getActivity(Activity activity) {
                initActivityContext(activity);
            }
        });
    }
    
    private static void initActivityContext(Activity activity) {
        try {
            contextWeakReference = new WeakReference<>(activity);
            rootFrameLayout = new WeakReference<>((FrameLayout) activity.getWindow().getDecorView());
        } catch (Exception e) {
            error("DialogX.init: 初始化异常，找不到Activity的根布局");
        }
    }
    
    protected static void log(Object o) {
        if (DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    protected static void error(Object o) {
        if (DEBUGMODE) Log.e(">>>", o.toString());
    }
    
    public abstract void onUIModeChange(Configuration newConfig);
    
    protected static void show(final View view) {
        if (view == null) return;
        final BaseDialog baseDialog = (BaseDialog) view.getTag();
        baseDialog.ownActivity = new WeakReference<>(contextWeakReference.get());
        baseDialog.dialogView = new WeakReference<>(view);
        
        log(baseDialog.dialogKey() + ".show");
        addDialogToRunningList(baseDialog);
        if (DialogX.implIMPLMode == DialogX.IMPL_MODE.VIEW) {
            if (rootFrameLayout == null || rootFrameLayout.get() == null) return;
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    rootFrameLayout.get().addView(view);
                }
            });
        } else {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    WindowUtil.show(contextWeakReference.get(), view, !(baseDialog instanceof PopTip));
                }
            });
        }
    }
    
    protected static void show(final Activity activity, final View view) {
        if (activity == null || view == null) return;
        if (activity.isDestroyed()) {
            error(((BaseDialog) view.getTag()).dialogKey() + ".show ERROR: activity is Destroyed.");
            return;
        }
        final BaseDialog baseDialog = (BaseDialog) view.getTag();
        baseDialog.ownActivity = new WeakReference<>(activity);
        baseDialog.dialogView = new WeakReference<>(view);
        
        log(baseDialog + ".show");
        addDialogToRunningList(baseDialog);
        if (DialogX.implIMPLMode == DialogX.IMPL_MODE.VIEW) {
            final FrameLayout activityRootView = (FrameLayout) activity.getWindow().getDecorView();
            if (activityRootView == null) {
                return;
            }
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    activityRootView.addView(view);
                }
            });
        } else {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    WindowUtil.show(activity, view, !(baseDialog instanceof PopTip));
                }
            });
        }
    }
    
    protected static void dismiss(final View dialogView) {
        if (dialogView == null) return;
        final BaseDialog baseDialog = (BaseDialog) dialogView.getTag();
        log(baseDialog.dialogKey() + ".dismiss");
        removeDialogToRunningList(baseDialog);
        if (baseDialog.dialogView != null) baseDialog.dialogView.clear();
        if (DialogX.implIMPLMode == DialogX.IMPL_MODE.VIEW) {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    if (dialogView.getParent() == null || !(dialogView.getParent() instanceof ViewGroup)) {
                        if (rootFrameLayout == null) return;
                        rootFrameLayout.get().removeView(dialogView);
                    } else {
                        ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                    }
                }
            });
        } else {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    WindowUtil.dismiss(dialogView);
                }
            });
        }
    }
    
    private static void addDialogToRunningList(BaseDialog baseDialog) {
        if (runningDialogList == null) runningDialogList = new ArrayList<>();
        runningDialogList.add(baseDialog);
    }
    
    private static void removeDialogToRunningList(BaseDialog baseDialog) {
        if (runningDialogList != null) runningDialogList.remove(baseDialog);
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
    protected long enterAnimDuration = -1;
    protected long exitAnimDuration = -1;
    
    public BaseDialog() {
        cancelable = DialogX.cancelable;
        style = DialogX.globalStyle;
        theme = DialogX.globalTheme;
        enterAnimDuration = DialogX.enterAnimDuration;
        exitAnimDuration = DialogX.exitAnimDuration;
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
        if (isNull(text)) {
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
        if (theme == DialogX.THEME.AUTO) {
            return (getContext().getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO;
        }
        return theme == DialogX.THEME.LIGHT;
    }
    
    public static FrameLayout getRootFrameLayout() {
        if (rootFrameLayout == null) {
            error("DialogX 未初始化。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return null;
        }
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
        return getResources().getColor(backgroundRes);
    }
    
    public enum BOOLEAN {
        TRUE, FALSE
    }
    
    public abstract String dialogKey();
    
    protected static void runOnMain(Runnable runnable) {
        if (!DialogX.autoRunOnUIThread) runnable.run();
        new Handler(Looper.getMainLooper()).post(runnable);
    }
    
    public Activity getActivity() {
        return ownActivity == null ? null : ownActivity.get();
    }
    
    protected void cleanActivityContext() {
        if (ownActivity != null) ownActivity.clear();
    }
    
    public static void recycleDialog(Activity activity) {
        if (DialogX.implIMPLMode == DialogX.IMPL_MODE.WINDOW) {
            if (runningDialogList != null) {
                CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
                for (BaseDialog baseDialog : copyOnWriteList) {
                    if (baseDialog.getActivity() == activity && baseDialog.dialogView != null) {
                        WindowUtil.dismiss(baseDialog.dialogView.get());
                    }
                }
            }
        }
    }
}
