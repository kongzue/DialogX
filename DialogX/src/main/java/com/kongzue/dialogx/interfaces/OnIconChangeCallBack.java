package com.kongzue.dialogx.interfaces;

import com.kongzue.dialogx.dialogs.BottomMenu;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/9 14:54
 */
public abstract class OnIconChangeCallBack {
    
    private boolean autoTintIconInLightOrDarkMode;
    
    public OnIconChangeCallBack() {
    }
    
    public OnIconChangeCallBack(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
    }
    
    public abstract int getIcon(BottomMenu bottomMenu, int index, String menuText);
    
    public boolean isAutoTintIconInLightOrDarkMode() {
        return autoTintIconInLightOrDarkMode;
    }
}
