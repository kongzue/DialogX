package com.kongzue.dialogx.interfaces;

import android.animation.ValueAnimator;
import android.view.ViewGroup;

import com.kongzue.dialogx.util.ObjectRunnable;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/9/5 9:21
 */
public abstract class DialogXAnimInterface<D> {

    public void doShowAnim(D dialog, ViewGroup dialogBodyView) {
    }

    public void doExitAnim(D dialog, ViewGroup dialogBodyView) {
    }
}
