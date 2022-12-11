package com.kongzue.dialogx.interfaces;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/12/11 17:39
 */
public abstract class BottomDialogSlideEventLifecycleCallback<D extends BaseDialog> extends DialogLifecycleCallback<D> {
    
    public boolean onSlideClose(D dialog) {
        return false;
    }
    
    public boolean onSlideTouchEvent(D dialog, View v, MotionEvent event) {
        return false;
    }
}
