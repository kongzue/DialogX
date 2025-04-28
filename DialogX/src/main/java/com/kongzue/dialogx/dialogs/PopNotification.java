package com.kongzue.dialogx.dialogs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
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
import com.kongzue.dialogx.interfaces.NoTouchInterface;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;
import com.kongzue.dialogx.interfaces.PopMoveDisplacementInterceptor;
import com.kongzue.dialogx.util.PopValueAnimator;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/20 11:59
 */
public class PopNotification extends BaseDialog implements NoTouchInterface {

    public static final int TIME_NO_AUTO_DISMISS_DELAY = -1;
    protected static List<PopNotification> popNotificationList;

    public static long overrideEnterDuration = -1;
    public static long overrideExitDuration = -1;
    public static int overrideEnterAnimRes = 0;
    public static int overrideExitAnimRes = 0;

    public static int maxShowCount = Integer.MAX_VALUE;
    public static PopMoveDisplacementInterceptor<PopNotification> moveDisplacementInterceptor;

    protected OnBindView<PopNotification> onBindView;
    protected DialogLifecycleCallback<PopNotification> dialogLifecycleCallback;
    protected PopNotification me = this;
    protected DialogImpl dialogImpl;
    protected int enterAnimResId = 0;
    protected int exitAnimResId = 0;
    protected DialogXStyle.PopNotificationSettings.ALIGN align;
    protected OnDialogButtonClickListener<PopNotification> onButtonClickListener;
    protected OnDialogButtonClickListener<PopNotification> onPopNotificationClickListener;
    protected boolean autoTintIconInLightOrDarkMode = true;
    protected BOOLEAN tintIcon;
    protected float backgroundRadius = DialogX.defaultPopNotificationBackgroundRadius;
    protected DialogXAnimInterface<PopNotification> dialogXAnimImpl;

    protected int iconResId;
    protected Bitmap iconBitmap;
    protected Drawable iconDrawable;
    protected CharSequence title;
    protected CharSequence message;
    protected CharSequence buttonText;
    protected int iconSize;
    protected boolean slideToClose = true;

    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo buttonTextInfo = new TextInfo().setBold(true);
    protected int[] bodyMargin = new int[]{-1, -1, -1, -1};

    protected PopNotification() {
        super();
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public static PopNotification build() {
        return new PopNotification();
    }

    public static PopNotification build(DialogXStyle style) {
        return new PopNotification().setStyle(style);
    }

    public static PopNotification build(OnBindView<PopNotification> onBindView) {
        return new PopNotification().setCustomView(onBindView);
    }

    public PopNotification(OnBindView<PopNotification> onBindView) {
        this.onBindView = onBindView;
    }

    public PopNotification(CharSequence title) {
        this.title = title;
    }

    public PopNotification(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
    }

    public PopNotification(int titleResId) {
        this.title = getString(titleResId);
    }

    public PopNotification(int titleResId, int messageResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }

    public PopNotification(int iconResId, CharSequence title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public PopNotification(int iconResId, CharSequence title, CharSequence message) {
        this.iconResId = iconResId;
        this.title = title;
        this.message = message;
    }

    public PopNotification(int iconResId, int titleResId, int messageResId) {
        this.iconResId = iconResId;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }

    public PopNotification(CharSequence title, OnBindView<PopNotification> onBindView) {
        this.title = title;
        this.onBindView = onBindView;
    }

    public PopNotification(CharSequence title, CharSequence message, OnBindView<PopNotification> onBindView) {
        this.title = title;
        this.message = message;
        this.onBindView = onBindView;
    }

    public PopNotification(int titleResId, OnBindView<PopNotification> onBindView) {
        this.title = getString(titleResId);
        this.onBindView = onBindView;
    }

    public PopNotification(int titleResId, int messageResId, OnBindView<PopNotification> onBindView) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.onBindView = onBindView;
    }

    public PopNotification(int iconResId, CharSequence title, OnBindView<PopNotification> onBindView) {
        this.iconResId = iconResId;
        this.title = title;
        this.onBindView = onBindView;
    }

    public PopNotification(int iconResId, CharSequence title, CharSequence message, OnBindView<PopNotification> onBindView) {
        this.iconResId = iconResId;
        this.title = title;
        this.message = message;
        this.onBindView = onBindView;
    }

    public PopNotification(int iconResId, int titleResId, int messageResId, OnBindView<PopNotification> onBindView) {
        this.iconResId = iconResId;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.onBindView = onBindView;
    }

    public PopNotification(int iconResId, int titleResId, int messageResId, int buttonTextResId) {
        this.iconResId = iconResId;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.buttonText = getString(buttonTextResId);
    }

    public PopNotification(int iconResId, int titleResId, int messageResId, int buttonTextResId, OnBindView<PopNotification> onBindView) {
        this.iconResId = iconResId;
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.buttonText = getString(buttonTextResId);
        this.onBindView = onBindView;
    }

    public PopNotification(int iconResId, CharSequence title, CharSequence message, CharSequence buttonText) {
        this.iconResId = iconResId;
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
    }

    public PopNotification(int iconResId, CharSequence title, CharSequence message, CharSequence buttonText, OnBindView<PopNotification> onBindView) {
        this.iconResId = iconResId;
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
        this.onBindView = onBindView;
    }

    public static PopNotification show(OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(CharSequence title) {
        PopNotification popNotification = new PopNotification(title);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(CharSequence title, CharSequence message) {
        PopNotification popNotification = new PopNotification(title, message);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int titleResId) {
        PopNotification popNotification = new PopNotification(titleResId);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int titleResId, int messageResId) {
        PopNotification popNotification = new PopNotification(titleResId, messageResId);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(CharSequence title, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(title, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(CharSequence title, CharSequence message, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(title, message, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int titleResId, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(titleResId, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int titleResId, int messageResId, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(titleResId, messageResId, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, CharSequence title, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(iconResId, title, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, CharSequence title, CharSequence message, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(iconResId, title, message, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, CharSequence title) {
        PopNotification popNotification = new PopNotification(iconResId, title);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, CharSequence title, CharSequence message) {
        PopNotification popNotification = new PopNotification(iconResId, title, message);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, int titleResId, int messageResId) {
        PopNotification popNotification = new PopNotification(iconResId, titleResId, messageResId);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, int titleResId, int messageResId, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(iconResId, titleResId, messageResId, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, int titleResId, int messageResId, int buttonTextResId) {
        PopNotification popNotification = new PopNotification(iconResId, titleResId, messageResId, buttonTextResId);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, int titleResId, int messageResId, int buttonTextResId, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(iconResId, titleResId, messageResId, buttonTextResId, onBindView);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, CharSequence title, CharSequence message, CharSequence buttonText) {
        PopNotification popNotification = new PopNotification(iconResId, title, message, buttonText);
        popNotification.show();
        return popNotification;
    }

    public static PopNotification show(int iconResId, CharSequence title, CharSequence message, CharSequence buttonText, OnBindView<PopNotification> onBindView) {
        PopNotification popNotification = new PopNotification(iconResId, title, message, buttonText, onBindView);
        popNotification.show();
        return popNotification;
    }

    public PopNotification show() {
        if (isHide && getDialogView() != null) {
            getDialogView().setVisibility(View.VISIBLE);
            return this;
        }
        super.beforeShow();
        if (getDialogView() == null) {
            if (DialogX.onlyOnePopNotification) {
                if (popNotificationList != null && !popNotificationList.isEmpty()) {
                    popNotificationList.get(popNotificationList.size() - 1).dismiss();
                }
            } else {
                if (popNotificationList != null) {
                    CopyOnWriteArrayList<PopNotification> popNotificationCopyOnWriteArrayList = new CopyOnWriteArrayList<>(popNotificationList);
                    for (int i = 0; i < popNotificationCopyOnWriteArrayList.size(); i++) {
                        PopNotification popInstance = popNotificationCopyOnWriteArrayList.get(i);
                        if (popNotificationCopyOnWriteArrayList.size() >= maxShowCount) {
                            if (i <= popNotificationCopyOnWriteArrayList.size() - maxShowCount) {
                                popInstance.dismiss();
                                popNotificationList.remove(popInstance);
                            }
                        }
                    }
                }
            }
            if (popNotificationList == null) popNotificationList = new ArrayList<>();
            popNotificationList.add(PopNotification.this);
            int layoutResId = isLightTheme() ? R.layout.layout_dialogx_popnotification_material : R.layout.layout_dialogx_popnotification_material_dark;
            if (style.popNotificationSettings() != null) {
                if (style.popNotificationSettings().layout(isLightTheme()) != 0) {
                    layoutResId = style.popNotificationSettings().layout(isLightTheme());
                }
                align = style.popNotificationSettings().align();
                if (align == null) align = DialogXStyle.PopNotificationSettings.ALIGN.TOP;
                int styleEnterAnimResId = style.popNotificationSettings().enterAnimResId(isLightTheme());
                int styleExitAnimResId = style.popNotificationSettings().exitAnimResId(isLightTheme());
                enterAnimResId = enterAnimResId == 0 ? (
                        overrideEnterAnimRes == 0 ? (styleEnterAnimResId != 0 ? styleEnterAnimResId : R.anim.anim_dialogx_notification_enter) : overrideEnterAnimRes
                ) : enterAnimResId;
                exitAnimResId = exitAnimResId == 0 ? (
                        overrideExitAnimRes == 0 ? (styleExitAnimResId != 0 ? styleExitAnimResId : R.anim.anim_dialogx_notification_exit) : overrideExitAnimRes
                ) : exitAnimResId;
                enterAnimDuration = enterAnimDuration == -1 ? (
                        overrideEnterDuration
                ) : enterAnimDuration;
                exitAnimDuration = exitAnimDuration == -1 ? (
                        overrideExitDuration
                ) : exitAnimDuration;
            }
            View dialogView = createView(layoutResId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
            show(dialogView);
        } else {
            show(getDialogView());
        }
        return this;
    }

    public PopNotification show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            if (DialogX.onlyOnePopNotification) {
                PopNotification oldInstance = null;
                if (popNotificationList != null && !popNotificationList.isEmpty()) {
                    oldInstance = popNotificationList.get(popNotificationList.size() - 1);
                }
                if (oldInstance != null) {
                    oldInstance.dismiss();
                }
            }
            if (popNotificationList == null) popNotificationList = new ArrayList<>();
            popNotificationList.add(PopNotification.this);
            int layoutResId = isLightTheme() ? R.layout.layout_dialogx_popnotification_material : R.layout.layout_dialogx_popnotification_material_dark;
            if (style.popNotificationSettings() != null) {
                if (style.popNotificationSettings().layout(isLightTheme()) != 0) {
                    layoutResId = style.popNotificationSettings().layout(isLightTheme());
                }
                align = style.popNotificationSettings().align();
                if (align == null) align = DialogXStyle.PopNotificationSettings.ALIGN.TOP;
                int styleEnterAnimResId = style.popNotificationSettings().enterAnimResId(isLightTheme());
                int styleExitAnimResId = style.popNotificationSettings().exitAnimResId(isLightTheme());
                enterAnimResId = enterAnimResId == 0 ? (
                        overrideEnterAnimRes == 0 ? (styleEnterAnimResId != 0 ? styleEnterAnimResId : R.anim.anim_dialogx_notification_enter) : overrideEnterAnimRes
                ) : enterAnimResId;
                exitAnimResId = exitAnimResId == 0 ? (
                        overrideExitAnimRes == 0 ? (styleExitAnimResId != 0 ? styleExitAnimResId : R.anim.anim_dialogx_notification_exit) : overrideExitAnimRes
                ) : exitAnimResId;
                enterAnimDuration = enterAnimDuration == -1 ? (
                        overrideEnterDuration
                ) : enterAnimDuration;
                exitAnimDuration = exitAnimDuration == -1 ? (
                        overrideExitDuration
                ) : exitAnimDuration;
            }
            View dialogView = createView(layoutResId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
            show(activity, dialogView);
        } else {
            show(activity, getDialogView());
        }
        return this;
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    protected Timer autoDismissTimer;
    protected long autoDismissDelay = Long.MIN_VALUE;

    public PopNotification autoDismiss(long delay) {
        autoDismissDelay = delay;
        if (autoDismissTimer != null) {
            autoDismissTimer.cancel();
        }
        if (delay < 0) return this;
        autoDismissTimer = new Timer();
        autoDismissTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                dismiss();
            }
        }, delay);
        return this;
    }

    public void resetAutoDismissTimer() {
        autoDismiss(autoDismissDelay);
    }

    private boolean isNoSetCustomDelay() {
        return autoDismissDelay == Long.MIN_VALUE;
    }

    public PopNotification showShort() {
        if (isNoSetCustomDelay()) autoDismiss(2000);
        if (!preShow && !isShow) {
            show();
        }
        return this;
    }

    public PopNotification showLong() {
        autoDismiss(3500);
        if (!preShow && !isShow) {
            show();
        }
        return this;
    }

    public PopNotification showAlways() {
        return noAutoDismiss();
    }

    public PopNotification noAutoDismiss() {
        autoDismiss(TIME_NO_AUTO_DISMISS_DELAY);
        return this;
    }

    private float defaultTop;

    public class DialogImpl implements DialogConvertViewInterface {

        public DialogXBaseRelativeLayout boxRoot;
        public ViewGroup boxBody;
        public ImageView imgDialogxPopIcon;
        public TextView txtDialogxPopTitle;
        public TextView txtDialogxPopMessage;
        public TextView txtDialogxButton;
        public RelativeLayout boxCustom;

        private List<View> blurViews;

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            imgDialogxPopIcon = convertView.findViewById(R.id.img_dialogx_pop_icon);
            txtDialogxPopTitle = convertView.findViewById(R.id.txt_dialogx_pop_title);
            txtDialogxPopMessage = convertView.findViewById(R.id.txt_dialogx_pop_message);
            txtDialogxButton = convertView.findViewById(R.id.txt_dialogx_button);
            boxCustom = convertView.findViewById(R.id.box_custom);

            blurViews = findAllBlurView(convertView);

            init();
            dialogImpl = this;
            refreshView();
        }

        @Override
        public void init() {
            if (titleTextInfo == null) titleTextInfo = DialogX.titleTextInfo;
            if (messageTextInfo == null) messageTextInfo = DialogX.messageTextInfo;
            if (buttonTextInfo == null) buttonTextInfo = DialogX.buttonTextInfo;
            if (backgroundColor == null) backgroundColor = DialogX.backgroundColor;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(getThisOrderIndex());
            }

            if (autoDismissTimer == null) {
                showShort();
            }

            boxRoot.setClickable(false);
            boxRoot.setFocusable(false);
            boxRoot.setParentDialog(me);
            boxRoot.setAutoUnsafePlacePadding(false);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;

                    setLifecycleState(Lifecycle.State.CREATED);
                    boxRoot.setAlpha(0f);

                    onDialogShow();
                    getDialogLifecycleCallback().onShow(me);
                    PopNotification.this.onShow(me);
                }

                @Override
                public void onDismiss() {
                    if (popNotificationList != null) {
                        popNotificationList.remove(PopNotification.this);
                        if (popNotificationList.isEmpty()) {
                            popNotificationList = null;
                        }
                    }
                    if (autoDismissTimer != null) {
                        autoDismissTimer.cancel();
                    }
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    PopNotification.this.onDismiss(me);
                    setLifecycleState(Lifecycle.State.DESTROYED);
                    dialogImpl = null;
                    System.gc();
                }
            });

            RelativeLayout.LayoutParams rlp;
            rlp = ((RelativeLayout.LayoutParams) boxBody.getLayoutParams());
            if (align == null) align = DialogXStyle.PopNotificationSettings.ALIGN.TOP;
            switch (align) {
                case TOP:
                    rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case BOTTOM:
                    rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    boxRoot.setAutoUnsafePlacePadding(true);
                    break;
                case CENTER:
                    rlp.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    rlp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    break;
            }
            boxBody.setLayoutParams(rlp);

            boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
                @Override
                public void onChange(Rect unsafeRect) {
                    if (align == DialogXStyle.PopNotificationSettings.ALIGN.TOP) {
                        boxBody.setY(defaultTop <= 0 ? defaultTop = (unsafeRect.top + bodyMargin[1]) : defaultTop);
                    } else if (align == DialogXStyle.PopNotificationSettings.ALIGN.TOP_INSIDE) {
                        boxBody.setPadding(0, unsafeRect.top, 0, 0);
                    }
                }
            });

            boxRoot.setOnBackPressedListener(new DialogXBaseRelativeLayout.PrivateBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    return false;
                }
            });

            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    getDialogXAnimImpl().doShowAnim(me, boxBody);

                    if (!DialogX.onlyOnePopNotification) {
                        if (popNotificationList != null) {
                            for (int i = 0; i < popNotificationList.size() - 1; i++) {
                                PopNotification popInstance = popNotificationList.get(i);
                                popInstance.moveBack(boxBody.getHeight());
                            }
                        }
                    }

                    Integer blurFrontColor = null;
                    Float popNotificationRadius = null;
                    if (getStyle().popNotificationSettings() != null && getStyle().popNotificationSettings().blurBackgroundSettings() != null &&
                            getStyle().popNotificationSettings().blurBackgroundSettings().blurBackground()) {
                        blurFrontColor = backgroundColor == null ?
                                getColorNullable(getIntStyleAttr(getStyle().popNotificationSettings().blurBackgroundSettings().blurForwardColorRes(isLightTheme()))) :
                                backgroundColor;
                        popNotificationRadius = getFloatStyleAttr((float) getStyle().popNotificationSettings().blurBackgroundSettings().blurBackgroundRoundRadiusPx());
                    }

                    if (blurViews != null) {
                        for (View blurView : blurViews) {
                            ((BlurViewType) blurView).setOverlayColor(backgroundColor == null ? blurFrontColor : backgroundColor);
                            ((BlurViewType) blurView).setRadiusPx(popNotificationRadius);
                        }
                    }
                    setLifecycleState(Lifecycle.State.RESUMED);
                }
            });

            boxBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dismissAnimFlag) {
                        log("skip click @ A");
                        return;
                    }
                    haptic(v);
                    if (onPopNotificationClickListener != null) {
                        if (!onPopNotificationClickListener.onClick(me, v)) {
                            dismiss();
                        }
                    } else {
                        dismiss();
                    }
                }
            });

            txtDialogxButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dismissAnimFlag) {
                        return;
                    }
                    haptic(v);
                    if (onButtonClickListener != null) {
                        if (!onButtonClickListener.onClick(me, v)) {
                            doDismiss(v);
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
            onDialogInit();
        }

        @Override
        public void refreshView() {
            if (boxRoot == null) {
                return;
            }
            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);
            if (backgroundColor != null) {
                tintColor(boxBody, backgroundColor);

                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setOverlayColor(backgroundColor);
                    }
                }
            }

            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
                boxCustom.setVisibility(View.VISIBLE);
            } else {
                boxCustom.setVisibility(View.GONE);
            }

            if (backgroundRadius > -1) {
                if (boxBody.getBackground() instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) boxBody.getBackground();
                    if (gradientDrawable != null)
                        gradientDrawable.setCornerRadius(backgroundRadius);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    boxBody.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), backgroundRadius);
                        }
                    });
                    boxBody.setClipToOutline(true);
                }

                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setRadiusPx(backgroundRadius);
                    }
                }
            }

            showText(txtDialogxPopTitle, title);
            showText(txtDialogxPopMessage, message);
            showText(txtDialogxButton, buttonText);

            useTextInfo(txtDialogxPopTitle, titleTextInfo);
            useTextInfo(txtDialogxPopMessage, messageTextInfo);
            useTextInfo(txtDialogxButton, buttonTextInfo);

            if (iconBitmap != null && !iconBitmap.isRecycled()) {
                imgDialogxPopIcon.setVisibility(View.VISIBLE);
                imgDialogxPopIcon.setImageBitmap(iconBitmap);
            } else {
                if (iconDrawable != null) {
                    imgDialogxPopIcon.setVisibility(View.VISIBLE);
                    imgDialogxPopIcon.setImageDrawable(iconDrawable);
                } else {
                    if (iconResId != 0) {
                        imgDialogxPopIcon.setVisibility(View.VISIBLE);
                        imgDialogxPopIcon.setImageResource(iconResId);
                    } else {
                        imgDialogxPopIcon.setVisibility(View.GONE);
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && tintIcon == BOOLEAN.TRUE) {
                if (autoTintIconInLightOrDarkMode) {
                    imgDialogxPopIcon.setImageTintList(txtDialogxPopTitle.getTextColors());
                } else {
                    imgDialogxPopIcon.setImageTintList(null);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgDialogxPopIcon.setImageTintList(null);
                }
            }
            if (iconSize > 0) {
                ViewGroup.LayoutParams iLp = imgDialogxPopIcon.getLayoutParams();
                iLp.width = iconSize;
                iLp.height = iconSize;
                imgDialogxPopIcon.setLayoutParams(iLp);
            }

            if (slideToClose) {
                boxBody.setOnTouchListener(new View.OnTouchListener() {

                    boolean touchDown;
                    float touchY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                touchDown = true;
                                touchY = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (touchDown) {
                                    float deltaY = event.getY() - touchY;
                                    if (boxBody.getY() + deltaY < defaultTop) {
                                        boxBody.setY(boxBody.getY() + deltaY);
                                    } else {
                                        boxBody.setY(defaultTop);
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                if (boxBody.getY() < defaultTop - DialogX.touchSlideTriggerThreshold) {
                                    doDismiss(v);
                                } else {
                                    PopValueAnimator valueAnimator = PopValueAnimator.ofFloat(boxBody.getY(), defaultTop);
                                    boxBody.setTag(valueAnimator);
                                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            if (getDialogImpl() == null || !isShow) {
                                                animation.cancel();
                                                return;
                                            }
                                            View bodyView = getDialogImpl().boxBody;
                                            if (bodyView != null && bodyView.isAttachedToWindow()) {
                                                bodyView.setY((Float) animation.getAnimatedValue());
                                            }
                                        }
                                    });
                                    valueAnimator.setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration).setInterpolator(new DecelerateInterpolator(2f));
                                    valueAnimator.start();

                                    if (Math.abs(boxBody.getY() - defaultTop) <= 1 && event.getY() <= touchY) {
                                        boxBody.callOnClick();
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });
            } else {
                boxBody.setOnTouchListener(null);
            }

            RelativeLayout.LayoutParams rlp = ((RelativeLayout.LayoutParams) boxBody.getLayoutParams());
            if (bodyMargin[0] != -1) rlp.leftMargin = bodyMargin[0];
            if (bodyMargin[1] != -1) rlp.topMargin = bodyMargin[1];
            if (bodyMargin[2] != -1) rlp.rightMargin = bodyMargin[2];
            if (bodyMargin[3] != -1) rlp.bottomMargin = bodyMargin[3];
            boxBody.setLayoutParams(rlp);
            onDialogRefreshUI();
        }

        @Override
        public void doDismiss(final View v) {
            if (PopNotification.this.preDismiss(PopNotification.this)) {
                return;
            }
            if (v != null) v.setEnabled(false);

            if (!dismissAnimFlag && boxRoot != null) {
                dismissAnimFlag = true;
                boxCustom.setVisibility(View.GONE);
                boxBody.setFocusable(false);
                boxBody.setClickable(false);
                boxBody.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                txtDialogxButton.setFocusable(false);
                txtDialogxButton.setClickable(false);
                boxRoot.post(new Runnable() {
                    @Override
                    public void run() {
                        getDialogXAnimImpl().doExitAnim(me, boxBody);

                        preRecycle = true;
                        runOnMainDelay(new Runnable() {
                            @Override
                            public void run() {
                                waitForDismiss();
                            }
                        }, getExitAnimationDuration(null));

                        if (popNotificationList != null) {
                            //使位于自己之前的PopTip moveDown
                            int index = popNotificationList.indexOf(me);
                            for (int i = 0; i < index; i++) {
                                PopNotification popNotification = popNotificationList.get(i);
                                popNotification.moveFront(boxBody.getHeight());
                            }
                        }
                    }
                });
            }
        }

        protected DialogXAnimInterface<PopNotification> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<PopNotification>() {
                    @Override
                    public void doShowAnim(PopNotification dialog, ViewGroup dialogBodyView) {
                        Animation enterAnim = AnimationUtils.loadAnimation(getApplicationContext(), enterAnimResId == 0 ? R.anim.anim_dialogx_notification_enter : enterAnimResId);
                        long enterAnimDuration = getEnterAnimationDuration(enterAnim);
                        enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                        enterAnim.setDuration(enterAnimDuration);
                        enterAnim.setFillAfter(true);
                        boxBody.startAnimation(enterAnim);

                        boxRoot.animate()
                                .setDuration(enterAnimDuration)
                                .alpha(1f)
                                .setInterpolator(new DecelerateInterpolator())
                                .setListener(null);
                    }

                    @Override
                    public void doExitAnim(PopNotification dialog, ViewGroup dialogBodyView) {
                        Animation exitAnim = AnimationUtils.loadAnimation(getApplicationContext() == null ? boxRoot.getContext() : getApplicationContext(), exitAnimResId == 0 ? R.anim.anim_dialogx_notification_exit : exitAnimResId);
                        long exitAnimDuration = getExitAnimationDuration(exitAnim);
                        exitAnim.setDuration(exitAnimDuration);
                        exitAnim.setFillAfter(true);
                        boxBody.startAnimation(exitAnim);

                        boxRoot.animate()
                                .alpha(0f)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(exitAnimDuration);
                    }
                };
            }
            return dialogXAnimImpl;
        }

        public long getExitAnimationDuration(@Nullable Animation defaultExitAnim) {
            if (defaultExitAnim == null && boxBody.getAnimation() != null) {
                defaultExitAnim = boxBody.getAnimation();
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
            if (defaultEnterAnim == null && boxBody.getAnimation() != null) {
                defaultEnterAnim = boxBody.getAnimation();
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
    }

    protected boolean preRecycle = false;

    /**
     * 之所以这样处理，在较为频繁的启停 popNotification 时可能存在 popNotification 关闭动画位置错误无法计算的问题
     * 使用 preRecycle 标记记录是否需要回收，而不是立即销毁
     * 等待所有 popNotification 处于待回收状态时一并回收可以避免此问题
     */
    private void waitForDismiss() {
        if (popNotificationList == null || popNotificationList.isEmpty()) {
            return;
        }
        preRecycle = true;
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
        CopyOnWriteArrayList<PopNotification> copyPopNotificationList = new CopyOnWriteArrayList<>(popNotificationList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            copyPopNotificationList.removeIf(Objects::isNull);
        } else {
            Iterator<PopNotification> iterator = copyPopNotificationList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    iterator.remove();
                }
            }
        }
//        boolean allPreRecycled = true;
//        for (PopNotification popTip : copyPopNotificationList) {
//            if (!popTip.preRecycle) {
//                allPreRecycled = false;
//                break;
//            }
//        }
//        if (allPreRecycled) {
//            for (PopNotification popTip : copyPopNotificationList) {
//                dismiss(popTip.getDialogView());
//            }
//        }
        dismiss(getDialogView());
    }

    private void moveBack(int newDialogHeight) {
        if (getDialogImpl() != null && getDialogImpl().boxBody != null) {
            View bodyView = getDialogImpl().boxBody;
            if (getDialogImpl() == null || bodyView == null) return;
            if (style.popNotificationSettings() != null)
                align = style.popNotificationSettings().align();
            if (align == null) align = DialogXStyle.PopNotificationSettings.ALIGN.TOP;
            float moveAimTop = 0;
            float y = bodyView.getY();
            if (bodyView.getTag() instanceof PopValueAnimator) {
                ((PopValueAnimator) bodyView.getTag()).end();
                y = ((PopValueAnimator) bodyView.getTag()).getEndValue();
            }
            switch (align) {
                case TOP:
                    moveAimTop = y + newDialogHeight * 1.1f;
                    break;
                case TOP_INSIDE:
                    moveAimTop = y + newDialogHeight - bodyView.getPaddingTop();
                    break;
                case CENTER:
                case BOTTOM:
                case BOTTOM_INSIDE:
                    moveAimTop = y - newDialogHeight * 1.1f;
                    break;
            }
            if (moveDisplacementInterceptor != null) {
                moveAimTop = moveDisplacementInterceptor.resetAnimY(popNotificationList == null ? 0 : popNotificationList.indexOf(me), me, bodyView.getY(), moveAimTop, (int) (bodyView.getHeight() / bodyView.getScaleY()), popNotificationList == null ? 1 : popNotificationList.size(), true);
            }
            final float fromY = bodyView.getY();
            float toY = moveAimTop;
            PopValueAnimator valueAnimator = PopValueAnimator.ofFloat(bodyView.getY(), moveAimTop);
            bodyView.setTag(valueAnimator);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (getDialogImpl() == null || !isShow) {
                        animation.cancel();
                        return;
                    }
                    View bodyView = getDialogImpl().boxBody;
                    float value = (Float) animation.getAnimatedValue();
                    float totalDistance = toY - fromY;
                    if (moveDisplacementInterceptor != null && moveDisplacementInterceptor.animUpdater(popNotificationList == null ? 0 : popNotificationList.indexOf(me), me, bodyView, fromY, toY, Math.max(0f, Math.min(1f, (totalDistance == 0f ? 1f : (value - fromY) / totalDistance))), animation, popNotificationList == null ? 1 : countDisplayPopNotificationNum(), true)) {
                        return;
                    }
                    if (bodyView != null && bodyView.isAttachedToWindow()) {
                        bodyView.setY(defaultTop = value);
                    }
                }
            });
            valueAnimator.setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration).setInterpolator(new DecelerateInterpolator(2f));
            valueAnimator.start();
        }
    }

    private void moveFront(int newDialogHeight) {
        if (getDialogImpl() != null && getDialogImpl().boxBody != null) {
            View bodyView = getDialogImpl().boxBody;
            if (getDialogImpl() == null || bodyView == null) return;
            if (style.popNotificationSettings() != null)
                align = style.popNotificationSettings().align();
            if (align == null) align = DialogXStyle.PopNotificationSettings.ALIGN.TOP;
            float moveAimTop = 0;
            float y = bodyView.getY();
            if (bodyView.getTag() instanceof PopValueAnimator) {
                ((PopValueAnimator) bodyView.getTag()).end();
                y = ((PopValueAnimator) bodyView.getTag()).getEndValue();
            }
            switch (align) {
                case TOP:
                    moveAimTop = y - newDialogHeight * 1.1f;
                    break;
                case TOP_INSIDE:
                    moveAimTop = y - newDialogHeight + bodyView.getPaddingTop();
                    break;
                case CENTER:
                case BOTTOM:
                case BOTTOM_INSIDE:
                    moveAimTop = y + newDialogHeight * 1.1f;
                    break;
            }
            if (moveDisplacementInterceptor != null) {
                moveAimTop = moveDisplacementInterceptor.resetAnimY(popNotificationList == null ? 0 : popNotificationList.indexOf(me), me, bodyView.getY(), moveAimTop, (int) (bodyView.getHeight() / bodyView.getScaleY()), popNotificationList == null ? 1 : popNotificationList.size(), false);
            }
            final float fromY = bodyView.getY();
            float toY = moveAimTop;
            PopValueAnimator valueAnimator = PopValueAnimator.ofFloat(fromY, toY);
            bodyView.setTag(valueAnimator);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (getDialogImpl() == null || !isShow) {
                        animation.cancel();
                        return;
                    }
                    View bodyView = getDialogImpl().boxBody;
                    float value = (Float) animation.getAnimatedValue();
                    float totalDistance = toY - fromY;
                    if (moveDisplacementInterceptor != null && moveDisplacementInterceptor.animUpdater(popNotificationList == null ? 0 : popNotificationList.indexOf(me), me, bodyView, fromY, toY, Math.max(0f, Math.min(1f, (totalDistance == 0f ? 1f : (value - fromY) / totalDistance))), animation, popNotificationList == null ? 1 : countDisplayPopNotificationNum(), false)) {
                        return;
                    }
                    if (bodyView != null && bodyView.isAttachedToWindow()) {
                        bodyView.setY(defaultTop = value);
                    }
                }
            });
            valueAnimator.setDuration(exitAnimDuration == -1 ? 300 : exitAnimDuration).setInterpolator(new DecelerateInterpolator(2f));
            valueAnimator.start();
        }
    }

    private int countDisplayPopNotificationNum() {
        if (popNotificationList == null) return 0;
        int count = 0;
        for (int i = 0; i < popNotificationList.size(); i++) {
            PopNotification tips = popNotificationList.get(i);
            if (tips != null && !tips.preRecycle) {
                count++;
            }
        }
        return count;
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

    public DialogLifecycleCallback<PopNotification> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<PopNotification>() {
        } : dialogLifecycleCallback;
    }

    public PopNotification setDialogLifecycleCallback(DialogLifecycleCallback<PopNotification> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public PopNotification setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public PopNotification setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }

    public PopNotification.DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public PopNotification setCustomView(OnBindView<PopNotification> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public PopNotification removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }

    public DialogXStyle.PopNotificationSettings.ALIGN getAlign() {
        return align;
    }

    public PopNotification setAlign(DialogXStyle.PopNotificationSettings.ALIGN align) {
        this.align = align;
        return this;
    }

    public int getIconResId() {
        return iconResId;
    }

    public PopNotification setIconResId(int iconResId) {
        this.iconResId = iconResId;
        refreshUI();
        return this;
    }

    public PopNotification setIcon(Bitmap bitmap) {
        this.iconBitmap = bitmap;
        refreshUI();
        return this;
    }

    public PopNotification setIcon(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }

    public int getIconSize() {
        return iconSize;
    }

    public PopNotification setIconSize(int iconSize) {
        this.iconSize = iconSize;
        refreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public PopNotification setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }

    public PopNotification setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }

    public CharSequence getButtonText() {
        return buttonText;
    }

    public PopNotification setButton(CharSequence buttonText) {
        this.buttonText = buttonText;
        refreshUI();
        return this;
    }

    public PopNotification setButton(int buttonTextResId) {
        this.buttonText = getString(buttonTextResId);
        refreshUI();
        return this;
    }

    public PopNotification setButton(CharSequence buttonText, OnDialogButtonClickListener<PopNotification> onButtonClickListener) {
        this.buttonText = buttonText;
        this.onButtonClickListener = onButtonClickListener;
        refreshUI();
        return this;
    }

    public PopNotification setButton(int buttonTextResId, OnDialogButtonClickListener<PopNotification> onButtonClickListener) {
        this.buttonText = getString(buttonTextResId);
        this.onButtonClickListener = onButtonClickListener;
        refreshUI();
        return this;
    }

    public PopNotification setButton(OnDialogButtonClickListener<PopNotification> onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public PopNotification setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getButtonTextInfo() {
        return buttonTextInfo;
    }

    public PopNotification setButtonTextInfo(TextInfo buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
        refreshUI();
        return this;
    }

    public OnDialogButtonClickListener<PopNotification> getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public PopNotification setOnButtonClickListener(OnDialogButtonClickListener<PopNotification> onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        return this;
    }

    public boolean isAutoTintIconInLightOrDarkMode() {
        return autoTintIconInLightOrDarkMode;
    }

    public PopNotification setAutoTintIconInLightOrDarkMode(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
        refreshUI();
        return this;
    }

    public OnDialogButtonClickListener<PopNotification> getOnPopNotificationClickListener() {
        return onPopNotificationClickListener;
    }

    public PopNotification setOnPopNotificationClickListener(OnDialogButtonClickListener<PopNotification> onPopNotificationClickListener) {
        this.onPopNotificationClickListener = onPopNotificationClickListener;
        refreshUI();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PopNotification setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }

    public PopNotification setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public PopNotification setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public PopNotification setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
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

        if (DialogX.onlyOnePopNotification) {
            PopNotification oldInstance = null;
            if (popNotificationList != null && !popNotificationList.isEmpty()) {
                oldInstance = popNotificationList.get(popNotificationList.size() - 1);
            }
            if (oldInstance != null) {
                oldInstance.dismiss();
            }
        }
        if (popNotificationList == null) popNotificationList = new ArrayList<>();
        popNotificationList.add(PopNotification.this);

        int layoutResId = isLightTheme() ? R.layout.layout_dialogx_popnotification_material : R.layout.layout_dialogx_popnotification_material_dark;
        if (style.popNotificationSettings() != null) {
            if (style.popNotificationSettings().layout(isLightTheme()) != 0) {
                layoutResId = style.popNotificationSettings().layout(isLightTheme());
            }
            align = style.popNotificationSettings().align();
            if (align == null) align = DialogXStyle.PopNotificationSettings.ALIGN.TOP;
            int styleEnterAnimResId = style.popNotificationSettings().enterAnimResId(isLightTheme());
            int styleExitAnimResId = style.popNotificationSettings().exitAnimResId(isLightTheme());
            enterAnimResId = enterAnimResId == 0 ? (
                    overrideEnterAnimRes == 0 ? (styleEnterAnimResId != 0 ? styleEnterAnimResId : R.anim.anim_dialogx_notification_enter) : overrideEnterAnimRes
            ) : enterAnimResId;
            exitAnimResId = exitAnimResId == 0 ? (
                    overrideExitAnimRes == 0 ? (styleExitAnimResId != 0 ? styleExitAnimResId : R.anim.anim_dialogx_notification_exit) : overrideExitAnimRes
            ) : exitAnimResId;
            enterAnimDuration = enterAnimDuration == -1 ? (
                    overrideEnterDuration
            ) : enterAnimDuration;
            exitAnimDuration = exitAnimDuration == -1 ? (
                    overrideExitDuration
            ) : exitAnimDuration;
        }
        enterAnimDuration = 0;
        View dialogView = createView(layoutResId);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
    }

    public void hide() {
        isHide = true;
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
    }

    public PopNotification setAnimResId(int enterResId, int exitResId) {
        this.enterAnimResId = enterResId;
        this.exitAnimResId = exitResId;
        return this;
    }

    public PopNotification setEnterAnimResId(int enterResId) {
        this.enterAnimResId = enterResId;
        return this;
    }

    public PopNotification setExitAnimResId(int exitResId) {
        this.exitAnimResId = exitResId;
        return this;
    }

    @Override
    protected void shutdown() {
        dismiss();
    }

    public PopNotification setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public PopNotification setMargin(int left, int top, int right, int bottom) {
        bodyMargin[0] = left;
        bodyMargin[1] = top;
        bodyMargin[2] = right;
        bodyMargin[3] = bottom;
        refreshUI();
        return this;
    }

    public PopNotification setMarginLeft(int left) {
        bodyMargin[0] = left;
        refreshUI();
        return this;
    }

    public PopNotification setMarginTop(int top) {
        bodyMargin[1] = top;
        refreshUI();
        return this;
    }

    public PopNotification setMarginRight(int right) {
        bodyMargin[2] = right;
        refreshUI();
        return this;
    }

    public PopNotification setMarginBottom(int bottom) {
        bodyMargin[3] = bottom;
        refreshUI();
        return this;
    }

    public int getMarginLeft() {
        return bodyMargin[0];
    }

    public int getMarginTop() {
        return bodyMargin[1];
    }

    public int getMarginRight() {
        return bodyMargin[2];
    }

    public int getMarginBottom() {
        return bodyMargin[3];
    }

    public PopNotification iconSuccess() {
        setTintIcon(false);
        int resId = R.mipmap.ico_dialogx_success;
        if (getStyle().popNotificationSettings() != null && getStyle().popNotificationSettings().defaultIconSuccess() != 0) {
            resId = getStyle().popNotificationSettings().defaultIconSuccess();
        }
        setIconSize(dip2px(26));
        setIconResId(resId);
        return this;
    }

    public PopNotification iconWarning() {
        setTintIcon(false);
        int resId = R.mipmap.ico_dialogx_warning;
        if (getStyle().popNotificationSettings() != null && getStyle().popNotificationSettings().defaultIconWarning() != 0) {
            resId = getStyle().popNotificationSettings().defaultIconWarning();
        }
        setIconSize(dip2px(26));
        setIconResId(resId);
        return this;
    }

    public PopNotification iconError() {
        setTintIcon(false);
        int resId = R.mipmap.ico_dialogx_error;
        if (getStyle().popNotificationSettings() != null && getStyle().popNotificationSettings().defaultIconError() != 0) {
            resId = getStyle().popNotificationSettings().defaultIconError();
        }
        setIconSize(dip2px(26));
        setIconResId(resId);
        return this;
    }

    public boolean getTintIcon() {
        return tintIcon == BOOLEAN.TRUE;
    }

    public PopNotification setTintIcon(boolean tintIcon) {
        this.tintIcon = tintIcon ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public CharSequence getTitle() {
        return title;
    }

    public PopNotification setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public PopNotification setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshUI();
        return this;
    }

    public PopNotification setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public DialogXAnimInterface<PopNotification> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public PopNotification setDialogXAnimImpl(DialogXAnimInterface<PopNotification> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public PopNotification setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public PopNotification setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public PopNotification setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    public boolean isSlideToClose() {
        return slideToClose;
    }

    public PopNotification setSlideToClose(boolean slideToClose) {
        this.slideToClose = slideToClose;
        refreshUI();
        return this;
    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new PopNotification() {
     *
     * @param dialog self
     * @Override public void onShow(PopNotification dialog) {
     * //...
     * }
     * }
     */
    protected void onShow(PopNotification dialog) {

    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new PopNotification() {
     *
     * @param dialog self
     * @Override public boolean onDismiss(PopNotification dialog) {
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
    protected void onDismiss(PopNotification dialog) {

    }

    public PopNotification setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public PopNotification onShow(DialogXRunnable<PopNotification> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public PopNotification onDismiss(DialogXRunnable<PopNotification> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public PopNotification appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public PopNotification setThisOrderIndex(int orderIndex) {
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

    public PopNotification bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public PopNotification setActionRunnable(int actionId, DialogXRunnable<PopNotification> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public PopNotification cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public PopNotification cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss() {
        dismiss();
    }

    public PopNotification bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }
}
