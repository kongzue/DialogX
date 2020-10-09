package com.kongzue.dialogx.interfaces;

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
    
    WaitTipRes overrideWaitTipRes();
    
    BottomDialogRes overrideBottomDialogRes();
    
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
    
    interface WaitTipRes {
        
        boolean blurBackground();
        
        int overrideBackgroundColorRes(boolean light);
        
        int overrideTextColorRes(boolean light);
    }
    
    interface BottomDialogRes {
        
        boolean touchSlide();
        
        int overrideDialogLayout(boolean light);
        
        int overrideMenuDividerDrawableRes(boolean light);
    
        int overrideMenuDividerHeight(boolean light);
    
        int overrideMenuTextColor(boolean light);
        
        float overrideBottomDialogMaxHeight();
    }
}