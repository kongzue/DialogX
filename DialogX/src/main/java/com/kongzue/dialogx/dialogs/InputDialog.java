package com.kongzue.dialogx.dialogs;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.dialogx.util.TextInfo;

import java.util.HashMap;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/24 13:53
 */
public class InputDialog extends MessageDialog {

    protected InputDialog() {
        super();
    }

    public static InputDialog build() {
        return new InputDialog();
    }

    public static InputDialog build(DialogXStyle style) {
        InputDialog dialog = new InputDialog();
        dialog.setStyle(style);
        return dialog;
    }

    public static InputDialog build(OnBindView<MessageDialog> onBindView) {
        return new InputDialog().setCustomView(onBindView);
    }

    public InputDialog(CharSequence title, CharSequence message, CharSequence okText) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
        this.okText = okText;
    }

    public InputDialog(CharSequence title, CharSequence message) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
    }

    public InputDialog(int titleResId, int messageResId, int okTextResId) {
        cancelable = DialogX.cancelable;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
    }

    public InputDialog(int titleResId, int messageResId) {
        cancelable = DialogX.cancelable;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }

    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText) {
        InputDialog inputDialog = new InputDialog(title, message, okText);
        inputDialog.show();
        return inputDialog;
    }

    public static InputDialog show(int titleResId, int messageResId, int okTextResId) {
        InputDialog inputDialog = new InputDialog(titleResId, messageResId, okTextResId);
        inputDialog.show();
        return inputDialog;
    }

    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
    }

    public InputDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        cancelable = DialogX.cancelable;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
    }

    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText);
        inputDialog.show();
        return inputDialog;
    }

    public static InputDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        InputDialog inputDialog = new InputDialog(titleResId, messageResId, okTextResId, cancelTextResId);
        inputDialog.show();
        return inputDialog;
    }

    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, String inputText) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.inputText = inputText;
    }

    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, String inputText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText, inputText);
        inputDialog.show();
        return inputDialog;
    }

    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
    }

    public InputDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId) {
        cancelable = DialogX.cancelable;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
        this.otherText = getString(otherTextResId);
    }

    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText, otherText);
        inputDialog.show();
        return inputDialog;
    }

    public static InputDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId) {
        InputDialog inputDialog = new InputDialog(titleResId, messageResId, okTextResId, cancelTextResId, otherTextResId);
        inputDialog.show();
        return inputDialog;
    }

    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText, String inputText) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
        this.inputText = inputText;
    }

    public InputDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId, int inputTextResId) {
        cancelable = DialogX.cancelable;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
        this.otherText = getString(otherTextResId);
        this.inputText = getString(inputTextResId);
    }

    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText, String inputText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText, otherText, inputText);
        inputDialog.show();
        return inputDialog;
    }

    public static InputDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId, int inputTextResId) {
        InputDialog inputDialog = new InputDialog(titleResId, messageResId, okTextResId, cancelTextResId, otherTextResId, inputTextResId);
        inputDialog.show();
        return inputDialog;
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    public CharSequence getOkButton() {
        return okText;
    }

    public InputDialog setOkButton(CharSequence okText) {
        this.okText = okText;
        refreshUI();
        return this;
    }

    public InputDialog setOkButton(int okTextResId) {
        this.okText = getString(okTextResId);
        refreshUI();
        return this;
    }

    public InputDialog setOkButton(OnInputDialogButtonClickListener<InputDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public InputDialog setOkButton(CharSequence okText, OnInputDialogButtonClickListener<InputDialog> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }

    public InputDialog setOkButton(int okTextResId, OnInputDialogButtonClickListener<InputDialog> okButtonClickListener) {
        this.okText = getString(okTextResId);
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelText;
    }

    public InputDialog setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }

    public InputDialog setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        refreshUI();
        return this;
    }

    public InputDialog setCancelButton(OnInputDialogButtonClickListener<InputDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public InputDialog setCancelButton(CharSequence cancelText, OnInputDialogButtonClickListener<InputDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }

    public InputDialog setCancelButton(int cancelTextResId, OnInputDialogButtonClickListener<InputDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }

    public CharSequence getOtherButton() {
        return otherText;
    }

    public InputDialog setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        refreshUI();
        return this;
    }

    public InputDialog setOtherButton(int otherTextResId) {
        this.otherText = getString(otherTextResId);
        refreshUI();
        return this;
    }

    public InputDialog setOtherButton(OnInputDialogButtonClickListener<InputDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public InputDialog setOtherButton(CharSequence otherText, OnInputDialogButtonClickListener<InputDialog> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }

    public InputDialog setOtherButton(int otherTextResId, OnInputDialogButtonClickListener<InputDialog> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }

    public OnInputDialogButtonClickListener<InputDialog> getInputOkButtonClickListener() {
        return (OnInputDialogButtonClickListener<InputDialog>) okButtonClickListener;
    }

    public InputDialog setOkButtonClickListener(OnInputDialogButtonClickListener<InputDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public OnInputDialogButtonClickListener getInputCancelButtonClickListener() {
        return (OnInputDialogButtonClickListener<InputDialog>) cancelButtonClickListener;
    }

    public InputDialog setCancelButtonClickListener(OnInputDialogButtonClickListener<InputDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public OnInputDialogButtonClickListener getInputOtherButtonClickListener() {
        return (OnInputDialogButtonClickListener<InputDialog>) otherButtonClickListener;
    }

    public InputDialog setOtherButtonClickListener(OnInputDialogButtonClickListener<InputDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public InputDialog setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }

    public InputDialog setTitle(int titleResId) {
        this.title = getString(titleResId);
        refreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public InputDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }

    public InputDialog setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }

    public String getInputText() {
        if (getDialogImpl() != null && getDialogImpl().txtInput != null) {
            return getDialogImpl().txtInput.getText().toString();
        }
        return inputText;
    }

    public InputDialog setInputText(String inputText) {
        this.inputText = inputText;
        refreshUI();
        return this;
    }

    public InputDialog setInputText(int inputTextResId) {
        this.inputText = getString(inputTextResId);
        refreshUI();
        return this;
    }

    public String getInputHintText() {
        return inputHintText;
    }

    public InputDialog setInputHintText(String inputHintText) {
        this.inputHintText = inputHintText;
        refreshUI();
        return this;
    }

    public InputDialog setInputHintText(int inputHintTextResId) {
        this.inputHintText = getString(inputHintTextResId);
        refreshUI();
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public InputDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public InputDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getOkTextInfo() {
        return okTextInfo;
    }

    public InputDialog setOkTextInfo(TextInfo okTextInfo) {
        this.okTextInfo = okTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }

    public InputDialog setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getOtherTextInfo() {
        return otherTextInfo;
    }

    public InputDialog setOtherTextInfo(TextInfo otherTextInfo) {
        this.otherTextInfo = otherTextInfo;
        refreshUI();
        return this;
    }

    public InputInfo getInputInfo() {
        return inputInfo;
    }

    public InputDialog setInputInfo(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
        refreshUI();
        return this;
    }

    public int getButtonOrientation() {
        return buttonOrientation;
    }

    public InputDialog setButtonOrientation(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshUI();
        return this;
    }

    public boolean isCancelable() {
        if (privateCancelable != null) {
            return privateCancelable == BOOLEAN.TRUE;
        }
        if (overrideCancelable != null) {
            return overrideCancelable == BOOLEAN.TRUE;
        }
        return cancelable;
    }

    public InputDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }

    public OnBackPressedListener<MessageDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<MessageDialog>) onBackPressedListener;
    }

    public InputDialog setOnBackPressedListener(OnBackPressedListener<MessageDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        return this;
    }

    public boolean isAutoShowInputKeyboard() {
        return autoShowInputKeyboard;
    }

    public InputDialog setAutoShowInputKeyboard(boolean autoShowInputKeyboard) {
        this.autoShowInputKeyboard = autoShowInputKeyboard;
        return this;
    }

    public InputDialog setCustomView(OnBindView<MessageDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public InputDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public InputDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }

    public InputDialog setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }

    public InputDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public InputDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public InputDialog setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }

    @Override
    public void restartDialog() {
        if (getDialogView() != null) {
            dismiss(getDialogView());
            isShow = false;
        }
        if (getDialogImpl().boxCustom != null) {
            getDialogImpl().boxCustom.removeAllViews();
        }
        int layoutId = getCustomDialogLayoutResId(isLightTheme()) != 0 ? getCustomDialogLayoutResId(isLightTheme()) : style.layout(isLightTheme());
        layoutId = layoutId == 0 ? (isLightTheme() ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark) : layoutId;

        String inputText = getInputText();
        enterAnimDuration = 0;
        View dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
        setInputText(inputText);
    }

    public InputDialog setAnimResId(int enterResId, int exitResId) {
        customEnterAnimResId = enterResId;
        customExitAnimResId = exitResId;
        return this;
    }

    public InputDialog setEnterAnimResId(int enterResId) {
        customEnterAnimResId = enterResId;
        return this;
    }

    public InputDialog setExitAnimResId(int exitResId) {
        customExitAnimResId = exitResId;
        return this;
    }

    public InputDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public InputDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public InputDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public InputDialog setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public InputDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public InputDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<MessageDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }

    public InputDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<MessageDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public InputDialog setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public InputDialog setTitleIcon(Bitmap titleIcon) {
        this.titleIcon = new BitmapDrawable(getResources(), titleIcon);
        refreshUI();
        return this;
    }

    public InputDialog setTitleIcon(int titleIconResId) {
        this.titleIcon = getResources().getDrawable(titleIconResId);
        refreshUI();
        return this;
    }

    public InputDialog setTitleIcon(Drawable titleIcon) {
        this.titleIcon = titleIcon;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<MessageDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public InputDialog setDialogXAnimImpl(DialogXAnimInterface<MessageDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public InputDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public InputDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public InputDialog setDialogLifecycleCallback(DialogLifecycleCallback<MessageDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public InputDialog setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public InputDialog onShow(DialogXRunnable<MessageDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public InputDialog onDismiss(DialogXRunnable<MessageDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public InputDialog setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public InputDialog appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public InputDialog setThisOrderIndex(int orderIndex) {
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

    public InputDialog bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public InputDialog setActionRunnable(int actionId, DialogXRunnable<MessageDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public InputDialog cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public InputDialog cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss() {
        dismiss();
    }

    public InputDialog bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }

    public InputDialog setCustomDialogLayoutResId(int customDialogLayoutId) {
        this.customDialogLayoutResId[0] = customDialogLayoutId;
        this.customDialogLayoutResId[1] = customDialogLayoutId;
        return this;
    }

    public InputDialog setCustomDialogLayoutResId(int customDialogLayoutId, boolean isLightTheme) {
        this.customDialogLayoutResId[isLightTheme ? 0 : 1] = customDialogLayoutId;
        return this;
    }
}
