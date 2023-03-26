package com.kongzue.dialogxdemo;

import android.content.Intent;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.IOSStyle;
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
        BaseFrameworkSettings.DEBUGMODE = BuildConfig.DEBUG;

        DialogX.init(this);
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW;
        DialogX.useHaptic = true;

        //演示仅覆写一个设置，不覆写其他设置：
        DialogX.globalStyle = new MaterialStyle(){
            @Override
            public PopTipSettings popTipSettings() {
                //DefaultPopTipSettings 是主题中默认的 PopTip 设置，以下演示仅覆写其中的 align 设置
                return new DefaultPopTipSettings(){
                    @Override
                    public ALIGN align() {
                        return ALIGN.BOTTOM;
                    }
                };
            }
        };

        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.onlyOnePopTip = false;
        DialogX.DEBUGMODE = BuildConfig.DEBUG;
    
        //以下代码用于测试后台 Service 启动对话框
//        DialogX.implIMPLMode = DialogX.IMPL_MODE.FLOATING_ACTIVITY;
//        Intent serviceStartIntent = new Intent(this, TestBackgroundService.class);
//        startService(serviceStartIntent);
    }
}
