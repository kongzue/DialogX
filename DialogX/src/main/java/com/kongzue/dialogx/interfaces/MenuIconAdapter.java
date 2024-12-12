package com.kongzue.dialogx.interfaces;

import android.widget.ImageView;

public abstract class MenuIconAdapter<D extends BaseDialog> extends OnIconChangeCallBack<D> {

    public MenuIconAdapter() {
    }

    public MenuIconAdapter(boolean autoTintIconInLightOrDarkMode) {
        super(autoTintIconInLightOrDarkMode);
    }

    public abstract boolean applyIcon(D dialog, int index, String menuText, ImageView iconImageView);

    @Override
    public int getIcon(D dialog, int index, String menuText) {
        return 0;
    }
}
