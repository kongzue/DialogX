package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
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
    
    private DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback;
    
    protected WaitDialog() {
        me = new WeakReference<>(this);
    }
    
    public static WaitDialog show(CharSequence message) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        if (dialogImpl != null) {
            setMessage(message);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.message = message;
            waitDialog.show();
            return waitDialog;
        }
    }
    
    public static WaitDialog show(CharSequence message,float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        if (dialogImpl != null) {
            setMessage(message);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.message = message;
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
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
        dialogView = createView(R.layout.layout_dialogx_wait);
        dialogImpl = new DialogImpl(dialogView);
        show(dialogView);
        return this;
    }
    
    protected DialogImpl dialogImpl;
    
    class DialogImpl implements DialogConvertViewInterface {
        DialogXBaseRelativeLayout boxRoot;
        MaxRelativeLayout bkg;
        BlurView blurView;
        RelativeLayout boxProgress;
        ProgressView progressView;
        RelativeLayout boxCustomView;
        TextView txtInfo;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            blurView = convertView.findViewById(R.id.blurView);
            boxProgress = convertView.findViewById(R.id.box_progress);
            progressView = convertView.findViewById(R.id.progressView);
            boxCustomView = convertView.findViewById(R.id.box_customView);
            txtInfo = convertView.findViewById(R.id.txt_info);
            init();
            refreshView();
        }
    
        public void init() {
            blurView.setRadiusPx(dip2px(15));
            boxRoot.setClickable(true);
            //txtInfo.getPaint().setFakeBoldText(true);
            
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            int enterAnimResId = R.anim.anim_dialogx_default_enter;
                            Animation enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                            enterAnim.setInterpolator(new DecelerateInterpolator());
                            bkg.startAnimation(enterAnim);
                            
                            boxRoot.animate().setDuration(enterAnim.getDuration()).alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null);
                            
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
                progressView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTip(readyTipType);
                    }
                }, 100);
            }
        }
        
        private float oldProgress;
    
        public void refreshView() {
            if (style.overrideWaitTipRes() != null) {
                int overrideBackgroundColorRes = style.overrideWaitTipRes().overrideBackgroundColorRes(isLightTheme());
                if (overrideBackgroundColorRes == 0) {
                    overrideBackgroundColorRes = isLightTheme() ? R.color.dialogxWaitBkgDark : R.color.dialogxWaitBkgLight;
                }
                blurView.setOverlayColor(getResources().getColor(overrideBackgroundColorRes));
                int overrideTextColorRes = style.overrideWaitTipRes().overrideTextColorRes(isLightTheme());
                if (overrideTextColorRes == 0) {
                    overrideTextColorRes = isLightTheme() ? R.color.white : R.color.black;
                }
                txtInfo.setTextColor(getResources().getColor(overrideTextColorRes));
                progressView.setColor(getResources().getColor(overrideTextColorRes));
                blurView.setUseBlur(style.overrideWaitTipRes().blurBackground());
            } else {
                if (isLightTheme()) {
                    blurView.setOverlayColor(getResources().getColor(R.color.dialogxWaitBkgDark));
                    progressView.setColor(Color.WHITE);
                    txtInfo.setTextColor(Color.WHITE);
                } else {
                    blurView.setOverlayColor(getResources().getColor(R.color.dialogxWaitBkgLight));
                    progressView.setColor(Color.BLACK);
                    txtInfo.setTextColor(Color.BLACK);
                }
            }
            
            if (waitProgress >= 0 && waitProgress <= 1 && oldProgress != waitProgress) {
                progressView.progress(waitProgress);
                oldProgress = waitProgress;
            }
            
            showText(txtInfo, message);
        }
        
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            
            int exitAnimResId = R.anim.anim_dialogx_default_exit;
            Animation enterAnim = AnimationUtils.loadAnimation(getContext(), exitAnimResId);
            enterAnim.setInterpolator(new AccelerateInterpolator());
            bkg.startAnimation(enterAnim);
            
            boxRoot.animate().setDuration(300).alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(enterAnim.getDuration()).setListener(new AnimatorListenerEndCallBack() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismiss(dialogView);
                }
            });
        }
        
        public void showTip(TYPE tip) {
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
            progressView.whenShowTick(new Runnable() {
                @Override
                public void run() {
                    refreshView();
                    progressView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doDismiss(null);
                        }
                    }, tipShowDuration);
                }
            });
        }
    }
    
    public void refreshUI() {
        if (dialogImpl == null) return;
        dialogImpl.refreshView();
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
        this.message = message;
        readyTipType = type;
        show();
    }
    
    public static CharSequence getMessage() {
        return me().message;
    }
    
    public static WaitDialog setMessage(CharSequence message) {
        me().message = message;
        me().refreshUI();
        return me();
    }
    
    public DialogLifecycleCallback<WaitDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<WaitDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public WaitDialog setDialogLifecycleCallback(DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
}
