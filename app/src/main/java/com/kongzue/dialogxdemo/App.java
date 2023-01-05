package com.kongzue.dialogxdemo;

import android.content.Intent;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogxdemo.service.TestBackgroundService;

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
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW;
        DialogX.useHaptic = true;
        DialogX.globalStyle = new MaterialStyle();
        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.onlyOnePopTip = false;
    
        //以下代码用于测试后台 Service 启动对话框
//        DialogX.implIMPLMode = DialogX.IMPL_MODE.FLOATING_ACTIVITY;
//        Intent serviceStartIntent = new Intent(this, TestBackgroundService.class);
//        startService(serviceStartIntent);
    }
}
