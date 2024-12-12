package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/9 14:54
 */
public abstract class OnIconChangeCallBack<D extends BaseDialog> {

    public OnIconChangeCallBack() {
    }

    private Boolean autoTintIconInLightOrDarkMode;

    public OnIconChangeCallBack(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
    }

    public abstract int getIcon(D dialog, int index, String menuText);

    public Boolean isAutoTintIconInLightOrDarkMode() {
        return autoTintIconInLightOrDarkMode;
    }
}
