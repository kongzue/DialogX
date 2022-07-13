package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/18 17:48
 */
public class PopMenuListView extends ListView {
    
    private float maxHeight = -1;
    
    public PopMenuListView(Context context) {
        super(context);
    }
    
    public PopMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public PopMenuListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public boolean isCanScroll() {
        boolean canScroll;
        int count = getCount();
        int firstVisiblePosition = getFirstVisiblePosition();
        int lastVisiblePosition = getLastVisiblePosition();
        canScroll = firstVisiblePosition != 0 || count != lastVisiblePosition + 1;
        return canScroll;
    }
    
    public float getMaxHeight() {
        return maxHeight;
    }
    
    public PopMenuListView setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (maxHeight <= specSize && maxHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Float.valueOf(maxHeight).intValue(), MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void layoutChildren() {
        try {
            super.layoutChildren();
        } catch (IllegalStateException e) {
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
            super.layoutChildren();
        }
    }
}
