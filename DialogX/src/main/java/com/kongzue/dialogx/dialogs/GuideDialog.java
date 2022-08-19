package com.kongzue.dialogx.dialogs;

import android.app.Activity;
import android.view.View;

import androidx.annotation.ColorInt;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/8/19 16:35
 */
public class GuideDialog extends CustomDialog {
    
    protected GuideDialog() {
        super();
    }
    
    public static GuideDialog build() {
        return new GuideDialog();
    }
    
    public static GuideDialog build(OnBindView<CustomDialog> onBindView) {
        return new GuideDialog(onBindView);
    }
    
    public GuideDialog(OnBindView<CustomDialog> onBindView) {
        this.onBindView = onBindView;
    }
    
    public static GuideDialog show(OnBindView<CustomDialog> onBindView) {
        GuideDialog guideDialog = new GuideDialog(onBindView);
        guideDialog.show();
        return guideDialog;
    }
    
    public static GuideDialog show(OnBindView<CustomDialog> onBindView, ALIGN align) {
        GuideDialog guideDialog = new GuideDialog(onBindView);
        guideDialog.align = align;
        guideDialog.show();
        return guideDialog;
    }
    
    public GuideDialog show() {
        super.show();
        return this;
    }
    
    public GuideDialog show(Activity activity) {
        super.show(activity);
        return this;
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    public GuideDialog setDialogLifecycleCallback(DialogLifecycleCallback<CustomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }
    
    public GuideDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public GuideDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public GuideDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }
    public GuideDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }
    
    public GuideDialog.DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public GuideDialog setCustomView(OnBindView<CustomDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    public GuideDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    public GuideDialog setEnterAnimResId(int enterAnimResId) {
        this.enterAnimResId = enterAnimResId;
        return this;
    }
    public GuideDialog setExitAnimResId(int exitAnimResId) {
        this.exitAnimResId = exitAnimResId;
        return this;
    }
    
    public GuideDialog setAnimResId(int enterAnimResId, int exitAnimResId) {
        this.enterAnimResId = enterAnimResId;
        this.exitAnimResId = exitAnimResId;
        return this;
    }
    public GuideDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }
    
    public GuideDialog setAutoUnsafePlacePadding(boolean autoUnsafePlacePadding) {
        this.autoUnsafePlacePadding = autoUnsafePlacePadding;
        refreshUI();
        return this;
    }
    
    public GuideDialog setFullScreen(boolean fullscreen) {
        this.autoUnsafePlacePadding = !autoUnsafePlacePadding;
        refreshUI();
        return this;
    }
    
    public GuideDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }
    
    public GuideDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }
    
    public GuideDialog setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }
    public GuideDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }
    public GuideDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }
    public GuideDialog setAlignBaseViewGravity(View baseView, int alignGravity) {
        this.baseView = baseView;
        this.alignViewGravity = alignGravity;
        baseViewLoc = new int[2];
        baseView.getLocationOnScreen(baseViewLoc);
        setFullScreen(true);
        return this;
    }
    
    public GuideDialog setAlignBaseViewGravity(View baseView, int alignGravity, int marginLeft,
                                                int marginTop, int marginRight, int marginBottom) {
        this.marginRelativeBaseView = new int[]{marginLeft, marginTop, marginRight, marginBottom};
        return setAlignBaseViewGravity(baseView, alignGravity);
    }
    
    public GuideDialog setBaseViewMargin(int[] marginRelativeBaseView) {
        this.marginRelativeBaseView = marginRelativeBaseView;
        return this;
    }
    
    public GuideDialog setBaseViewMargin(int marginLeft, int marginTop,
                                          int marginRight, int marginBottom) {
        this.marginRelativeBaseView = new int[]{marginLeft, marginTop, marginRight, marginBottom};
        return this;
    }
    
    public GuideDialog setBaseViewMarginLeft(int marginLeft) {
        this.marginRelativeBaseView[0] = marginLeft;
        return this;
    }
    
    public GuideDialog setBaseViewMarginTop(int marginTop) {
        this.marginRelativeBaseView[1] = marginTop;
        return this;
    }
    
    public GuideDialog setBaseViewMarginRight(int marginRight) {
        this.marginRelativeBaseView[2] = marginRight;
        return this;
    }
    
    public GuideDialog setBaseViewMarginBottom(int marginBottom) {
        this.marginRelativeBaseView[3] = marginBottom;
        return this;
    }
    
    /**
     * 设置对话框 UI 宽度（单位：像素）
     *
     * @param width 宽度（像素）
     * @return CustomDialog实例
     */
    public GuideDialog setWidth(int width) {
        this.width = width;
        refreshUI();
        return this;
    }
    /**
     * 设置对话框 UI 高度（单位：像素）
     *
     * @param height 高度（像素）
     * @return CustomDialog实例
     */
    public GuideDialog setHeight(int height) {
        this.height = height;
        refreshUI();
        return this;
    }
    
    
    public GuideDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<CustomDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }
}
