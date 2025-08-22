package com.kongzue.dialogx.dialogs;

import static android.view.View.VISIBLE;

import android.app.Activity;
import android.view.View;

import androidx.annotation.ColorInt;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.color.ColorPickerType;
import com.kongzue.dialogx.color.ColorPickerView;
import com.kongzue.dialogx.color.OnColorValueChangedListener;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBindView;

public class ColorPickerDialog extends BottomDialog implements OnColorValueChangedListener {
    @ColorInt
    private int value;
    private OnColorValueChangedListener listener;

    protected ColorPickerDialog() {
        super();
    }

    public ColorPickerDialog(OnBindView<BottomDialog> onBindView) {
        super(onBindView);
    }

    public ColorPickerDialog(CharSequence title, OnBindView<BottomDialog> onBindView) {
        super(title, onBindView);
    }

    public ColorPickerDialog(int titleResId, OnBindView<BottomDialog> onBindView) {
        super(titleResId, onBindView);
    }

    public ColorPickerDialog(CharSequence title, CharSequence message) {
        super(title, message);
    }

    public ColorPickerDialog(int titleResId, int messageResId) {
        super(titleResId, messageResId);
    }

    public ColorPickerDialog(CharSequence title, CharSequence message, OnBindView<BottomDialog> onBindView) {
        super(title, message, onBindView);
    }

    public ColorPickerDialog(int titleResId, int messageResId, OnBindView<BottomDialog> onBindView) {
        super(titleResId, messageResId, onBindView);
    }

    public ColorPickerDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        super(titleResId, messageResId, okTextResId, cancelTextResId);
    }

    public ColorPickerDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        super(title, message, okText, cancelText);
    }

    public ColorPickerDialog(int titleResId, int messageResId, int okTextResId) {
        super(titleResId, messageResId, okTextResId);
    }

    public ColorPickerDialog(CharSequence title, CharSequence message, CharSequence okText) {
        super(title, message, okText);
    }

    public ColorPickerDialog(int titleResId, int messageResId, int okTextResId, OnBindView<BottomDialog> onBindView) {
        super(titleResId, messageResId, okTextResId, onBindView);
    }

    public ColorPickerDialog(CharSequence title, CharSequence message, CharSequence okText, OnBindView<BottomDialog> onBindView) {
        super(title, message, okText, onBindView);
    }

    public ColorPickerDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId, OnBindView<BottomDialog> onBindView) {
        super(titleResId, messageResId, okTextResId, cancelTextResId, onBindView);
    }

    public ColorPickerDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, OnBindView<BottomDialog> onBindView) {
        super(title, message, okText, cancelText, onBindView);
    }

    public static ColorPickerDialog build() {
        return new ColorPickerDialog();
    }

    public static ColorPickerDialog build(DialogXStyle style) {
        return (ColorPickerDialog) new ColorPickerDialog().setStyle(style);
    }

    public static ColorPickerDialog build(OnBindView<BottomDialog> onBindView) {
        return (ColorPickerDialog) new ColorPickerDialog().setCustomView(onBindView);
    }

    public static ColorPickerDialog show(CharSequence title, CharSequence message) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(title, message);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, int messageResId) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, messageResId);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(CharSequence title, CharSequence message, OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(title, message, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, int messageResId, OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, messageResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(CharSequence title, OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(title, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, int messageResId, int okTextResId) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, messageResId, okTextResId);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, messageResId, okTextResId, cancelTextResId);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, int messageResId, int okTextResId, OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, messageResId, okTextResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static ColorPickerDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId, OnBindView<BottomDialog> onBindView) {
        ColorPickerDialog bottomDialog = new ColorPickerDialog(titleResId, messageResId, okTextResId, cancelTextResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    @Override
    public ColorPickerDialog show() {
        if (isHide && getDialogView() != null && isShow) {
            if (hideWithExitAnim && getDialogImpl() != null) {
                getDialogView().setVisibility(VISIBLE);
                getDialogImpl().getDialogXAnimImpl().doShowAnim(me, getDialogImpl().bkg);
            } else {
                getDialogView().setVisibility(VISIBLE);
            }
            return this;
        }
        super.beforeShow();
        if (getDialogView() == null) {
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            if (style.overrideBottomDialogRes() != null) {
                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
            }

            View dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            initialColorPicker(dialogImpl.colorPickerView);
            if (dialogView != null) dialogView.setTag(me);
            show(dialogView);
        } else {
            show(getDialogView());
        }
        return this;
    }

    @Override
    public void show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            if (style.overrideBottomDialogRes() != null) {
                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
            }

            View dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            initialColorPicker(dialogImpl.colorPickerView);
            if (dialogView != null) dialogView.setTag(me);
            show(activity, dialogView);
        } else {
            show(activity, getDialogView());
        }
    }

    public ColorPickerDialog setValue(int value) {
        this.value = value;
        return this;
    }

    public ColorPickerDialog setListener(OnColorValueChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isAllowInterceptTouch() {
        return false;
    }

    private void initialColorPicker(ColorPickerView view) {
        view.setVisibility(VISIBLE);
        view.setValue(value);
        view.setListener(listener);
        view.setHapticFeedbackEnabled(isHapticFeedbackEnabled == 1);
    }

    @Override
    public void onColorValueChanged(ColorPickerType type, int value) {
        if (type == ColorPickerType.FINAL_COLOR || type == ColorPickerType.INSTANT_COLOR) {
            this.value = value;
            if (listener != null) listener.onColorValueChanged(type, value);
        }
    }
}
