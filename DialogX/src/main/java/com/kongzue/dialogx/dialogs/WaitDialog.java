package com.kongzue.dialogx.dialogs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.BlurViewType;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;
import com.kongzue.dialogx.util.views.ProgressView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    protected boolean bkgInterceptTouch = true;
    protected OnBindView<WaitDialog> onBindView;
    protected int customEnterAnimResId;
    protected int customExitAnimResId;
    protected float backgroundRadius = DialogX.defaultWaitAndTipDialogBackgroundRadius;
    protected DialogXAnimInterface<WaitDialog> dialogXAnimImpl;
    protected OnBackPressedListener<WaitDialog> onBackPressedListener;

    public enum TYPE {
        /**
         * @deprecated NONE 和 PROGRESSING 不建议使用，禁止使用此方法。
         * 此类型等同于直接使用 WaitDialog，因此请勿使用 TipDialog 并指定使用 TYPE.NONE，
         * 如有需要，请直接使用： WaitDialog.show(...)
         * <p>
         * 要是用进度，请直接使用 WaitDialog.show(float)
         */
        @Deprecated NONE, SUCCESS, WARNING, ERROR, @Deprecated PROGRESSING
    }

    protected CharSequence message;
    protected long tipShowDuration = 1500;
    protected float waitProgress = -1;
    protected int showType = -1;        //-1:WaitDialog 状态标示符，其余为 TipDialog 状态标示
    protected TextInfo messageTextInfo;
    protected Integer maskColor = null;
    protected BOOLEAN privateCancelable;

    protected DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback;
    protected OnBackgroundMaskClickListener<WaitDialog> onBackgroundMaskClickListener;

    protected WaitDialog() {
        super();
        ownActivity = new WeakReference<>(getTopActivity());
        cancelable = DialogX.cancelableTipDialog;
    }

    protected static WaitDialog instanceBuild() {
        return new WaitDialog();
    }

    public static WaitDialog build() {
        return new WaitDialog();
    }

    public static WaitDialog showWaitWithDefaultText() {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(null, TYPE.NONE);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    protected static WaitDialog showTipWithDefaultText(TYPE tip) {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(null, tip);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(CharSequence message) {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(message, TYPE.NONE);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(Activity activity, CharSequence message) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(message, TYPE.NONE);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(int messageResId) {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(messageResId, TYPE.NONE);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(Activity activity, int messageResId) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(messageResId, TYPE.NONE);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(CharSequence message, float progress) {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(message, TYPE.PROGRESSING);
            dialog.setProgress(progress);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(Activity activity, CharSequence message, float progress) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(message, TYPE.PROGRESSING);
            dialog.setProgress(progress);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(int messageResId, float progress) {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(messageResId, TYPE.PROGRESSING);
            dialog.setProgress(progress);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(Activity activity, int messageResId, float progress) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(messageResId, TYPE.PROGRESSING);
            dialog.setProgress(progress);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(Activity activity, float progress) {
        WaitDialog dialog = getInstance(activity);
        if (dialog != null) {
            dialog.setTip(TYPE.PROGRESSING);
            dialog.setProgress(progress);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
        }
    }

    public static WaitDialog show(float progress) {
        WaitDialog dialog = getInstance();
        if (dialog != null) {
            dialog.setTip(TYPE.PROGRESSING);
            dialog.setProgress(progress);
            if (dialog.getDialogImpl() == null) {
                dialog.show();
            } else {
                dialog.cancelDelayDismissTimer();
            }
            return dialog;
        } else {
            return instanceBuild();
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

    private WeakReference<View> dialogView;

    protected View getWaitDialogView() {
        if (dialogView == null) return null;
        return dialogView.get();
    }

    protected void setWaitDialogView(View v) {
        dialogView = new WeakReference<>(v);
        setDialogView(v);
    }

    public WaitDialog show() {
        super.beforeShow();
        runOnMain(new Runnable() {
            @Override
            public void run() {
                int layoutResId = R.layout.layout_dialogx_wait;
                if (style.overrideWaitTipRes() != null && style.overrideWaitTipRes().overrideWaitLayout(isLightTheme()) != 0) {
                    layoutResId = style.overrideWaitTipRes().overrideWaitLayout(isLightTheme());
                }
                dialogImpl = new WeakReference<>(new DialogImpl(layoutResId));
                if (getDialogImpl() != null) {
                    getDialogImpl().lazyCreate();
                    if (getWaitDialogView() != null) {
                        getWaitDialogView().setTag(WaitDialog.this);
                        show(getWaitDialogView());
                    }
                }
            }
        });
        return this;
    }

    public WaitDialog show(final Activity activity) {
        super.beforeShow();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int layoutResId = R.layout.layout_dialogx_wait;
                if (style.overrideWaitTipRes() != null && style.overrideWaitTipRes().overrideWaitLayout(isLightTheme()) != 0) {
                    layoutResId = style.overrideWaitTipRes().overrideWaitLayout(isLightTheme());
                }
                dialogImpl = new WeakReference<>(new DialogImpl(layoutResId));
                if (getDialogImpl() != null) {
                    getDialogImpl().lazyCreate();
                    if (getWaitDialogView() != null) {
                        getWaitDialogView().setTag(WaitDialog.this);
                        show(activity, getWaitDialogView());
                    }
                }
            }
        });
        return this;
    }

    protected WeakReference<DialogImpl> dialogImpl;

    public class DialogImpl implements DialogConvertViewInterface {

        private List<View> blurViews;

        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout bkg;
        public RelativeLayout boxProgress;
        public ProgressViewInterface progressView;
        public RelativeLayout boxCustomView;
        public TextView txtInfo;

        private int layoutResId;

        public DialogImpl(int layoutResId) {
            this.layoutResId = getCustomDialogLayoutResId(isLightTheme()) != 0 ? getCustomDialogLayoutResId(isLightTheme()) : layoutResId;
        }

        public void lazyCreate() {
            View dialogView = createView(layoutResId);
            if (dialogView == null) return;
            setWaitDialogView(dialogView);
            boxRoot = dialogView.findViewById(R.id.box_root);
            bkg = dialogView.findViewById(R.id.bkg);
            boxProgress = dialogView.findViewById(R.id.box_progress);
            View progressViewCache = (View) style.overrideWaitTipRes().overrideWaitView(getOwnActivity(), isLightTheme());
            if (progressViewCache == null) {
                progressViewCache = new ProgressView(getOwnActivity());
            }
            progressView = (ProgressViewInterface) progressViewCache;
            boxProgress.addView(progressViewCache, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            boxCustomView = dialogView.findViewById(R.id.box_customView);
            txtInfo = dialogView.findViewById(R.id.txt_info);

            blurViews = findAllBlurView(dialogView);

            init();
            setDialogImpl(this);
            refreshView();
        }

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            boxProgress = convertView.findViewById(R.id.box_progress);
            View progressViewCache = (View) style.overrideWaitTipRes().overrideWaitView(getOwnActivity(), isLightTheme());
            if (progressViewCache == null) {
                progressViewCache = new ProgressView(getOwnActivity());
            }
            progressView = (ProgressViewInterface) progressViewCache;
            boxProgress.addView(progressViewCache, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            boxCustomView = convertView.findViewById(R.id.box_customView);
            txtInfo = convertView.findViewById(R.id.txt_info);
            init();
            setDialogImpl(this);
            refreshView();
        }

        public void init() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getDialogView() != null) {
                getDialogView().setTranslationZ(getThisOrderIndex());
            }

            if (messageTextInfo == null) messageTextInfo = DialogX.tipTextInfo;
            if (backgroundColor == null) backgroundColor = DialogX.tipBackgroundColor;

            blurViews = findAllBlurView(dialogView.get());

            Integer blurFrontColor = getColor(isLightTheme() ? R.color.dialogxWaitBkgDark : R.color.dialogxWaitBkgLight);
            Float dialogXRadius = (float) dip2px(15);
            if (style.overrideWaitTipRes() != null) {
                dialogXRadius = getFloatStyleAttr((float) style.overrideWaitTipRes().overrideRadiusPx(), dialogXRadius);
                blurFrontColor = getColorNullable(getIntStyleAttr(style.overrideWaitTipRes().overrideBackgroundColorRes(isLightTheme()), isLightTheme() ? R.color.dialogxWaitBkgDark : R.color.dialogxWaitBkgLight), blurFrontColor);
            }
            if (blurViews != null) {
                for (View blurView : blurViews) {
                    ((BlurViewType) blurView).setOverlayColor(backgroundColor == null ? blurFrontColor : backgroundColor);
                    ((BlurViewType) blurView).setRadiusPx(dialogXRadius);
                }
            } else {
                GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rect_dialogx_material_wait_bkg);
                gradientDrawable.setColor(blurFrontColor);
                gradientDrawable.setCornerRadius(dialogXRadius);
                bkg.setBackground(gradientDrawable);
            }
            boxRoot.setClickable(true);

            boxRoot.setParentDialog(WaitDialog.this);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;
                    setLifecycleState(Lifecycle.State.CREATED);
                    boxRoot.setAlpha(0f);
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            if (getOwnActivity() == null) return;

                            getDialogXAnimImpl().doShowAnim(WaitDialog.this, bkg);

                            onDialogShow();
                            getDialogLifecycleCallback().onShow(WaitDialog.this);
                            setLifecycleState(Lifecycle.State.RESUMED);
                        }
                    });
                }

                @Override
                public void onDismiss() {
                    cleanInstance();
                }
            });

            if (readyTipType != null && readyTipType != TYPE.NONE) {
                progressView.noLoading();
                ((View) progressView).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTip(readyTipType);
                    }
                }, 100);
            }

            boxRoot.setOnBackPressedListener(new DialogXBaseRelativeLayout.PrivateBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null) {
                        if (onBackPressedListener.onBackPressed(WaitDialog.this)) {
                            dismiss();
                        }
                    } else {
                        if (isCancelable()) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
            onDialogInit();
        }

        private float oldProgress = -1;

        public void refreshView() {
            if (boxRoot == null || getOwnActivity() == null) {
                return;
            }
            boxRoot.setAutoUnsafePlacePadding(isEnableImmersiveMode());
            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);

            bkg.setMaxWidth(getMaxWidth());
            bkg.setMaxHeight(getMaxHeight());
            bkg.setMinWidth(getMinWidth());
            bkg.setMinHeight(getMinHeight());

            if (backgroundColor != null) {
                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setOverlayColor(backgroundColor);
                    }
                } else {
                    GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rect_dialogx_material_wait_bkg);
                    gradientDrawable.setColor(getBackgroundColor());
                    gradientDrawable.setCornerRadius(getRadius());
                    bkg.setBackground(gradientDrawable);
                }
            }
            if (style.overrideWaitTipRes() != null) {
                int overrideTextColorRes = getIntStyleAttr(style.overrideWaitTipRes().overrideTextColorRes(isLightTheme()), isLightTheme() ? R.color.white : R.color.black);
                txtInfo.setTextColor(getResources().getColor(overrideTextColorRes));
                progressView.setColor(getResources().getColor(overrideTextColorRes));
            } else {
                int overrideTextColorRes = isLightTheme() ? R.color.white : R.color.black;
                txtInfo.setTextColor(getResources().getColor(overrideTextColorRes));
                progressView.setColor(getResources().getColor(overrideTextColorRes));
            }
            if (DialogX.tipProgressColor != null) progressView.setColor(DialogX.tipProgressColor);

            if (waitProgress >= 0 && waitProgress <= 1 && oldProgress != waitProgress) {
                progressView.progress(waitProgress);
                oldProgress = waitProgress;
            }
            if (backgroundRadius > -1) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bkg.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), backgroundRadius);
                        }
                    });
                    bkg.setClipToOutline(true);
                }
                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setRadiusPx(backgroundRadius);
                    }
                }
            }

            showText(txtInfo, message == null ? getDefaultTipText(readyTipType) : message);
            useTextInfo(txtInfo, messageTextInfo);

            if (maskColor != null) {
                boxRoot.setBackgroundColor(maskColor);
            }

            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustomView, WaitDialog.this);
                boxCustomView.setVisibility(View.VISIBLE);
                boxProgress.setVisibility(View.GONE);
            } else {
                boxCustomView.setVisibility(View.GONE);
                boxProgress.setVisibility(View.VISIBLE);
            }

            if (bkgInterceptTouch) {
                if (isCancelable()) {
                    boxRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onBackgroundMaskClickListener == null || !onBackgroundMaskClickListener.onClick(WaitDialog.this, v)) {
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
            onDialogRefreshUI();
        }

        private CharSequence getDefaultTipText(TYPE readyTipType) {
            switch (readyTipType) {
                case WARNING:
                    return DialogX.defaultTipDialogWarningText;
                case SUCCESS:
                    return DialogX.defaultTipDialogSuccessText;
                case ERROR:
                    return DialogX.defaultTipDialogErrorText;
                case NONE:
                    return DialogX.defaultWaitDialogWaitingText;
                default:
                    return null;
            }
        }

        public void doDismiss(final View v) {
            if (WaitDialog.this.preDismiss(WaitDialog.this)) {
                return;
            }
            if (boxRoot == null) return;
            if (getOwnActivity() == null) return;

            if (!dismissAnimFlag && boxRoot != null) {
                dismissAnimFlag = true;
                boxRoot.post(new Runnable() {
                    @Override
                    public void run() {
                        if (v != null) v.setEnabled(false);
                        getDialogXAnimImpl().doExitAnim(WaitDialog.this, bkg);

                        runOnMainDelay(new Runnable() {
                            @Override
                            public void run() {
                                if (boxRoot != null) {
                                    boxRoot.setVisibility(View.GONE);
                                }
                                dismiss(getWaitDialogView());
                            }
                        }, getExitAnimationDuration(null));
                    }
                });
            }
        }

        protected DialogXAnimInterface<WaitDialog> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<WaitDialog>() {
                    @Override
                    public void doShowAnim(WaitDialog dialog, ViewGroup dialogBodyView) {
                        int enterAnimResId = R.anim.anim_dialogx_default_enter;
                        if (overrideEnterAnimRes != 0) {
                            enterAnimResId = overrideEnterAnimRes;
                        }
                        if (customEnterAnimResId != 0) {
                            enterAnimResId = customEnterAnimResId;
                        }
                        Animation enterAnim = AnimationUtils.loadAnimation(getOwnActivity(), enterAnimResId);
                        long enterAnimDurationTemp = getEnterAnimationDuration(enterAnim);
                        enterAnim.setInterpolator(new DecelerateInterpolator());
                        enterAnim.setDuration(enterAnimDurationTemp);
                        bkg.startAnimation(enterAnim);

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                        bkgAlpha.setDuration(enterAnimDurationTemp);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                            }
                        });
                        bkgAlpha.start();

                        boxRoot.animate().setDuration(enterAnimDurationTemp).alpha(1f).setInterpolator(new DecelerateInterpolator()).setListener(null);
                    }

                    @Override
                    public void doExitAnim(WaitDialog dialog, ViewGroup dialogBodyView) {
                        Context context = getOwnActivity();
                        if (context == null) context = boxRoot.getContext();
                        if (context == null) return;

                        int exitAnimResId = R.anim.anim_dialogx_default_exit;
                        if (overrideExitAnimRes != 0) {
                            exitAnimResId = overrideExitAnimRes;
                        }
                        if (customExitAnimResId != 0) {
                            exitAnimResId = customExitAnimResId;
                        }
                        Animation exitAnim = AnimationUtils.loadAnimation(context, exitAnimResId);
                        long exitAnimDurationTemp = getExitAnimationDuration(exitAnim);
                        exitAnim.setDuration(exitAnimDurationTemp);
                        exitAnim.setInterpolator(new AccelerateInterpolator());
                        bkg.startAnimation(exitAnim);

                        boxRoot.animate().alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(exitAnimDurationTemp);

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1f, 0f);
                        bkgAlpha.setDuration(exitAnimDurationTemp);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (boxRoot != null) {
                                    boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                                }
                            }
                        });
                        bkgAlpha.start();
                    }
                };
            }
            return dialogXAnimImpl;
        }

        public long getExitAnimationDuration(@Nullable Animation defaultExitAnim) {
            if (defaultExitAnim == null && bkg.getAnimation() != null) {
                defaultExitAnim = bkg.getAnimation();
            }
            long exitAnimDurationTemp = (defaultExitAnim == null || defaultExitAnim.getDuration() == 0) ? 300 : defaultExitAnim.getDuration();
            if (overrideExitDuration >= 0) {
                exitAnimDurationTemp = overrideExitDuration;
            }
            if (exitAnimDuration != -1) {
                exitAnimDurationTemp = exitAnimDuration;
            }
            return exitAnimDurationTemp;
        }

        public long getEnterAnimationDuration(@Nullable Animation defaultEnterAnim) {
            if (defaultEnterAnim == null && bkg.getAnimation() != null) {
                defaultEnterAnim = bkg.getAnimation();
            }
            long enterAnimDurationTemp = (defaultEnterAnim == null || defaultEnterAnim.getDuration() == 0) ? 300 : defaultEnterAnim.getDuration();
            if (overrideEnterDuration >= 0) {
                enterAnimDurationTemp = overrideEnterDuration;
            }
            if (enterAnimDuration >= 0) {
                enterAnimDurationTemp = enterAnimDuration;
            }
            return enterAnimDurationTemp;
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

                    if (boxProgress != null && boxProgress.getVisibility() == View.VISIBLE) {
                        //此事件是在完成衔接动画绘制后执行的逻辑
                        progressView.whenShowTick(new Runnable() {
                            @Override
                            public void run() {
                                getDialogLifecycleCallback().onShow(WaitDialog.this);
                                refreshView();
                                if (tipShowDuration > 0) {
                                    ((View) progressView).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (showType > -1) {
                                                doDismiss(null);
                                            }
                                        }
                                    }, tipShowDuration);
                                }
                            }
                        });
                    } else {
                        getDialogLifecycleCallback().onShow(WaitDialog.this);
                        refreshView();
                        if (tipShowDuration > 0) {
                            runOnMainDelay(new Runnable() {
                                @Override
                                public void run() {
                                    if (showType > -1) {
                                        doDismiss(null);
                                    }
                                }
                            }, tipShowDuration);
                        }
                    }
                }
            });
        }
    }

    public void cleanInstance() {
        isShow = false;
        getDialogLifecycleCallback().onDismiss(WaitDialog.this);
        setLifecycleState(Lifecycle.State.DESTROYED);
        if (dialogImpl != null) dialogImpl.clear();
        dialogImpl = null;
        if (dialogView != null) dialogView.clear();
        dialogView = null;
        dialogLifecycleCallback = null;
        if (ownActivity != null) ownActivity.clear();
        BaseDialog.gc();
    }

    private void setDialogImpl(DialogImpl d) {
        if (dialogImpl != null && dialogImpl.get() != d) dialogImpl = new WeakReference<>(d);
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
        if (getDialogImpl() == null) return;
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (getDialogImpl() != null) getDialogImpl().refreshView();
            }
        });
    }

    public void doDismiss() {
        isShow = false;
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (getDialogImpl() != null) {
                    getDialogImpl().doDismiss(null);
                }
            }
        });
    }

    public static void dismiss() {
        WaitDialog instance = getInstance();
        if (instance != null) {
            instance.doDismiss();
        }
    }

    public static void dismiss(Activity activity) {
        WaitDialog instance = getInstance(activity);
        if (instance != null) instance.doDismiss();
    }

    protected Timer delayDismissTimer;

    protected void cancelDelayDismissTimer() {
        if (delayDismissTimer != null) {
            delayDismissTimer.cancel();
        }
    }

    public static void dismiss(long delayTime) {
        WaitDialog instance = getInstance();
        if (instance != null) {
            instance.doDismiss(delayTime);
        }
    }

    public void doDismiss(long delayTime) {
        cancelDelayDismissTimer();
        delayDismissTimer = new Timer();
        delayDismissTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                doDismiss();
            }
        }, delayTime);
    }

    protected static WaitDialog me() {
        WaitDialog instance = getInstance();
        return instance == null ? instanceBuild() : instance;
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

    protected void showTip(TYPE type) {
        if (readyTipType == type) {
            return;
        }
        showType = type.ordinal();
        readyTipType = type;
        if (getDialogImpl() != null) {
            getDialogImpl().showTip(type);
        }
    }

    protected void setTip(TYPE type) {
        showTip(type);
    }

    protected void setTip(CharSequence message, TYPE type) {
        this.message = message;
        showTip(type);
        refreshUI();
    }

    protected void setTip(int messageResId, TYPE type) {
        this.message = getString(messageResId);
        showTip(type);
        refreshUI();
    }

    protected void setTipShowDuration(long tipShowDuration) {
        this.tipShowDuration = tipShowDuration;
        showTip(readyTipType);
    }

    public static CharSequence getMessage() {
        return me().message;
    }

    public static WaitDialog setMessage(CharSequence message) {
        WaitDialog instance = getInstance();
        if (instance != null) {
            instance.preMessage(message);
            instance.refreshUI();
            return instance;
        }
        return instanceBuild();
    }

    public static WaitDialog setMessage(int messageResId) {
        WaitDialog instance = getInstance();
        if (instance != null) {
            instance.preMessage(messageResId);
            instance.refreshUI();
            return instance;
        }
        return instanceBuild();
    }

    public boolean isCancelable() {
        if (privateCancelable != null) {
            return privateCancelable == BOOLEAN.TRUE;
        }
        if (overrideCancelable != null) {
            return overrideCancelable == BOOLEAN.TRUE;
        }
        return DialogX.cancelableTipDialog;
    }

    public WaitDialog setCancelable(boolean cancelable) {
        privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
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
        this.message = message;
        return this;
    }

    protected WaitDialog preMessage(int messageResId) {
        this.message = getString(messageResId);
        return this;
    }

    public DialogLifecycleCallback<WaitDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<WaitDialog>() {
        } : dialogLifecycleCallback;
    }

    public WaitDialog setDialogLifecycleCallback(DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me());
        return this;
    }

    public DialogImpl getDialogImpl() {
        if (dialogImpl == null) return null;
        return dialogImpl.get();
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

    public OnBackPressedListener<WaitDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<WaitDialog>) onBackPressedListener;
    }

    public WaitDialog setOnBackPressedListener(OnBackPressedListener<WaitDialog> onBackPressedListener) {
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
    public void restartDialog() {
        refreshUI();
    }

    /**
     * 获取当前 WaitDialog 显示状态
     * <p>
     * 值的含义：
     * -1:  WaitDialog 等待状态
     * 0:   TipDialog 无状态（与 WaitDialog 等待状态相同）
     * 1:   TipDialog 完成状态
     * 2:   TipDialog 警告状态
     * 3:   TipDialog 错误状态
     *
     * @return showType
     */
    public static int getType() {
        return me().showType;
    }

    public WaitDialog setAnimResId(int enterResId, int exitResId) {
        customEnterAnimResId = enterResId;
        customExitAnimResId = exitResId;
        return this;
    }

    public WaitDialog setEnterAnimResId(int enterResId) {
        customEnterAnimResId = enterResId;
        return this;
    }

    public WaitDialog setExitAnimResId(int exitResId) {
        customExitAnimResId = exitResId;
        return this;
    }

    public static WaitDialog getInstance() {
        return getInstance(null);
    }

    public static WaitDialog getInstance(Activity activity) {
        if (activity == null) {
            activity = getTopActivity();
        }
        if (isActivityDestroyed(activity)) {
            return null;
        }
        for (BaseDialog baseDialog : getRunningDialogList()) {
            if (baseDialog instanceof WaitDialog) {
                if (baseDialog.isShow() && baseDialog.getOwnActivity() == activity) {
                    return (WaitDialog) baseDialog;
                }
            }
        }
        return new WaitDialog();
    }

    @Override
    protected void shutdown() {
        dismiss();
    }

    public WaitDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public WaitDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public WaitDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public WaitDialog setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public WaitDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public WaitDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<WaitDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }

    public WaitDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<WaitDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public WaitDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public WaitDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public WaitDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }

    public WaitDialog setMessageContent(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }

    public WaitDialog setMessageContent(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }

    public CharSequence getMessageContent() {
        return message;
    }

    public WaitDialog setTipType(TYPE type) {
        showTip(type);
        return this;
    }

    public WaitDialog setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius < 0 ? dip2px(15) : backgroundRadius;
    }

    public DialogXAnimInterface<WaitDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public WaitDialog setDialogXAnimImpl(DialogXAnimInterface<WaitDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public WaitDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public WaitDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public WaitDialog onShow(DialogXRunnable<WaitDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public WaitDialog onDismiss(DialogXRunnable<WaitDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public WaitDialog setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public WaitDialog setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public WaitDialog appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public WaitDialog setThisOrderIndex(int orderIndex) {
        this.thisOrderIndex = orderIndex;
        if (getDialogView() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(orderIndex);
            } else {
                error("DialogX: " + dialogKey() + " 执行 .setThisOrderIndex(" + orderIndex + ") 失败：系统不支持此方法，SDK-API 版本必须大于 21（LOLLIPOP）");
            }
        }
        return this;
    }

    public WaitDialog bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public WaitDialog setActionRunnable(int actionId, DialogXRunnable<WaitDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public WaitDialog cleanAction(int actionId){
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public WaitDialog cleanAllAction(){
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss(){
        dismiss();
    }

    public WaitDialog bindDismissWithLifecycleOwner(LifecycleOwner owner){
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }

    public WaitDialog setCustomDialogLayoutResId(int customDialogLayoutId) {
        this.customDialogLayoutResId[0] = customDialogLayoutId;
        this.customDialogLayoutResId[1] = customDialogLayoutId;
        return this;
    }

    public WaitDialog setCustomDialogLayoutResId(int customDialogLayoutId, boolean isLightTheme) {
        this.customDialogLayoutResId[isLightTheme ? 0 : 1] = customDialogLayoutId;
        return this;
    }
}
