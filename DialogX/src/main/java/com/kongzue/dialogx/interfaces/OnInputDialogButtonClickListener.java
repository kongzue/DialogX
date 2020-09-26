package com.kongzue.dialogx.interfaces;

import android.view.View;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/8 21:09
 */
public interface OnInputDialogButtonClickListener extends OnDialogButtonClickListener{
    
    boolean onClick(BaseDialog baseDialog, View v, String inputStr);
}
