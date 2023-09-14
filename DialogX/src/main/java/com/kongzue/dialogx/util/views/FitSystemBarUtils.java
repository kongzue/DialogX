package com.kongzue.dialogx.util.views;


import static androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kongzue.dialogx.DialogX;

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
        applyWindowInsets();
    }

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
            ViewCompat.setWindowInsetsAnimationCallback(contentView,
                    new WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {

                        @NonNull
                        @Override
                        public WindowInsetsCompat onProgress(
                                @NonNull WindowInsetsCompat insets,
                                @NonNull List<WindowInsetsAnimationCompat> runningAnimations
                        ) {
                            if (smoothPadding) {
                                formatInsets(insets, new RelativePadding(initialPadding));
                            }
                            return insets;
                        }

                        @Override
                        public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
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
            log("KONGZUE DEBUG DIALOGX: AttachedToWindow ok");
            ViewCompat.requestApplyInsets(contentView);
        } else {
            log("KONGZUE DEBUG DIALOGX: wait AttachedToWindow");
            contentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                    view.removeOnAttachStateChangeListener(this);

                    log("KONGZUE DEBUG DIALOGX: onViewAttachedToWindow");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            //修复<=API29的部分设备上存在的非安全区不回调的问题
                            View parentView = (View) view.getParent();
                            parentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                    WindowInsets windowInsets = v.getRootView().getRootWindowInsets();
                                    if (windowInsets != null) {
                                        log("    KONGZUE DEBUG DIALOGX: RootView get Insets");
                                        formatInsets(WindowInsetsCompat.toWindowInsetsCompat(windowInsets), new RelativePadding(initialPadding));
                                    } else {
                                        log("    KONGZUE DEBUG DIALOGX: RootView not get Insets");
                                    }
                                }
                            });

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

    /**
     * 针对不同版本处理Insets
     */
    private void formatInsets(WindowInsetsCompat insetsCompat, RelativePadding initialPadding) {
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
            return;
        }

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
        //加上用户自定义的
        initialPadding.start += callBack.initialPadding(Orientation.Start);
        initialPadding.top += callBack.initialPadding(Orientation.Top);
        initialPadding.end += callBack.initialPadding(Orientation.End);
        initialPadding.bottom += callBack.initialPadding(Orientation.Bottom);

        initialPadding.applyToView(contentView);
        //四边 非安全区 传递回去
        callBack.unsafeRect(
                initialPadding.start,
                initialPadding.top,
                initialPadding.end,
                initialPadding.bottom
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
}
