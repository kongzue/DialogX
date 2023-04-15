package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogXBaseBottomDialog;
import com.kongzue.dialogx.interfaces.DynamicWindowInsetsAnimationListener;
import com.kongzue.dialogx.interfaces.NoTouchInterface;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 13:53
 */
public class DialogXBaseRelativeLayout extends RelativeLayout {

    public static boolean debugMode = false;

    private OnSafeInsetsChangeListener onSafeInsetsChangeListener;
    private BaseDialog parentDialog;
    private boolean autoUnsafePlacePadding = true;
    private boolean focusable = true;
    private boolean interceptBack = true;

    private OnLifecycleCallBack onLifecycleCallBack;
    private PrivateBackPressedListener onBackPressedListener;

    public DialogXBaseRelativeLayout(Context context) {
        super(context);
        init(null);
    }

    public DialogXBaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DialogXBaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private boolean isInited = false;

    private void init(AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setForceDarkAllowed(false);
        }
        if (!isInited) {
            if (attrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DialogXBaseRelativeLayout);
                focusable = a.getBoolean(R.styleable.DialogXBaseRelativeLayout_baseFocusable, true);
                autoUnsafePlacePadding = a.getBoolean(R.styleable.DialogXBaseRelativeLayout_autoSafeArea, true);
                interceptBack = a.getBoolean(R.styleable.DialogXBaseRelativeLayout_interceptBack, true);
                a.recycle();
                isInited = true;
            }
            if (focusable) {
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
            }
            setBkgAlpha(0f);
            if (getParentDialog() != null && getParentDialog().getDialogImplMode() != DialogX.IMPL_MODE.VIEW) {
                setFitsSystemWindows(true);
            }
            setClipChildren(false);
            setClipToPadding(false);
        }
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (!useWindowInsetsAnimation && (DialogX.useActivityLayoutTranslationNavigationBar || (getParentDialog() != null && getParentDialog().getDialogImplMode() != DialogX.IMPL_MODE.VIEW))) {
            log("#fitSystemWindows paddingView: b=" + insets.bottom);
            paddingView(insets.left, insets.top, insets.right, insets.bottom);
        }
        return super.fitSystemWindows(insets);
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!useWindowInsetsAnimation && (DialogX.useActivityLayoutTranslationNavigationBar || (getParentDialog() != null && getParentDialog().getDialogImplMode() != DialogX.IMPL_MODE.VIEW))) {
                paddingView(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
                log("#dispatchApplyWindowInsets paddingView: b=" + insets.getSystemWindowInsetBottom());
            }
        }
        return super.dispatchApplyWindowInsets(insets);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void paddingView(Insets insets) {
        if (insets != null) {
            paddingView(insets.left, insets.top, insets.right, insets.bottom);
            log("#paddingView(insets) paddingView: b=" + insets.bottom);
        }
    }

    public void paddingView(WindowInsets insets) {
        if (!isAttachedToWindow()) {
            getDynamicWindowInsetsAnimationListener(parentKey).remove(dynamicWindowInsetsAnimationListener);
            return;
        }
        if (insets == null) {
            if (BaseDialog.publicWindowInsets() != null) {
                insets = BaseDialog.publicWindowInsets();
            } else {
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (insets.getSystemWindowInsetLeft() == 0 && insets.getSystemWindowInsetTop() == 0 && insets.getSystemWindowInsetRight() == 0 && insets.getSystemWindowInsetBottom() == 0) {
                getWindowInsetsByDisplayMetrics();
                return;
            }
            paddingView(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            log("#paddingView(WindowInsets) paddingView: b=" + insets.getSystemWindowInsetBottom());
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        log("#dispatchKeyEvent: KeyCode=" + event.getKeyCode());
        if (isAttachedToWindow() && event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK && interceptBack) {
            if (onBackPressedListener != null) {
                return onBackPressedListener.onBackPressed();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParentDialog() instanceof NoTouchInterface) {
            return super.onTouchEvent(event);
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    private ViewTreeObserver.OnGlobalLayoutListener decorViewLayoutListener;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            final ViewParent parent = getParent();
            if (parent instanceof View) {
                ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));
            }
            ViewCompat.requestApplyInsets(this);

            if (getParentDialog() == null || getParentDialog().getOwnActivity() == null) return;

            View decorView = (View) getParent();
            if (decorView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    parentKey = Integer.toHexString(decorView.hashCode());
                    getDynamicWindowInsetsAnimationListener(parentKey).add(dynamicWindowInsetsAnimationListener = new DynamicWindowInsetsAnimationListener() {
                        @Override
                        public void onChange(WindowInsets windowInsets) {
                            paddingView(windowInsets);
                        }
                    });
                    paddingWindowInsetsByDefault();
                    initDynamicSafeAreaListener();
                } else {
                    decorView.getViewTreeObserver().addOnGlobalLayoutListener(decorViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            paddingWindowInsetsByDefault();
                        }
                    });
                    decorViewLayoutListener.onGlobalLayout();
                }
            }

            if (onLifecycleCallBack != null) {
                onLifecycleCallBack.onShow();
            }
            isLightMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO;
        }
    }

    static Map<String, List<DynamicWindowInsetsAnimationListener>> dynamicWindowInsetsAnimationListenerListMap = new HashMap<>();
    DynamicWindowInsetsAnimationListener dynamicWindowInsetsAnimationListener;
    String parentKey;

    public List<DynamicWindowInsetsAnimationListener> getDynamicWindowInsetsAnimationListener(String key) {
        List<DynamicWindowInsetsAnimationListener> list = dynamicWindowInsetsAnimationListenerListMap.get(key);
        if (list == null) {
            list = new ArrayList<>();
            dynamicWindowInsetsAnimationListenerListMap.put(key, list);
        }
        return list;
    }

    boolean useWindowInsetsAnimation = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void initDynamicSafeAreaListener() {
        View decorView = (View) getParent();
        if (decorView != null) {
            useWindowInsetsAnimation = true;
            ViewCompat.setWindowInsetsAnimationCallback(decorView, new WindowInsetsAnimationCompat.Callback(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE) {

                @NonNull
                @Override
                public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                    View decorView = (View) getParent();
                    if (decorView != null) {
                        String key = Integer.toHexString(decorView.hashCode());
                        List<DynamicWindowInsetsAnimationListener> dynamicWindowInsetsAnimationListenerList = getDynamicWindowInsetsAnimationListener(key);
                        if (dynamicWindowInsetsAnimationListenerList != null) {
                            for (DynamicWindowInsetsAnimationListener listener : dynamicWindowInsetsAnimationListenerList) {
                                listener.onChange(insets.toWindowInsets());
                            }
                        }
                    }
                    return insets;
                }
            });
        }
    }

    private void paddingWindowInsetsByDefault() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paddingView(getRootWindowInsets());
        } else {
            getWindowInsetsByDisplayMetrics();
        }
    }

    private void getWindowInsetsByDisplayMetrics() {
        if (getParentDialog() == null || getParentDialog().getOwnActivity() == null) return;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getParentDialog().getOwnActivity().getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        Rect rect = new Rect();
        View decorView = (View) getParent();
        decorView.getWindowVisibleDisplayFrame(rect);
        if (rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0) return;
        paddingView(rect.left, rect.top, displayMetrics.widthPixels - rect.right, displayMetrics.heightPixels - rect.bottom);
        log("#getWindowInsetsByDisplayMetrics paddingView: b=" + (displayMetrics.heightPixels - rect.bottom));
    }

    @Override
    protected void onDetachedFromWindow() {
        View decorView = (View) getParent();
        if (decorViewLayoutListener != null && decorView != null) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(decorViewLayoutListener);
        }
        if (onLifecycleCallBack != null) {
            onLifecycleCallBack.onDismiss();
        }
        getDynamicWindowInsetsAnimationListener(parentKey).remove(dynamicWindowInsetsAnimationListener);
        onSafeInsetsChangeListener = null;
        parentDialog = null;
        super.onDetachedFromWindow();
    }

    @Override
    public boolean performClick() {
        if (!isEnabled()) return false;
        return super.performClick();
    }

    @Override
    public boolean callOnClick() {
        if (!isEnabled()) return false;
        return super.callOnClick();
    }

    public DialogXBaseRelativeLayout setOnLifecycleCallBack(OnLifecycleCallBack onLifecycleCallBack) {
        this.onLifecycleCallBack = onLifecycleCallBack;
        return this;
    }

    public float getSafeHeight() {
        return getMeasuredHeight() - unsafePlace.bottom - unsafePlace.top;
    }

    private WeakReference<View> requestFocusView;

    public void bindFocusView(View view) {
        requestFocusView = new WeakReference<>(view);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (getParentDialog() != null && getParentDialog() instanceof NoTouchInterface) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            initDynamicSafeAreaListener();
        }
        if (direction == View.FOCUS_DOWN && requestFocusView != null && requestFocusView.get() != null) {
            return requestFocusView.get().requestFocus();
        }
        View findFocusView = findFocus();
        if (findFocusView != null && findFocusView != this) {
            findFocusView.requestFocus();
            return true;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public void requestFocusOnResume() {
        log("#requestFocusOnResume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && getRootWindowInsets() != null && getRootWindowInsets().getStableInsets() != null) {
            paddingView(getRootWindowInsets().getStableInsets());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            initDynamicSafeAreaListener();
        }
        View findFocusView = findFocus();
        if (findFocusView != null && findFocusView != this) {
            findFocusView.requestFocus();
            return;
        }
        requestFocus();
    }

    public abstract static class OnLifecycleCallBack {
        public void onShow() {
        }

        public abstract void onDismiss();
    }

    protected Rect unsafePlace = new Rect();

    private void paddingView(int left, int top, int right, int bottom) {
        unsafePlace = new Rect(left, top, right, bottom);
        if (onSafeInsetsChangeListener != null) onSafeInsetsChangeListener.onChange(unsafePlace);
        MaxRelativeLayout bkgView = findViewById(R.id.bkg);
        if (bkgView != null && bkgView.getLayoutParams() instanceof LayoutParams) {
            LayoutParams bkgLp = (LayoutParams) bkgView.getLayoutParams();
            if (bkgLp.getRules()[ALIGN_PARENT_BOTTOM] == RelativeLayout.TRUE && isAutoUnsafePlacePadding()) {
                bkgView.setNavBarHeight(bottom);
                setPadding(extraPadding[0] + left, extraPadding[1] + top, extraPadding[2] + right, extraPadding[3]);
                if (getParentDialog() instanceof DialogXBaseBottomDialog) {
                    if (((DialogXBaseBottomDialog) getParentDialog()).isBottomNonSafetyAreaBySelf()) {
                        bkgView.setPadding(0, 0, 0, 0);
                        return;
                    }
                }
                bkgView.setPadding(0, 0, 0, bottom);
                return;
            }
        }
        if (isAutoUnsafePlacePadding())
            setPadding(extraPadding[0] + left, extraPadding[1] + top, extraPadding[2] + right, extraPadding[3] + bottom);
    }

    public DialogXBaseRelativeLayout setOnBackPressedListener(PrivateBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        return this;
    }

    public OnSafeInsetsChangeListener getOnSafeInsetsChangeListener() {
        return onSafeInsetsChangeListener;
    }

    public DialogXBaseRelativeLayout setOnSafeInsetsChangeListener(OnSafeInsetsChangeListener onSafeInsetsChangeListener) {
        this.onSafeInsetsChangeListener = onSafeInsetsChangeListener;
        return this;
    }

    public boolean isAutoUnsafePlacePadding() {
        return autoUnsafePlacePadding;
    }

    public Rect getUnsafePlace() {
        return unsafePlace;
    }

    public DialogXBaseRelativeLayout setAutoUnsafePlacePadding(boolean autoUnsafePlacePadding) {
        this.autoUnsafePlacePadding = autoUnsafePlacePadding;
        if (!autoUnsafePlacePadding) {
            setPadding(extraPadding[0], extraPadding[1], extraPadding[2], extraPadding[3]);
        }
        return this;
    }

    public BaseDialog getParentDialog() {
        return parentDialog;
    }

    public DialogXBaseRelativeLayout setParentDialog(BaseDialog parentDialog) {
        this.parentDialog = parentDialog;
        if (parentDialog!=null && parentDialog.getDialogImplMode() != DialogX.IMPL_MODE.VIEW) {
            setFitsSystemWindows(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                paddingView(getRootWindowInsets());
            }
        }
        return this;
    }

    boolean isLightMode = true;

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean newLightStatus = ((newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO);
        if (isLightMode != newLightStatus && DialogX.globalTheme == DialogX.THEME.AUTO && getParentDialog()!=null) {
            getParentDialog().restartDialog();
        }
    }

    float nowBkgAlphaValue;

    public DialogXBaseRelativeLayout setBkgAlpha(float alpha) {
        nowBkgAlphaValue = alpha;
        if (getBackground() != null) getBackground().mutate().setAlpha((int) (alpha * 255));
        return this;
    }

    @Override
    public void setBackground(Drawable background) {
        background.setAlpha((int) (nowBkgAlphaValue * 255));
        super.setBackground(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        setBackground(new ColorDrawable(color));
    }

    public boolean isBaseFocusable() {
        return focusable;
    }

    public boolean isInterceptBack() {
        return interceptBack;
    }

    public DialogXBaseRelativeLayout setInterceptBack(boolean interceptBack) {
        this.interceptBack = interceptBack;
        return this;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE && getAlpha() == 0f) {
            setAlpha(0.01f);
        }
        super.setVisibility(visibility);
    }

    public interface PrivateBackPressedListener {
        boolean onBackPressed();
    }

    int[] extraPadding = new int[4];

    public void setRootPadding(int left, int top, int right, int bottom) {
        extraPadding[0] = left;
        extraPadding[1] = top;
        extraPadding[2] = right;
        extraPadding[3] = bottom;
    }

    public int getRootPaddingLeft() {
        return extraPadding[0];
    }

    public int getRootPaddingTop() {
        return extraPadding[1];
    }

    public int getRootPaddingRight() {
        return extraPadding[2];
    }

    public int getRootPaddingBottom() {
        return extraPadding[3];
    }

    public int getUseAreaWidth() {
        return getWidth() - getRootPaddingRight();
    }

    public int getUseAreaHeight() {
        return getHeight() - getRootPaddingBottom();
    }

    protected void log(String s) {
        if (debugMode) {
            Log.e(">>>" , s);
        }
    }
}
