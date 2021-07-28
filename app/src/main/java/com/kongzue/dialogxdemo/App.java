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
        DialogX.implIMPLMode= DialogX.IMPL_MODE.DIALOG_FRAGMENT;
        DialogX.useHaptic = true;
        DialogX.globalStyle = new MaterialStyle() {
            @Override
            public PopTipSettings popTipSettings() {
                return new PopTipSettings() {
                    @Override
                    public int layout(boolean light) {
                        return light ? R.layout.layout_dialogx_poptip_snackbar : R.layout.layout_dialogx_poptip_snackbar_dark;
                    }

                    @Override
                    public ALIGN align() {
                        return ALIGN.BOTTOM;
                    }

                    @Override
                    public int enterAnimResId(boolean light) {
                        return com.kongzue.dialogx.R.anim.anim_dialogx_default_enter;
                    }

                    @Override
                    public int exitAnimResId(boolean light) {
                        return com.kongzue.dialogx.R.anim.anim_dialogx_default_exit;
                    }
                };
            }
        };
    
        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.onlyOnePopTip = false;
    }
}
