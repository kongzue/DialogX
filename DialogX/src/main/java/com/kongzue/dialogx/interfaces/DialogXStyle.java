package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 13:14
 */
public interface DialogXStyle {
    
    int styleVer = 1;
    
    int BUTTON_OK = 1;
    int BUTTON_CANCEL = 2;
    int BUTTON_OTHER = 3;
    int SPACE = 4;
    int SPLIT = 5;
    
    int layout(boolean light);
    
    int enterAnimResId();
    
    int exitAnimResId();
    
    int[] verticalButtonOrder();
    
    int[] horizontalButtonOrder();
    
    int splitWidthPx();
    
    int splitColorRes(boolean light);
    
    BlurBackgroundSetting blurSettings();
    
    HorizontalButtonRes overrideHorizontalButtonRes();
    
    VerticalButtonRes overrideVerticalButtonRes();
    
    interface BlurBackgroundSetting {
        
        boolean blurBackground();
        
        int blurForwardColorRes(boolean light);
        
        int blurBackgroundRoundRadiusPx();
    }
    
    interface HorizontalButtonRes {
        
        int overrideHorizontalOkButtonBackgroundRes(int visibleButtonCount, boolean light);
        
        int overrideHorizontalCancelButtonBackgroundRes(int visibleButtonCount, boolean light);
        
        int overrideHorizontalOtherButtonBackgroundRes(int visibleButtonCount, boolean light);
    }
    
    interface VerticalButtonRes {
        
        int overrideVerticalOkButtonBackgroundRes(int visibleButtonCount, boolean light);
        
        int overrideVerticalCancelButtonBackgroundRes(int visibleButtonCount, boolean light);
        
        int overrideVerticalOtherButtonBackgroundRes(int visibleButtonCount, boolean light);
    }
}
