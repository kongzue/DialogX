package com.kongzue.dialogx.dialogs;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;

import java.util.HashMap;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/28 23:53
 */
public class TipDialog extends WaitDialog {

    /**
     * 参数 duration 使用此值，或小于 0 的任意整数时，
     * TipDialog 将不自动关闭
     */
    public static final int NO_AUTO_DISMISS = -1;

    protected TipDialog() {
        super();
    }

    public static WaitDialog showTipWithDefaultText(TYPE tip) {
        return WaitDialog.showTipWithDefaultText(tip);
    }

    public static WaitDialog show(int messageResId) {
        return show((Activity) null, messageResId);
    }

    public static WaitDialog show(Activity activity, int messageResId) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(messageResId, TYPE.WARNING);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(CharSequence message) {
        return show(null, message);
    }

    public static WaitDialog show(Activity activity, CharSequence message) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(message, TYPE.WARNING);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(int messageResId, TYPE tip) {
        return show(null, messageResId, tip);
    }

    public static WaitDialog show(Activity activity, int messageResId, TYPE tip) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(messageResId, tip);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(CharSequence message, TYPE tip) {
        return show(null, message, tip);
    }

    public static WaitDialog show(Activity activity, CharSequence message, TYPE tip) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(message, tip);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(int messageResId, TYPE tip, long duration) {
        return show(null, messageResId, tip, duration);
    }

    public static WaitDialog show(Activity activity, int messageResId, TYPE tip, long duration) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(messageResId, tip);
            dialog.setTipShowDuration(duration);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(CharSequence message, TYPE tip, long duration) {
        return show(null, message, tip, duration);
    }

    public static WaitDialog show(Activity activity, CharSequence message, TYPE tip, long duration) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(message, tip);
            dialog.setTipShowDuration(duration);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    public TipDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public TipDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public TipDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public TipDialog setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public TipDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public TipDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<WaitDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }

    public TipDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<WaitDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public TipDialog setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public TipDialog setDialogXAnimImpl(DialogXAnimInterface<WaitDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public TipDialog setOnBackPressedListener(OnBackPressedListener<WaitDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }

    public TipDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public TipDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public TipDialog setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public TipDialog onShow(DialogXRunnable<WaitDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public TipDialog onDismiss(DialogXRunnable<WaitDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public TipDialog setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public TipDialog appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public TipDialog setThisOrderIndex(int orderIndex) {
        this.thisOrderIndex = orderIndex;
        if (getDialogView() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(orderIndex);
            } else {
                error("DialogX: " + dialogKey() + " 执行 .setThisOrderIndex(" + orderIndex + ") 失败：系统不支持此方法，SDK-API 版本必须大于 21（LOLLIPOP）");
            }
        }
        return this;
    }

    public TipDialog bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public TipDialog setActionRunnable(int actionId, DialogXRunnable<WaitDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public TipDialog cleanAction(int actionId){
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public TipDialog cleanAllAction(){
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss(){
        dismiss();
    }

    public TipDialog bindDismissWithLifecycleOwner(LifecycleOwner owner){
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }

    public TipDialog setCustomDialogLayoutResId(int customDialogLayoutId) {
        this.customDialogLayoutResId[0] = customDialogLayoutId;
        this.customDialogLayoutResId[1] = customDialogLayoutId;
        return this;
    }

    public TipDialog setCustomDialogLayoutResId(int customDialogLayoutId, boolean isLightTheme) {
        this.customDialogLayoutResId[isLightTheme ? 0 : 1] = customDialogLayoutId;
        return this;
    }
}
