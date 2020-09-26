package com.kongzue.dialogx.impl;

import android.animation.Animator;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 14:37
 */
public abstract class AnimatorListenerEndCallBack implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) {
    
    }
    
    @Override
    public abstract void onAnimationEnd(Animator animation);
    
    @Override
    public void onAnimationCancel(Animator animation) {
    
    }
    
    @Override
    public void onAnimationRepeat(Animator animation) {
    
    }
}
