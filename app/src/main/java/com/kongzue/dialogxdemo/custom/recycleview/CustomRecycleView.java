package com.kongzue.dialogxdemo.custom.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialogx.interfaces.ScrollController;

/**
 * 自定义滑动布局范例。此处以 RecyclerView 为主要例子
 * 请注意实现 ScrollController，BottomDialog 需要根据此接口给出的数据进行滑动事件拦截的处理和判断
 *
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/5 9:23
 */
public class CustomRecycleView extends RecyclerView implements ScrollController {
    
    public CustomRecycleView(@NonNull Context context) {
        super(context);
    }
    
    public CustomRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CustomRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    /**
     * 滑动锁定判断依据，若此标记被置为 true，则意味着滑动由父布局处理，请勿进行任何滚动操作。
     * 具体请参考 {@link #onTouchEvent(MotionEvent)} 的处理方案，其他诸如 ScrollView 处理方式相同。
     */
    boolean lockScroll;
    
    @Override
    public boolean isLockScroll() {
        return lockScroll;
    }
    
    @Override
    public void lockScroll(boolean lockScroll) {
        this.lockScroll = lockScroll;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (lockScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
    
    /**
     * 是否可以滑动判断依据，若当前滑动布局内容高度小于布局高度则为不可滑动。
     * 此处列举的是 RecycleView 的判断依据编写方法，
     * ScrollView 的范例请参考 {@link com.kongzue.dialogx.util.views.BottomDialogScrollView#isCanScroll()}
     *
     * @return 是否可滑动
     */
    @Override
    public boolean isCanScroll() {
        return canScrollVertically(1) || canScrollVertically(-1);
    }
    
    /**
     * 此处请给出已滑动距离值，BottomDialog 需要根据此值判断当子布局滑动过程中，父布局是否需要介入滑动流程。
     * 此处列举的是 RecycleView 的判断依据编写方法，
     * ScrollView 的范例请参考 {@link com.kongzue.dialogx.util.views.BottomDialogScrollView#getScrollDistance()}
     *
     * @return 已滑动距离
     */
    public int getScrollDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibleItem = this.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }
    
}
