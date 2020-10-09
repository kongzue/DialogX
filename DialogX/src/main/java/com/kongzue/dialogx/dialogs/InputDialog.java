package com.kongzue.dialogx.dialogs;

import android.view.View;

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
        this.title = title;
        this.message = message;
        this.okText = okText;
    }
    
    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText) {
        InputDialog inputDialog = new InputDialog(title, message, okText);
        inputDialog.show();
        return inputDialog;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
    }
    
    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText);
        inputDialog.show();
        return inputDialog;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, String inputText) {
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
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
    }
    
    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText, otherText);
        inputDialog.show();
        return inputDialog;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText, String inputText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
        this.inputText = inputText;
    }
    
    public static InputDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText, String inputText) {
        InputDialog inputDialog = new InputDialog(title, message, okText, cancelText, otherText, inputText);
        inputDialog.show();
        return inputDialog;
    }
    
    public CharSequence getOkButton() {
        return okText;
    }
    
    public InputDialog setOkButton(CharSequence okText) {
        this.okText = okText;
        refreshUI();
        return this;
    }
    
    public InputDialog setOkButton(OnInputDialogButtonClickListener okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }
    
    public InputDialog setOkButton(CharSequence okText, OnInputDialogButtonClickListener okButtonClickListener) {
        this.okText = okText;
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
    
    public InputDialog setCancelButton(OnInputDialogButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public InputDialog setCancelButton(CharSequence cancelText, OnInputDialogButtonClickListener cancelButtonClickListener) {
        this.cancelText = cancelText;
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
    
    public InputDialog setOtherButton(OnInputDialogButtonClickListener otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }
    
    public InputDialog setOtherButton(CharSequence otherText, OnInputDialogButtonClickListener otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOkButtonClickListener() {
        return (OnInputDialogButtonClickListener) okButtonClickListener;
    }
    
    public InputDialog setOkButtonClickListener(OnInputDialogButtonClickListener okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }
    
    public OnInputDialogButtonClickListener getCancelButtonClickListener() {
        return (OnInputDialogButtonClickListener) cancelButtonClickListener;
    }
    
    public InputDialog setCancelButtonClickListener(OnInputDialogButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOtherButtonClickListener() {
        return (OnInputDialogButtonClickListener) otherButtonClickListener;
    }
    
    public InputDialog setOtherButtonClickListener(OnInputDialogButtonClickListener otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
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
    
    public CharSequence getMessage() {
        return message;
    }
    
    public InputDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }
    
    public String getInputText() {
        return inputText;
    }
    
    public InputDialog setInputText(String inputText) {
        this.inputText = inputText;
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
        return cancelable;
    }
    
    public InputDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
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
    
    public InputDialog setCustomView(OnBindView<MessageDialog> onBindView){
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public InputDialog removeCustomView(){
        this.onBindView.clean();
        refreshUI();
        return this;
    }
}
