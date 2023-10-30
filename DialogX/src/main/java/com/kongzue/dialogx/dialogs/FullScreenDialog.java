package com.kongzue.dialogx.dialogs;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.lifecycle.Lifecycle;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXBaseBottomDialog;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;
import com.kongzue.dialogx.interfaces.ScrollController;
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
public class FullScreenDialog extends BaseDialog implements DialogXBaseBottomDialog {

    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static BOOLEAN overrideCancelable;

    protected OnBindView<FullScreenDialog> onBindView;
    protected OnBackPressedListener<FullScreenDialog> onBackPressedListener;
    protected BOOLEAN privateCancelable;
    protected boolean hideZoomBackground;
    protected float backgroundRadius = -1;
    protected boolean allowInterceptTouch = true;
    protected DialogXAnimInterface<FullScreenDialog> dialogXAnimImpl;
    protected boolean bottomNonSafetyAreaBySelf = false;
    protected boolean hideActivityContentView;
    protected Integer maskColor = null;

    protected DialogLifecycleCallback<FullScreenDialog> dialogLifecycleCallback;
    protected OnBackgroundMaskClickListener<FullScreenDialog> onBackgroundMaskClickListener;

    protected FullScreenDialog me = this;

    protected FullScreenDialog() {
        super();
    }

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
        FullScreenDialog FullScreenDialog = new FullScreenDialog(onBindView);
        FullScreenDialog.show();
        return FullScreenDialog;
    }

    public FullScreenDialog show() {
        if (isHide && getDialogView() != null && isShow) {
            if (hideWithExitAnim && getDialogImpl() != null) {
                getDialogView().setVisibility(View.VISIBLE);
                getDialogImpl().getDialogXAnimImpl().doShowAnim(me, getDialogImpl().bkg);
            } else {
                getDialogView().setVisibility(View.VISIBLE);
            }
            return this;
        }
        super.beforeShow();
        if (getDialogView() == null) {
            View dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
            show(dialogView);
        } else {
            show(getDialogView());
        }
        return this;
    }

    public void show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            View dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
            show(activity, dialogView);
        } else {
            show(activity, getDialogView());
        }
    }

    protected DialogImpl dialogImpl;

    public class DialogImpl implements DialogConvertViewInterface {

        private FullScreenDialogTouchEventInterceptor fullScreenDialogTouchEventInterceptor;

        public ActivityScreenShotImageView imgZoomActivity;
        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxBkg;
        public MaxRelativeLayout bkg;
        public RelativeLayout boxCustom;
        public ScrollController scrollView;

        public DialogImpl setScrollView(ScrollController scrollView) {
            this.scrollView = scrollView;
            return this;
        }

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            imgZoomActivity = convertView.findViewById(R.id.img_zoom_activity);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            bkg = convertView.findViewById(R.id.bkg);
            boxCustom = convertView.findViewById(R.id.box_custom);
            imgZoomActivity.hideActivityContentView = hideActivityContentView;

            imgZoomActivity.bindDialog(FullScreenDialog.this);

            if (hideZoomBackground) {
                convertView.setBackgroundResource(R.color.black20);
                imgZoomActivity.setVisibility(View.GONE);
            } else {
                convertView.setBackgroundResource(R.color.black);
                imgZoomActivity.setVisibility(View.VISIBLE);
            }
            init();
            dialogImpl = this;
            refreshView();
        }

        public float bkgEnterAimY = -1;
        protected int enterY;

        private Rect mUnsafeRect = new Rect(0, 0, 0, 0);

        public float getEnterY() {
            return Math.max(0, boxRoot.getSafeHeight() - enterY);
        }

        @Override
        public void init() {
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;

                    setLifecycleState(Lifecycle.State.CREATED);
                    onDialogShow();

                    getDialogLifecycleCallback().onShow(me);
                    FullScreenDialog.this.onShow(me);
                }

                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    FullScreenDialog.this.onDismiss(me);
                    fullScreenDialogTouchEventInterceptor = null;
                    dialogImpl = null;
                    dialogLifecycleCallback = null;
                    setLifecycleState(Lifecycle.State.DESTROYED);
                    System.gc();
                }
            });

            boxRoot.setOnBackPressedListener(new DialogXBaseRelativeLayout.PrivateBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null) {
                        if (onBackPressedListener.onBackPressed(me)) {
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

            fullScreenDialogTouchEventInterceptor = new FullScreenDialogTouchEventInterceptor(me, dialogImpl);
            boxRoot.setBkgAlpha(0f);

            bkg.setY(boxRoot.getHeight());
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    getDialogXAnimImpl().doShowAnim(me, bkg);
                    setLifecycleState(Lifecycle.State.RESUMED);
                }
            });
            boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
                @Override
                public void onChange(Rect unsafeRect) {
                    mUnsafeRect.set(unsafeRect);
                    makeEnterY();
                    if (!enterAnimRunning) {
                        bkg.setY(getEnterY());
                    }
                }
            });

            bkg.setOnYChanged(new MaxRelativeLayout.OnYChanged() {
                @Override
                public void y(float y) {
                    float realY = y + bkg.getTop();
                    float zoomScale = 1 - (boxRoot.getHeight() - realY) * 0.00002f;
                    if (zoomScale > 1) zoomScale = 1;
                    if (!hideZoomBackground) {
                        imgZoomActivity.setScale(zoomScale);
                        imgZoomActivity.setRadius(dip2px(15) * ((boxRoot.getHeight() - realY) / boxRoot.getHeight()));
                    }
                }
            });

            /**
             * 给自定义布局增加监听，如果布局高度发生改变，则重新计算位置，位置发生变化，则再次使用动画移动布局到指定位置
             * 目的是给自定义布局高度为wrap_content的用于纠正布局的Y轴位置
             */
            boxCustom.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    int oldHeight = oldBottom - oldTop;
                    int newHeight = bottom - top;
                    if (oldHeight != newHeight) {
                        /**
                         * 高度发生改变
                         * 这里判断是否在两种动画途中
                         */
                        if (!enterAnimRunning && !boxRoot.getFitSystemBarUtils().isInSmoothingPadding()) {
                            makeEnterY();
                            float newBkgEnterAimY = boxRoot.getSafeHeight() - mUnsafeRect.bottom - enterY - boxRoot.getUnsafePlace().top;
                            if (newBkgEnterAimY < 0) newBkgEnterAimY = 0;
                            if (newBkgEnterAimY != bkgEnterAimY && bkg.getY() != newBkgEnterAimY) {
                                float oldVal = bkgEnterAimY;
                                bkgEnterAimY = newBkgEnterAimY;
                                //需要重新定义终点
                                doShowAnimRepeat((int) oldVal, (int) newBkgEnterAimY, true);
                            } else if (bkg.getY() != newBkgEnterAimY) {
                                bkg.setY(newBkgEnterAimY);
                            }
                        }
                    }
                }
            });
            onDialogInit();
        }

        private boolean isMatchParentHeightCustomView() {
            if (onBindView != null && onBindView.getCustomView() != null) {
                ViewGroup.LayoutParams lp = onBindView.getCustomView().getLayoutParams();
                if (lp != null) {
                    return lp.height == MATCH_PARENT;
                }
            }
            return false;
        }

        private void makeEnterY() {
            int customViewHeight = boxCustom.getHeight();

            if (customViewHeight == 0 || isMatchParentHeightCustomView()) {
                customViewHeight = ((int) boxRoot.getSafeHeight());
            }
            enterY = customViewHeight;
        }

        @Override
        public void refreshView() {
            if (boxRoot == null || getOwnActivity() == null) {
                return;
            }
            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);
            if (backgroundColor != null) {
                tintColor(bkg, backgroundColor);
            }

            bkg.setMaxWidth(getMaxWidth());
            bkg.setMaxHeight(getMaxHeight());
            bkg.setMinimumWidth(getMinWidth());
            bkg.setMinimumHeight(getMinHeight());

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
            if (backgroundRadius > -1) {
                GradientDrawable gradientDrawable = (GradientDrawable) bkg.getBackground();
                if (gradientDrawable != null) gradientDrawable.setCornerRadii(new float[]{
                        backgroundRadius, backgroundRadius, backgroundRadius, backgroundRadius, 0, 0, 0, 0
                });
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bkg.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, view.getWidth(), (int) (view.getHeight() + backgroundRadius), backgroundRadius);
                        }
                    });
                    bkg.setClipToOutline(true);
                }
            }
            if (maskColor != null) {
                boxRoot.setBackgroundColor(maskColor);
            }

            if (onBindView != null) {
                onBindView.bindParent(boxCustom, me);
                if (onBindView.getCustomView() instanceof ScrollController) {
                    scrollView = (ScrollController) onBindView.getCustomView();
                } else {
                    View scrollController = onBindView.getCustomView().findViewWithTag("ScrollController");
                    if (scrollController instanceof ScrollController) {
                        scrollView = (ScrollController) scrollController;
                    }
                }
            }

            if (hideZoomBackground) {
                getDialogView().setBackgroundResource(R.color.black20);
                imgZoomActivity.setVisibility(View.GONE);
            } else {
                getDialogView().setBackgroundResource(R.color.black);
                imgZoomActivity.setVisibility(View.VISIBLE);
            }

            fullScreenDialogTouchEventInterceptor.refresh(me, this);

            onDialogRefreshUI();
        }

        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            if (getOwnActivity() == null) return;

            if (!dismissAnimFlag && getDialogXAnimImpl() != null) {
                dismissAnimFlag = true;
                getDialogXAnimImpl().doExitAnim(me, bkg);

                runOnMainDelay(new Runnable() {
                    @Override
                    public void run() {
                        if (boxRoot != null) {
                            boxRoot.setVisibility(View.GONE);
                        }
                        dismiss(getDialogView());
                    }
                }, getExitAnimationDuration());
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

        private boolean enterAnimRunning = true;

        /**
         * 弹窗显示的动画
         * 动画执行途中实时检测终点是否改变，如果改变则中断这次动画重新设置新终点的动画并执行
         *
         * @param start 起点位置
         * @param end   终点位置
         */
        private void doShowAnimRepeat(int start, int end, boolean isRepeat) {
            enterAnimRunning = true;
            long enterAnimDurationTemp = getEnterAnimationDuration();

            ValueAnimator enterAnimVal = ValueAnimator.ofInt(start, end);
            enterAnimVal.setDuration(enterAnimDurationTemp);
            enterAnimVal.setInterpolator(new DecelerateInterpolator());
            enterAnimVal.addUpdateListener(animation -> {
                int thisVal = (int) animation.getAnimatedValue();
                bkg.setY(thisVal);

                makeEnterY();
                float newBkgEnterAimY = boxRoot.getSafeHeight() - enterY;
                if (newBkgEnterAimY < 0) newBkgEnterAimY = 0;
                if (newBkgEnterAimY != bkgEnterAimY) {
                    bkgEnterAimY = newBkgEnterAimY;
                    //需要重新定义终点
                    animation.cancel();
                    doShowAnimRepeat(thisVal, (int) newBkgEnterAimY, true);
                } else if (thisVal >= end) {
                    enterAnimRunning = false;
                }
            });
            enterAnimVal.start();
            bkg.setVisibility(View.VISIBLE);

            if (!isRepeat) {
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
        }

        protected DialogXAnimInterface<FullScreenDialog> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<FullScreenDialog>() {
                    @Override
                    public void doShowAnim(FullScreenDialog dialog, ViewGroup dialogBodyView) {
//                        long enterAnimDurationTemp = getEnterAnimationDuration();
                        makeEnterY();
                        bkgEnterAimY = boxRoot.getSafeHeight() - enterY;
                        if (bkgEnterAimY < 0) bkgEnterAimY = 0;
                        //启动带监控终点位置变化的动画
                        doShowAnimRepeat(boxRoot.getHeight(), (int) bkgEnterAimY, false);
//                        ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", boxRoot.getHeight(), bkgEnterAimY);
//                        enterAnim.setDuration(enterAnimDurationTemp);
//                        enterAnim.setInterpolator(new DecelerateInterpolator());
//                        enterAnim.start();
//                        bkg.setVisibility(View.VISIBLE);
//
//                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
//                        bkgAlpha.setDuration(enterAnimDurationTemp);
//                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                float value = (float) animation.getAnimatedValue();
//                                boxRoot.setBkgAlpha(value);
//                                enterAnimRunning = !(value == 1f);
//                            }
//                        });
//                        bkgAlpha.start();
                    }

                    @Override
                    public void doExitAnim(FullScreenDialog dialog, ViewGroup dialogBodyView) {
                        long exitAnimDurationTemp = getExitAnimationDuration();

                        ObjectAnimator exitAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), boxBkg.getHeight());
                        exitAnim.setDuration(exitAnimDurationTemp);
                        exitAnim.start();

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1f, 0f);
                        bkgAlpha.setDuration(exitAnimDurationTemp);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                boxRoot.setBkgAlpha(value);
                                enterAnimRunning = !(value == 1f);
                            }
                        });
                        bkgAlpha.start();
                    }
                };
            }
            return dialogXAnimImpl;
        }

        public long getExitAnimationDuration() {
            long exitAnimDurationTemp = 300;
            if (overrideExitDuration >= 0) {
                exitAnimDurationTemp = overrideExitDuration;
            }
            if (exitAnimDuration != -1) {
                exitAnimDurationTemp = exitAnimDuration;
            }
            return exitAnimDurationTemp;
        }

        public long getEnterAnimationDuration() {
            long enterAnimDurationTemp = 300;
            if (overrideEnterDuration >= 0) {
                enterAnimDurationTemp = overrideEnterDuration;
            }
            if (enterAnimDuration >= 0) {
                enterAnimDurationTemp = enterAnimDuration;
            }
            return enterAnimDurationTemp;
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

    public DialogLifecycleCallback<FullScreenDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<FullScreenDialog>() {
        } : dialogLifecycleCallback;
    }

    public FullScreenDialog setDialogLifecycleCallback(DialogLifecycleCallback<FullScreenDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public OnBackPressedListener<FullScreenDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<FullScreenDialog>) onBackPressedListener;
    }

    public FullScreenDialog setOnBackPressedListener(OnBackPressedListener<FullScreenDialog> onBackPressedListener) {
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
        if (getDialogView() != null) {
            dismiss(getDialogView());
            isShow = false;
        }
        if (getDialogImpl().boxCustom != null) {
            getDialogImpl().boxCustom.removeAllViews();
        }
        enterAnimDuration = 0;
        View dialogView = createView(isLightTheme() ? R.layout.layout_dialogx_fullscreen : R.layout.layout_dialogx_fullscreen_dark);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
    }

    private boolean isHide;

    public void hide() {
        isHide = true;
        hideWithExitAnim = false;
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
    }

    protected boolean hideWithExitAnim;

    public void hideWithExitAnim() {
        hideWithExitAnim = true;
        isHide = true;
        if (getDialogImpl() != null) {
            getDialogImpl().getDialogXAnimImpl().doExitAnim(me, getDialogImpl().bkg);
            runOnMainDelay(new Runnable() {
                @Override
                public void run() {
                    if (getDialogView() != null) {
                        getDialogView().setVisibility(View.GONE);
                    }
                }
            }, getDialogImpl().getExitAnimationDuration());
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

    public FullScreenDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public FullScreenDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public FullScreenDialog setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public FullScreenDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public OnBackgroundMaskClickListener<FullScreenDialog> getOnBackgroundMaskClickListener() {
        return onBackgroundMaskClickListener;
    }

    public FullScreenDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<FullScreenDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public FullScreenDialog setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public boolean isAllowInterceptTouch() {
        return allowInterceptTouch;
    }

    public FullScreenDialog setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<FullScreenDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public FullScreenDialog setDialogXAnimImpl(DialogXAnimInterface<FullScreenDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public FullScreenDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public FullScreenDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new FullScreenDialog() {
     *
     * @param dialog self
     * @Override public void onShow(FullScreenDialog dialog) {
     * //...
     * }
     * }
     */
    public void onShow(FullScreenDialog dialog) {

    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new FullScreenDialog() {
     *
     * @param dialog self
     * @Override public boolean onDismiss(FullScreenDialog dialog) {
     * WaitDialog.show("Please Wait...");
     * if (dialog.getButtonSelectResult() == BUTTON_SELECT_RESULT.BUTTON_OK) {
     * //点击了OK的情况
     * //...
     * } else {
     * //其他按钮点击、对话框dismiss的情况
     * //...
     * }
     * return false;
     * }
     * }
     */
    //用于使用 new 构建实例时，override 的生命周期事件
    public void onDismiss(FullScreenDialog dialog) {

    }

    public boolean isBottomNonSafetyAreaBySelf() {
        return bottomNonSafetyAreaBySelf;
    }

    public FullScreenDialog setBottomNonSafetyAreaBySelf(boolean bottomNonSafetyAreaBySelf) {
        this.bottomNonSafetyAreaBySelf = bottomNonSafetyAreaBySelf;
        return this;
    }

    /**
     * 是否在显示 FullScreenDialog 时不对 activity 的界面内容进行渲染，这将提升一定的性能
     * 只可以在使用 build 方法构建且在执行show方法之前使用
     * 但这将引发一些问题，例如输入法弹出时 FullScreenDialog 无法上浮等
     *
     * @param hideActivityContentView 是否显示 activity 的界面内容
     * @return this
     */
    public FullScreenDialog hideActivityContentView(boolean hideActivityContentView) {
        this.hideActivityContentView = hideActivityContentView;
        return this;
    }

    public FullScreenDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }
}
