package com.kongzue.dialogx.util;

import com.kongzue.dialogx.interfaces.BaseDialog;

import java.util.ArrayList;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/8/8 15:08
 */
public class DialogListBuilder {

    ArrayList<BaseDialog> dialogs;

    public static DialogListBuilder create(BaseDialog... dialogs) {
        DialogListBuilder builder = new DialogListBuilder();
        for (BaseDialog d : dialogs) {
            if (d != null) builder.add(d);
        }
        return builder;
    }

    public DialogListBuilder add(BaseDialog dialog) {
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }
        if (dialog == null || dialog.isShow() || dialog.isPreShow()) {
            return this;
        }
        dialog.setDialogListBuilder(this);
        dialogs.add(dialog);
        return this;
    }

    public DialogListBuilder show() {
        if (dialogs == null || dialogs.isEmpty()) {
            return this;
        }
        while (!dialogs.isEmpty() && dialogs.get(0) == null) {
            dialogs.remove(0);
        }
        dialogs.get(0).show();
        return this;
    }

    public void showNext() {
        if (dialogs == null || dialogs.isEmpty()) {
            return;
        }
        while (!dialogs.isEmpty() && dialogs.get(0) == null) {
            dialogs.remove(0);
        }
        if (!dialogs.isEmpty() && dialogs.get(0) != null) {
            dialogs.get(0).show();
        }
    }

    public boolean isEmpty() {
        if (dialogs == null) {
            return true;
        }
        while (!dialogs.isEmpty() && dialogs.get(0) == null) {
            dialogs.remove(0);
        }
        return dialogs.isEmpty();
    }

    public void clear() {
        if (dialogs != null) {
            dialogs.clear();
        }
    }
}
