package com.kongzue.dialogxdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.impl.ActivityLifecycleImpl;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.ProgressView;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/21 18:01
 */
public class App extends BaseApp<App> {
    @Override
    public void init() {
        DialogX.init(this);
        DialogX.implIMPLMode= DialogX.IMPL_MODE.VIEW;
        DialogX.useHaptic = true;
        DialogX.globalStyle = new MaterialStyle();
        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.onlyOnePopTip = false;
    }
}
