package com.kongzue.dialogx.dialogs;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
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
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/20 11:59
 */
public class CustomDialog extends BaseDialog {
    
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
        dialogView = createView(R.layout.layout_dialogx_custom);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(dialogKey());
        show(dialogView);
    }
    
    public void show(Activity activity) {
        super.beforeShow();
        dialogView = createView(R.layout.layout_dialogx_custom);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(dialogKey());
        show(activity, dialogView);
    }
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxCustom;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            boxCustom = convertView.findViewById(R.id.box_custom);
            
            init();
            refreshView();
        }
        
        @Override
        public void init() {
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    getDialogLifecycleCallback().onShow(me);
                    boxCustom.setVisibility(View.GONE);
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                }
            });
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null && onBackPressedListener.onBackPressed()) {
                        dismiss();
                        return false;
                    }
                    if (isCancelable()) {
                        dismiss();
                    }
                    return false;
                }
            });
            
            RelativeLayout.LayoutParams rlp;
            rlp = ((RelativeLayout.LayoutParams) boxCustom.getLayoutParams());
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
            
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    if (enterAnimResId == R.anim.anim_dialogx_default_enter && exitAnimResId == R.anim.anim_dialogx_default_exit) {
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
                    }
                    Animation enterAnim;
                    if (enterAnimResId == R.anim.anim_dialogx_default_enter) {
                        enterAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_dialogx_default_enter);
                        enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                    } else {
                        enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                    }
                    if (enterAnimDuration != -1) {
                        enterAnim.setDuration(enterAnimDuration);
                    }
                    boxCustom.setVisibility(View.VISIBLE);
                    boxCustom.startAnimation(enterAnim);
                }
            });
        }
        
        @Override
        public void refreshView() {
            boxRoot.setAutoUnsafePlacePadding(autoUnsafePlacePadding);
            if (isCancelable()) {
                boxRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss(v);
                    }
                });
            } else {
                boxRoot.setOnClickListener(null);
            }
            
            boxRoot.setBackgroundColor(maskColor);
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
            }
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            
            Animation exitAnim = AnimationUtils.loadAnimation(getContext(), exitAnimResId);
            if (exitAnimDuration != -1) {
                exitAnim.setDuration(exitAnimDuration);
            }
            boxCustom.startAnimation(exitAnim);
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
        }
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    public void refreshUI() {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl != null) dialogImpl.refreshView();
            }
        });
    }
    
    public void dismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    public DialogLifecycleCallback<CustomDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<CustomDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public CustomDialog setDialogLifecycleCallback(DialogLifecycleCallback<CustomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
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
    public void onUIModeChange(Configuration newConfig) {
        if (dialogView != null) {
            dismiss(dialogView);
        }
        if (getDialogImpl().boxCustom != null) {
            getDialogImpl().boxCustom.removeAllViews();
        }
        
        enterAnimDuration = 0;
        dialogView = createView(R.layout.layout_dialogx_custom);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(dialogKey());
        show(dialogView);
    }
}
