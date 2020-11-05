package com.kongzue.dialogx.style;

import android.content.Context;
import android.content.res.Resources;

import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.iostheme.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 13:09
 */
public class IOSStyle implements DialogXStyle {
    
    private IOSStyle() {
    }
    
    public static IOSStyle style() {
        return new IOSStyle();
    }
    
    @Override
    public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_ios : R.layout.layout_dialogx_ios_dark;
    }
    
    @Override
    public int enterAnimResId() {
        return R.anim.anim_dialogx_ios_enter;
    }
    
    @Override
    public int exitAnimResId() {
        return 0;
    }
    
    @Override
    public int[] verticalButtonOrder() {
        return new int[]{BUTTON_OK, SPLIT, BUTTON_OTHER, SPLIT, BUTTON_CANCEL};
    }
    
    @Override
    public int[] horizontalButtonOrder() {
        return new int[]{BUTTON_CANCEL, SPLIT, BUTTON_OTHER, SPLIT, BUTTON_OK};
    }
    
    @Override
    public int splitWidthPx() {
        return 1;
    }
    
    @Override
    public int splitColorRes(boolean light) {
        return light ? R.color.dialogxIOSSplitLight : R.color.dialogxIOSSplitDark;
    }
    
    @Override
    public BlurBackgroundSetting messageDialogBlurSettings() {
        return new BlurBackgroundSetting() {
            @Override
            public boolean blurBackground() {
                return true;
            }
            
            @Override
            public int blurForwardColorRes(boolean light) {
                return light ? R.color.dialogxIOSBkgLight : R.color.dialogxIOSBkgDark;
            }
            
            @Override
            public int blurBackgroundRoundRadiusPx() {
                return dip2px(15);
            }
        };
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    @Override
    public HorizontalButtonRes overrideHorizontalButtonRes() {
        return new HorizontalButtonRes() {
            @Override
            public int overrideHorizontalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
                if (visibleButtonCount == 1) {
                    return light ? R.drawable.button_dialogx_ios_bottom_light : R.drawable.button_dialogx_ios_bottom_night;
                } else {
                    return light ? R.drawable.button_dialogx_ios_right_light : R.drawable.button_dialogx_ios_right_night;
                }
            }
            
            @Override
            public int overrideHorizontalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_ios_left_light : R.drawable.button_dialogx_ios_left_night;
            }
            
            @Override
            public int overrideHorizontalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_ios_center_light : R.drawable.button_dialogx_ios_center_night;
            }
        };
    }
    
    @Override
    public VerticalButtonRes overrideVerticalButtonRes() {
        return new VerticalButtonRes() {
            @Override
            public int overrideVerticalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_ios_center_light : R.drawable.button_dialogx_ios_center_night;
            }
            
            @Override
            public int overrideVerticalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_ios_bottom_light : R.drawable.button_dialogx_ios_bottom_night;
            }
            
            @Override
            public int overrideVerticalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_ios_center_light : R.drawable.button_dialogx_ios_center_night;
            }
        };
    }
    
    @Override
    public WaitTipRes overrideWaitTipRes() {
        return new WaitTipRes() {
            @Override
            public boolean blurBackground() {
                return true;
            }
            
            @Override
            public int overrideBackgroundColorRes(boolean light) {
                return 0;
            }
            
            @Override
            public int overrideTextColorRes(boolean light) {
                return light ? R.color.white : R.color.black;
            }
    
            @Override
            public ProgressViewInterface overrideWaitView(Context context, boolean light) {
                return null;
            }
        };
    }
    
    @Override
    public BottomDialogRes overrideBottomDialogRes() {
        return new BottomDialogRes() {
            
            @Override
            public boolean touchSlide() {
                return false;
            }
            
            @Override
            public int overrideDialogLayout(boolean light) {
                //return light ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
                return light ? R.layout.layout_dialogx_bottom_ios : R.layout.layout_dialogx_bottom_ios_dark;
            }
            
            @Override
            public int overrideMenuDividerDrawableRes(boolean light) {
                return light ? R.drawable.rect_dialogx_ios_menu_split_divider : R.drawable.rect_dialogx_ios_menu_split_divider_night;
            }
            
            @Override
            public int overrideMenuDividerHeight(boolean light) {
                return 1;
            }
            
            @Override
            public int overrideMenuTextColor(boolean light) {
                return light ? R.color.dialogxIOSBlue : R.color.dialogxIOSBlueDark;
            }
            
            @Override
            public float overrideBottomDialogMaxHeight() {
                return 0f;
            }
            
            @Override
            public int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility) {
                if (light) {
                    if (index == 0) {
                        return isContentVisibility ? R.layout.item_dialogx_ios_bottom_menu_center_light : R.layout.item_dialogx_ios_bottom_menu_top_light;
                    } else if (index == count - 1) {
                        return R.layout.item_dialogx_ios_bottom_menu_bottom_light;
                    } else {
                        return R.layout.item_dialogx_ios_bottom_menu_center_light;
                    }
                } else {
                    if (index == 0) {
                        return isContentVisibility ? R.layout.item_dialogx_ios_bottom_menu_center_dark : R.layout.item_dialogx_ios_bottom_menu_top_dark;
                    } else if (index == count - 1) {
                        return R.layout.item_dialogx_ios_bottom_menu_bottom_dark;
                    } else {
                        return R.layout.item_dialogx_ios_bottom_menu_center_dark;
                    }
                }
            }
    
            @Override
            public int overrideSelectionMenuBackgroundColor(boolean light) {
                return 0;
            }
    
            @Override
            public boolean selectionImageTint(boolean light) {
                return true;
            }
        };
    }
    
    @Override
    public PopTipSettings popTipSettings() {
        return new PopTipSettings() {
            @Override
            public int layout(boolean light) {
                return light?R.layout.layout_dialogx_poptip_ios :R.layout.layout_dialogx_poptip_ios_dark;
            }
            
            @Override
            public ALIGN align() {
                return ALIGN.TOP;
            }
            
            @Override
            public int enterAnimResId(boolean b) {
                return R.anim.anim_dialogx_ios_top_enter;
            }
            
            @Override
            public int exitAnimResId(boolean b) {
                return R.anim.anim_dialogx_ios_top_exit;
            }
        };
    }
}
