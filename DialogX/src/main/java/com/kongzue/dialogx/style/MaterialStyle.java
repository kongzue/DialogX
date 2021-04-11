package com.kongzue.dialogx.style;

import android.content.Context;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.util.views.ProgressView;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 13:09
 */
public class MaterialStyle implements DialogXStyle {
    
    public static MaterialStyle style() {
        return new MaterialStyle();
    }
    
    @Override
    public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark;
    }
    
    @Override
    public int enterAnimResId() {
        return R.anim.anim_dialogx_default_enter;
    }
    
    @Override
    public int exitAnimResId() {
        return R.anim.anim_dialogx_default_exit;
    }
    
    @Override
    public int[] verticalButtonOrder() {
        return new int[]{BUTTON_OK, BUTTON_OTHER, BUTTON_CANCEL};
    }
    
    @Override
    public int[] horizontalButtonOrder() {
        return new int[]{BUTTON_OTHER, SPACE, BUTTON_CANCEL, BUTTON_OK};
    }
    
    @Override
    public int splitWidthPx() {
        return 1;
    }
    
    @Override
    public int splitColorRes(boolean light) {
        return 0;
    }
    
    @Override
    public BlurBackgroundSetting messageDialogBlurSettings() {
        return null;
    }
    
    @Override
    public HorizontalButtonRes overrideHorizontalButtonRes() {
        return new HorizontalButtonRes() {
            @Override
            public int overrideHorizontalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_material_light : R.drawable.button_dialogx_material_night;
            }
            
            @Override
            public int overrideHorizontalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_material_light : R.drawable.button_dialogx_material_night;
            }
            
            @Override
            public int overrideHorizontalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_material_light : R.drawable.button_dialogx_material_night;
            }
        };
    }
    
    @Override
    public VerticalButtonRes overrideVerticalButtonRes() {
        return new VerticalButtonRes() {
            @Override
            public int overrideVerticalOkButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_material_light : R.drawable.button_dialogx_material_night;
            }
            
            @Override
            public int overrideVerticalCancelButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_material_light : R.drawable.button_dialogx_material_night;
            }
            
            @Override
            public int overrideVerticalOtherButtonBackgroundRes(int visibleButtonCount, boolean light) {
                return light ? R.drawable.button_dialogx_material_light : R.drawable.button_dialogx_material_night;
            }
        };
    }
    
    @Override
    public WaitTipRes overrideWaitTipRes() {
        return new WaitTipRes() {
            @Override
            public int overrideWaitLayout(boolean light) {
                return R.layout.layout_dialogx_wait;
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
                return light ? R.color.white : R.color.black;
            }
            
            @Override
            public ProgressViewInterface overrideWaitView(Context context, boolean light) {
                return new ProgressView(context);
            }
        };
    }
    
    @Override
    public BottomDialogRes overrideBottomDialogRes() {
        return new BottomDialogRes() {
            
            @Override
            public boolean touchSlide() {
                return true;
            }
            
            @Override
            public int overrideDialogLayout(boolean light) {
                return light ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            }
            
            @Override
            public int overrideMenuDividerDrawableRes(boolean light) {
                return light ? R.drawable.rect_dialogx_material_menu_split_divider : R.drawable.rect_dialogx_material_menu_split_divider_night;
            }
            
            @Override
            public int overrideMenuDividerHeight(boolean light) {
                return 1;
            }
            
            @Override
            public int overrideMenuTextColor(boolean light) {
                return light ? R.color.black90 : R.color.white90;
            }
            
            @Override
            public float overrideBottomDialogMaxHeight() {
                return 0.6f;
            }
            
            @Override
            public int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility) {
                return 0;
            }
            
            @Override
            public int overrideSelectionMenuBackgroundColor(boolean light) {
                return 0;
            }
            
            @Override
            public boolean selectionImageTint(boolean light) {
                return false;
            }
            
            @Override
            public int overrideSelectionImage(boolean light, boolean isSelected) {
                return isSelected ? R.mipmap.img_dialogx_bottom_menu_material_item_selection : R.mipmap.img_dialogx_bottom_menu_material_item_non_select;
            }
    
            @Override
            public int overrideMultiSelectionImage(boolean light, boolean isSelected) {
                return isSelected ? R.mipmap.img_dialogx_bottom_menu_material_item_multi_selection : R.mipmap.img_dialogx_bottom_menu_material_item_non_multi_select;
            }
    
        };
    }
    
    @Override
    public PopTipSettings popTipSettings() {
        return new PopTipSettings() {
            @Override
            public int layout(boolean light) {
                return light ? R.layout.layout_dialogx_poptip_material : R.layout.layout_dialogx_poptip_material_dark;
            }
            
            @Override
            public ALIGN align() {
                return ALIGN.BOTTOM;
            }
            
            @Override
            public int enterAnimResId(boolean light) {
                return R.anim.anim_dialogx_default_enter;
            }
            
            @Override
            public int exitAnimResId(boolean light) {
                return R.anim.anim_dialogx_default_exit;
            }
        };
    }
    
}
