package com.kongzue.dialogx.util;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.interfaces.BaseDialog;

import java.lang.ref.WeakReference;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/5/17 15:16
 */
public class DialogXImplModeAgent {
    
    private DialogX.IMPL_MODE implMode;
    private WeakReference<BaseDialog> dialogWeakReference;
    
    public DialogXImplModeAgent(DialogX.IMPL_MODE implMode, BaseDialog dialog) {
        this.implMode = implMode;
        this.dialogWeakReference = new WeakReference<>(dialog);
    }
    
    public DialogX.IMPL_MODE getImplMode() {
        return implMode;
    }
    
    public DialogXImplModeAgent setImplMode(DialogX.IMPL_MODE implMode) {
        this.implMode = implMode;
        return this;
    }
    
    public BaseDialog getDialog() {
        if (dialogWeakReference == null) {
            return null;
        }
        return dialogWeakReference.get();
    }
    
    public DialogXImplModeAgent setDialogWeakReference(BaseDialog dialog) {
        this.dialogWeakReference = new WeakReference<>(dialog);
        return this;
    }
    
    public void recycle() {
        if (dialogWeakReference != null) dialogWeakReference.clear();
        dialogWeakReference = null;
        implMode = null;
    }
}
