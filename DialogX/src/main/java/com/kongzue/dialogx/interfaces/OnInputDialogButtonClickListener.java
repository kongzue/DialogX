package com.kongzue.dialogx.interfaces;

import android.view.View;

import com.kongzue.dialogx.dialogs.MessageDialog;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/8 21:09
 */
public interface OnInputDialogButtonClickListener<D extends BaseDialog> extends BaseOnDialogClickCallback{
    
    boolean onClick(D baseDialog, View v, String inputStr);
}
