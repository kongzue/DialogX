package com.kongzue.dialogxdemo.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.dialogx.interfaces.ScrollController;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/3/4 13:02
 */
public class SCWebView extends WebView implements ScrollController {
    public SCWebView(@NonNull Context context) {
        super(context);
    }
    
    public SCWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SCWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public SCWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    @Override
    @Deprecated
    public boolean isLockScroll() {
        return lockScroll;
    }
    
    boolean lockScroll;
    
    @Override
    public void lockScroll(boolean lockScroll) {
        this.lockScroll = lockScroll;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lockScroll) return false;
        return super.onTouchEvent(event);
    }
    
    @Override
    public int getScrollDistance() {
        return getScrollY();
    }
    
    @Override
    public boolean isCanScroll() {
        return true;
    }
}
