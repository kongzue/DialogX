package com.kongzue.dialogx.util;

import android.animation.ValueAnimator;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/7/19 14:31
 */
public class PopValueAnimator extends ValueAnimator {
    
    float startValue;
    float endValue;
    
    public static PopValueAnimator ofFloat(float... values) {
        PopValueAnimator anim = new PopValueAnimator();
        anim.setFloatValues(values);
        return anim;
    }
    
    @Override
    public void setFloatValues(float... values) {
        if (values.length > 1) {
            startValue = values[0];
            endValue = values[values.length - 1];
        }
        super.setFloatValues(values);
    }
    
    public float getStartValue() {
        return startValue;
    }
    
    public float getEndValue() {
        return endValue;
    }
}
