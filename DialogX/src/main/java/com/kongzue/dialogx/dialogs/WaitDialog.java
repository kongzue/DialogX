package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.BlurView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;
import com.kongzue.dialogx.util.views.ProgressView;

import java.lang.ref.WeakReference;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/27 14:50
 */
public class WaitDialog extends BaseDialog {
    
    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static int overrideEnterAnimRes = 0;
    public static int overrideExitAnimRes = 0;
    public static BOOLEAN overrideCancelable;
    protected OnBindView<WaitDialog> onBindView;
    
    public enum TYPE {
        NONE,
        SUCCESS,
        WARNING,
        ERROR
    }
    
    protected static WeakReference<WaitDialog> me;
    protected CharSequence message;
    protected long tipShowDuration = 1500;
    protected float waitProgress = -1;
    protected int showType = -1;        //-1:Waitdialog 状态标示符，其余为 TipDialog 状态标示
    protected TextInfo messageTextInfo;
    protected int maskColor = -1;
    protected BOOLEAN privateCancelable;
    
    private DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback;
    
    protected WaitDialog() {
        super();
        me = new WeakReference<>(this);
        cancelable = DialogX.cancelableTipDialog;
    }
    
    public static WaitDialog show(CharSequence message) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        me().showType = -1;
        if (dialogImpl != null) {
            dialogImpl.progressView.loading();
            setMessage(message);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.message = message;
            waitDialog.show();
            return waitDialog;
        }
    }
    
    public static WaitDialog show(Activity activity, CharSequence message) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        me().showType = -1;
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.progressView.loading();
            setMessage(message);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.message = message;
            waitDialog.show(activity);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(int messageResId) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        me().showType = -1;
        if (dialogImpl != null) {
            dialogImpl.progressView.loading();
            setMessage(messageResId);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(messageResId);
            waitDialog.show();
            return waitDialog;
        }
    }
    
    public static WaitDialog show(Activity activity, int messageResId) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        me().showType = -1;
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            dialogImpl.progressView.loading();
            setMessage(messageResId);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(messageResId);
            waitDialog.show(activity);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(CharSequence message, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        me().preMessage(message);
        if (dialogImpl != null) {
            setMessage(message);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(message);
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(Activity activity, CharSequence message, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        me().preMessage(message);
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            setMessage(message);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(message);
            waitDialog.show(activity);
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(int messageResId, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        me().preMessage(messageResId);
        if (dialogImpl != null) {
            setMessage(messageResId);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(messageResId);
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(Activity activity, int messageResId, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        me().preMessage(messageResId);
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            setMessage(messageResId);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(messageResId);
            waitDialog.show(activity);
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(Activity activity, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        if (dialogImpl != null && dialogImpl.bkg.getContext() == activity) {
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.show(activity);
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        if (dialogImpl != null) {
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public float getProgress() {
        return waitProgress;
    }
    
    public WaitDialog setProgress(float waitProgress) {
        this.waitProgress = waitProgress;
        refreshUI();
        return this;
    }
    
    private View dialogView;
    
    public WaitDialog show() {
        super.beforeShow();
        int layoutResId = R.layout.layout_dialogx_wait;
        if (style.overrideWaitTipRes() != null && style.overrideWaitTipRes().overrideWaitLayout(isLightTheme()) != 0) {
            layoutResId = style.overrideWaitTipRes().overrideWaitLayout(isLightTheme());
        }
        dialogImpl = new DialogImpl(layoutResId);
        runOnMain(new Runnable() {
            @Override
            public void run() {
                dialogImpl.lazyCreate();
                dialogView.setTag(me.get());
                show(dialogView);
            }
        });
        return this;
    }
    
    public WaitDialog show(final Activity activity) {
        super.beforeShow();
        int layoutResId = R.layout.layout_dialogx_wait;
        if (style.overrideWaitTipRes() != null && style.overrideWaitTipRes().overrideWaitLayout(isLightTheme()) != 0) {
            layoutResId = style.overrideWaitTipRes().overrideWaitLayout(isLightTheme());
        }
        dialogImpl = new DialogImpl(layoutResId);
        runOnMain(new Runnable() {
            @Override
            public void run() {
                dialogImpl.lazyCreate();
                dialogView.setTag(me.get());
                show(activity, dialogView);
            }
        });
        return this;
    }
    
    protected DialogImpl dialogImpl;
    
    public class DialogImpl implements DialogConvertViewInterface {
        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout bkg;
        public BlurView blurView;
        public RelativeLayout boxProgress;
        public ProgressViewInterface progressView;
        public RelativeLayout boxCustomView;
        public TextView txtInfo;
        
        private int layoutResId;
        
        public DialogImpl(int layoutResId) {
            this.layoutResId = layoutResId;
        }
        
        public void lazyCreate(){
            dialogView = createView(layoutResId);
            boxRoot = dialogView.findViewById(R.id.box_root);
            bkg = dialogView.findViewById(R.id.bkg);
            blurView = dialogView.findViewById(R.id.blurView);
            boxProgress = dialogView.findViewById(R.id.box_progress);
            View progressViewCache = (View) style.overrideWaitTipRes().overrideWaitView(getContext(), isLightTheme());
            if (progressViewCache == null) {
                progressViewCache = new ProgressView(getContext());
            }
            progressView = (ProgressViewInterface) progressViewCache;
            boxProgress.addView(progressViewCache, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            boxCustomView = dialogView.findViewById(R.id.box_customView);
            txtInfo = dialogView.findViewById(R.id.txt_info);
            init();
            dialogImpl = this;
            refreshView();
        }
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            blurView = convertView.findViewById(R.id.blurView);
            boxProgress = convertView.findViewById(R.id.box_progress);
            View progressViewCache = (View) style.overrideWaitTipRes().overrideWaitView(getContext(), isLightTheme());
            if (progressViewCache == null) {
                progressViewCache = new ProgressView(getContext());
            }
            progressView = (ProgressViewInterface) progressViewCache;
            boxProgress.addView(progressViewCache, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            boxCustomView = convertView.findViewById(R.id.box_customView);
            txtInfo = convertView.findViewById(R.id.txt_info);
            init();
            dialogImpl = this;
            refreshView();
        }
        
        public void init() {
            if (messageTextInfo == null) messageTextInfo = DialogX.tipTextInfo;
            if (backgroundColor == -1) backgroundColor = DialogX.tipBackgroundColor;
            
            if (style.overrideWaitTipRes() == null) {
                blurView.setRadiusPx(dip2px(15));
            } else {
                blurView.setRadiusPx(style.overrideWaitTipRes().overrideRadiusPx() < 0 ? dip2px(15) : style.overrideWaitTipRes().overrideRadiusPx());
            }
            boxRoot.setClickable(true);
            
            boxRoot.setParentDialog(me.get());
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            int enterAnimResId = R.anim.anim_dialogx_default_enter;
                            if (overrideEnterAnimRes != 0) {
                                enterAnimResId = overrideEnterAnimRes;
                            }
                            Animation enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                            long enterAnimDurationTemp = enterAnim.getDuration();
                            enterAnim.setInterpolator(new DecelerateInterpolator());
                            if (overrideEnterDuration >= 0) {
                                enterAnimDurationTemp = overrideEnterDuration;
                            }
                            if (enterAnimDuration >= 0) {
                                enterAnimDurationTemp = enterAnimDuration;
                            }
                            enterAnim.setDuration(enterAnimDurationTemp);
                            bkg.startAnimation(enterAnim);
                            
                            boxRoot.animate()
                                    .setDuration(enterAnimDurationTemp)
                                    .alpha(1f)
                                    .setInterpolator(new DecelerateInterpolator())
                                    .setListener(null);
                            
                            getDialogLifecycleCallback().onShow(me());
                        }
                    });
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    dialogImpl = null;
                    getDialogLifecycleCallback().onDismiss(me());
                    me.clear();
                }
            });
            
            if (readyTipType != null) {
                progressView.noLoading();
                ((View) progressView).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTip(readyTipType);
                    }
                }, 100);
            }
            
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
        }
        
        private float oldProgress;
        
        public void refreshView() {
            if (style.overrideWaitTipRes() != null) {
                int overrideBackgroundColorRes = style.overrideWaitTipRes().overrideBackgroundColorRes(isLightTheme());
                if (overrideBackgroundColorRes == 0) {
                    overrideBackgroundColorRes = isLightTheme() ? R.color.dialogxWaitBkgDark : R.color.dialogxWaitBkgLight;
                }
                blurView.setOverlayColor(backgroundColor == -1 ? getResources().getColor(overrideBackgroundColorRes) : backgroundColor);
                int overrideTextColorRes = style.overrideWaitTipRes().overrideTextColorRes(isLightTheme());
                if (overrideTextColorRes == 0) {
                    overrideTextColorRes = isLightTheme() ? R.color.white : R.color.black;
                }
                txtInfo.setTextColor(getResources().getColor(overrideTextColorRes));
                progressView.setColor(getResources().getColor(overrideTextColorRes));
                blurView.setUseBlur(style.overrideWaitTipRes().blurBackground());
            } else {
                if (isLightTheme()) {
                    blurView.setOverlayColor(backgroundColor == -1 ? getResources().getColor(R.color.dialogxWaitBkgDark) : backgroundColor);
                    progressView.setColor(Color.WHITE);
                    txtInfo.setTextColor(Color.WHITE);
                } else {
                    blurView.setOverlayColor(backgroundColor == -1 ? getResources().getColor(R.color.dialogxWaitBkgLight) : backgroundColor);
                    progressView.setColor(Color.BLACK);
                    txtInfo.setTextColor(Color.BLACK);
                }
            }
            if (DialogX.tipProgressColor != -1) progressView.setColor(DialogX.tipProgressColor);
            
            if (waitProgress >= 0 && waitProgress <= 1 && oldProgress != waitProgress) {
                progressView.progress(waitProgress);
                oldProgress = waitProgress;
            }
            
            showText(txtInfo, message);
            useTextInfo(txtInfo, messageTextInfo);
            
            if (maskColor != -1) boxRoot.setBackgroundColor(maskColor);
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustomView, me.get());
                boxCustomView.setVisibility(View.VISIBLE);
                boxProgress.setVisibility(View.GONE);
            } else {
                boxCustomView.setVisibility(View.GONE);
                boxProgress.setVisibility(View.VISIBLE);
            }
        }
        
        public void doDismiss(final View v) {
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    if (v != null) v.setEnabled(false);
                    
                    int exitAnimResId = R.anim.anim_dialogx_default_exit;
                    if (overrideExitAnimRes != 0) {
                        exitAnimResId = overrideExitAnimRes;
                    }
                    Animation exitAnim = AnimationUtils.loadAnimation(getContext(), exitAnimResId);
                    long exitAnimDurationTemp = exitAnim.getDuration();
                    if (overrideExitDuration >= 0) {
                        exitAnimDurationTemp = overrideExitDuration;
                    }
                    if (exitAnimDuration != -1) {
                        exitAnimDurationTemp = exitAnimDuration;
                    }
                    exitAnim.setDuration(exitAnimDurationTemp);
                    exitAnim.setInterpolator(new AccelerateInterpolator());
                    bkg.startAnimation(exitAnim);
                    
                    boxRoot.animate()
                            .alpha(0f)
                            .setInterpolator(new AccelerateInterpolator())
                            .setDuration(exitAnimDurationTemp);
                    
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss(dialogView);
                        }
                    }, exitAnimDurationTemp);
                }
            });
        }
        
        public void showTip(final TYPE tip) {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    showType = tip.ordinal();
                    if (progressView == null) return;
                    switch (tip) {
                        case NONE:
                            progressView.loading();
                            return;
                        case SUCCESS:
                            progressView.success();
                            break;
                        case WARNING:
                            progressView.warning();
                            break;
                        case ERROR:
                            progressView.error();
                            break;
                    }
                    
                    //此事件是在完成衔接动画绘制后执行的逻辑
                    progressView.whenShowTick(new Runnable() {
                        @Override
                        public void run() {
                            getDialogLifecycleCallback().onShow(WaitDialog.this);
                            refreshView();
                            ((View) progressView).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (showType > -1) {
                                        doDismiss(null);
                                    }
                                }
                            }, tipShowDuration);
                        }
                    });
                }
            });
        }
    }
    
    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }
    
    @Override
    public boolean isLightTheme() {
        if (DialogX.tipTheme == null) {
            return super.isLightTheme();
        } else {
            return DialogX.tipTheme == DialogX.THEME.LIGHT;
        }
    }
    
    public void refreshUI() {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl != null) dialogImpl.refreshView();
            }
        });
    }
    
    public void doDismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    public static void dismiss() {
        me().doDismiss();
    }
    
    protected static WaitDialog me() {
        if (me == null || me.get() == null) me = new WeakReference<>(new WaitDialog());
        return me.get();
    }
    
    protected TYPE readyTipType;
    
    protected void showTip(CharSequence message, TYPE type) {
        showType = type.ordinal();
        this.message = message;
        readyTipType = type;
        show();
    }
    
    protected void showTip(Activity activity, CharSequence message, TYPE type) {
        showType = type.ordinal();
        this.message = message;
        readyTipType = type;
        show(activity);
    }
    
    protected void showTip(int messageResId, TYPE type) {
        showType = type.ordinal();
        this.message = getString(messageResId);
        readyTipType = type;
        show();
    }
    
    protected void showTip(Activity activity, int messageResId, TYPE type) {
        showType = type.ordinal();
        this.message = getString(messageResId);
        readyTipType = type;
        show(activity);
    }
    
    public static CharSequence getMessage() {
        return me().message;
    }
    
    public static WaitDialog setMessage(CharSequence message) {
        me().preMessage(message);
        me().refreshUI();
        return me();
    }
    
    public static WaitDialog setMessage(int messageResId) {
        me().preMessage(messageResId);
        me().refreshUI();
        return me();
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
    
    public WaitDialog setCancelable(boolean cancelable) {
        privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        return this;
    }
    
    /**
     * 用于从 WaitDialog 到 TipDialog 的消息设置
     * 此方法不会立即执行，而是等到动画衔接完成后由事件设置
     *
     * @param message 消息
     * @return me
     */
    protected WaitDialog preMessage(CharSequence message) {
        me().message = message;
        return me();
    }
    
    protected WaitDialog preMessage(int messageResId) {
        me().message = getString(messageResId);
        return me();
    }
    
    public DialogLifecycleCallback<WaitDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<WaitDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public WaitDialog setDialogLifecycleCallback(DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me.get());
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public WaitDialog setCustomView(OnBindView<WaitDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public WaitDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public WaitDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public WaitDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }
    
    public WaitDialog setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }
    
    public WaitDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }
    
    public WaitDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }
    
    public long getExitAnimDuration() {
        return exitAnimDuration;
    }
    
    public WaitDialog setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }
    
    @Override
    public void onUIModeChange(Configuration newConfig) {
        refreshUI();
    }
    
    public static WaitDialog getInstance() {
        return me();
    }
}
