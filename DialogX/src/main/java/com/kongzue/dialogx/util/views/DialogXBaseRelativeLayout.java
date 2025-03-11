package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.annotation.Px;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogXBaseBottomDialog;
import com.kongzue.dialogx.interfaces.DialogXSafetyModeInterface;
import com.kongzue.dialogx.interfaces.NoTouchInterface;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;

import java.lang.ref.WeakReference;

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
    private WeakReference<BaseDialog> parentDialog;
    private boolean autoUnsafePlacePadding = true;
    private boolean focusable = true;
    private boolean interceptBack = true;

    private OnLifecycleCallBack onLifecycleCallBack;
    private PrivateBackPressedListener onBackPressedListener;

    private FitSystemBarUtils fitSystemBarUtils;

    public FitSystemBarUtils getFitSystemBarUtils() {
        return fitSystemBarUtils;
    }

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
            }
            setBkgAlpha(0f);
            if (getParentDialog() != null && getParentDialog().getDialogImplMode() != DialogX.IMPL_MODE.VIEW) {
                setFitsSystemWindows(true);
            }
            setClipChildren(false);
            setClipToPadding(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setDefaultFocusHighlightEnabled(false);
            }

            //新增的 设置监听 OnApplyWindowInsetsListener
            log("KONGZUE DEBUG DIALOGX: create fitSystemBarUtils");
            fitSystemBarUtils = FitSystemBarUtils.attachView(this, new FitSystemBarUtils.CallBack() {
                @Override
                public boolean isEnable(FitSystemBarUtils.Orientation orientation) {
                    return true;
                }

                @Override
                public void unsafeRect(int start, int top, int end, int bottom) {
                    log("KONGZUE DEBUG DIALOGX: unsafeRect t=" + top + " b=" + bottom);
                    if (unsafePlace == null) {
                        unsafePlace = new Rect();
                    }
                    // TODO Fix bug #370 在这里对这个重新做下处理，未详细测试，比如键盘弹起时的情况
                    Insets systemBarInsets = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getRootWindowInsets() != null) {
                        WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(getRootWindowInsets());
                        boolean navigationBarsVisible = windowInsetsCompat.isVisible(WindowInsetsCompat.Type.navigationBars());
                        boolean imeVisible = windowInsetsCompat.isVisible(WindowInsetsCompat.Type.ime());
                        if (!imeVisible && navigationBarsVisible) {
                            systemBarInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
                            if (systemBarInsets.bottom == bottom && systemBarInsets.top == top && systemBarInsets.left == start && systemBarInsets.right == end) {
                                systemBarInsets = null;
                            }
                        }
                    }
                    if (systemBarInsets != null) {
                        unsafePlace.left = Math.max(systemBarInsets.left, start);
                        unsafePlace.top = Math.max(systemBarInsets.top, top);
                        unsafePlace.right = Math.max(systemBarInsets.right, end);
                        unsafePlace.bottom = Math.max(systemBarInsets.bottom, bottom);
                    } else {
                        unsafePlace.left = start;
                        unsafePlace.top = top;
                        unsafePlace.right = end;
                        unsafePlace.bottom = bottom;
                    }

                    if (onSafeInsetsChangeListener != null) {
                        onSafeInsetsChangeListener.onChange(unsafePlace);
                    }

                    setUnsafePadding(unsafePlace.left, unsafePlace.top, unsafePlace.right, unsafePlace.bottom);
                }

                @Override
                public int initialPadding(FitSystemBarUtils.Orientation orientation) {
                    switch (orientation) {
                        case Start:
                            return extraPadding[0];
                        case Top:
                            return extraPadding[1];
                        case End:
                            return extraPadding[2];
                        case Bottom:
                            return extraPadding[3];
                    }
                    return 0;
                }
            });
        }
    }

    public void setUnsafePadding(@Px int start, @Px int top, @Px int end, @Px int bottom) {
        log("KONGZUE DEBUG DIALOGX: setUnsafePadding=" + getParentDialog() + " t=" + top + " b=" + bottom);
        if (DialogX.ignoreUnsafeInsetsHorizontal) {
            log("  KONGZUE DEBUG DIALOGX: ignoreUnsafeInsetsHorizontal, start and end set 0");
            start = 0;
            end = 0;
        }
        if (isAlignBottomDialog(getParentDialog())) {
            log("  KONGZUE DEBUG DIALOGX: Dialog is align bottom");
            View dialogXSafetyArea = findViewWithTag("DialogXSafetyArea");
            if (dialogXSafetyArea instanceof DialogXSafetyModeInterface) {
                int dialogxSafetyMode = ((DialogXSafetyModeInterface) dialogXSafetyArea).getDialogXSafetyMode();
                boolean hasTop = (dialogxSafetyMode & 0x1) != 0;
                boolean hasLeft = (dialogxSafetyMode & 0x2) != 0;
                boolean hasBottom = (dialogxSafetyMode & 0x4) != 0;
                boolean hasRight = (dialogxSafetyMode & 0x8) != 0;
                log("    KONGZUE DEBUG DIALOGX: dialogXSafetyArea" + dialogXSafetyArea + " hasLeft=" + hasLeft + "hasTop=" + hasTop + " hasRight=" + hasRight + " hasBottom=" + hasBottom);
                dialogXSafetyArea.setPadding(hasLeft ? start : 0, hasTop ? top : 0, hasRight ? end : 0, hasBottom ? bottom : 0);
                if (hasTop) {
                    top = 0;
                }
                if (hasLeft) {
                    start = 0;
                }
                if (hasRight) {
                    end = 0;
                }
                if (hasBottom) {
                    bottom = 0;
                }
            } else {
                ViewGroup bkgView = findViewById(R.id.bkg);
                if (!((DialogXBaseBottomDialog) getParentDialog()).isBottomNonSafetyAreaBySelf() && bkgView != null) {
                    log("    KONGZUE DEBUG DIALOGX: bkgView.setPadding b=" + bottom);
                    bkgView.setPadding(0, 0, 0, bottom);
                }
                bottom = 0;
            }
        }
        if (isAutoUnsafePlacePadding()) {
            log("  KONGZUE DEBUG DIALOGX: root.setPadding t=" + top + " b=" + bottom);
            setPadding(start, top, end, bottom);
        }
    }

    private boolean isAlignBottomDialog(BaseDialog parentDialog) {
        return getParentDialog() instanceof DialogXBaseBottomDialog || findViewWithTag("DialogXSafetyArea") instanceof DialogXSafetyModeInterface;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        log("#dispatchKeyEvent: KeyCode=" + event.getKeyCode());
        if (isAttachedToWindow() && event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK && interceptBack) {
            if (onBackPressedListener != null && !parentDialog.get().isHide()) {
                return onBackPressedListener.onBackPressed();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    boolean touch;
    float touchDownX, touchDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch = true;
                touchDownX = event.getX();
                touchDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (touch && findFocus() != this && getParentDialog() != null) {
                    float deltaTouchOffset = getParentDialog().dip2px(5);
                    if (Math.abs(event.getX() - touchDownX) <= deltaTouchOffset && Math.abs(event.getY() - touchDownY) <= deltaTouchOffset) {
                        callOnClick();
                    }
                }
                break;
        }
        if (getParentDialog() instanceof NoTouchInterface) {
            return super.onTouchEvent(event);
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            if (getParentDialog() == null || getParentDialog().getOwnActivity() == null) return;
            if (onLifecycleCallBack != null) {
                onLifecycleCallBack.onShow();
            }
            isLightMode = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO;
            if (focusable) {
                requestFocus();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (onLifecycleCallBack != null) {
            onLifecycleCallBack.onDismiss();
        }
        if (fitSystemBarUtils != null) {
            fitSystemBarUtils.recycle();
        }
        fitSystemBarUtils = null;
        onSafeInsetsChangeListener = null;
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
        if (view != this) {
            requestFocusView = new WeakReference<>(view);
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (getParentDialog() != null && getParentDialog() instanceof NoTouchInterface) {
            return false;
        }
        if (direction == View.FOCUS_DOWN && requestFocusView != null && requestFocusView.get() != null && requestFocusView.get() != this) {
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
        return this;
    }

    public BaseDialog getParentDialog() {
        return parentDialog == null ? null : parentDialog.get();
    }

    public DialogXBaseRelativeLayout setParentDialog(BaseDialog parentDialog) {
        this.parentDialog = new WeakReference<>(parentDialog);
        if (parentDialog != null && parentDialog.getDialogImplMode() != DialogX.IMPL_MODE.VIEW) {
            setFitsSystemWindows(true);
        }
        if (unsafePlace != null) {
            log("KONGZUE DEBUG DIALOGX: setParentDialog()=" + getParentDialog());
            setUnsafePadding(unsafePlace.left, unsafePlace.top, unsafePlace.right, unsafePlace.bottom);
        } else {
            log("KONGZUE DEBUG DIALOGX: setParentDialog() unsafePlace is null");
        }
        return this;
    }

    boolean isLightMode = true;

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean newLightStatus = ((newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO);
        if (isLightMode != newLightStatus && DialogX.globalTheme == DialogX.THEME.AUTO && getParentDialog() != null) {
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
        if (debugMode && DialogX.DEBUGMODE) {
            Log.e(">>>", s);
        }
    }
}
