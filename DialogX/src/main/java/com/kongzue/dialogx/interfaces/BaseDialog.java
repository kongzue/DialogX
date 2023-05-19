package com.kongzue.dialogx.interfaces;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.impl.ActivityLifecycleImpl;
import com.kongzue.dialogx.impl.DialogFragmentImpl;
import com.kongzue.dialogx.util.ActivityRunnable;
import com.kongzue.dialogx.util.DialogListBuilder;
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
public abstract class BaseDialog implements LifecycleOwner {

    private static Thread uiThread;
    private static WeakReference<Activity> activityWeakReference;
    protected WeakReference<Activity> ownActivity;
    private WeakReference<FrameLayout> rootFrameLayout;
    private static List<BaseDialog> runningDialogList;
    private WeakReference<View> dialogView;
    protected WeakReference<DialogFragmentImpl> ownDialogFragmentImpl;
    protected DialogX.IMPL_MODE dialogImplMode = DialogX.implIMPLMode;
    protected WeakReference<DialogXFloatingWindowActivity> floatingWindowActivity;
    private WeakReference<DialogListBuilder> dialogListBuilder;
    protected LifecycleRegistry lifecycle = new LifecycleRegistry(this);

    public enum BUTTON_SELECT_RESULT {
        NONE,           //未做出选择
        BUTTON_OK,      //选择了确定按钮
        BUTTON_CANCEL,  //选择了取消按钮
        BUTTON_OTHER    //选择了其他按钮
    }

    public static void init(Context context) {
        if (context == null) {
            context = ActivityLifecycleImpl.getTopActivity();
        }
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
        if (ActivityLifecycleImpl.isExemptActivities(activity)) {
            return;
        }
        try {
            uiThread = Looper.getMainLooper().getThread();
            activityWeakReference = new WeakReference<>(activity);
        } catch (Exception e) {
            e.printStackTrace();
            error("DialogX.init: 初始化异常，找不到Activity的根布局");
        }
    }

    protected static void log(Object o) {
        if (DEBUGMODE) {
            Log.i(">>>", o.toString());
        }
    }

    protected static void error(Object o) {
        if (DEBUGMODE) {
            Log.e(">>>", o.toString());
        }
    }

    public static void onActivityResume(Activity activity) {
        if (runningDialogList != null) {
            CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
            for (int i = copyOnWriteList.size() - 1; i >= 0; i--) {
                BaseDialog baseDialog = copyOnWriteList.get(i);
                if (baseDialog.getOwnActivity() == activity && baseDialog.isShow && baseDialog.getDialogView() != null) {
                    View boxRoot = baseDialog.getDialogView().findViewById(R.id.box_root);
                    if (boxRoot instanceof DialogXBaseRelativeLayout) {
                        if (((DialogXBaseRelativeLayout) boxRoot).isBaseFocusable()) {
                            ((DialogXBaseRelativeLayout) boxRoot).requestFocusOnResume();
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void requestDialogFocus() {
        if (runningDialogList != null) {
            CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
            for (int i = copyOnWriteList.size() - 1; i >= 0; i--) {
                BaseDialog baseDialog = copyOnWriteList.get(i);
                if (baseDialog.getOwnActivity() == getTopActivity() && baseDialog.isShow && baseDialog.getDialogView() != null) {
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

    public abstract void restartDialog();

    protected static void show(final View view) {
        if (view == null) {
            return;
        }
        final BaseDialog dialog = (BaseDialog) view.getTag();
        if (dialog != null) {
            if (dialog.isShow) {
                if (dialog.getDialogView() != null) {
                    dialog.getDialogView().setVisibility(View.VISIBLE);
                    return;
                }
                error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                return;
            }
            dialog.dialogView = new WeakReference<>(view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                publicWindowInsets(dialog.getRootFrameLayout().getRootWindowInsets());
            }

            log(dialog.dialogKey() + ".show");

            addDialogToRunningList(dialog);
            switch (dialog.dialogImplMode) {
                case WINDOW:
                    WindowUtil.show(dialog.getOwnActivity(), view, !(dialog instanceof NoTouchInterface));
                    break;
                case DIALOG_FRAGMENT:
                    DialogFragmentImpl dialogFragment = new DialogFragmentImpl(dialog, view);
                    dialogFragment.show(getSupportFragmentManager(dialog.getOwnActivity()), "DialogX");
                    dialog.ownDialogFragmentImpl = new WeakReference<>(dialogFragment);
                    break;
                case FLOATING_ACTIVITY:
                    if (waitRunDialogX == null) {
                        waitRunDialogX = new HashMap<>();
                    }
                    waitRunDialogX.put(dialog.dialogKey(), new ActivityRunnable() {
                        @Override
                        public void run(Activity activity) {
                            dialog.floatingWindowActivity = new WeakReference<>((DialogXFloatingWindowActivity) activity);
                            dialog.floatingWindowActivity.get().setFromActivity(dialog.getOwnActivity());
                            final FrameLayout activityRootView = getDecorView(activity);
                            if (activityRootView == null) {
                                return;
                            }
                            runOnMain(new Runnable() {
                                @Override
                                public void run() {
                                    if (view.getParent() == dialog.getRootFrameLayout()) {
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
                    if (dialogXFloatingWindowActivity != null && dialogXFloatingWindowActivity.isSameFrom(dialog.getOwnActivity().hashCode())) {
                        dialogXFloatingWindowActivity.showDialogX(dialog.dialogKey());
                        return;
                    }
                    Intent intent = new Intent(getPrivateContext(), DialogXFloatingWindowActivity.class);
                    if (dialog.getOwnActivity() == null) {
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    }
                    intent.putExtra("dialogXKey", dialog.dialogKey());
                    intent.putExtra("fromActivityUiStatus", dialog.getOwnActivity() == null ? 0 : (getDecorView(dialog.getOwnActivity()) == null ? 0 : getDecorView(dialog.getOwnActivity()).getSystemUiVisibility()));
                    intent.putExtra("from", getPrivateContext().hashCode());
                    getPrivateContext().startActivity(intent);
                    int version = Integer.valueOf(Build.VERSION.SDK_INT);
                    if (version > 5 && dialog.getOwnActivity() != null) {
                        dialog.getOwnActivity().overridePendingTransition(0, 0);
                    }
                    break;
                default:
                    if (dialog.getRootFrameLayout() == null) {
                        return;
                    }
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getParent() == dialog.getRootFrameLayout()) {
                                error(((BaseDialog) view.getTag()).dialogKey() + "已处于显示状态，请勿重复执行 show() 指令。");
                                return;
                            }
                            if (view.getParent() != null) {
                                ((ViewGroup) view.getParent()).removeView(view);
                            }
                            dialog.getRootFrameLayout().addView(view);
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
        if (dialogXKey == null) {
            return null;
        }
        return waitRunDialogX.get(dialogXKey);
    }

    protected static void show(final Activity activity, final View view) {
        if (activity == null || view == null) {
            return;
        }
        if (activityWeakReference == null || activityWeakReference.get() == null || ActivityLifecycleImpl.getApplicationContext() == null) {
            init(activity.getApplicationContext());
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
            baseDialog.dialogView = new WeakReference<>(view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                publicWindowInsets(baseDialog.getRootFrameLayout().getRootWindowInsets());
            }

            log(baseDialog + ".show");
            addDialogToRunningList(baseDialog);

            switch (baseDialog.dialogImplMode) {
                case WINDOW:
                    WindowUtil.show(activity, view, !(baseDialog instanceof NoTouchInterface));
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
                            baseDialog.floatingWindowActivity.get().setFromActivity(baseDialog.getOwnActivity());
                            final FrameLayout activityRootView = getDecorView(activity);
                            if (activityRootView == null) {
                                return;
                            }
                            runOnMain(new Runnable() {
                                @Override
                                public void run() {
                                    if (view.getParent() == baseDialog.getRootFrameLayout()) {
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
                    intent.putExtra("fromActivityUiStatus", getDecorView(activity) == null ? 0 : getDecorView(activity).getSystemUiVisibility());
                    activity.startActivity(intent);
                    int version = Integer.valueOf(Build.VERSION.SDK_INT);
                    if (version > 5) {
                        activity.overridePendingTransition(0, 0);
                    }
                    break;
                default:
                    final FrameLayout activityRootView = getDecorView(activity);
                    if (activityRootView == null) {
                        return;
                    }
                    runOnMain(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getParent() == baseDialog.getRootFrameLayout()) {
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

    private void setOwnActivity(Activity activity) {
        ownActivity = new WeakReference<>(activity);
    }

    protected static void dismiss(final View dialogView) {
        if (dialogView == null) {
            return;
        }
        final BaseDialog baseDialog = (BaseDialog) dialogView.getTag();
        log(baseDialog.dialogKey() + ".dismiss");
        removeDialogToRunningList(baseDialog);
        if (baseDialog.dialogView != null) {
            baseDialog.dialogView.clear();
        }

        switch (baseDialog.dialogImplMode) {
            case WINDOW:
                WindowUtil.dismiss(dialogView);
                break;
            case DIALOG_FRAGMENT:
                if (baseDialog.ownDialogFragmentImpl != null && baseDialog.ownDialogFragmentImpl.get() != null) {
                    baseDialog.ownDialogFragmentImpl.get().dismiss();
                }
                break;
            case FLOATING_ACTIVITY:
                if (baseDialog.floatingWindowActivity != null && baseDialog.floatingWindowActivity.get() != null) {
                    FrameLayout rootView = getDecorView(baseDialog.floatingWindowActivity.get());
                    if (rootView != null) {
                        rootView.removeView(dialogView);
                    }
                    baseDialog.floatingWindowActivity.get().finish(baseDialog.dialogKey());
                    requestDialogFocus();
                }
                break;
            default:
                runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        if (dialogView.getParent() == null || !(dialogView.getParent() instanceof ViewGroup)) {
                            if (baseDialog.getRootFrameLayout() == null) {
                                return;
                            }
                            baseDialog.getRootFrameLayout().removeView(dialogView);
                        } else {
                            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                        }
                        requestDialogFocus();
                    }
                }, true);
                break;
        }
        if (baseDialog.getDialogListBuilder() != null && !baseDialog.getDialogListBuilder().isEmpty()) {
            baseDialog.getDialogListBuilder().showNext();
        }
    }

    private static void addDialogToRunningList(BaseDialog baseDialog) {
        if (runningDialogList == null) {
            runningDialogList = new CopyOnWriteArrayList<>();
        }
        runningDialogList.add(baseDialog);
    }

    private static void removeDialogToRunningList(BaseDialog baseDialog) {
        if (runningDialogList != null) {
            runningDialogList.remove(baseDialog);
        }
    }

    public static Activity getTopActivity() {
        if (activityWeakReference == null || activityWeakReference.get() == null) {
            //尝试反射初始化
            init(null);
            if (activityWeakReference == null || activityWeakReference.get() == null) {
                //若还为空，无奈，尝试直接反射拿顶层 activity
                Activity topActivity = ActivityLifecycleImpl.getTopActivity();
                init(topActivity);
                return topActivity;
            }
            return activityWeakReference.get();
        }
        return activityWeakReference.get();
    }

    /**
     * @return 获取上下文
     * @Deprecated 已废弃，将在未来版本删除此方法，建议使用 {@link #getOwnActivity()} 替代此方法
     */
    @Deprecated
    public static Context getContext() {
        return getPrivateContext();
    }

    private static Context getPrivateContext() {
        Activity activity = getTopActivity();
        if (activity == null) {
            Context applicationContext = getApplicationContext();
            if (applicationContext == null) {
                error("DialogX 未初始化(E2)。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
                return null;
            }
            return applicationContext;
        }
        return activity;
    }

    public static Context getApplicationContext() {
        return ActivityLifecycleImpl.getApplicationContext();
    }

    /**
     * 自动执行，不建议自行调用此方法
     *
     * @hide
     */
    public static void cleanContext() {
        if (activityWeakReference != null) {
            activityWeakReference.clear();
        }
        activityWeakReference = null;
        System.gc();
    }

    protected abstract void shutdown();

    protected boolean cancelable = true;
    protected boolean isShow;
    protected DialogXStyle style;
    protected DialogX.THEME theme;
    protected boolean autoShowInputKeyboard;
    protected int backgroundColor = -1;
    protected long enterAnimDuration = -1;
    protected long exitAnimDuration = -1;
    protected int maxWidth;
    protected int maxHeight;
    protected int minWidth;
    protected int minHeight;
    protected int[] screenPaddings = new int[4];

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
        if (getApplicationContext() == null) {
            error("DialogX 未初始化(E3)。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return null;
        }
        return LayoutInflater.from(getApplicationContext()).inflate(layoutId, null);
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
        if (textInfo == null) {
            return;
        }
        if (textView == null) {
            return;
        }
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(textInfo.getFontSizeComplexUnit(), textInfo.getFontSize());
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
        if (textView == null) {
            return;
        }
        if (isNull(text)) {
            textView.setVisibility(View.GONE);
            textView.setText("");
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
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
        if (getApplicationContext() == null) {
            return Resources.getSystem();
        }
        return getApplicationContext().getResources();
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public boolean isLightTheme() {
        if (theme == DialogX.THEME.AUTO) {
            if (getApplicationContext() == null) {
                return theme == DialogX.THEME.LIGHT;
            }
            return (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO;
        }
        return theme == DialogX.THEME.LIGHT;
    }

    public FrameLayout getRootFrameLayout() {
        Activity activity = getOwnActivity();
        if (activity == null) {
            activity = getTopActivity();
            if (activity == null) {
                error("DialogX 错误：在 getRootFrameLayout() 时无法获取绑定的 activity，请确认是否正确初始化：\n" +
                        "DialogX.init(context);\n\n" +
                        "或者使用 .show(activity) 启动对话框\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
                return null;
            }
            setOwnActivity(activity);
        }
        FrameLayout decorView = getDecorView(activity);
        if (decorView == null) {
            error("DialogX 错误：在 getRootFrameLayout() 时无法获 activity(" + activity + ") 的 decorView，请检查该 activity 是否正常显示且可以使 DialogX 基于其显示。\n" +
                    "若该 activity 不可用，可通过以下代码配置豁免 DialogX 对话框绑定至该 activity，例如：\n" +
                    "DialogX.unsupportedActivitiesPackageNames = new String[]{\n" +
                    "        \"com.bytedance.sdk.openadsdk.stub.activity\",\n" +
                    "        \"com.mobile.auth.gatewayauth\",\n" +
                    "        \"com.google.android.gms.ads\"\n" +
                    "};\n\n" +
                    "另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return null;
        }
        rootFrameLayout = new WeakReference<>(decorView);
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
    protected boolean preShow;

    protected void beforeShow() {
        preShow = true;
        dismissAnimFlag = false;
        setOwnActivity(getTopActivity());
        if (getOwnActivity() == null) {
            //尝试重新获取 activity
            init(null);
            if (getOwnActivity() == null) {
                error("DialogX 未初始化(E5)。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
                return;
            }
        }
        if (style.styleVer != DialogXStyle.styleVer) {
            error("DialogX 所引用的 Style 不符合当前适用版本：" + DialogXStyle.styleVer + " 引入的 Style(" + style.getClass().getSimpleName() + ") 版本" + style.styleVer);
        }

        if (dialogImplMode != DialogX.IMPL_MODE.VIEW && getOwnActivity() instanceof LifecycleOwner) {
            Lifecycle lifecycle = ((LifecycleOwner) getOwnActivity()).getLifecycle();
            lifecycle.addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        recycleDialog(getOwnActivity());
                    }
                }
            });
        }

        //Hide IME
        if (!(this instanceof NoTouchInterface)) {
            View view = getOwnActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getOwnActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected String getString(int titleResId) {
        if (getApplicationContext() == null) {
            error("DialogX 未初始化(E6)。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return null;
        }
        return getResources().getString(titleResId);
    }

    protected int getColor(int backgroundRes) {
        if (getApplicationContext() == null) {
            error("DialogX 未初始化(E7)。\n请检查是否在启动对话框前进行初始化操作，使用以下代码进行初始化：\nDialogX.init(context);\n\n另外建议您前往查看 DialogX 的文档进行使用：https://github.com/kongzue/DialogX");
            return Color.BLACK;
        }
        return getResources().getColor(backgroundRes);
    }

    protected Integer getColorNullable(Integer res) {
        return res == null ? null : getColor(res);
    }

    protected Integer getColorNullable(Integer res, Integer defaultResId) {
        return res == null ? getColor(defaultResId) : getColor(res);
    }

    public enum BOOLEAN {
        TRUE, FALSE
    }

    public abstract String dialogKey();

    protected static void runOnMain(Runnable runnable) {
        if (!DialogX.autoRunOnUIThread || (getUiThread() != null && Thread.currentThread() == getUiThread())) {
            runnable.run();
            return;
        }
        runOnMain(runnable, true);
    }

    protected static Thread getUiThread() {
        if (uiThread == null) {
            uiThread = Looper.getMainLooper().getThread();
        }
        return uiThread;
    }

    protected static void runOnMain(Runnable runnable, boolean needWaitMainLooper) {
        getMainHandler().post(runnable);
    }

    protected static void runOnMainDelay(Runnable runnable, long delay) {
        if (delay < 0) {
            return;
        }
        if (!DialogX.autoRunOnUIThread) {
            runnable.run();
        }
        getMainHandler().postDelayed(runnable, delay);
    }

    public View getDialogView() {
        if (dialogView == null) {
            return null;
        }
        return dialogView.get();
    }

    public Activity getOwnActivity() {
        if (ownActivity == null || ownActivity.get() == null) {
            setOwnActivity(getTopActivity());
        }
        return ownActivity.get();
    }

    protected void cleanActivityContext() {
        if (ownActivity != null) {
            ownActivity.clear();
        }
        ownActivity = null;
    }

    public static void cleanAll() {
        if (runningDialogList != null) {
            CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
            for (BaseDialog baseDialog : copyOnWriteList) {
                if (baseDialog.isShow()) {
                    baseDialog.shutdown();
                }
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
                        if (baseDialog.getOwnActivity() == activity && baseDialog.dialogView != null) {
                            WindowUtil.dismiss(baseDialog.dialogView.get());
                        }
                    }
                }
                break;
            case DIALOG_FRAGMENT:
                if (runningDialogList != null) {
                    CopyOnWriteArrayList<BaseDialog> copyOnWriteList = new CopyOnWriteArrayList<>(runningDialogList);
                    for (BaseDialog baseDialog : copyOnWriteList) {
                        if (baseDialog.getOwnActivity() == activity && baseDialog.ownDialogFragmentImpl != null && baseDialog.ownDialogFragmentImpl.get() != null) {
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
                        if (baseDialog.getOwnActivity() == activity) {
                            baseDialog.cleanActivityContext();
                            runningDialogList.remove(baseDialog);
                        }
                    }
                }
                break;
        }
        if (activity == getTopActivity()) {
            cleanContext();
        }
    }

    public static List<BaseDialog> getRunningDialogList() {
        if (runningDialogList == null) {
            return new ArrayList<>();
        }
        return new CopyOnWriteArrayList<>(runningDialogList);
    }

    protected void imeShow(EditText editText, boolean show) {
        if (getOwnActivity() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getOwnActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public DialogX.IMPL_MODE getDialogImplMode() {
        return dialogImplMode;
    }

    protected static WindowInsets windowInsets;

    public static WindowInsets publicWindowInsets() {
        return windowInsets;
    }

    public static void publicWindowInsets(WindowInsets windowInsets) {
        if (windowInsets == null) {
            return;
        }
        BaseDialog.windowInsets = windowInsets;
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

    static WeakReference<Handler> mMainHandler;

    private static Handler getMainHandler() {
        if (mMainHandler != null && mMainHandler.get() != null) {
            return mMainHandler.get();
        }
        mMainHandler = new WeakReference<>(new Handler(Looper.getMainLooper()));
        return mMainHandler.get();
    }

    public DialogListBuilder getDialogListBuilder() {
        if (dialogListBuilder == null) {
            return null;
        }
        return dialogListBuilder.get();
    }

    public void setDialogListBuilder(DialogListBuilder dialogListBuilder) {
        this.dialogListBuilder = new WeakReference<>(dialogListBuilder);
    }

    public void cleanDialogList() {
        this.dialogListBuilder = null;
    }

    public boolean isPreShow() {
        return preShow;
    }

    public abstract <D extends BaseDialog> D show();

    protected void onDialogShow() {
    }

    protected void onDialogInit() {
    }

    protected void onDialogRefreshUI() {
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public int getMaxWidth() {
        if (maxWidth == 0) {
            return DialogX.dialogMaxWidth;
        }
        return maxWidth;
    }

    public int getMaxHeight() {
        if (maxHeight == 0) {
            return DialogX.dialogMaxHeight;
        }
        return maxHeight;
    }

    public int getMinWidth() {
        if (minWidth == 0) {
            return DialogX.dialogMinWidth;
        }
        return minWidth;
    }

    public int getMinHeight() {
        if (minHeight == 0) {
            return DialogX.dialogMinHeight;
        }
        return minHeight;
    }

    protected void setLifecycleState(Lifecycle.State s) {
        if (lifecycle == null || s == null) return;
        try {
            lifecycle.setCurrentState(s);
        } catch (Exception e) {
        }
    }

    protected static FrameLayout getDecorView(Activity activity) {
        if (activity == null || activity.getWindow() == null || !(activity.getWindow().getDecorView() instanceof FrameLayout))
            return null;
        return (FrameLayout) activity.getWindow().getDecorView();
    }

    protected List<View> findAllBlurView(View v) {
        List<View> result = new ArrayList<>();
        if (v instanceof BlurViewType) {
            result.add(v);
        }
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            for (int i = 0; i < group.getChildCount(); i++) {
                List<View> child = findAllBlurView(group.getChildAt(i));
                if (child != null) result.addAll(child);
            }
        }
        if (!result.isEmpty()) {
            return result;
        }
        return null;
    }

    protected Integer getIntStyleAttr(Integer styleValue) {
        return styleValue <= 0 ? null : styleValue;
    }

    protected Integer getIntStyleAttr(Integer styleValue, Integer defaultValue) {
        return styleValue <= 0 ? defaultValue : styleValue;
    }

    protected Float getFloatStyleAttr(Float styleValue) {
        return styleValue <= 0 ? null : styleValue;
    }

    protected Float getFloatStyleAttr(Float styleValue, Float defaultValue) {
        if (styleValue <= 0) {
            log("styleValue=" + styleValue + "<=0 ");
            log("return defaultValue=" + defaultValue);
            return defaultValue;
        }
        log("return styleValue=" + styleValue);
        return styleValue;
    }
}
