package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.kongzue.dialogx.interfaces.BottomMenuListViewTouchEvent;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.ScrollController;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 23:42
 */
public class DialogListView extends ListView implements ScrollController {

    private BottomMenuListViewTouchEvent bottomMenuListViewTouchEvent;

    public DialogListView(Context context) {
        super(context);
    }

    public DialogListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private DialogConvertViewInterface dialogImpl;

    public DialogListView(DialogConvertViewInterface dialog, Context context) {
        super(context);
        dialogImpl = dialog;
        setVerticalScrollBarEnabled(false);
    }

    public DialogListView(DialogConvertViewInterface dialog, Context context, int theme) {
        super(new ContextThemeWrapper(context, theme));
        dialogImpl = dialog;
        setVerticalScrollBarEnabled(false);
    }

//    private int dip2px(float dpValue) {
//        final float scale = Resources.getSystem().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }
//
//    private int mPosition;
//    private float touchDownY;
//
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;
        switch (actionMasked){
            case MotionEvent.ACTION_DOWN:
                if (bottomMenuListViewTouchEvent != null) {
                    bottomMenuListViewTouchEvent.down(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (bottomMenuListViewTouchEvent != null) {
                    bottomMenuListViewTouchEvent.move(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (bottomMenuListViewTouchEvent != null) {
                    bottomMenuListViewTouchEvent.up(ev);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public BottomMenuListViewTouchEvent getBottomMenuListViewTouchEvent() {
        return bottomMenuListViewTouchEvent;
    }

    public DialogListView setBottomMenuListViewTouchEvent(BottomMenuListViewTouchEvent bottomMenuListViewTouchEvent) {
        this.bottomMenuListViewTouchEvent = bottomMenuListViewTouchEvent;
        return this;
    }

    @Override
    public int getScrollDistance() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        int scrollY = -top + firstVisiblePosition * c.getHeight();
        return scrollY;
    }

    @Override
    public boolean isCanScroll() {
        return true;
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
    public boolean isLockScroll() {
        return lockScroll;
    }
}
