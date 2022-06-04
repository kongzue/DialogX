package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BottomMenuListViewTouchEvent;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 23:42
 */
public class BottomDialogListView extends ListView {
    
    private BottomMenuListViewTouchEvent bottomMenuListViewTouchEvent;
    
    public BottomDialogListView(Context context) {
        super(context);
    }
    
    public BottomDialogListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public BottomDialogListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    private BottomDialog.DialogImpl dialogImpl;
    
    public BottomDialogListView(BottomDialog.DialogImpl dialog, Context context) {
        super(context);
        dialogImpl = dialog;
        setVerticalScrollBarEnabled(false);
    }
    
    public BottomDialogListView(BottomDialog.DialogImpl dialog, Context context,int theme) {
        super(new ContextThemeWrapper(context,theme));
        dialogImpl = dialog;
        setVerticalScrollBarEnabled(false);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    private int mPosition;
    private float touchDownY;
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;
        
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            touchDownY = ev.getY();
            if (bottomMenuListViewTouchEvent != null) {
                bottomMenuListViewTouchEvent.down(ev);
            }
            mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            return super.dispatchTouchEvent(ev);
        }
        
        if (actionMasked == MotionEvent.ACTION_MOVE) {
            if (bottomMenuListViewTouchEvent != null) {
                bottomMenuListViewTouchEvent.move(ev);
            }
            if (Math.abs(touchDownY - ev.getY()) > dip2px(5)) {
                ev.setAction(MotionEvent.ACTION_CANCEL);
                dispatchTouchEvent(ev);
                return false;
            }
            return true;
        }
        if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {
            if (bottomMenuListViewTouchEvent != null) {
                bottomMenuListViewTouchEvent.up(ev);
            }
            if (pointToPosition((int) ev.getX(), (int) ev.getY()) == mPosition) {
                super.dispatchTouchEvent(ev);
            } else {
                setPressed(false);
                invalidate();
            }
        }
        
        return super.dispatchTouchEvent(ev);
    }
    
    public BottomMenuListViewTouchEvent getBottomMenuListViewTouchEvent() {
        return bottomMenuListViewTouchEvent;
    }
    
    private int size = 1;
    
    @Override
    public void setAdapter(ListAdapter adapter) {
        size = adapter.getCount();
        super.setAdapter(adapter);
    }
    
    public BottomDialogListView setBottomMenuListViewTouchEvent(BottomMenuListViewTouchEvent bottomMenuListViewTouchEvent) {
        this.bottomMenuListViewTouchEvent = bottomMenuListViewTouchEvent;
        return this;
    }
}
