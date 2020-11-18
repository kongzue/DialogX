package com.kongzue.dialogx.interfaces;

import android.view.MotionEvent;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/11/18 15:01
 */
public abstract class BottomMenuListViewTouchEvent {
    
    public void down(MotionEvent event){};
    public void move(MotionEvent event){};
    public void up(MotionEvent event){};
}
