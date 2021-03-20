package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;
import com.kongzue.dialogx.util.FullScreenDialogTouchEventInterceptor;
import com.kongzue.dialogx.util.views.ActivityScreenShotImageView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 15:17
 */
public class FullScreenDialog extends BaseDialog {
    
    public static BOOLEAN overrideCancelable;
    protected OnBindView<FullScreenDialog> onBindView;
    protected BOOLEAN privateCancelable;
    
    protected DialogLifecycleCallback<FullScreenDialog> dialogLifecycleCallback;
    
    protected FullScreenDialog me = this;
    
    protected FullScreenDialog() {
        super();
    }
    
    private View dialogView;
    
    public static FullScreenDialog build() {
        return new FullScreenDialog();
    }
    
    public FullScreenDialog(OnBindView<FullScreenDialog> onBindView) {
        this.onBindView = onBindView;
    }
    
    public static FullScreenDialog show(OnBindView<FullScreenDialog> onBindView) {
        FullScreenDialog fullScreenDialog = new FullScreenDialog(onBindView);
        fullScreenDialog.show();
        return fullScreenDialog;
    }
    
    public void show() {
        super.beforeShow();
        dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(dialogKey());
        show(dialogView);
    }
    
    public void show(Activity activity) {
        super.beforeShow();
        dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(dialogKey());
        show(activity, dialogView);
    }
    
    protected DialogImpl dialogImpl;
    
    public class DialogImpl implements DialogConvertViewInterface  {
        
        private FullScreenDialogTouchEventInterceptor fullScreenDialogTouchEventInterceptor;
        
        public ActivityScreenShotImageView imgZoomActivity;
        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxBkg;
        public MaxRelativeLayout bkg;
        public RelativeLayout boxCustom;
        
        public DialogImpl(View convertView) {
            imgZoomActivity = convertView.findViewById(R.id.img_zoom_activity);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            bkg = convertView.findViewById(R.id.bkg);
            boxCustom = convertView.findViewById(R.id.box_custom);
            init();
            refreshView();
        }
        
        public float bkgEnterAimY = -1;
        
        @Override
        public void init() {
            boxRoot.setParentDialog(me);
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
                    if (isCancelable()) {
                        dismiss();
                    }
                    return false;
                }
            });
            
            fullScreenDialogTouchEventInterceptor = new FullScreenDialogTouchEventInterceptor(me, dialogImpl);
            
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    bkgEnterAimY = boxRoot.getSafeHeight() - boxCustom.getHeight();
                    if (bkgEnterAimY < 0) bkgEnterAimY = 0;
                    
                    boxRoot.animate()
                            .setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration)
                            .alpha(1f)
                            .setInterpolator(new DecelerateInterpolator())
                            .setListener(null);
                    
                    ObjectAnimator exitAnim = ObjectAnimator.ofFloat(bkg, "y", boxRoot.getHeight(), bkgEnterAimY);
                    exitAnim.setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration);
                    exitAnim.start();
                }
            });
            
            bkg.setOnYChanged(new MaxRelativeLayout.OnYChanged() {
                @Override
                public void y(float y) {
                    float zoomScale = 1 - (boxRoot.getHeight() - y) * 0.00002f;
                    if (zoomScale > 1) zoomScale = 1;
                    imgZoomActivity.setScaleX(zoomScale);
                    imgZoomActivity.setScaleY(zoomScale);
                    
                    imgZoomActivity.setRadius(dip2px(15) * ((boxRoot.getHeight() - y) / boxRoot.getHeight()));
                }
            });
            
            boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
                @Override
                public void onChange(Rect unsafeRect) {
                    if (unsafeRect.bottom > dip2px(100)) {
                        ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), 0);
                        enterAnim.setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration);
                        enterAnim.start();
                    }
                }
            });
        }
        
        @Override
        public void refreshView() {
            if (backgroundColor != -1) {
                tintColor(bkg, backgroundColor);
            }
            
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
            
            fullScreenDialogTouchEventInterceptor.refresh(me, this);
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            
            ObjectAnimator exitAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), boxBkg.getHeight());
            exitAnim.setDuration(exitAnimDuration == -1 ? 300 : exitAnimDuration);
            exitAnim.start();
            
            boxRoot.animate()
                    .alpha(0f)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(exitAnim.getDuration())
                    .setListener(new AnimatorListenerEndCallBack() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dismiss(dialogView);
                        }
                    });
        }
        
        public void preDismiss() {
            if (isCancelable()) {
                doDismiss(boxRoot);
            } else {
                ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), bkgEnterAimY);
                enterAnim.setDuration(exitAnimDuration == -1 ? 300 : exitAnimDuration);
                enterAnim.start();
            }
        }
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    public void refreshUI() {
        if (getRootFrameLayout() == null) return;
        getRootFrameLayout().post(new Runnable() {
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
    
    public DialogLifecycleCallback<FullScreenDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<FullScreenDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public FullScreenDialog setDialogLifecycleCallback(DialogLifecycleCallback<FullScreenDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public FullScreenDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public FullScreenDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public FullScreenDialog setTheme(DialogX.THEME theme) {
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
    
    public FullScreenDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public FullScreenDialog setCustomView(OnBindView<FullScreenDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public FullScreenDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public FullScreenDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }
    
    public FullScreenDialog setBackgroundColorRes(@ColorRes int backgroundColorRes) {
        this.backgroundColor = getColor(backgroundColorRes);
        refreshUI();
        return this;
    }
    
    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }
    
    public FullScreenDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }
    
    public long getExitAnimDuration() {
        return exitAnimDuration;
    }
    
    public FullScreenDialog setExitAnimDuration(long exitAnimDuration) {
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
        enterAnimDuration = 0;
        dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
        dialogImpl = new DialogImpl(dialogView);
        dialogView.setTag(dialogKey());
        show(dialogView);
    }
}
