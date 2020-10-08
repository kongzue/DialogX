package com.kongzue.dialogx.dialogs;

import java.lang.ref.WeakReference;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/28 23:53
 */
public class TipDialog extends WaitDialog {
    
    public static TipDialog show(CharSequence message) {
        TipDialog tipDialog = new TipDialog();
        tipDialog.message = message;
        tipDialog.show();
        return tipDialog;
    }
    
    protected TipDialog() {
        super();
    }
    
    public static WaitDialog show(CharSequence message, TYPE tip) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        if (dialogImpl != null) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(message, tip);
        }
        return me();
    }
    
    public static WaitDialog show(CharSequence message, TYPE tip, long duration) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        me().tipShowDuration = duration;
        if (dialogImpl != null) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(message, tip);
        }
        return me();
    }
}
