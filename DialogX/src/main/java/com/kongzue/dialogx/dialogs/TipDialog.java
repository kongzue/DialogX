package com.kongzue.dialogx.dialogs;

import android.app.Activity;

import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;

import java.lang.ref.WeakReference;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/28 23:53
 */
public class TipDialog extends WaitDialog {
    
    protected TipDialog() {
        super();
    }
    
    public static WaitDialog show(int messageResId) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        if (dialogImpl != null) {
            dialogImpl.showTip(TYPE.WARNING);
        } else {
            me().showTip(messageResId, TYPE.WARNING);
        }
        return me();
    }
    
    public static WaitDialog show(Activity activity, int messageResId) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.showTip(TYPE.WARNING);
        } else {
            me().showTip(activity, messageResId, TYPE.WARNING);
        }
        return me();
    }
    
    public static WaitDialog show(CharSequence message) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(message);
        if (dialogImpl != null) {
            dialogImpl.showTip(TYPE.WARNING);
        } else {
            me().showTip(message, TYPE.WARNING);
        }
        return me();
    }
    
    public static WaitDialog show(Activity activity, CharSequence message) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(message);
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.showTip(TYPE.WARNING);
        } else {
            me().showTip(activity, message, TYPE.WARNING);
        }
        return me();
    }
    
    public static WaitDialog show(int messageResId, TYPE tip) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        if (dialogImpl != null) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(messageResId, tip);
        }
        return me();
    }
    
    public static WaitDialog show(Activity activity, int messageResId, TYPE tip) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(activity, messageResId, tip);
        }
        return me();
    }
    
    public static WaitDialog show(CharSequence message, TYPE tip) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(message);
        if (dialogImpl != null) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(message, tip);
        }
        return me();
    }
    
    public static WaitDialog show(Activity activity, CharSequence message, TYPE tip) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(message);
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(activity, message, tip);
        }
        return me();
    }
    
    public static WaitDialog show(int messageResId, TYPE tip, long duration) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        me().tipShowDuration = duration;
        if (dialogImpl != null) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(messageResId, tip);
        }
        return me();
    }
    
    public static WaitDialog show(Activity activity, int messageResId, TYPE tip, long duration) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        me().tipShowDuration = duration;
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(activity, messageResId, tip);
        }
        return me();
    }
    
    public static WaitDialog show(CharSequence message, TYPE tip, long duration) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(message);
        me().tipShowDuration = duration;
        if (dialogImpl != null) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(message, tip);
        }
        return me();
    }
    
    public static WaitDialog show(Activity activity, CharSequence message, TYPE tip, long duration) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(message);
        me().tipShowDuration = duration;
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.showTip(tip);
        } else {
            me().showTip(activity, message, tip);
        }
        return me();
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
}
