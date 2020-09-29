package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxRelativeLayout);
            maxWidth = a.getDimensionPixelSize(R.styleable.MaxRelativeLayout_maxLayoutWidth, 0);
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxRelativeLayout_maxLayoutHeight, 0);
            
            a.recycle();
        }
    }
    
    public MaxRelativeLayout setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }
    
    public MaxRelativeLayout setMaxWidth(int maxWidth) {
        if (maxWidth > 0 && this.maxWidth != 0) this.maxWidth = maxWidth;
        return this;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        
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
    }
}
