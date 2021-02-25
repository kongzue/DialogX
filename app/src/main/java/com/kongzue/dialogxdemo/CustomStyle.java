package com.kongzue.dialogxdemo;

import android.content.Context;
import android.content.res.Resources;

import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.views.ProgressView;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/2/20 19:58
 */
public class CustomStyle implements DialogXStyle {
    
    private CustomStyle() {
    }
    
    public static CustomStyle style() {
        return new CustomStyle();
    }
    
    @Override
    public int layout(boolean light) {
        return light ? com.kongzue.dialogx.iostheme.R.layout.layout_dialogx_ios : com.kongzue.dialogx.iostheme.R.layout.layout_dialogx_ios_dark;
    }
    
    @Override
    public int enterAnimResId() {
        return com.kongzue.dialogx.iostheme.R.anim.anim_dialogx_ios_enter;
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
        return light ? com.kongzue.dialogx.iostheme.R.color.dialogxIOSSplitLight : com.kongzue.dialogx.iostheme.R.color.dialogxIOSSplitDark;
    }
    
    @Override
    public DialogXStyle.BlurBackgroundSetting messageDialogBlurSettings() {
        return new DialogXStyle.BlurBackgroundSetting() {
            @Override
            public boolean blurBackground() {
                return true;
            }
            
            @Override
            public int blurForwardColorRes(boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.color.dialogxIOSBkgLight : com.kongzue.dialogx.iostheme.R.color.dialogxIOSBkgDark;
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
    public DialogXStyle.HorizontalButtonRes overrideHorizontalButtonRes() {
        return new DialogXStyle.HorizontalButtonRes() {
            @Override
            public int overrideHorizontalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
                if (visibleButtonCount == 1) {
                    return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_bottom_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_bottom_night;
                } else {
                    return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_right_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_right_night;
                }
            }
            
            @Override
            public int overrideHorizontalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_left_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_left_night;
            }
            
            @Override
            public int overrideHorizontalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_center_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_center_night;
            }
        };
    }
    
    @Override
    public DialogXStyle.VerticalButtonRes overrideVerticalButtonRes() {
        return new DialogXStyle.VerticalButtonRes() {
            @Override
            public int overrideVerticalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_center_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_center_night;
            }
            
            @Override
            public int overrideVerticalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_bottom_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_bottom_night;
            }
            
            @Override
            public int overrideVerticalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_center_light : com.kongzue.dialogx.iostheme.R.drawable.button_dialogx_ios_center_night;
            }
        };
    }
    
    @Override
    public DialogXStyle.WaitTipRes overrideWaitTipRes() {
        return new DialogXStyle.WaitTipRes() {
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
                return light ? com.kongzue.dialogx.iostheme.R.color.white : com.kongzue.dialogx.iostheme.R.color.black;
            }
            
            @Override
            public ProgressViewInterface overrideWaitView(Context context, boolean light) {
                return new ProgressView(context).setLightMode(light);
            }
        };
    }
    
    @Override
    public DialogXStyle.BottomDialogRes overrideBottomDialogRes() {
        return new DialogXStyle.BottomDialogRes() {
            
            @Override
            public boolean touchSlide() {
                return false;
            }
            
            @Override
            public int overrideDialogLayout(boolean light) {
                //return light ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
                return light ? com.kongzue.dialogx.iostheme.R.layout.layout_dialogx_bottom_ios : com.kongzue.dialogx.iostheme.R.layout.layout_dialogx_bottom_ios_dark;
            }
            
            @Override
            public int overrideMenuDividerDrawableRes(boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.drawable.rect_dialogx_ios_menu_split_divider : com.kongzue.dialogx.iostheme.R.drawable.rect_dialogx_ios_menu_split_divider_night;
            }
            
            @Override
            public int overrideMenuDividerHeight(boolean light) {
                return 1;
            }
            
            @Override
            public int overrideMenuTextColor(boolean light) {
                return light ? com.kongzue.dialogx.iostheme.R.color.dialogxIOSBlue : com.kongzue.dialogx.iostheme.R.color.dialogxIOSBlueDark;
            }
            
            @Override
            public float overrideBottomDialogMaxHeight() {
                return 0f;
            }
            
            @Override
            public int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility) {
                if (light) {
                    if (index == 0) {
                        return isContentVisibility ? com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_center_light : com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_top_light;
                    } else if (index == count - 1) {
                        return com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_bottom_light;
                    } else {
                        return com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_center_light;
                    }
                } else {
                    if (index == 0) {
                        return isContentVisibility ? com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_center_dark : com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_top_dark;
                    } else if (index == count - 1) {
                        return com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_bottom_dark;
                    } else {
                        return com.kongzue.dialogx.iostheme.R.layout.item_dialogx_ios_bottom_menu_center_dark;
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
            
            @Override
            public int overrideSelectionImage(boolean light, boolean isSelected) {
                return 0;
            }
        };
    }
    
    @Override
    public DialogXStyle.PopTipSettings popTipSettings() {
        return new DialogXStyle.PopTipSettings() {
            @Override
            public int layout(boolean light) {
                return light? com.kongzue.dialogx.iostheme.R.layout.layout_dialogx_poptip_ios : com.kongzue.dialogx.iostheme.R.layout.layout_dialogx_poptip_ios_dark;
            }
            
            @Override
            public ALIGN align() {
                return ALIGN.BOTTOM;
            }
            
            @Override
            public int enterAnimResId(boolean b) {
                return com.kongzue.dialogx.iostheme.R.anim.anim_dialogx_bottom_enter;
            }
            
            @Override
            public int exitAnimResId(boolean b) {
                return com.kongzue.dialogx.iostheme.R.anim.anim_dialogx_bottom_exit;
            }
        };
    }
}
