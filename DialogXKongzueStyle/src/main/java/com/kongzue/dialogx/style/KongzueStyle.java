package com.kongzue.dialogx.style;

import android.content.Context;

import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.kongzuetheme.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/26 13:09
 */
public class KongzueStyle extends DialogXStyle {

    public static KongzueStyle style() {
        return new KongzueStyle();
    }

    @Override
    public int layout(boolean light) {
        return light ? R.layout.layout_dialogx_kongzue : R.layout.layout_dialogx_kongzue_dark;
    }

    @Override
    public int enterAnimResId() {
        return 0;
    }

    @Override
    public int exitAnimResId() {
        return 0;
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

    public class DefaultWaitTipRes extends WaitTipRes {
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

    public class DefaultBottomDialogRes extends BottomDialogRes {
        @Override
        public boolean touchSlide() {
            return false;
        }

        @Override
        public int overrideDialogLayout(boolean light) {
            return light ? R.layout.layout_dialogx_bottom_kongzue : R.layout.layout_dialogx_bottom_kongzue_dark;
        }

        @Override
        public int overrideMenuDividerDrawableRes(boolean light) {
            return light ? R.color.dialogxKongzueButtonSplitLineColor : R.color.dialogxKongzueDarkButtonSplitLineColor;
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
            return light ? R.layout.item_dialogx_kongzue_bottom_menu_normal_text : R.layout.item_dialogx_kongzue_bottom_menu_normal_text_dark;
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

        @Override
        public int overrideMultiSelectionImage(boolean b, boolean b1) {
            return 0;
        }
    }

    @Override
    public PopTipSettings popTipSettings() {
        return new DefaultPopTipSettings();
    }

    public class DefaultPopTipSettings extends PopTipSettings {
        @Override
        public int layout(boolean light) {
            return light ? R.layout.layout_dialogx_poptip_kongzue : R.layout.layout_dialogx_poptip_kongzue_dark;
        }

        @Override
        public ALIGN align() {
            return ALIGN.BOTTOM;
        }

        @Override
        public int enterAnimResId(boolean b) {
            return 0;
        }

        @Override
        public int exitAnimResId(boolean b) {
            return 0;
        }

        @Override
        public boolean tintIcon() {
            return true;
        }
    }

    @Override
    public PopMenuSettings popMenuSettings() {
        return new DefaultPopMenuSettings();
    }

    public class DefaultPopMenuSettings extends PopMenuSettings {
        @Override
        public int layout(boolean light) {
            return light ? R.layout.layout_dialogx_popmenu_kongzue : R.layout.layout_dialogx_popmenu_kongzue_dark;
        }

        @Override
        public BlurBackgroundSetting blurBackgroundSettings() {
            return null;
        }

        @Override
        public int backgroundMaskColorRes() {
            return 0;
        }

        @Override
        public int overrideMenuDividerDrawableRes(boolean light) {
            return light ? R.color.dialogxKongzueButtonSplitLineColor : R.color.dialogxKongzueDarkButtonSplitLineColor;
        }

        @Override
        public int overrideMenuDividerHeight(boolean light) {
            return 1;
        }

        @Override
        public int overrideMenuTextColor(boolean light) {
            return 0;
        }

        @Override
        public int overrideMenuItemLayoutRes(boolean light) {
            return light ? R.layout.item_dialogx_kongzue_popmenu_light : R.layout.item_dialogx_kongzue_popmenu_dark;
        }

        @Override
        public int overrideMenuItemBackgroundRes(boolean light, int index, int count, boolean isContentVisibility) {
            return light ? R.drawable.button_dialogx_kongzue_menu_light : R.drawable.button_dialogx_kongzue_menu_night;
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
        public int paddingVertical() {
            return 0;
        }
    }

    @Override
    public PopNotificationSettings popNotificationSettings() {
        return new DefaultPopNotificationSettings();
    }

    public class DefaultPopNotificationSettings extends PopNotificationSettings {
        @Override
        public int layout(boolean light) {
            return light ? R.layout.layout_dialogx_popnotification_kongzue : R.layout.layout_dialogx_popnotification_kongzue_dark;
        }

        @Override
        public PopNotificationSettings.ALIGN align() {
            return ALIGN.TOP_INSIDE;
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
}
