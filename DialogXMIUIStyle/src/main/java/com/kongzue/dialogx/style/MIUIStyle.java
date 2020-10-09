package com.kongzue.dialogx.style;

import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.miuistyle.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 17:04
 */
public class MIUIStyle implements DialogXStyle {
    
    private MIUIStyle() {
    }
    
    public static MIUIStyle style() {
        return new MIUIStyle();
    }
    
    @Override
    public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_miui : R.layout.layout_dialogx_miui_dark;
    }
    
    @Override
    public int enterAnimResId() {
        return R.anim.anim_dialogx_bottom_enter;
    }
    
    @Override
    public int exitAnimResId() {
        return R.anim.anim_dialogx_bottom_exit;
    }
    
    @Override
    public int[] verticalButtonOrder() {
        return new int[]{BUTTON_OK, BUTTON_OTHER, BUTTON_CANCEL};
    }
    
    @Override
    public int[] horizontalButtonOrder() {
        return new int[]{BUTTON_CANCEL, BUTTON_OTHER, BUTTON_OK};
    }
    
    @Override
    public int splitWidthPx() {
        return 0;
    }
    
    @Override
    public int splitColorRes(boolean light) {
        return 0;
    }
    
    @Override
    public BlurBackgroundSetting blurSettings() {
        return null;
    }
    
    @Override
    public HorizontalButtonRes overrideHorizontalButtonRes() {
        return null;
    }
    
    @Override
    public VerticalButtonRes overrideVerticalButtonRes() {
        return null;
    }
    
    @Override
    public WaitTipRes overrideWaitTipRes() {
        return new WaitTipRes() {
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
                return light ? R.color.white : R.color.black;
            }
        };
    }
    
    @Override
    public BottomDialogRes overrideBottomDialogRes() {
        return null;
    }
}
