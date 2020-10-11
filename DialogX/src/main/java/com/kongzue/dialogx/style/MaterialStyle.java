package com.kongzue.dialogx.style;

import android.content.res.Resources;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.DialogXStyle;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 13:09
 */
public class MaterialStyle implements DialogXStyle {
    
    private MaterialStyle() {
    }
    
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
                return light?R.color.black90:R.color.white90;
            }

            @Override
            public float overrideBottomDialogMaxHeight() {
                return 0.6f;
            }
    
            @Override
            public int overrideMenuCancelButtonBackgroundRes(boolean b) {
                return 0;
            }
    
            @Override
            public int overrideMenuItemLayout(boolean b, int i, int i1) {
                return R.layout.item_dialogx_material_bottom_menu_normal_text;
            }
    
        };
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
