package com.kongzue.dialogxdemo.custom.style;

import android.content.Context;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.views.NoArticulatedProgressView;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/2/20 19:58
 */
public class CustomStyle extends MaterialStyle {
    
    public static CustomStyle style() {
        return new CustomStyle();
    }
    
    @Override
    public WaitTipRes overrideWaitTipRes() {
        return new WaitTipRes() {
            @Override
            public int overrideWaitLayout(boolean b) {
                return 0;
            }
    
            @Override
            public int overrideRadiusPx() {
                return -1;
            }
    
            @Override
            public boolean blurBackground() {
                return false;
            }
        
            @Override
            public int overrideBackgroundColorRes(boolean light) {
                return 0;
            }
        
            @Override
            public int overrideTextColorRes(boolean light) {
                return light ? com.kongzue.dialogx.R.color.white : com.kongzue.dialogx.R.color.black;
            }
        
            @Override
            public ProgressViewInterface overrideWaitView(Context context, boolean light) {
                return new NoArticulatedProgressView(context);
            }
        };
    }
}
