package com.kongzue.dialogx.interfaces;

import android.view.View;

public interface OnMenuButtonClickListener<D extends BaseDialog> extends BaseOnDialogClickCallback{

    boolean onClick(D dialog, View v);
}
