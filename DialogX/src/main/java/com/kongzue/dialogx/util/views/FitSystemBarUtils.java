package com.kongzue.dialogx.util.views;

import static androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.interfaces.BaseDialog;

import java.util.List;

public class FitSystemBarUtils {

    //当前是否正在平滑填充中
    private boolean inSmoothingPadding = false;

    public boolean isInSmoothingPadding() {
        return inSmoothingPadding;
    }

    //是否需要处理挖孔屏，刘海屏
    public boolean safeCutOutPadding = true;

    //是否需要平滑填充，仅在android R及以上生效，默认开启
    public boolean smoothPadding = true;

    private View contentView;

    private CallBack callBack;

    private BaseDialog dialog;

    /**
     * 绑定到View
     *
     * @param view 指定View
     */
    public static FitSystemBarUtils attachView(View view) {
        return attachView(view, new CallBack() {
            @Override
            public boolean isEnable(Orientation orientation) {
                return true;
            }

            @Override
            public void unsafeRect(int start, int top, int end, int bottom) {

            }

            @Override
            public int initialPadding(Orientation orientation) {
                return 0;
            }
        });
    }

    /**
     * 绑定到View
     *
     * @param view     指定View
     * @param callBack 用于判断是否需要某条边的padding，会非常频繁的调用，所以不要在里面做需要耗时的操作，以免卡UI线程
     */
    public static FitSystemBarUtils attachView(View view, CallBack callBack) {
        return new FitSystemBarUtils(view, callBack);
    }


    /**
     * 绑定到View
     *
     * @param view   指定View
     * @param start  是否需要paddingStart
     * @param top    是否需要paddingTop
     * @param end    是否需要paddingEnd
     * @param bottom 是否需要paddingBottom
     */
    public static FitSystemBarUtils attachView(
            View view,
            boolean start,
            boolean top,
            boolean end,
            boolean bottom
    ) {
        return attachView(view, new CallBack() {
            @Override
            public boolean isEnable(Orientation orientation) {
                switch (orientation) {
                    case Start:
                        return start;
                    case Top:
                        return top;
                    case End:
                        return end;
                    case Bottom:
                        return bottom;
                }
                return false;
            }

            @Override
            public void unsafeRect(int start, int top, int end, int bottom) {

            }

            @Override
            public int initialPadding(Orientation orientation) {
                return 0;
            }
        });
    }

    private FitSystemBarUtils() {
    }

    public FitSystemBarUtils(View view, CallBack callBack) {
//        view.setFitsSystemWindows(true);
        contentView = view;
        this.callBack = callBack;
        if (view instanceof DialogXBaseRelativeLayout) {
            dialog = ((DialogXBaseRelativeLayout) view).getParentDialog();
        }
        applyWindowInsets();
    }

    View.OnLayoutChangeListener rootViewLayoutChangeListener;

    public void applyWindowInsets() {
//        view.fitsSystemWindows = true
        //创建原始padding的快照
        RelativePadding initialPadding = new RelativePadding(
                ViewCompat.getPaddingStart(contentView),
                contentView.getPaddingTop(),
                ViewCompat.getPaddingEnd(contentView),
                contentView.getPaddingBottom()
        );
        //不带平滑变化的
        ViewCompat.setOnApplyWindowInsetsListener(contentView, (view, insets) -> {
            if (inSmoothingPadding) return insets;
            formatInsets(insets, new RelativePadding(initialPadding));
            return insets;
        });
        //带平滑变化的，但是支支持android R及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            log("FitSystemBarUtils: setWindowInsetsAnimationCallback");
            ViewCompat.setWindowInsetsAnimationCallback(contentView,
                    new WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {

                        @NonNull
                        @Override
                        public WindowInsetsCompat onProgress(
                                @NonNull WindowInsetsCompat insets,
                                @NonNull List<WindowInsetsAnimationCompat> runningAnimations
                        ) {
                            log("FitSystemBarUtils: setWindowInsetsAnimationCallback#onProgress: " + insets);
                            if (smoothPadding) {
                                formatInsets(insets, new RelativePadding(initialPadding));
                            }
                            return insets;
                        }

                        @Override
                        public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
                            log("FitSystemBarUtils: setWindowInsetsAnimationCallback#onEnd ");
                            inSmoothingPadding = false;
                            super.onEnd(animation);
                        }

                        @Override
                        public void onPrepare(@NonNull WindowInsetsAnimationCompat animation) {
                            inSmoothingPadding = smoothPadding;
                            super.onPrepare(animation);
                        }
                    });
        }

        if (ViewCompat.isAttachedToWindow(contentView)) {
            log("FitSystemBarUtils: AttachedToWindow ok");
            ViewCompat.requestApplyInsets(contentView);
        } else {
            log("FitSystemBarUtils: wait AttachedToWindow");
            contentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                    view.removeOnAttachStateChangeListener(this);

                    log("FitSystemBarUtils: onViewAttachedToWindow");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || getAppTargetSDKVersion() < Build.VERSION_CODES.R)) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            //修复<=API29的部分设备上存在的非安全区不回调的问题
                            View parentView = (View) view.getParent();
                            if (rootViewLayoutChangeListener != null) {
                                parentView.removeOnLayoutChangeListener(rootViewLayoutChangeListener);
                            }
                            rootViewLayoutChangeListener = new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                    WindowInsets windowInsets = v.getRootView().getRootWindowInsets();
                                    if (windowInsets != null) {
                                        log("    FitSystemBarUtils: RootView get Insets");
                                        formatInsets(WindowInsetsCompat.toWindowInsetsCompat(windowInsets), new RelativePadding(initialPadding));
                                    } else {
                                        log("    FitSystemBarUtils: RootView not get Insets");
                                    }
                                }
                            };
                            parentView.addOnLayoutChangeListener(rootViewLayoutChangeListener);
                            parentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                                @Override
                                public void onViewAttachedToWindow(View v) {
                                }

                                @Override
                                public void onViewDetachedFromWindow(View v) {
                                    parentView.removeOnLayoutChangeListener(rootViewLayoutChangeListener);
                                }
                            });
                        } else {
                            View parentView = (View) view.getParent();
                            ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    // 获取状态栏高度
                                    Rect insets = new Rect();
                                    parentView.getWindowVisibleDisplayFrame(insets);
                                    int statusBarHeight = insets.top;

                                    // 获取导航栏高度
                                    int navigationBarHeight = 0;
                                    Resources resources = parentView.getResources();
                                    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                                    if (resourceId > 0) {
                                        navigationBarHeight = resources.getDimensionPixelSize(resourceId);
                                    }

                                    log("FitSystemBarUtils: below Android M use support mode: statusBarHeight=" + statusBarHeight + ", navigationBarHeight=" + navigationBarHeight);
                                    int deviceOrientation = checkOrientationAndStatusBarSide();
                                    log("    FitSystemBarUtils: deviceOrientation = " + deviceOrientation);
                                    switch (deviceOrientation) {
                                        case 1:
                                        case -1:
                                            initialPadding.end = navigationBarHeight;
                                            initialPadding.start = 0;
                                            break;
                                        default:
                                            initialPadding.top = statusBarHeight;
                                            initialPadding.bottom = navigationBarHeight;
                                            break;
                                    }

                                    applyCallBack(initialPadding);

                                    parentView.getViewTreeObserver().removeOnPreDrawListener(this);

                                    return true;
                                }
                            };
                            parentView.getViewTreeObserver().addOnPreDrawListener(preDrawListener);

                        }
                    }

                    ViewCompat.requestApplyInsets(view);
                }

                @Override
                public void onViewDetachedFromWindow(View view) {

                }
            });
        }
    }

    RelativePadding relativePaddingCache;

    /**
     * 针对不同版本处理Insets
     */
    private void formatInsets(WindowInsetsCompat insetsCompat, RelativePadding initialPadding) {
        if (contentView == null || insetsCompat == null || initialPadding == null) return;

        relativePaddingCache = initialPadding;
        int cutoutPaddingLeft = 0;
        int cutoutPaddingTop = 0;
        int cutoutPaddingRight = 0;
        int cutoutPaddingBottom = 0;

        int systemWindowInsetLeft = 0;
        int systemWindowInsetTop = 0;
        int systemWindowInsetRight = 0;
        int systemWindowInsetBottom = 0;

        if (safeCutOutPadding) {
            DisplayCutoutCompat displayCutout = insetsCompat.getDisplayCutout();
            if (null != displayCutout) {
                cutoutPaddingTop = displayCutout.getSafeInsetTop();
                cutoutPaddingLeft = displayCutout.getSafeInsetLeft();
                cutoutPaddingRight = displayCutout.getSafeInsetRight();
                cutoutPaddingBottom = displayCutout.getSafeInsetRight();
            }
        }
        Insets systemBars = insetsCompat.getInsets(
                WindowInsetsCompat.Type.ime() | WindowInsetsCompat.Type.systemBars()
        );
        systemWindowInsetLeft = systemBars.left;
        systemWindowInsetRight = systemBars.right;

        //对api低于30的设备，做额外判断，api 30+的不需要这个
        int suv = contentView.getRootView().getWindowSystemUiVisibility();
        boolean statusBar = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            statusBar = (suv & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;
        }
        boolean naviBar = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            naviBar = (suv & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        }
        if (naviBar && (insetsCompat.isVisible(WindowInsetsCompat.Type.ime())
                || insetsCompat.isVisible(WindowInsetsCompat.Type.navigationBars()))
        ) {
            systemWindowInsetBottom = systemBars.bottom;
        }
        if (statusBar && insetsCompat.isVisible(WindowInsetsCompat.Type.statusBars())) {
            systemWindowInsetTop = systemBars.top;
        }
        if (isWrongInsets(systemBars)) {
            log("    FitSystemBarUtils: isWrongInsets try special mode...");
            int deviceOrientation = checkOrientationAndStatusBarSide();
            log("    FitSystemBarUtils: deviceOrientation = " + deviceOrientation);
            switch (deviceOrientation) {
                case 1:
                    initialPadding.end = getStatusBarHeight();
                    initialPadding.start = getNavigationBarHeight();
                    break;
                default:
                    initialPadding.top = getStatusBarHeight();
                    initialPadding.bottom = getNavigationBarHeight();
                    break;
            }
            addListenerWhenImeHeightChanged();
        } else {
            specialMode = false;
            if (callBack.isEnable(Orientation.Top)) {
                initialPadding.top += Math.max(systemWindowInsetTop, cutoutPaddingTop);
            }
            if (callBack.isEnable(Orientation.Bottom)) {
                initialPadding.bottom += Math.max(systemWindowInsetBottom, cutoutPaddingBottom);
            }

            boolean isRtl =
                    ViewCompat.getLayoutDirection(contentView) == ViewCompat.LAYOUT_DIRECTION_RTL;
            if (callBack.isEnable(Orientation.Start)) {
                if (isRtl) {
                    initialPadding.start += Math.max(systemWindowInsetRight, cutoutPaddingRight);
                } else {
                    initialPadding.start += Math.max(systemWindowInsetLeft, cutoutPaddingLeft);
                }
            }
            if (callBack.isEnable(Orientation.End)) {
                if (isRtl) {
                    initialPadding.end += Math.max(systemWindowInsetLeft, cutoutPaddingLeft);
                } else {
                    initialPadding.end += Math.max(systemWindowInsetRight, cutoutPaddingRight);
                }
            }
        }

        applyCallBack(initialPadding);
    }

    private void applyCallBack() {
        if (relativePaddingCache != null) {
            applyCallBack(relativePaddingCache);
        }
    }

    private void applyCallBack(RelativePadding initialPadding) {
        if (callBack == null) {
            return;
        }
        //加上用户自定义的
        initialPadding.start += callBack.initialPadding(Orientation.Start);
        initialPadding.top += callBack.initialPadding(Orientation.Top);
        initialPadding.end += callBack.initialPadding(Orientation.End);
        initialPadding.bottom += callBack.initialPadding(Orientation.Bottom);

        initialPadding.applyToView(contentView);
        //四边 非安全区 传递回去
        log("    KONGZUE DEBUG DIALOGX FitSystemBarUtils callBack: left=" + initialPadding.start + " top=" + initialPadding.top +
                " right=" + initialPadding.end + " bottom=" + initialPadding.bottom + " specialMode=" + specialMode +
                " specialModeImeHeight=" + specialModeImeHeight
        );
        callBack.unsafeRect(
                initialPadding.start,
                initialPadding.top,
                initialPadding.end,
                initialPadding.bottom + (specialMode ? specialModeImeHeight : 0)
        );
    }

    private boolean isWrongInsets(Insets systemBars) {
        return systemBars.top == 0 && systemBars.bottom == 0 && systemBars.left == 0 && systemBars.right == 0;
    }

    public interface CallBack {

        //是否启用指定边的insets
        boolean isEnable(Orientation orientation);

        //非安全区
        void unsafeRect(int start, int top, int end, int bottom);

        //用户自定义的padding
        int initialPadding(Orientation orientation);
    }

    public static class RelativePadding {
        int start;
        int top;
        int end;
        int bottom;

        public RelativePadding(int start, int top, int end, int bottom) {
            this.start = start;
            this.top = top;
            this.end = end;
            this.bottom = bottom;
        }

        public RelativePadding(RelativePadding other) {
            start = other.start;
            top = other.top;
            end = other.end;
            bottom = other.bottom;
        }

        /**
         * Applies this relative padding to the view.
         */
        public void applyToView(View view) {
            if (view instanceof DialogXBaseRelativeLayout) {
                //((DialogXBaseRelativeLayout) view).setUnsafePadding(start, top, end, bottom);
            } else {
                ViewCompat.setPaddingRelative(view, start, top, end, bottom);
            }
        }
    }

    enum Orientation {
        Start, Top, End, Bottom
    }

    protected void log(String s) {
        if (DialogXBaseRelativeLayout.debugMode && DialogX.DEBUGMODE) {
            Log.e(">>>", s);
        }
    }

    private int getStatusBarHeight() {
        if (isFullScreen() || getDecorView() == null) {
            return 0;
        }
        WindowInsetsController insetsController = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ? getDecorView().getWindowInsetsController() : null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && insetsController != null && (insetsController.getSystemBarsBehavior() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == 0) {
            return 0;
        }
        Resources res;
        if (contentView == null || contentView.getContext() == null) {
            res = Resources.getSystem();
        } else {
            res = contentView.getContext().getResources();
        }
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarHeight() {
        if (isFullScreen() || getDecorView() == null) {
            return 0;
        }
        WindowInsetsController insetsController = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ? getDecorView().getWindowInsetsController() : null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && insetsController != null && (insetsController.getSystemBarsBehavior() & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) == 0) {
            log("getNavigationBarHeight =0");
            return 0;
        }
        Resources res;
        if (contentView == null || contentView.getContext() == null) {
            res = Resources.getSystem();
        } else {
            res = contentView.getContext().getResources();
        }
        int result = 0;
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private boolean isFullScreen() {
        Activity activity = getActivity();
        if (activity == null) {
            return false;
        }
        // 通过检查窗口标志来判断
        int flags = activity.getWindow().getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
            return true;
        }
        // 通过检查系统 UI 标志来判断
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        if ((uiOptions & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
            return true;
        }
        return false;
    }

    private int checkOrientationAndStatusBarSide() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        // 判断是否为横屏
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 判断状态栏位置
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_90:
                    // 设备旋转了 90 度，状态栏在左侧
                    return -1;
                case Surface.ROTATION_270:
                    // 设备旋转了 270 度，状态栏在右侧
                    return 1;
                default:
                    // 其他情况，不应当发生在横屏状态
                    return 0;
            }
        }
        return 0;
    }

    private int specialModeImeHeight;
    private boolean specialMode;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private View getDecorView() {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getWindow().getDecorView();
    }

    private void addListenerWhenImeHeightChanged() {
        specialMode = true;
        View decorView = getDecorView();
        if (decorView == null) return;
        if (onGlobalLayoutListener != null) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int screenHeight = decorView.getHeight();
                WindowInsetsController insetsController = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ? getDecorView().getWindowInsetsController() : null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && insetsController != null && (insetsController.getSystemBarsBehavior() & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) == 0) {
                    r.bottom = screenHeight;
                }
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight != specialModeImeHeight) {
                    specialModeImeHeight = keypadHeight;
                    log("    FitSystemBarUtils: specialModeImeHeight=" + specialModeImeHeight);
                    applyCallBack();
                }
            }
        });
    }

    private int getAppTargetSDKVersion() {
        try {
            Context context = BaseDialog.getApplicationContext();
            if (context == null) {
                return -1;
            }
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            int targetSdkVersion = applicationInfo.targetSdkVersion;
            return targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Activity getActivity() {
        if (dialog == null) return BaseDialog.getTopActivity();
        return dialog.getOwnActivity();
    }

    public void recycle() {
        View decorView = getDecorView();
        if (decorView != null && onGlobalLayoutListener != null) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        onGlobalLayoutListener = null;
        callBack = null;
        contentView = null;
        dialog = null;
    }
}
