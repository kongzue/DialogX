package com.kongzue.dialogx.util.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.kongzue.dialogx.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/9/24 17:34
 */
public class MaxRelativeLayout extends RelativeLayout {
    
    private int maxWidth;
    private int maxHeight;
    private boolean lockWidth;
    private boolean interceptTouch = true;
    
    public MaxRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }
    
    public MaxRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public MaxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private float startAnimValue = 0, endAnimValue = 0;
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxRelativeLayout);
            maxWidth = a.getDimensionPixelSize(R.styleable.MaxRelativeLayout_maxLayoutWidth, 0);
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxRelativeLayout_maxLayoutHeight, 0);
            lockWidth = a.getBoolean(R.styleable.MaxRelativeLayout_lockWidth, false);
            interceptTouch = a.getBoolean(R.styleable.MaxRelativeLayout_interceptTouch, true);
            
            a.recycle();
        }
        
        if (!isInEditMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animate().setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = (float) animation.getAnimatedValue();
                        long value = (long) (startAnimValue + (endAnimValue - startAnimValue) * progress);
                        if (onYChangedListener != null) onYChangedListener.y(value);
                    }
                });
            }
        }
    }
    
    private ScrollView childScrollView;
    
    public MaxRelativeLayout setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }
    
    public MaxRelativeLayout setMaxWidth(int maxWidth) {
        if (maxWidth > 0 && this.maxWidth != 0) this.maxWidth = maxWidth;
        return this;
    }
    
    private int preWidth = -1;
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        
        if (preWidth == -1 && widthSize != 0) {
            preWidth = widthSize;
        }
        if (lockWidth) {
            maxWidth = Math.min(widthSize, preWidth);
        }
        if (maxHeight > 0) {
            heightSize = Math.min(heightSize, maxHeight);
        }
        if (maxWidth > 0) {
            widthSize = Math.min(widthSize, maxWidth);
        }
        
        View blurView = findViewWithTag("blurView");
        if (blurView != null) {
            LayoutParams lp = (LayoutParams) blurView.getLayoutParams();
            lp.width = getMeasuredWidth();
            lp.height = getMeasuredHeight();
            blurView.setLayoutParams(lp);
        }
        
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);
        
        childScrollView = findViewById(R.id.scrollView);
    }
    
    private boolean isMove = false;
    private int touchY, touchX;
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!interceptTouch) {
            return super.onInterceptTouchEvent(event);
        }
        if (onTouchListener != null) {
            onTouchListener.onTouch(this, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (!isMove) {
                        return false;
                    }
                    isMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isMove) {
                        touchY = (int) event.getRawY();
                        touchX = (int) event.getRawX();
                    }
                    isMove = true;
                    
                    float moveY = event.getRawY();
                    float moveX = event.getRawX();
                    
                    if (Math.abs(moveY - touchY) > dip2px(20) || Math.abs(moveX - touchX) > dip2px(20)) {
                        final ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        return true;
                    }
                    break;
            }
            return isMove;
        }
        return super.onInterceptTouchEvent(event);
    }
    
    public boolean isChildScrollViewCanScroll() {
        if (childScrollView == null) return false;
        if (!childScrollView.isEnabled()) {
            return false;
        }
        View child = childScrollView.getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return childScrollView.getHeight() < childHeight;
        }
        return false;
    }
    
    private OnTouchListener onTouchListener;
    
    public void setTouchCallBack(OnTouchListener l) {
        onTouchListener = l;
        super.setOnTouchListener(l);
    }
    
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public boolean isLockWidth() {
        return lockWidth;
    }
    
    public MaxRelativeLayout setLockWidth(boolean lockWidth) {
        this.lockWidth = lockWidth;
        return this;
    }
    
    private OnYChanged onYChangedListener;
    
    public interface OnYChanged {
        void y(float y);
    }
    
    @Override
    public void setY(float y) {
        super.setY(y);
    }
    
    public OnYChanged getOnYChanged() {
        return onYChangedListener;
    }
    
    public MaxRelativeLayout setOnYChanged(OnYChanged onYChanged) {
        this.onYChangedListener = onYChanged;
        return this;
    }
    
    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        if (onYChangedListener != null) onYChangedListener.y(translationY);
    }
}
