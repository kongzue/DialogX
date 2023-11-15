package com.kongzue.dialogx.style;

import android.content.Context;
import android.content.res.Resources;

import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.miuistyle.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 17:04
 */
public class MIUIStyle extends DialogXStyle {
    
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
    public BlurBackgroundSetting messageDialogBlurSettings() {
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
        return new DefaultWaitTipRes();
    }

    public class DefaultWaitTipRes extends WaitTipRes{
        @Override
        public int overrideWaitLayout(boolean light) {
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
            return light ? R.color.white : R.color.black;
        }

        @Override
        public ProgressViewInterface overrideWaitView(Context context, boolean light) {
            return null;
        }
    }

    @Override
    public BottomDialogRes overrideBottomDialogRes() {
        return new DefaultBottomDialogRes();
    }

    public class DefaultBottomDialogRes extends BottomDialogRes{
        @Override
        public boolean touchSlide() {
            return false;
        }

        @Override
        public int overrideDialogLayout(boolean light) {
            return light ? R.layout.layout_dialogx_bottom_miui : R.layout.layout_dialogx_bottom_miui_dark;
        }

        @Override
        public int overrideMenuDividerDrawableRes(boolean light) {
            return 0;
        }

        @Override
        public int overrideMenuDividerHeight(boolean light) {
            return 0;
        }

        @Override
        public int overrideMenuTextColor(boolean light) {
            return light ? R.color.black : R.color.dialogxMIUITextDark;
        }

        @Override
        public float overrideBottomDialogMaxHeight() {
            return 0.6f;
        }

        @Override
        public int overrideMenuItemLayout(boolean light, int index, int count, boolean isContentVisibility) {
            if (light) {
                if (index == 0) {
                    return R.layout.item_dialogx_miui_bottom_menu_top_light;
                } else if (index == count - 1) {
                    return R.layout.item_dialogx_miui_bottom_menu_bottom_light;
                } else {
                    return R.layout.item_dialogx_miui_bottom_menu_center_light;
                }
            } else {
                if (index == 0) {
                    return R.layout.item_dialogx_miui_bottom_menu_top_dark;
                } else if (index == count - 1) {
                    return R.layout.item_dialogx_miui_bottom_menu_bottom_dark;
                } else {
                    return R.layout.item_dialogx_miui_bottom_menu_center_dark;
                }
            }
        }

        @Override
        public int overrideSelectionMenuBackgroundColor(boolean light) {
            return light ? R.color.dialogxMIUIItemSelectionBkg : R.color.dialogxMIUIItemSelectionBkgDark;
        }

        @Override
        public boolean selectionImageTint(boolean light) {
            return false;
        }

        @Override
        public int overrideSelectionImage(boolean light, boolean isSelected) {
            return 0;
        }

        @Override
        public int overrideMultiSelectionImage(boolean b, boolean b1) {
            return 0;
        }
    }
    
    @Override
    public PopTipSettings popTipSettings() {
        return null;
    }
    
    @Override
    public PopNotificationSettings popNotificationSettings() {
        return new DefaultPopNotificationSettings();
    }

    public class DefaultPopNotificationSettings extends PopNotificationSettings{
        @Override
        public int layout(boolean light) {
            return light ? R.layout.layout_dialogx_popnotification_miui : R.layout.layout_dialogx_popnotification_miui_dark;
        }

        @Override
        public PopNotificationSettings.ALIGN align() {
            return ALIGN.TOP;
        }

        @Override
        public int enterAnimResId(boolean light) {
            return com.kongzue.dialogx.R.anim.anim_dialogx_notification_enter;
        }

        @Override
        public int exitAnimResId(boolean light) {
            return com.kongzue.dialogx.R.anim.anim_dialogx_notification_exit;
        }

        @Override
        public boolean tintIcon() {
            return false;
        }
    }
    
    @Override
    public PopMenuSettings popMenuSettings() {
        return new DefaultPopMenuSettings();
    }

    public class DefaultPopMenuSettings extends PopMenuSettings{
        @Override
        public int layout(boolean light) {
            return light ? R.layout.layout_dialogx_popmenu_miui : R.layout.layout_dialogx_popmenu_miui_dark;
        }

        @Override
        public BlurBackgroundSetting blurBackgroundSettings() {
            return null;
        }

        @Override
        public int backgroundMaskColorRes() {
            return R.color.black20;
        }

        @Override
        public int overrideMenuDividerDrawableRes(boolean b) {
            return 0;
        }

        @Override
        public int overrideMenuDividerHeight(boolean b) {
            return 0;
        }

        @Override
        public int overrideMenuTextColor(boolean b) {
            return 0;
        }

        @Override
        public int overrideMenuItemLayoutRes(boolean b) {
            return R.layout.item_dialogx_miui_popmenu;
        }

        @Override
        public int overrideMenuItemBackgroundRes(boolean light, int index, int count, boolean isContentVisibility) {
            if (light) {
                if (index == 0) {
                    return R.drawable.button_dialogx_miui_top_light;
                } else if (index == count - 1) {
                    return R.drawable.button_dialogx_miui_bottom_light;
                } else {
                    return R.drawable.button_dialogx_miui_center_light;
                }
            } else {
                if (index == 0) {
                    return R.drawable.button_dialogx_miui_top_night;
                } else if (index == count - 1) {
                    return R.drawable.button_dialogx_miui_bottom_night;
                } else {
                    return R.drawable.button_dialogx_miui_center_night;
                }
            }
        }

        @Override
        public int overrideSelectionMenuBackgroundColor(boolean b) {
            return 0;
        }

        @Override
        public boolean selectionImageTint(boolean b) {
            return false;
        }

        @Override
        public int paddingVertical() {
            return dip2px(10);
        }
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
