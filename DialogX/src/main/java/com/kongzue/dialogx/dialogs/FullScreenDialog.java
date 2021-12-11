package com.kongzue.dialogx.dialogs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
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
    
    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static BOOLEAN overrideCancelable;
    
    protected OnBindView<FullScreenDialog> onBindView;
    protected BOOLEAN privateCancelable;
    protected boolean hideZoomBackground;
    
    protected DialogLifecycleCallback<FullScreenDialog> dialogLifecycleCallback;
    
    protected FullScreenDialog me = this;
    
    protected FullScreenDialog() {
        super();
    }
    
    private View dialogView;
    
    public static FullScreenDialog build() {
        return new FullScreenDialog();
    }
    
    public static FullScreenDialog build(OnBindView<FullScreenDialog> onBindView) {
        return new FullScreenDialog(onBindView);
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
        if (getDialogView() == null) {
            dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(dialogView);
    }
    
    public void show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(activity, dialogView);
    }
    
    protected DialogImpl dialogImpl;
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        private FullScreenDialogTouchEventInterceptor fullScreenDialogTouchEventInterceptor;
        
        public ActivityScreenShotImageView imgZoomActivity;
        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxBkg;
        public MaxRelativeLayout bkg;
        public RelativeLayout boxCustom;
        
        public DialogImpl(View convertView) {
            if (convertView == null) return;
            imgZoomActivity = convertView.findViewById(R.id.img_zoom_activity);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            bkg = convertView.findViewById(R.id.bkg);
            boxCustom = convertView.findViewById(R.id.box_custom);
            
            if (hideZoomBackground) {
                dialogView.setBackgroundResource(R.color.black20);
                imgZoomActivity.setVisibility(View.GONE);
            } else {
                dialogView.setBackgroundResource(R.color.black);
                imgZoomActivity.setVisibility(View.VISIBLE);
            }
            init();
            dialogImpl = this;
            refreshView();
        }
        
        public float bkgEnterAimY = -1;
        private long enterAnimDurationTemp = 300;
        
        @Override
        public void init() {
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    
                    getDialogLifecycleCallback().onShow(me);
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    fullScreenDialogTouchEventInterceptor = null;
                    dialogImpl = null;
                    dialogLifecycleCallback = null;
                    System.gc();
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
            
            enterAnimDurationTemp = 300;
            if (overrideEnterDuration >= 0) {
                enterAnimDurationTemp = overrideEnterDuration;
            }
            if (enterAnimDuration >= 0) {
                enterAnimDurationTemp = enterAnimDuration;
            }
            boxRoot.setBkgAlpha(0f);
            
            bkg.setY(boxRoot.getHeight());
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    int customViewHeight = boxCustom.getHeight();
                    if (customViewHeight == 0) {
                        //实测在 Android 10 中，离屏情况下 View可能无法得到正确高度（恒 0），此时直接按照全屏高度处理
                        //其他版本 Android 未发现此问题
                        showEnterAnim((int) boxRoot.getSafeHeight());
                    } else {
                        showEnterAnim(customViewHeight);
                    }
                }
            });
            
            boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
                @Override
                public void onChange(Rect unsafeRect) {
                    if (unsafeRect.bottom > dip2px(100)) {
                        ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), 0);
                        enterAnim.setDuration(enterAnimDurationTemp);
                        enterAnim.start();
                    }
                }
            });
            
            bkg.setOnYChanged(new MaxRelativeLayout.OnYChanged() {
                @Override
                public void y(float y) {
                    float zoomScale = 1 - (boxRoot.getHeight() - y) * 0.00002f;
                    if (zoomScale > 1) zoomScale = 1;
                    if (!hideZoomBackground) {
                        imgZoomActivity.setScaleX(zoomScale);
                        imgZoomActivity.setScaleY(zoomScale);
                        
                        imgZoomActivity.setRadius(dip2px(15) * ((boxRoot.getHeight() - y) / boxRoot.getHeight()));
                    }
                }
            });
        }
        
        private void showEnterAnim(int customViewHeight) {
            bkgEnterAimY = boxRoot.getSafeHeight() - customViewHeight;
            if (bkgEnterAimY < 0) bkgEnterAimY = 0;
            
            ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", boxRoot.getHeight(), bkgEnterAimY);
            enterAnim.setDuration(enterAnimDurationTemp);
            enterAnim.setInterpolator(new DecelerateInterpolator());
            enterAnim.start();
            bkg.setVisibility(View.VISIBLE);
            
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
        
        @Override
        public void refreshView() {
            if (backgroundColor != -1) {
                tintColor(bkg, backgroundColor);
            }
            
            bkg.setMaxWidth(getMaxWidth());
            
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
                onBindView.bindParent(boxCustom, me);
            }
            
            if (hideZoomBackground) {
                dialogView.setBackgroundResource(R.color.black20);
                imgZoomActivity.setVisibility(View.GONE);
            } else {
                dialogView.setBackgroundResource(R.color.black);
                imgZoomActivity.setVisibility(View.VISIBLE);
            }
            
            fullScreenDialogTouchEventInterceptor.refresh(me, this);
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            if (getContext() == null) return;
    
            if (!dismissAnimFlag) {
                dismissAnimFlag = true;
                long exitAnimDurationTemp = 300;
                if (overrideExitDuration >= 0) {
                    exitAnimDurationTemp = overrideExitDuration;
                }
                if (exitAnimDuration >= 0) {
                    exitAnimDurationTemp = exitAnimDuration;
                }
    
                ObjectAnimator exitAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), boxBkg.getHeight());
                exitAnim.setDuration(exitAnimDurationTemp);
                exitAnim.start();
    
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
    
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss(dialogView);
                    }
                }, exitAnimDurationTemp);
            }
        }
        
        public void preDismiss() {
            if (isCancelable()) {
                doDismiss(boxRoot);
            } else {
                long exitAnimDurationTemp = 300;
                if (overrideExitDuration >= 0) {
                    exitAnimDurationTemp = overrideExitDuration;
                }
                if (exitAnimDuration >= 0) {
                    exitAnimDurationTemp = exitAnimDuration;
                }
                
                ObjectAnimator exitAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), bkgEnterAimY);
                exitAnim.setDuration(exitAnimDurationTemp);
                exitAnim.start();
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
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    public DialogLifecycleCallback<FullScreenDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<FullScreenDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public FullScreenDialog setDialogLifecycleCallback(DialogLifecycleCallback<FullScreenDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
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
    
    public boolean isHideZoomBackground() {
        return hideZoomBackground;
    }
    
    public FullScreenDialog setHideZoomBackground(boolean hideZoomBackground) {
        this.hideZoomBackground = hideZoomBackground;
        refreshUI();
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
        dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
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
    
    public FullScreenDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }
    
    public FullScreenDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }
}
