package com.kongzue.dialogx.dialogs;

import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
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
    
    public InputDialog() {
        me = this;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, String inputText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.inputText = inputText;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
    }
    
    public InputDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText, String inputText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
        this.inputText = inputText;
    }
    
    public CharSequence getOkButton() {
        return okText;
    }
    
    public InputDialog setOkButton(CharSequence okText) {
        this.okText = okText;
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
    
    public CharSequence getOtherButton() {
        return otherText;
    }
    
    public InputDialog setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        refreshUI();
        return this;
    }
    
    public OnDialogButtonClickListener getOkButtonClickListener() {
        return okButtonClickListener;
    }
    
    public InputDialog setOkButtonClickListener(OnDialogButtonClickListener okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }
    
    public OnDialogButtonClickListener getCancelButtonClickListener() {
        return cancelButtonClickListener;
    }
    
    public InputDialog setCancelButtonClickListener(OnDialogButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public OnDialogButtonClickListener getOtherButtonClickListener() {
        return otherButtonClickListener;
    }
    
    public InputDialog setOtherButtonClickListener(OnDialogButtonClickListener otherButtonClickListener) {
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
}
