package com.kongzue.dialogx.interfaces;

import android.view.View;

public interface OnBottomMenuButtonClickListener <D extends BaseDialog> extends BaseOnDialogClickCallback{

    boolean onClick(D dialog, View v);
}
