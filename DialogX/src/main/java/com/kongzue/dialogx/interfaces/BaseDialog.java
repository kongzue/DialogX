package com.kongzue.dialogx.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.impl.ActivityLifecycleImpl;
import com.kongzue.dialogx.impl.DialogFragmentImpl;
import com.kongzue.dialogx.util.ActivityRunnable;
import com.kongzue.dialogx.util.DialogXFloatingWindowActivity;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.WindowUtil;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    protected static WeakReference<Thread> uiThread;
    private static WeakReference<FrameLayout> rootFrameLayout;
    private static WeakReference<Activity> contextWeakReference;
    protected WeakReference<Activity> ownActivity;
    private static List<BaseDialog> runningDialogList;
    private WeakReference<View> dialogView;
    protected WeakReference<DialogFragmentImpl> ownDialogFragmentImpl;
    protected DialogX.IMPL_MODE dialogImplMode = DialogX.implIMPLMode;
    protected WeakReference<DialogXFloatingWindowActivity> floatingWindowActivity;
    
    public static void init(Context context) {
        if (context == null) context = ActivityLifecycleImpl.getTopActivity();
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
            uiThread = new WeakReference<>(Thread.currentThread());
            contextWeakReference = new WeakReference<>(activity);
            rootFrameLayout = new WeakReference<>((FrameLayout) activity.getWindow().getDecorView());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                publicWindowInsets(rootFrameLayout.get().getRootWindowInsets());
            }
        } catch (Exception e) {
            e.printStackTrace();
            error("DialogX.init: 初始化异常，找不到Activity的根布局");
        }
    }
    
    protected static void log(Object o) {
        if (DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    protected static void error(Object o) {
        if (DEBUGMODE) Log.e(">>>", o.toString());
    }
    
    public static void onActivityResume(Activity activity) {
        if (runningDialogList != null) {
            CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
            for (int i = copyOnWriteList.size() - 1; i >= 0; i--) {
                BaseDialog baseDialog = copyOnWriteList.get(i);
                if (baseDialog.getActivity() == activity && baseDialog.isShow && baseDialog.getDialogView() != null) {
                    View boxRoot = baseDialog.getDialogView().findViewById(R.id.box_root);
                    if (boxRoot instanceof DialogXBaseRelativeLayout) {
                        if (((DialogXBaseRelativeLayout) boxRoot).isBaseFocusable()) {
                            boxRoot.requestFocus();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private static void requestDialogFocus() {
        if (getContext() instanceof Activity) {
            onActivityResume((Activity) getContext());
        }
    }
    
    public abstract void restartDialog();
    
    protected static void show(final View view) {
        if (view == null) return;
        final BaseDialog baseDialog = (BaseDialog) view.getTag();
        if (baseDialog != null) {
            if (baseDialog.isShow) {
                if (baseDialog.getDialogView() != null) {
                    baseDialog.getDialogView().setVisibility(View.VISIBLE);
                    return;
                }
                error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                return;
            }
            baseDialog.ownActivity = new WeakReference<>(contextWeakReference.get());
            baseDialog.dialogView = new WeakReference<>(view);
            
            log(baseDialog.dialogKey() + ".show");
            addDialogToRunningList(baseDialog);
            
            switch (baseDialog.dialogImplMode) {
                case WINDOW:
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            WindowUtil.show(contextWeakReference.get(), view, !(baseDialog instanceof PopTip));
                        }
                    });
                    break;
                case DIALOG_FRAGMENT:
                    DialogFragmentImpl dialogFragment = new DialogFragmentImpl(baseDialog, view);
                    dialogFragment.show(getSupportFragmentManager(contextWeakReference.get()), "DialogX");
                    baseDialog.ownDialogFragmentImpl = new WeakReference<>(dialogFragment);
                    break;
                case FLOATING_ACTIVITY:
                    if (waitRunDialogX == null) {
                        waitRunDialogX = new HashMap<>();
                    }
                    waitRunDialogX.put(baseDialog.dialogKey(), new ActivityRunnable() {
                        @Override
                        public void run(Activity activity) {
                            baseDialog.floatingWindowActivity = new WeakReference<>((DialogXFloatingWindowActivity) activity);
                            final FrameLayout activityRootView = (FrameLayout) activity.getWindow().getDecorView();
                            if (activityRootView == null) {
                                return;
                            }
                            runOnMain(new Runnable() {
                                @Override
                                public void run() {
                                    if (view.getParent() == rootFrameLayout.get()) {
                                        error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                                        return;
                                    }
                                    if (view.getParent() != null) {
                                        ((ViewGroup) view.getParent()).removeView(view);
                                    }
                                    activityRootView.addView(view);
                                }
                            });
                        }
                    });
                    DialogXFloatingWindowActivity dialogXFloatingWindowActivity = DialogXFloatingWindowActivity.getDialogXFloatingWindowActivity();
                    if (dialogXFloatingWindowActivity != null && dialogXFloatingWindowActivity.isSameFrom(contextWeakReference.get().hashCode())) {
                        dialogXFloatingWindowActivity.showDialogX(baseDialog.dialogKey());
                        return;
                    }
                    Intent intent = new Intent(contextWeakReference.get(), DialogXFloatingWindowActivity.class);
                    intent.putExtra("dialogXKey", baseDialog.dialogKey());
                    intent.putExtra("fromActivityUiStatus", contextWeakReference.get().getWindow().getDecorView().getSystemUiVisibility());
                    intent.putExtra("from", contextWeakReference.get().hashCode());
                    contextWeakReference.get().startActivity(intent);
                    int version = Integer.valueOf(Build.VERSION.SDK_INT);
                    if (version > 5) {
                        contextWeakReference.get().overridePendingTransition(0, 0);
                    }
                    break;
                default:
                    if (rootFrameLayout == null || rootFrameLayout.get() == null) return;
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getParent() == rootFrameLayout.get()) {
                                error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                                return;
                            }
                            if (view.getParent() != null) {
                                ((ViewGroup) view.getParent()).removeView(view);
                            }
                            rootFrameLayout.get().addView(view);
                        }
                    });
                    break;
            }
        }
    }
    
    private static FragmentManager getSupportFragmentManager(Activity activity) {
        return (activity instanceof AppCompatActivity) ? ((AppCompatActivity) activity).getSupportFragmentManager() : null;
    }
    
    private static Map<String, ActivityRunnable> waitRunDialogX;
    
    public static ActivityRunnable getActivityRunnable(String dialogXKey) {
        if (dialogXKey == null) return null;
        return waitRunDialogX.get(dialogXKey);
    }
    
    protected static void show(final Activity activity, final View view) {
        if (activity == null || view == null) return;
        if (contextWeakReference == null || contextWeakReference.get() == null) {
            initActivityContext(activity);
        }
        final BaseDialog baseDialog = (BaseDialog) view.getTag();
        if (baseDialog != null) {
            if (baseDialog.getDialogView() != null) {
                baseDialog.getDialogView().setVisibility(View.VISIBLE);
            }
            if (baseDialog.isShow) {
                error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                return;
            }
            if (activity.isDestroyed()) {
                error(((BaseDialog) view.getTag()).dialogKey() + ".show ERROR: activity is Destroyed.");
                return;
            }
            baseDialog.ownActivity = new WeakReference<>(activity);
            baseDialog.dialogView = new WeakReference<>(view);
            
            log(baseDialog + ".show");
            addDialogToRunningList(baseDialog);
            switch (baseDialog.dialogImplMode) {
                case WINDOW:
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            WindowUtil.show(activity, view, !(baseDialog instanceof PopTip));
                        }
                    });
                    break;
                case DIALOG_FRAGMENT:
                    DialogFragmentImpl dialogFragment = new DialogFragmentImpl(baseDialog, view);
                    dialogFragment.show(getSupportFragmentManager(activity), "DialogX");
                    baseDialog.ownDialogFragmentImpl = new WeakReference<>(dialogFragment);
                    break;
                case FLOATING_ACTIVITY:
                    if (waitRunDialogX == null) {
                        waitRunDialogX = new HashMap<>();
                    }
                    waitRunDialogX.put(baseDialog.dialogKey(), new ActivityRunnable() {
                        @Override
                        public void run(Activity activity) {
                            baseDialog.floatingWindowActivity = new WeakReference<>((DialogXFloatingWindowActivity) activity);
                            final FrameLayout activityRootView = (FrameLayout) activity.getWindow().getDecorView();
                            if (activityRootView == null) {
                                return;
                            }
                            runOnMain(new Runnable() {
                                @Override
                                public void run() {
                                    if (view.getParent() == rootFrameLayout.get()) {
                                        error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                                        return;
                                    }
                                    if (view.getParent() != null) {
                                        ((ViewGroup) view.getParent()).removeView(view);
                                    }
                                    activityRootView.addView(view);
                                }
                            });
                        }
                    });
                    DialogXFloatingWindowActivity dialogXFloatingWindowActivity = DialogXFloatingWindowActivity.getDialogXFloatingWindowActivity();
                    if (dialogXFloatingWindowActivity != null && dialogXFloatingWindowActivity.isSameFrom(activity.hashCode())) {
                        dialogXFloatingWindowActivity.showDialogX(baseDialog.dialogKey());
                        return;
                    }
                    Intent intent = new Intent(activity, DialogXFloatingWindowActivity.class);
                    intent.putExtra("dialogXKey", baseDialog.dialogKey());
                    intent.putExtra("from", activity.hashCode());
                    intent.putExtra("fromActivityUiStatus", activity.getWindow().getDecorView().getSystemUiVisibility());
                    activity.startActivity(intent);
                    int version = Integer.valueOf(Build.VERSION.SDK_INT);
                    if (version > 5) {
                        activity.overridePendingTransition(0, 0);
                    }
                    break;
                default:
                    final FrameLayout activityRootView = (FrameLayout) activity.getWindow().getDecorView();
                    if (activityRootView == null) {
                        return;
                    }
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getParent() == rootFrameLayout.get()) {
                                error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                                return;
                            }
                            if (view.getParent() != null) {
                                ((ViewGroup) view.getParent()).removeView(view);
                            }
                            activityRootView.addView(view);
                        }
                    });
                    break;
            }
        }
    }
    
    protected static void dismiss(final View dialogView) {
        if (dialogView == null) return;
        final BaseDialog baseDialog = (BaseDialog) dialogView.getTag();
        log(baseDialog.dialogKey() + ".dismiss");
        removeDialogToRunningList(baseDialog);
        if (baseDialog.dialogView != null) baseDialog.dialogView.clear();
        
        switch (baseDialog.dialogImplMode) {
            case WINDOW:
                runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        WindowUtil.dismiss(dialogView);
                    }
                });
                break;
            case DIALOG_FRAGMENT:
                if (baseDialog.ownDialogFragmentImpl != null && baseDialog.ownDialogFragmentImpl.get() != null) {
                    baseDialog.ownDialogFragmentImpl.get().dismiss();
                }
                break;
            case FLOATING_ACTIVITY:
                if (baseDialog.floatingWindowActivity != null && baseDialog.floatingWindowActivity.get() != null) {
                    FrameLayout rootView = ((FrameLayout) baseDialog.floatingWindowActivity.get().getWindow().getDecorView());
                    if (rootView != null) rootView.removeView(dialogView);
                    baseDialog.floatingWindowActivity.get().finish(baseDialog.dialogKey());
                    requestDialogFocus();
                }
                break;
            default:
                runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        if (dialogView.getParent() == null || !(dialogView.getParent() instanceof ViewGroup)) {
                            if (rootFrameLayout == null) return;
                            rootFrameLayout.get().removeView(dialogView);
                        } else {
                            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                        }
                        requestDialogFocus();
                    }
                }, true);
                break;
        }
    }
    
    private static void addDialogToRunningList(BaseDialog baseDialog) {
        if (runningDialogList == null) runningDialogList = new CopyOnWriteArrayList<>();
        runningDialogList.add(baseDialog);
    }
    
    private static void removeDialogToRunningList(BaseDialog baseDialog) {
        if (runningDialogList != null) runningDialogList.remove(baseDialog);
    }
    
    public static Context getContext() {
        if (contextWeakReference == null) {
            init(null);
            if (contextWeakReference == null) {
                return ActivityLifecycleImpl.getTopActivity();
            }
            return contextWeakReference.get();
        }
        return contextWeakReference.get();
    }
    
    /**
     * 自动执行，不建议自行调用此方法
     *
     * @hide
     */
    public static void cleanContext() {
        if (contextWeakReference != null) contextWeakReference.clear();
        contextWeakReference = null;
        System.gc();
    }
    
    protected abstract void shutdown();
    
    protected boolean cancelable = true;
    protected OnBackPressedListener onBackPressedListener;
    protected boolean isShow;
    protected DialogXStyle style;
    protected DialogX.THEME theme;
    protected boolean autoShowInputKeyboard;
    protected int backgroundColor = -1;
    protected long enterAnimDuration = -1;
    protected long exitAnimDuration = -1;
    protected int maxWidth;
    
    public BaseDialog() {
        cancelable = DialogX.cancelable;
        style = DialogX.globalStyle;
        theme = DialogX.globalTheme;
        enterAnimDuration = DialogX.enterAnimDuration;
        exitAnimDuration = DialogX.exitAnimDuration;
        autoShowInputKeyboard = DialogX.autoShowInputKeyboard;
    }
    
    public abstract boolean isCancelable();
    
    public View createView(int layoutId) {
        if (getContext() == null) {
            error("DialogX 未初始化。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return null;
        }
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
    
    public static void useTextInfo(TextView textView, TextInfo textInfo) {
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
        if (textInfo.isShowEllipsis()) {
            textView.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            textView.setEllipsize(null);
        }
        if (textInfo.getMaxLines() != -1) {
            textView.setMaxLines(textInfo.getMaxLines());
        } else {
            textView.setMaxLines(Integer.MAX_VALUE);
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
            if (getContext() == null) return theme == DialogX.THEME.LIGHT;
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
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
    
    /**
     * 此标记用于拦截重复 dismiss 指令导致的关闭动画抖动异常
     */
    protected boolean dismissAnimFlag;
    
    protected void beforeShow() {
        dismissAnimFlag = false;
        if (getContext() == null) {
            init(null);
            if (getContext() == null) {
                error("DialogX 未初始化。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
                return;
            }
        }
        if (style.styleVer != DialogXStyle.styleVer) {
            error("DialogX 所引用的 Style 不符合当前适用版本：" + DialogXStyle.styleVer + " 引入的 Style(" + style.getClass().getSimpleName() + ") 版本" + style.styleVer);
        }
        
        //Hide IME
        View view = ((Activity) BaseDialog.getContext()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) BaseDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        if (!DialogX.autoRunOnUIThread || (uiThread != null && Thread.currentThread() == uiThread.get())) {
            runnable.run();
            return;
        }
        runOnMain(runnable, true);
    }
    
    protected static void runOnMain(Runnable runnable, boolean needWaitMainLooper) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
    
    protected static void runOnMainDelay(Runnable runnable, long delay) {
        if (!DialogX.autoRunOnUIThread) runnable.run();
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delay);
    }
    
    public View getDialogView() {
        if (dialogView == null) return null;
        return dialogView.get();
    }
    
    public Activity getActivity() {
        return ownActivity == null ? null : ownActivity.get();
    }
    
    protected void cleanActivityContext() {
        if (ownActivity != null) ownActivity.clear();
        ownActivity = null;
    }
    
    public static void cleanAll() {
        if (runningDialogList != null) {
            CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
            for (BaseDialog baseDialog : copyOnWriteList) {
                if (baseDialog.isShow()) baseDialog.shutdown();
                baseDialog.cleanActivityContext();
                runningDialogList.remove(baseDialog);
            }
        }
    }
    
    public static void recycleDialog(Activity activity) {
        switch (DialogX.implIMPLMode) {
            case WINDOW:
                if (runningDialogList != null) {
                    CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
                    for (BaseDialog baseDialog : copyOnWriteList) {
                        if (baseDialog.getActivity() == activity && baseDialog.dialogView != null) {
                            WindowUtil.dismiss(baseDialog.dialogView.get());
                        }
                    }
                }
                break;
            case DIALOG_FRAGMENT:
                if (runningDialogList != null) {
                    CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
                    for (BaseDialog baseDialog : copyOnWriteList) {
                        if (baseDialog.getActivity() == activity && baseDialog.ownDialogFragmentImpl != null && baseDialog.ownDialogFragmentImpl.get() != null) {
                            baseDialog.ownDialogFragmentImpl.get().dismiss();
                        }
                    }
                }
                break;
            case FLOATING_ACTIVITY:
                
                break;
            default:
                if (runningDialogList != null) {
                    CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
                    for (BaseDialog baseDialog : copyOnWriteList) {
                        if (baseDialog.getActivity() == activity) {
                            baseDialog.cleanActivityContext();
                            runningDialogList.remove(baseDialog);
                        }
                    }
                }
                break;
        }
        if (activity == getContext()) {
            cleanContext();
        }
    }
    
    public static List<BaseDialog> getRunningDialogList() {
        if (runningDialogList == null) return new ArrayList<>();
        return new CopyOnWriteArrayList<>(runningDialogList);
    }
    
    protected void imeShow(EditText editText, boolean show) {
        if (getContext() == null) return;
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
    
    public int getMaxWidth() {
        if (maxWidth == 0) return DialogX.dialogMaxWidth;
        return maxWidth;
    }
    
    public DialogX.IMPL_MODE getDialogImplMode() {
        return dialogImplMode;
    }
    
    protected static WindowInsets windowInsets;
    
    public static WindowInsets publicWindowInsets() {
        return windowInsets;
    }
    
    public static void publicWindowInsets(WindowInsets windowInsets) {
        if (windowInsets != null) BaseDialog.windowInsets = windowInsets;
        if (runningDialogList != null) {
            CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
            for (int i = copyOnWriteList.size() - 1; i >= 0; i--) {
                BaseDialog baseDialog = copyOnWriteList.get(i);
                if (baseDialog.isShow && baseDialog.getDialogView() != null) {
                    View boxRoot = baseDialog.getDialogView().findViewById(R.id.box_root);
                    if (boxRoot instanceof DialogXBaseRelativeLayout) {
                        ((DialogXBaseRelativeLayout) boxRoot).paddingView(windowInsets);
                    }
                }
            }
        }
    }
    
    protected void bindFloatingActivity(DialogXFloatingWindowActivity activity) {
        floatingWindowActivity = new WeakReference<>(activity);
    }
}
