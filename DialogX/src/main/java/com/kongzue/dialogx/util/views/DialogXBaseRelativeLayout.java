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
import androidx.annotation.Px;
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

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 13:53
 */
public class DialogXBaseRelativeLayout extends RelativeLayout {

    public static boolean debugMode = true;

    private OnSafeInsetsChangeListener onSafeInsetsChangeListener;
    private BaseDialog parentDialog;
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
        }
        //新增的 设置监听 OnApplyWindowInsetsListener
        fitSystemBarUtils = FitSystemBarUtils.attachView(this, new FitSystemBarUtils.CallBack() {
            @Override
            public boolean isEnable(FitSystemBarUtils.Orientation orientation) {
                return true;
            }

            @Override
            public void unsafeRect(int start, int top, int end, int bottom) {
                if (unsafePlace == null) {
                    unsafePlace = new Rect();
                }
                unsafePlace.left = start;
                unsafePlace.top = top;
                unsafePlace.right = end;
                unsafePlace.bottom = bottom;

                if (onSafeInsetsChangeListener != null) {
                    onSafeInsetsChangeListener.onChange(unsafePlace);
                }

                //做下判断，如果是底部对话框，则把paddingBottom设为0，改为推起子控件
                MaxRelativeLayout bkgView = findViewById(R.id.bkg);
                if (bkgView != null && bkgView.getLayoutParams() instanceof LayoutParams) {
                    LayoutParams bkgLp = (LayoutParams) bkgView.getLayoutParams();
                    if (bkgLp.getRules()[ALIGN_PARENT_BOTTOM] == RelativeLayout.TRUE && isAutoUnsafePlacePadding()) {
                        setPadding(extraPadding[0] + unsafePlace.left,
                                extraPadding[1] + unsafePlace.top,
                                extraPadding[2] + unsafePlace.right,
                                extraPadding[3]
                        );
                        bkgView.setNavBarHeight(bottom);
                        if (getParentDialog() instanceof DialogXBaseBottomDialog) {
                            if (((DialogXBaseBottomDialog) getParentDialog()).isBottomNonSafetyAreaBySelf()) {
                                bkgView.setPadding(0, 0, 0, 0);
                                return;
                            }
                        }
                        unsafePlace.bottom = 0;
                        bkgView.setPadding(0, 0, 0, bottom);
                    }
                }
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

    public void setUnsafePadding(@Px int start, @Px int top, @Px int end, @Px int bottom) {
        if (!isAutoUnsafePlacePadding()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            setPaddingRelative(start, top, end, bottom);
        } else {
            setPadding(start, top, end, bottom);
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
                if (touch && findFocus() != this) {
                    float deltaTouchOffset = parentDialog.dip2px(5);
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

    @Override
    protected void onDetachedFromWindow() {
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
        return parentDialog;
    }

    public DialogXBaseRelativeLayout setParentDialog(BaseDialog parentDialog) {
        this.parentDialog = parentDialog;
        if (parentDialog != null && parentDialog.getDialogImplMode() != DialogX.IMPL_MODE.VIEW) {
            setFitsSystemWindows(true);
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
        if (debugMode) {
            Log.e(">>>", s);
        }
    }
}
