package com.kongzue.dialogx.dialogs;

import android.content.res.Configuration;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.dialogx.util.TextInfo;

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
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText) {
        cancelable = DialogX.cancelable;
        this.title = title;
        this.message = message;
        this.okText = okText;
    }
    
    public InputDialog(int titleResId, int messageResId, int okTextResId) {
        cancelable = DialogX.cancelable;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
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
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public InputDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
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
    public void onUIModeChange(Configuration newConfig) {
        if (dialogView != null) {
            dismiss(dialogView);
        }
        if (getDialogImpl().boxCustom!=null){
            getDialogImpl().boxCustom.removeAllViews();
        }
        int layoutId = style.layout(isLightTheme());
        layoutId = layoutId == 0 ? (isLightTheme() ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark) : layoutId;
        
        String inputText = getInputText();
        enterAnimDuration = 0;
        dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(me);
        show(dialogView);
        setInputText(inputText);
    }
}
