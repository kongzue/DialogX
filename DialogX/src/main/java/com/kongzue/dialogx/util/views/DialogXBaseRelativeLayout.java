package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 13:53
 */
public class DialogXBaseRelativeLayout extends RelativeLayout {
    
    private OnLifecycleCallBack onLifecycleCallBack;
    private OnBackPressedListener onBackPressedListener;
    
    public DialogXBaseRelativeLayout(Context context) {
        super(context);
        init();
    }
    
    public DialogXBaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public DialogXBaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    public DialogXBaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    
    private void init() {
        setFocusableInTouchMode(true);
        requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getRootWindowInsets()!=null) {
            onApplyWindowInsets(getRootWindowInsets());
        }
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (onBackPressedListener != null) {
                onBackPressedListener.onBackPressed();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (onLifecycleCallBack != null) {
            onLifecycleCallBack.onShow();
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (onLifecycleCallBack != null) {
            onLifecycleCallBack.onDismiss();
        }
    }
    
    @Override
    public boolean performClick() {
        return super.performClick();
    }
    
    public DialogXBaseRelativeLayout setOnLifecycleCallBack(OnLifecycleCallBack onLifecycleCallBack) {
        this.onLifecycleCallBack = onLifecycleCallBack;
        return this;
    }
    
    public abstract static class OnLifecycleCallBack {
        public void onShow() {
        }
        
        public abstract void onDismiss();
    }
    
    private void paddingView(int left, int top, int right, int bottom) {
        MaxRelativeLayout bkgView = findViewById(R.id.bkg);
        if (bkgView != null) {
            LayoutParams bkgLp = (LayoutParams) bkgView.getLayoutParams();
            if(bkgLp.getRules()[ALIGN_PARENT_BOTTOM] == RelativeLayout.TRUE){
                bkgView.setPadding(0, 0, 0, bottom);
                setPadding(left, top, right, 0);
                return;
            }
        }
        setPadding(left, top, right, bottom);
    }
    
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        paddingView(insets.left,insets.top,insets.right,insets.bottom);
        return true;
    }
    
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paddingView(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
        }
        return super.onApplyWindowInsets(windowInsets);
    }
    
    public DialogXBaseRelativeLayout setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        return this;
    }
}
