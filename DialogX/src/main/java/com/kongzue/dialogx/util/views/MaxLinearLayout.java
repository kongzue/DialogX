package com.kongzue.dialogx.util.views;

import static android.view.View.MeasureSpec.EXACTLY;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.kongzue.dialogx.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/26 11:01
 */
public class MaxLinearLayout extends LinearLayout {
    
    private int maxWidth;
    private int maxHeight;
    private int minWidth;
    private int minHeight;
    
    public MaxLinearLayout(Context context) {
        super(context);
        init(context, null);
    }
    
    public MaxLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public MaxLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialogXMaxLayout);
            maxWidth = a.getDimensionPixelSize(R.styleable.DialogXMaxLayout_maxLayoutWidth, 0);
            maxHeight = a.getDimensionPixelSize(R.styleable.DialogXMaxLayout_maxLayoutHeight, 0);
            minWidth = a.getDimensionPixelSize(R.styleable.DialogXMaxLayout_minLayoutWidth, 0);
            minHeight = a.getDimensionPixelSize(R.styleable.DialogXMaxLayout_minLayoutHeight, 0);
            
            a.recycle();
        }
        minWidth = minWidth == 0 ? getMinimumWidth() : minWidth;
        minHeight = minHeight == 0 ? getMinimumHeight() : minHeight;
    }
    
    public MaxLinearLayout setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }
    
    public MaxLinearLayout setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
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
        if (heightSize > maxHeight && maxHeight != 0) {
            heightSize = maxHeight;
        }
        if (widthSize > maxWidth && maxWidth != 0) {
            widthSize = maxWidth;
        }
        View blurView = findViewWithTag("blurView");
        View contentView = findViewWithoutTag("blurView");
        if (contentView != null && blurView != null) {
            int widthTemp = contentView.getMeasuredWidth() == 0 ? getMeasuredWidth() : contentView.getMeasuredWidth();
            int heightTemp = contentView.getMeasuredHeight() == 0 ? getMeasuredHeight() : contentView.getMeasuredHeight();
            if (widthTemp < minWidth) widthTemp = minWidth;
            if (heightTemp < minHeight) heightTemp = minHeight;
            
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) blurView.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            lp.width = widthTemp;
            lp.height = heightTemp;
            blurView.setLayoutParams(lp);
        } else {
            if (blurView != null) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) blurView.getLayoutParams();
                lp.width = getMeasuredWidth();
                lp.height = getMeasuredHeight();
                blurView.setLayoutParams(lp);
            }
        }
        
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);
    }
    
    private View findViewWithoutTag(String tag) {
        for (int i = 0; i < getChildCount(); i++) {
            if (!tag.equals(getChildAt(i).getTag())) {
                return getChildAt(i);
            }
        }
        return null;
    }
    
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    @Override
    public void setMinimumHeight(int minHeight) {
        this.minHeight = minHeight;
        super.setMinimumHeight(minHeight);
    }
    
    @Override
    public void setMinimumWidth(int minWidth) {
        this.minWidth = minWidth;
        super.setMinimumWidth(minWidth);
    }
}
