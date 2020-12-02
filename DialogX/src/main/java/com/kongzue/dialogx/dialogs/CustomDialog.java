package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
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
    
    protected OnBindView<CustomDialog> onBindView;
    protected DialogLifecycleCallback<CustomDialog> dialogLifecycleCallback;
    protected CustomDialog me = this;
    protected DialogImpl dialogImpl;
    protected int enterAnimResId = R.anim.anim_dialogx_default_enter;
    protected int exitAnimResId = R.anim.anim_dialogx_default_exit;
    protected ALIGN align = ALIGN.CENTER;
    protected boolean autoUnsafePlacePadding = true;
    private View dialogView;
    
    public enum ALIGN {
        CENTER,
        TOP,
        BOTTOM
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
        show(dialogView);
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
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    
                    getDialogLifecycleCallback().onShow(me);
                    
                    if (onBindView != null) onBindView.onBind(me, onBindView.getCustomView());
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
                    if (cancelable) {
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
            }
            boxCustom.setLayoutParams(rlp);
            
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    boxRoot.animate().setDuration(300).alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(100).setListener(null);
                    
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
                        }
                    }
                    Animation enterAnim;
                    if (enterAnimResId == R.anim.anim_dialogx_default_enter) {
                        enterAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_dialogx_default_enter);
                        enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                    } else {
                        enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                    }
                    boxCustom.startAnimation(enterAnim);
                }
            });
        }
        
        @Override
        public void refreshView() {
            boxRoot.setAutoUnsafePlacePadding(autoUnsafePlacePadding);
            if (cancelable) {
                boxRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss(v);
                    }
                });
            } else {
                boxRoot.setOnClickListener(null);
            }
            
            if (onBindView != null) {
                if (onBindView.getCustomView() != null) {
                    boxCustom.removeView(onBindView.getCustomView());
                    ViewGroup.LayoutParams lp = onBindView.getCustomView().getLayoutParams();
                    if (lp == null) {
                        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    boxCustom.addView(onBindView.getCustomView(), lp);
                }
            }
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            
            Animation exitAnim = AnimationUtils.loadAnimation(getContext(), exitAnimResId);
            boxCustom.startAnimation(exitAnim);
            
            boxRoot.animate().setDuration(300).alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(exitAnim.getDuration()).setListener(new AnimatorListenerEndCallBack() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismiss(dialogView);
                }
            });
        }
    }
    
    public void refreshUI() {
        if (getRootFrameLayout() == null) return;
        getRootFrameLayout().post(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl!=null)dialogImpl.refreshView();
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
        return cancelable;
    }
    
    public CustomDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
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
}
