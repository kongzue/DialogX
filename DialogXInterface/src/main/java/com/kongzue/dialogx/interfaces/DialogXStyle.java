package com.kongzue.dialogx.interfaces;

import android.content.Context;

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
    
    BlurBackgroundSetting messageDialogBlurSettings();
    
    HorizontalButtonRes overrideHorizontalButtonRes();
    
    VerticalButtonRes overrideVerticalButtonRes();
    
    WaitTipRes overrideWaitTipRes();
    
    BottomDialogRes overrideBottomDialogRes();
    
    PopTipSettings popTipSettings();
    
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
    
        ProgressViewInterface overrideWaitView(Context context, boolean light);
    }
    
    interface BottomDialogRes {
        
        boolean touchSlide();
        
        int overrideDialogLayout(boolean light);
        
        int overrideMenuDividerDrawableRes(boolean light);
        
        int overrideMenuDividerHeight(boolean light);
        
        int overrideMenuTextColor(boolean light);
        
        float overrideBottomDialogMaxHeight();
        
        int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility);
        
        int overrideSelectionMenuBackgroundColor(boolean light);
        
        boolean selectionImageTint(boolean light);
    }
    
    interface PopTipSettings {
        
        int layout(boolean light);
    
        ALIGN align();
    
        enum ALIGN {
            CENTER,
            TOP,
            BOTTOM,
            TOP_INSIDE,
            BOTTOM_INSIDE
        }
        
        int enterAnimResId(boolean light);
    
        int exitAnimResId(boolean light);
    }
}