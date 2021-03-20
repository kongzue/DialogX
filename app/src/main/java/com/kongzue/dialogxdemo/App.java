package com.kongzue.dialogxdemo;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.style.MaterialStyle;

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
        DialogX.globalStyle = CustomStyle.style();
        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.onlyOnePopTip = false;
    }
}
