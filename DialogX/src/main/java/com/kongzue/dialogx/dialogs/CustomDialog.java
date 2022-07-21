package com.kongzue.dialogx.dialogs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;

import java.lang.ref.WeakReference;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/20 11:59
 */
public class CustomDialog extends BaseDialog {
    
    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static int overrideEnterAnimRes = 0;
    public static int overrideExitAnimRes = 0;
    public static int overrideMaskEnterAnimRes = R.anim.anim_dialogx_default_alpha_enter;
    public static int overrideMaskExitAnimRes = R.anim.anim_dialogx_default_exit;
    public static BOOLEAN overrideCancelable;
    protected OnBindView<CustomDialog> onBindView;
    protected DialogLifecycleCallback<CustomDialog> dialogLifecycleCallback;
    protected CustomDialog me = this;
    protected DialogImpl dialogImpl;
    protected int enterAnimResId = R.anim.anim_dialogx_default_enter;
    protected int exitAnimResId = R.anim.anim_dialogx_default_exit;
    protected ALIGN align = ALIGN.CENTER;
    protected boolean autoUnsafePlacePadding = true;
    private View dialogView;
    protected int maskColor = Color.TRANSPARENT;
    protected BOOLEAN privateCancelable;
    protected boolean bkgInterceptTouch = true;
    protected OnBackgroundMaskClickListener<CustomDialog> onBackgroundMaskClickListener;
    
    protected View baseView;
    protected int alignViewGravity = -1;                                    //指定菜单相对 baseView 的位置
    protected int width = -1;                                               //指定菜单宽度
    protected int height = -1;                                              //指定菜单高度
    protected int[] baseViewLoc;
    protected int[] marginRelativeBaseView = new int[4];
    
    public enum ALIGN {
        CENTER,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
    
    protected CustomDialog() {
        super();
    }
    
    public static CustomDialog build() {
        return new CustomDialog();
    }
    
    public static CustomDialog build(OnBindView<CustomDialog> onBindView) {
        return new CustomDialog().setCustomView(onBindView);
    }
    
    public CustomDialog(OnBindView<CustomDialog> onBindView) {
        this.onBindView = onBindView;
    }
    
    public static CustomDialog show(OnBindView<CustomDialog> onBindView) {
        CustomDialog customDialog = new CustomDialog(onBindView);
        customDialog.show();
        return customDialog;
    }
    
    public static CustomDialog show(OnBindView<CustomDialog> onBindView, ALIGN align) {
        CustomDialog customDialog = new CustomDialog(onBindView);
        customDialog.align = align;
        customDialog.show();
        return customDialog;
    }
    
    public void show() {
        super.beforeShow();
        if (getDialogView() == null) {
            dialogView = createView(R.layout.layout_dialogx_custom);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(dialogView);
    }
    
    public void show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            dialogView = createView(R.layout.layout_dialogx_custom);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(activity, dialogView);
    }
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout boxCustom;
        
        public DialogImpl(View convertView) {
            if (convertView == null) return;
            boxRoot = convertView.findViewById(R.id.box_root);
            boxCustom = convertView.findViewById(R.id.box_custom);
            
            init();
            dialogImpl = this;
            refreshView();
        }
        
        @Override
        public void init() {
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;
                    getDialogLifecycleCallback().onShow(me);
                    boxCustom.setVisibility(View.GONE);
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    dialogImpl = null;
                    dialogLifecycleCallback = null;
                    System.gc();
                }
            });
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null) {
                        if (onBackPressedListener.onBackPressed()) {
                            dismiss();
                        }
                        return false;
                    }
                    if (isCancelable()) {
                        dismiss();
                    }
                    return false;
                }
            });
            
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    Animation enterAnim;
                    if (enterAnimResId == R.anim.anim_dialogx_default_enter &&
                            exitAnimResId == R.anim.anim_dialogx_default_exit &&
                            baseView == null) {
                        switch (align) {
                            case TOP:
                                enterAnimResId = R.anim.anim_dialogx_top_enter;
                                exitAnimResId = R.anim.anim_dialogx_top_exit;
                                break;
                            case BOTTOM:
                                enterAnimResId = R.anim.anim_dialogx_bottom_enter;
                                exitAnimResId = R.anim.anim_dialogx_bottom_exit;
                                break;
                            case LEFT:
                                enterAnimResId = R.anim.anim_dialogx_left_enter;
                                exitAnimResId = R.anim.anim_dialogx_left_exit;
                                break;
                            case RIGHT:
                                enterAnimResId = R.anim.anim_dialogx_right_enter;
                                exitAnimResId = R.anim.anim_dialogx_right_exit;
                                break;
                        }
                        enterAnim = AnimationUtils.loadAnimation(getTopActivity(), enterAnimResId);
                        enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                    } else {
                        int enterAnimResIdTemp = R.anim.anim_dialogx_default_enter;
                        if (overrideEnterAnimRes != 0) {
                            enterAnimResIdTemp = overrideEnterAnimRes;
                        }
                        if (enterAnimResId != 0) {
                            enterAnimResIdTemp = enterAnimResId;
                        }
                        enterAnim = AnimationUtils.loadAnimation(getTopActivity(), enterAnimResIdTemp);
                    }
                    long enterAnimDurationTemp = enterAnim.getDuration();
                    if (overrideEnterDuration >= 0) {
                        enterAnimDurationTemp = overrideEnterDuration;
                    }
                    if (enterAnimDuration >= 0) {
                        enterAnimDurationTemp = enterAnimDuration;
                    }
                    enterAnim.setDuration(enterAnimDurationTemp);
                    boxCustom.setVisibility(View.VISIBLE);
                    boxCustom.startAnimation(enterAnim);
                    
                    boxRoot.setBackgroundColor(maskColor);
                    if (overrideMaskEnterAnimRes != 0) {
                        Animation maskEnterAnim = AnimationUtils.loadAnimation(getTopActivity(), overrideMaskEnterAnimRes);
                        maskEnterAnim.setInterpolator(new DecelerateInterpolator(2f));
                        maskEnterAnim.setDuration(enterAnimDurationTemp);
                        boxRoot.startAnimation(maskEnterAnim);
                    }
                    
                    ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                    bkgAlpha.setDuration(enterAnimDurationTemp);
                    bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            boxRoot.setBkgAlpha(value);
                        }
                    });
                    bkgAlpha.start();
                }
            });
        }
        
        boolean initSetCustomViewLayoutListener = false;
        
        @Override
        public void refreshView() {
            if (boxRoot == null || getTopActivity() == null) {
                return;
            }
            if (baseView != null) {
                if (!initSetCustomViewLayoutListener) {
                    RelativeLayout.LayoutParams rlp;
                    rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    boxCustom.setLayoutParams(rlp);
                    
                    Runnable onLayoutChangeRunnable = new Runnable() {
                        @Override
                        public void run() {
                            int baseViewLeft = baseViewLoc[0];
                            int baseViewTop = baseViewLoc[1];
                            int calX = 0, calY = 0;
                            if (alignViewGravity != -1) {
                                if (isAlignBaseViewGravity(Gravity.CENTER_VERTICAL)) {
                                    calY = (baseViewTop + baseView.getMeasuredHeight() / 2 - boxCustom.getHeight() / 2);
                                }
                                if (isAlignBaseViewGravity(Gravity.CENTER_HORIZONTAL)) {
                                    calX = (baseViewLeft + baseView.getMeasuredWidth() / 2 - boxCustom.getWidth() / 2);
                                }
                                if (isAlignBaseViewGravity(Gravity.CENTER)) {
                                    calX = (baseViewLeft + baseView.getMeasuredWidth() / 2 - boxCustom.getWidth() / 2);
                                    calY = (baseViewTop + baseView.getMeasuredHeight() / 2 - boxCustom.getHeight() / 2);
                                }
                                
                                if (isAlignBaseViewGravity(Gravity.TOP)) {
                                    calY = baseViewTop - boxCustom.getHeight() - marginRelativeBaseView[3];
                                }
                                if (isAlignBaseViewGravity(Gravity.LEFT)) {
                                    calX = baseViewLeft - boxCustom.getWidth() - marginRelativeBaseView[2];
                                }
                                if (isAlignBaseViewGravity(Gravity.RIGHT)) {
                                    calX = baseViewLeft + baseView.getWidth() + marginRelativeBaseView[0];
                                }
                                if (isAlignBaseViewGravity(Gravity.BOTTOM)) {
                                    calY = baseViewTop + baseView.getHeight() + marginRelativeBaseView[1];
                                }
                                
                                if (calX != 0) boxCustom.setX(calX);
                                if (calY != 0) boxCustom.setY(calY);
                            }
                        }
                    };
                    
                    boxCustom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            onLayoutChangeRunnable.run();
                        }
                    });
                    initSetCustomViewLayoutListener = true;
                }
            } else {
                RelativeLayout.LayoutParams rlp;
                rlp = ((RelativeLayout.LayoutParams) boxCustom.getLayoutParams());
                if (rlp == null) {
                    rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                switch (align) {
                    case TOP:
                        rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        break;
                    case BOTTOM:
                        rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        break;
                    case CENTER:
                        rlp.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rlp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                        break;
                    case LEFT:
                        rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        break;
                    case RIGHT:
                        rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        break;
                }
                boxCustom.setLayoutParams(rlp);
            }
            
            boxRoot.setAutoUnsafePlacePadding(autoUnsafePlacePadding);
            if (bkgInterceptTouch) {
                if (isCancelable()) {
                    boxRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onBackgroundMaskClickListener == null || !onBackgroundMaskClickListener.onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    });
                } else {
                    boxRoot.setOnClickListener(null);
                }
            } else {
                boxRoot.setClickable(false);
            }
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
            }
            
            if (width != -1) {
                boxCustom.setMaxWidth(width);
                boxCustom.setMinimumWidth(width);
            }
            
            if (height != -1) {
                boxCustom.setMaxHeight(height);
                boxCustom.setMinimumHeight(height);
            }
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            if (!dismissAnimFlag) {
                dismissAnimFlag = true;
                boxCustom.post(new Runnable() {
                    @Override
                    public void run() {
                        int exitAnimResIdTemp = R.anim.anim_dialogx_default_exit;
                        if (overrideExitAnimRes != 0) {
                            exitAnimResIdTemp = overrideExitAnimRes;
                        }
                        if (exitAnimResId != 0) {
                            exitAnimResIdTemp = exitAnimResId;
                        }
                        
                        Animation exitAnim = AnimationUtils.loadAnimation(getTopActivity() == null ? boxCustom.getContext() : getTopActivity(), exitAnimResIdTemp);
                        
                        long exitAnimDurationTemp = exitAnim.getDuration();
                        if (overrideExitDuration >= 0) {
                            exitAnimDurationTemp = overrideExitDuration;
                        }
                        if (exitAnimDuration >= 0) {
                            exitAnimDurationTemp = exitAnimDuration;
                        }
                        exitAnim.setDuration(exitAnimDurationTemp);
                        exitAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            
                            }
                            
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                dismiss(dialogView);
                            }
                            
                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            
                            }
                        });
                        boxCustom.startAnimation(exitAnim);
                        
                        if (overrideMaskExitAnimRes != 0) {
                            Animation maskExitAnim = AnimationUtils.loadAnimation(getTopActivity(), overrideMaskExitAnimRes);
                            maskExitAnim.setDuration(exitAnimDurationTemp);
                            maskExitAnim.setInterpolator(new DecelerateInterpolator(2f));
                            boxRoot.startAnimation(maskExitAnim);
                        }
                        
                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1f, 0f);
                        bkgAlpha.setDuration(exitAnimDurationTemp);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (boxRoot != null) {
                                    float value = (float) animation.getAnimatedValue();
                                    boxRoot.setBkgAlpha(value);
                                    if (value == 0) boxRoot.setVisibility(View.GONE);
                                }
                            }
                        });
                        bkgAlpha.start();
                    }
                });
            }
        }
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    public void refreshUI() {
        if (getDialogImpl() == null) return;
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl != null) dialogImpl.refreshView();
            }
        });
    }
    
    public void dismiss() {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl == null) return;
                dialogImpl.doDismiss(null);
            }
        });
    }
    
    public DialogLifecycleCallback<CustomDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<CustomDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public CustomDialog setDialogLifecycleCallback(DialogLifecycleCallback<CustomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public CustomDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public CustomDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public CustomDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
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
    
    public CustomDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }
    
    public CustomDialog.DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public CustomDialog setCustomView(OnBindView<CustomDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public CustomDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public int getEnterAnimResId() {
        return enterAnimResId;
    }
    
    public CustomDialog setEnterAnimResId(int enterAnimResId) {
        this.enterAnimResId = enterAnimResId;
        return this;
    }
    
    public int getExitAnimResId() {
        return exitAnimResId;
    }
    
    public CustomDialog setExitAnimResId(int exitAnimResId) {
        this.exitAnimResId = exitAnimResId;
        return this;
    }
    
    public CustomDialog setAnimResId(int enterAnimResId, int exitAnimResId) {
        this.enterAnimResId = enterAnimResId;
        this.exitAnimResId = exitAnimResId;
        return this;
    }
    
    public ALIGN getAlign() {
        return align;
    }
    
    public CustomDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }
    
    public boolean isAutoUnsafePlacePadding() {
        return autoUnsafePlacePadding;
    }
    
    public CustomDialog setAutoUnsafePlacePadding(boolean autoUnsafePlacePadding) {
        this.autoUnsafePlacePadding = autoUnsafePlacePadding;
        refreshUI();
        return this;
    }
    
    public CustomDialog setFullScreen(boolean fullscreen) {
        this.autoUnsafePlacePadding = !autoUnsafePlacePadding;
        refreshUI();
        return this;
    }
    
    public CustomDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }
    
    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }
    
    public CustomDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }
    
    public long getExitAnimDuration() {
        return exitAnimDuration;
    }
    
    public CustomDialog setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }
    
    @Override
    public void restartDialog() {
        if (dialogView != null) {
            dismiss(dialogView);
            isShow = false;
        }
        if (getDialogImpl().boxCustom != null) {
            getDialogImpl().boxCustom.removeAllViews();
        }
        
        enterAnimDuration = 0;
        dialogView = createView(R.layout.layout_dialogx_custom);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
    }
    
    public void hide() {
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void shutdown() {
        dismiss();
    }
    
    public CustomDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }
    
    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }
    
    public CustomDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }
    
    public int getAlignBaseViewGravity() {
        return alignViewGravity;
    }
    
    /**
     * 判断是否有设置对应的位置关系
     *
     * @param gravity 位置关系
     * @return 是否具备位置关系
     */
    public boolean isAlignBaseViewGravity(int gravity) {
        return (alignViewGravity & gravity) == gravity;
    }
    
    public CustomDialog setAlignBaseViewGravity(View baseView, int alignGravity) {
        this.baseView = baseView;
        this.alignViewGravity = alignGravity;
        baseViewLoc = new int[2];
        baseView.getLocationOnScreen(baseViewLoc);
        setFullScreen(true);
        return this;
    }
    
    public CustomDialog setAlignBaseViewGravity(View baseView, int alignGravity, int marginLeft,
                                                int marginTop, int marginRight, int marginBottom) {
        this.marginRelativeBaseView = new int[]{marginLeft, marginTop, marginRight, marginBottom};
        return setAlignBaseViewGravity(baseView, alignGravity);
    }
    
    public int[] getBaseViewMargin() {
        return marginRelativeBaseView;
    }
    
    public CustomDialog setBaseViewMargin(int[] marginRelativeBaseView) {
        this.marginRelativeBaseView = marginRelativeBaseView;
        return this;
    }
    
    public CustomDialog setBaseViewMargin(int marginLeft, int marginTop,
                                                  int marginRight, int marginBottom) {
        this.marginRelativeBaseView = new int[]{marginLeft, marginTop, marginRight, marginBottom};
        return this;
    }
    
    public CustomDialog setBaseViewMarginLeft(int marginLeft){
        this.marginRelativeBaseView[0] = marginLeft;
        return this;
    }
    
    public CustomDialog setBaseViewMarginTop(int marginTop){
        this.marginRelativeBaseView[1] = marginTop;
        return this;
    }
    
    public CustomDialog setBaseViewMarginRight(int marginRight){
        this.marginRelativeBaseView[2] = marginRight;
        return this;
    }
    
    public CustomDialog setBaseViewMarginBottom(int marginBottom){
        this.marginRelativeBaseView[3] = marginBottom;
        return this;
    }
    
    public int getBaseViewMarginLeft(int marginLeft){
        return this.marginRelativeBaseView[0];
    }
    
    public int getBaseViewMarginTop(int marginLeft){
        return this.marginRelativeBaseView[1];
    }
    
    public int getBaseViewMarginRight(int marginLeft){
        return this.marginRelativeBaseView[2];
    }
    
    public int getBaseViewMarginBottom(int marginLeft){
        return this.marginRelativeBaseView[3];
    }
    
    public View getBaseView() {
        return baseView;
    }
    
    public int getWidth() {
        return width;
    }
    
    /**
     * 设置对话框 UI 宽度（单位：像素）
     *
     * @param width 宽度（像素）
     * @return CustomDialog实例
     */
    public CustomDialog setWidth(int width) {
        this.width = width;
        refreshUI();
        return this;
    }
    
    public int getHeight() {
        return height;
    }
    
    /**
     * 设置对话框 UI 高度（单位：像素）
     *
     * @param height 高度（像素）
     * @return CustomDialog实例
     */
    public CustomDialog setHeight(int height) {
        this.height = height;
        refreshUI();
        return this;
    }
    
    public OnBackgroundMaskClickListener<CustomDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }
    
    public CustomDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<CustomDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }
}
