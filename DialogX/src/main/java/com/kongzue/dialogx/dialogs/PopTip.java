package com.kongzue.dialogx.dialogs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Outline;
import android.graphics.Rect;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class PopTip extends BaseDialog implements NoTouchInterface {

    public static final int TIME_NO_AUTO_DISMISS_DELAY = -1;
    protected static List<PopTip> popTipList;

    public static long overrideEnterDuration = -1;
    public static long overrideExitDuration = -1;
    public static int overrideEnterAnimRes = 0;
    public static int overrideExitAnimRes = 0;
    public static int maxShowCount = Integer.MAX_VALUE;
    public static PopMoveDisplacementInterceptor<PopTip> moveDisplacementInterceptor;

    protected OnBindView<PopTip> onBindView;
    protected DialogLifecycleCallback<PopTip> dialogLifecycleCallback;
    protected PopTip me = this;
    protected DialogImpl dialogImpl;
    protected int enterAnimResId = 0;
    protected int exitAnimResId = 0;
    protected DialogXStyle.PopTipSettings.ALIGN align;
    protected OnDialogButtonClickListener<PopTip> onButtonClickListener;
    protected OnDialogButtonClickListener<PopTip> onPopTipClickListener;
    protected BOOLEAN tintIcon;
    protected float backgroundRadius = DialogX.defaultPopTipBackgroundRadius;
    protected DialogXAnimInterface<PopTip> dialogXAnimImpl;

    protected int iconResId;
    protected CharSequence message;
    protected CharSequence buttonText;

    protected TextInfo messageTextInfo;
    protected TextInfo buttonTextInfo = new TextInfo().setBold(true);
    protected int[] bodyMargin = new int[]{-1, -1, -1, -1};

    protected PopTip() {
        super();
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public static PopTip build() {
        return new PopTip();
    }

    public static PopTip build(DialogXStyle style) {
        return new PopTip().setStyle(style);
    }

    public static PopTip build(OnBindView<PopTip> onBindView) {
        return new PopTip().setCustomView(onBindView);
    }

    public PopTip(OnBindView<PopTip> onBindView) {
        this.onBindView = onBindView;
    }

    public PopTip(CharSequence message) {
        this.message = message;
    }

    public PopTip(int messageResId) {
        this.message = getString(messageResId);
    }

    public static PopTip tip(String message) {
        return PopTip.show(message);
    }

    public static PopTip tip(int messageResId) {
        return PopTip.show(messageResId);
    }

    public static PopTip tip(String message, String buttonText) {
        return PopTip.show(message, buttonText);
    }

    public static PopTip tip(int messageResId, int buttonTextResId) {
        return PopTip.show(messageResId, buttonTextResId);
    }

    public static PopTip tip(int iconResId, String message) {
        return PopTip.show(iconResId, message);
    }

    public static PopTip tip(int iconResId, String message, String buttonText) {
        return PopTip.show(iconResId, message, buttonText);
    }

    public PopTip(int iconResId, CharSequence message) {
        this.iconResId = iconResId;
        this.message = message;
    }

    public PopTip(int iconResId, CharSequence message, CharSequence buttonText) {
        this.iconResId = iconResId;
        this.message = message;
        this.buttonText = buttonText;
    }

    public PopTip(int iconResId, int messageResId, int buttonTextResId) {
        this.iconResId = iconResId;
        this.message = getString(messageResId);
        this.buttonText = getString(buttonTextResId);
    }

    public PopTip(CharSequence message, CharSequence buttonText) {
        this.message = message;
        this.buttonText = buttonText;
    }

    public PopTip(int messageResId, int buttonTextResId) {
        this.message = getString(messageResId);
        this.buttonText = getString(buttonTextResId);
    }

    public PopTip(CharSequence message, OnBindView<PopTip> onBindView) {
        this.message = message;
        this.onBindView = onBindView;
    }

    public PopTip(int messageResId, OnBindView<PopTip> onBindView) {
        this.message = getString(messageResId);
        this.onBindView = onBindView;
    }

    public PopTip(int iconResId, CharSequence message, OnBindView<PopTip> onBindView) {
        this.iconResId = iconResId;
        this.message = message;
        this.onBindView = onBindView;
    }

    public PopTip(int iconResId, CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        this.iconResId = iconResId;
        this.message = message;
        this.buttonText = buttonText;
        this.onBindView = onBindView;
    }

    public PopTip(int iconResId, int messageResId, int buttonTextResId, OnBindView<PopTip> onBindView) {
        this.iconResId = iconResId;
        this.message = getString(messageResId);
        this.buttonText = getString(buttonTextResId);
        this.onBindView = onBindView;
    }

    public PopTip(CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        this.message = message;
        this.buttonText = buttonText;
        this.onBindView = onBindView;
    }

    public PopTip(int messageResId, int buttonTextResId, OnBindView<PopTip> onBindView) {
        this.message = getString(messageResId);
        this.buttonText = getString(buttonTextResId);
        this.onBindView = onBindView;
    }

    public static PopTip show(OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(CharSequence message) {
        PopTip popTip = new PopTip(message);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int messageResId) {
        PopTip popTip = new PopTip(messageResId);
        popTip.show();
        return popTip;
    }

    public static PopTip show(CharSequence message, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(message, onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int messageResId, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(messageResId, onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(CharSequence message, CharSequence buttonText) {
        PopTip popTip = new PopTip(message, buttonText);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int messageResId, int buttonTextResId) {
        PopTip popTip = new PopTip(messageResId, buttonTextResId);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int iconResId, CharSequence message, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(iconResId, message, onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int iconResId, CharSequence message) {
        PopTip popTip = new PopTip(iconResId, message);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int iconResId, CharSequence message, CharSequence buttonText) {
        PopTip popTip = new PopTip(iconResId, message, buttonText);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int iconResId, CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(iconResId, message, buttonText, onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int iconResId, int messageResId, int buttonTextResId, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(iconResId, messageResId, buttonTextResId, onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(message, buttonText, onBindView);
        popTip.show();
        return popTip;
    }

    public static PopTip show(int messageResId, int buttonTextResId, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(messageResId, buttonTextResId, onBindView);
        popTip.show();
        return popTip;
    }

    public PopTip show() {
        if (isHide && getDialogView() != null) {
            getDialogView().setVisibility(View.VISIBLE);
            return this;
        }
        super.beforeShow();
        if (getDialogView() == null) {
            if (DialogX.onlyOnePopTip) {
                PopTip oldInstance = null;
                if (popTipList != null && !popTipList.isEmpty()) {
                    oldInstance = popTipList.get(popTipList.size() - 1);
                }
                if (oldInstance != null) {
                    oldInstance.dismiss();
                }
            } else {
                if (popTipList != null) {
                    CopyOnWriteArrayList<PopTip> copyPopTipList = new CopyOnWriteArrayList<>(popTipList);
                    for (int i = 0; i < copyPopTipList.size(); i++) {
                        PopTip popInstance = copyPopTipList.get(i);
                        if (copyPopTipList.size() < maxShowCount) {
                            popInstance.moveBack();
                        } else {
                            if (i <= copyPopTipList.size() - maxShowCount) {
                                popInstance.dismiss();
                                popTipList.remove(popInstance);
                            } else {
                                popInstance.moveBack();
                            }
                        }
                    }
                }
            }
            if (popTipList == null) popTipList = new ArrayList<>();
            popTipList.add(PopTip.this);
            int layoutResId = isLightTheme() ? R.layout.layout_dialogx_poptip_material : R.layout.layout_dialogx_poptip_material_dark;
            if (style.popTipSettings() != null) {
                if (style.popTipSettings().layout(isLightTheme()) != 0) {
                    layoutResId = style.popTipSettings().layout(isLightTheme());
                }
                if (align == null) {
                    if (style.popTipSettings().align() == null) {
                        align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
                    } else {
                        align = style.popTipSettings().align();
                    }
                }
                int styleEnterAnimResId = style.popTipSettings().enterAnimResId(isLightTheme());
                int styleExitAnimResId = style.popTipSettings().exitAnimResId(isLightTheme());
                enterAnimResId = enterAnimResId == 0 ? (
                        overrideEnterAnimRes == 0 ? (styleEnterAnimResId != 0 ? styleEnterAnimResId : R.anim.anim_dialogx_default_enter) : overrideEnterAnimRes
                ) : enterAnimResId;
                exitAnimResId = exitAnimResId == 0 ? (
                        overrideExitAnimRes == 0 ? (styleExitAnimResId != 0 ? styleExitAnimResId : R.anim.anim_dialogx_default_exit) : overrideExitAnimRes
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

    public PopTip show(Activity activity) {
        super.beforeShow();
        if (getDialogView() == null) {
            if (DialogX.onlyOnePopTip) {
                PopTip oldInstance = null;
                if (popTipList != null && !popTipList.isEmpty()) {
                    oldInstance = popTipList.get(popTipList.size() - 1);
                }
                if (oldInstance != null) {
                    oldInstance.dismiss();
                }
            } else {
                if (popTipList != null) {
                    CopyOnWriteArrayList<PopTip> copyPopTipList = new CopyOnWriteArrayList<>(popTipList);
                    for (int i = 0; i < copyPopTipList.size(); i++) {
                        PopTip popInstance = copyPopTipList.get(i);
                        if (copyPopTipList.size() < maxShowCount) {
                            popInstance.moveBack();
                        } else {
                            if (i <= copyPopTipList.size() - maxShowCount) {
                                popInstance.dismiss();
                                popTipList.remove(popInstance);
                            } else {
                                popInstance.moveBack();
                            }
                        }
                    }
                }
            }
            if (popTipList == null) popTipList = new ArrayList<>();
            popTipList.add(PopTip.this);
            int layoutResId = isLightTheme() ? R.layout.layout_dialogx_poptip_material : R.layout.layout_dialogx_poptip_material_dark;
            if (style.popTipSettings() != null) {
                if (style.popTipSettings().layout(isLightTheme()) != 0) {
                    layoutResId = style.popTipSettings().layout(isLightTheme());
                }

                if (align == null) {
                    if (style.popTipSettings().align() == null) {
                        align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
                    } else {
                        align = style.popTipSettings().align();
                    }
                }
                int styleEnterAnimResId = style.popTipSettings().enterAnimResId(isLightTheme());
                int styleExitAnimResId = style.popTipSettings().exitAnimResId(isLightTheme());
                enterAnimResId = enterAnimResId == 0 ? (
                        overrideEnterAnimRes == 0 ? (styleEnterAnimResId != 0 ? styleEnterAnimResId : R.anim.anim_dialogx_default_enter) : overrideEnterAnimRes
                ) : enterAnimResId;
                exitAnimResId = exitAnimResId == 0 ? (
                        overrideExitAnimRes == 0 ? (styleExitAnimResId != 0 ? styleExitAnimResId : R.anim.anim_dialogx_default_exit) : overrideExitAnimRes
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
    protected long autoDismissDelay;

    public PopTip autoDismiss(long delay) {
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

    public PopTip showShort() {
        autoDismiss(2000);
        if (!preShow && !isShow) {
            show();
        }
        return this;
    }

    public PopTip showLong() {
        autoDismiss(3500);
        if (!preShow && !isShow) {
            show();
        }
        return this;
    }

    public PopTip showAlways() {
        return noAutoDismiss();
    }

    public PopTip noAutoDismiss() {
        autoDismiss(TIME_NO_AUTO_DISMISS_DELAY);
        return this;
    }

    public class DialogImpl implements DialogConvertViewInterface {

        public DialogXBaseRelativeLayout boxRoot;
        public LinearLayout boxBody;
        public ImageView imgDialogxPopIcon;
        public TextView txtDialogxPopText;
        public RelativeLayout boxCustom;
        public TextView txtDialogxButton;

        private List<View> blurViews;

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            imgDialogxPopIcon = convertView.findViewById(R.id.img_dialogx_pop_icon);
            txtDialogxPopText = convertView.findViewById(R.id.txt_dialogx_pop_text);
            boxCustom = convertView.findViewById(R.id.box_custom);
            txtDialogxButton = convertView.findViewById(R.id.txt_dialogx_button);

            blurViews = findAllBlurView(convertView);

            init();
            dialogImpl = this;
            refreshView();
        }

        @Override
        public void init() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(getThisOrderIndex());
            }

            if (messageTextInfo == null) messageTextInfo = DialogX.popTextInfo;
            if (buttonTextInfo == null) buttonTextInfo = DialogX.buttonTextInfo;
            if (backgroundColor == null) backgroundColor = DialogX.backgroundColor;

            if (autoDismissTimer == null) {
                showShort();
            }

            boxRoot.setParentDialog(me);
            boxRoot.setAutoUnsafePlacePadding(true);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;
                    setLifecycleState(Lifecycle.State.CREATED);
                    boxRoot.setAlpha(0f);
                    onDialogShow();
                    getDialogLifecycleCallback().onShow(me);
                    PopTip.this.onShow(me);
                }

                @Override
                public void onDismiss() {
                    if (popTipList != null) {
                        popTipList.remove(PopTip.this);
                        if (popTipList.isEmpty()) {
                            popTipList = null;
                        }
                    }
                    isShow = false;

                    if (autoDismissTimer != null) {
                        autoDismissTimer.cancel();
                    }
                    getDialogLifecycleCallback().onDismiss(me);
                    PopTip.this.onDismiss(me);
                    setLifecycleState(Lifecycle.State.DESTROYED);
                    dialogImpl = null;
                    System.gc();
                }
            });

            applyPopTipAlign();

            boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
                @Override
                public void onChange(Rect unsafeRect) {
                    if (align == DialogXStyle.PopTipSettings.ALIGN.TOP_INSIDE) {
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
                    setLifecycleState(Lifecycle.State.RESUMED);
                }
            });

            txtDialogxButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

        private void applyPopTipAlign() {
            RelativeLayout.LayoutParams rlp;
            rlp = ((RelativeLayout.LayoutParams) boxBody.getLayoutParams());
            if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
            switch (align) {
                case TOP:
                    rlp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case BOTTOM:
                    rlp.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    rlp.removeRule(RelativeLayout.CENTER_IN_PARENT);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case CENTER:
                    rlp.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    rlp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    break;
            }
            boxBody.setLayoutParams(rlp);
        }

        @Override
        public void refreshView() {
            if (boxRoot == null || getOwnActivity() == null) {
                return;
            }
            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);
            if (backgroundColor != null) {
                tintColor(boxBody, backgroundColor);
                tintColor(txtDialogxButton, backgroundColor);

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

            showText(txtDialogxPopText, message);
            showText(txtDialogxButton, buttonText);

            useTextInfo(txtDialogxPopText, messageTextInfo);
            useTextInfo(txtDialogxButton, buttonTextInfo);

            if (iconResId != 0) {
                imgDialogxPopIcon.setVisibility(View.VISIBLE);
                imgDialogxPopIcon.setImageResource(iconResId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isTintIcon()) {
                        imgDialogxPopIcon.setImageTintList(txtDialogxPopText.getTextColors());
                    } else {
                        imgDialogxPopIcon.setImageTintList(null);
                    }
                }
            } else {
                imgDialogxPopIcon.setVisibility(View.GONE);
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

            if (onPopTipClickListener != null) {
                boxBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!onPopTipClickListener.onClick(me, v)) {
                            haptic(v);
                            dismiss();
                        }
                    }
                });
            } else {
                boxBody.setOnClickListener(null);
                boxBody.setClickable(false);
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
            if (PopTip.this.preDismiss(PopTip.this)) {
                return;
            }
            if (v != null) v.setEnabled(false);

            if (!dismissAnimFlag && boxRoot != null) {
                dismissAnimFlag = true;
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

                        if (popTipList != null) {
                            //使位于自己之前的PopTip moveDown
                            int index = popTipList.indexOf(me);
                            for (int i = 0; i < index; i++) {
                                PopTip popTip = popTipList.get(i);
                                popTip.moveFront();
                            }
                        }
                    }
                });
            }
        }

        protected DialogXAnimInterface<PopTip> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<PopTip>() {
                    @Override
                    public void doShowAnim(PopTip dialog, ViewGroup dialogBodyView) {
                        Animation enterAnim = AnimationUtils.loadAnimation(getOwnActivity(), enterAnimResId == 0 ? R.anim.anim_dialogx_default_enter : enterAnimResId);
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
                    public void doExitAnim(PopTip dialog, ViewGroup dialogBodyView) {
                        Animation exitAnim = AnimationUtils.loadAnimation(getOwnActivity() == null ? boxRoot.getContext() : getOwnActivity(), exitAnimResId == 0 ? R.anim.anim_dialogx_default_exit : exitAnimResId);
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
     * 之所以这样处理，在较为频繁的启停 PopTip 时可能存在 PopTip 关闭动画位置错误无法计算的问题
     * 使用 preRecycle 标记记录是否需要回收，而不是立即销毁
     * 等待所有 PopTip 处于待回收状态时一并回收可以避免此问题
     */
    private void waitForDismiss() {
        if (popTipList == null || popTipList.isEmpty()) {
            return;
        }
        preRecycle = true;
        if (getDialogView() != null) {
            getDialogView().setVisibility(View.GONE);
        }
        CopyOnWriteArrayList<PopTip> copyPopTipList = new CopyOnWriteArrayList<>(popTipList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            copyPopTipList.removeIf(Objects::isNull);
        } else {
            Iterator<PopTip> iterator = copyPopTipList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    iterator.remove();
                }
            }
        }
        dismiss(getDialogView());
    }

    private void moveBack() {
        if (getDialogImpl() != null && getDialogImpl().boxBody != null) {
            if (getDialogImpl() == null || getDialogImpl().boxBody == null) return;
            View bodyView = getDialogImpl().boxBody;
            bodyView.post(new Runnable() {
                @Override
                public void run() {
                    if (getDialogImpl() == null) {
                        return;
                    }
                    if (align == null && style.popTipSettings() != null) {
                        align = style.popTipSettings().align();
                    }
                    if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.TOP;
                    float moveAimTop = 0;
                    switch (align) {
                        case TOP:
                            moveAimTop = bodyView.getY() + bodyView.getHeight() * 1.3f;
                            break;
                        case TOP_INSIDE:
                            moveAimTop = bodyView.getY() + bodyView.getHeight() - bodyView.getPaddingTop();
                            break;
                        case CENTER:
                        case BOTTOM:
                        case BOTTOM_INSIDE:
                            moveAimTop = bodyView.getY() - bodyView.getHeight() * 1.3f;
                            break;
                    }
                    if (moveDisplacementInterceptor != null) {
                        moveAimTop = moveDisplacementInterceptor.resetAnimY(popTipList == null ? 0 : popTipList.indexOf(me), me, bodyView.getY(), moveAimTop, (int) (bodyView.getHeight() / bodyView.getScaleY()), popTipList == null ? 1 : popTipList.size(), true);
                    }
                    if (bodyView.getTag() instanceof ValueAnimator) {
                        ((ValueAnimator) bodyView.getTag()).end();
                    }
                    //log("#Animation from:" + bodyView.getY() + " to:" + moveAimTop);
                    final float fromY = bodyView.getY();
                    float toY = moveAimTop;
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromY, toY);
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
                            if (moveDisplacementInterceptor != null && moveDisplacementInterceptor.animUpdater(popTipList == null ? 0 : popTipList.indexOf(me), me, bodyView, fromY, toY, Math.max(0f, Math.min(1f, (totalDistance == 0f ? 1f : (value - fromY) / totalDistance))), animation, popTipList == null ? 1 : countDisplayPopTipsNum(), true)) {
                                return;
                            }
                            if (bodyView != null && bodyView.isAttachedToWindow()) {
                                bodyView.setY(value);
                            }
                        }
                    });
                    valueAnimator.setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration).setInterpolator(new DecelerateInterpolator(2f));
                    valueAnimator.start();
                }
            });
        }
    }

    private void moveFront() {
        if (getDialogImpl() != null && getDialogImpl().boxBody != null) {
            if (getDialogImpl() == null || getDialogImpl().boxBody == null) return;
            View bodyView = getDialogImpl().boxBody;
            bodyView.post(new Runnable() {
                @Override
                public void run() {
                    if (getDialogImpl() == null) {
                        return;
                    }
                    if (align == null && style.popTipSettings() != null) {
                        align = style.popTipSettings().align();
                    }
                    if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.TOP;
                    float moveAimTop = 0;
                    switch (align) {
                        case TOP:
                            moveAimTop = bodyView.getY() - bodyView.getHeight() * 1.3f;
                            break;
                        case TOP_INSIDE:
                            moveAimTop = bodyView.getY() - bodyView.getHeight() + bodyView.getPaddingTop();
                            break;
                        case CENTER:
                        case BOTTOM:
                        case BOTTOM_INSIDE:
                            moveAimTop = bodyView.getY() + bodyView.getHeight() * 1.3f;
                            break;
                    }
                    if (moveDisplacementInterceptor != null) {
                        moveAimTop = moveDisplacementInterceptor.resetAnimY(popTipList == null ? 0 : popTipList.indexOf(me), me, bodyView.getY(), moveAimTop, (int) (bodyView.getHeight() / bodyView.getScaleY()), popTipList == null ? 1 : popTipList.size(), false);
                    }
                    if (bodyView.getTag() instanceof ValueAnimator) {
                        ((ValueAnimator) bodyView.getTag()).end();
                    }
                    //log("#Animation from:" + bodyView.getY() + " to:" + moveAimTop);
                    final float fromY = bodyView.getY();
                    float toY = moveAimTop;
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromY, toY);
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
                            if (moveDisplacementInterceptor != null && moveDisplacementInterceptor.animUpdater(popTipList == null ? 0 : popTipList.indexOf(me), me, bodyView, fromY, toY, Math.max(0f, Math.min(1f, (totalDistance == 0f ? 1f : (value - fromY) / totalDistance))), animation, popTipList == null ? 1 : countDisplayPopTipsNum(), false)) {
                                return;
                            }
                            if (bodyView != null && bodyView.isAttachedToWindow()) {
                                bodyView.setY(value);
                            }
                        }
                    });
                    valueAnimator.setDuration(exitAnimDuration == -1 ? 300 : exitAnimDuration).setInterpolator(new AccelerateInterpolator(2f));
                    valueAnimator.start();
                }
            });
        }
    }

    private int countDisplayPopTipsNum() {
        if (popTipList == null) return 0;
        int count = 0;
        for (int i = 0; i < popTipList.size(); i++) {
            PopTip tips = popTipList.get(i);
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

    public DialogLifecycleCallback<PopTip> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<PopTip>() {
        } : dialogLifecycleCallback;
    }

    public PopTip setDialogLifecycleCallback(DialogLifecycleCallback<PopTip> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public PopTip setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public PopTip setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }

    public PopTip.DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public PopTip setCustomView(OnBindView<PopTip> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public PopTip removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }

    public DialogXStyle.PopTipSettings.ALIGN getAlign() {
        return align;
    }

    public PopTip setAlign(DialogXStyle.PopTipSettings.ALIGN align) {
        this.align = align;
        if (getDialogImpl() != null) {
            getDialogImpl().applyPopTipAlign();
        }
        return this;
    }

    public int getIconResId() {
        return iconResId;
    }

    public PopTip setIconResId(int iconResId) {
        this.iconResId = iconResId;
        refreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public PopTip setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }

    public PopTip setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }

    public CharSequence getButtonText() {
        return buttonText;
    }

    public PopTip setButton(CharSequence buttonText) {
        this.buttonText = buttonText;
        refreshUI();
        return this;
    }

    public PopTip setButton(int buttonTextResId) {
        this.buttonText = getString(buttonTextResId);
        refreshUI();
        return this;
    }

    public PopTip setButton(CharSequence buttonText, OnDialogButtonClickListener<PopTip> onButtonClickListener) {
        this.buttonText = buttonText;
        this.onButtonClickListener = onButtonClickListener;
        refreshUI();
        return this;
    }

    public PopTip setButton(int buttonTextResId, OnDialogButtonClickListener<PopTip> onButtonClickListener) {
        this.buttonText = getString(buttonTextResId);
        this.onButtonClickListener = onButtonClickListener;
        refreshUI();
        return this;
    }

    public PopTip setButton(OnDialogButtonClickListener<PopTip> onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public PopTip setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getButtonTextInfo() {
        return buttonTextInfo;
    }

    public PopTip setButtonTextInfo(TextInfo buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
        refreshUI();
        return this;
    }

    public OnDialogButtonClickListener<PopTip> getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public PopTip setOnButtonClickListener(OnDialogButtonClickListener<PopTip> onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        return this;
    }

    @Deprecated
    public boolean isAutoTintIconInLightOrDarkMode() {
        return isTintIcon();
    }

    @Deprecated
    public PopTip setAutoTintIconInLightOrDarkMode(boolean autoTintIconInLightOrDarkMode) {
        setTintIcon(autoTintIconInLightOrDarkMode);
        return this;
    }

    public OnDialogButtonClickListener<PopTip> getOnPopTipClickListener() {
        return onPopTipClickListener;
    }

    public PopTip setOnPopTipClickListener(OnDialogButtonClickListener<PopTip> onPopTipClickListener) {
        this.onPopTipClickListener = onPopTipClickListener;
        refreshUI();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PopTip setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }

    public PopTip setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public PopTip setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public PopTip setExitAnimDuration(long exitAnimDuration) {
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

        if (DialogX.onlyOnePopTip) {
            PopTip oldInstance = null;
            if (popTipList != null && !popTipList.isEmpty()) {
                oldInstance = popTipList.get(popTipList.size() - 1);
            }
            if (oldInstance != null) {
                oldInstance.dismiss();
            }
        } else {
            if (popTipList != null) {
                CopyOnWriteArrayList<PopTip> copyPopTipList = new CopyOnWriteArrayList<>(popTipList);
                for (int i = 0; i < copyPopTipList.size(); i++) {
                    PopTip popInstance = copyPopTipList.get(i);
                    if (copyPopTipList.size() < maxShowCount) {
                        popInstance.moveBack();
                    } else {
                        if (i <= copyPopTipList.size() - maxShowCount) {
                            popInstance.dismiss();
                            popTipList.remove(popInstance);
                        } else {
                            popInstance.moveBack();
                        }
                    }
                }
            }
        }
        if (popTipList == null) popTipList = new ArrayList<>();
        popTipList.add(PopTip.this);

        int layoutResId = isLightTheme() ? R.layout.layout_dialogx_poptip_material : R.layout.layout_dialogx_poptip_material_dark;
        if (style.popTipSettings() != null) {
            if (style.popTipSettings().layout(isLightTheme()) != 0) {
                layoutResId = style.popTipSettings().layout(isLightTheme());
            }
            if (align == null) {
                if (style.popTipSettings().align() == null) {
                    align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
                } else {
                    align = style.popTipSettings().align();
                }
            }
            int styleEnterAnimResId = style.popTipSettings().enterAnimResId(isLightTheme());
            int styleExitAnimResId = style.popTipSettings().exitAnimResId(isLightTheme());
            enterAnimResId = enterAnimResId == 0 ? (
                    overrideEnterAnimRes == 0 ? (styleEnterAnimResId != 0 ? styleEnterAnimResId : R.anim.anim_dialogx_default_enter) : overrideEnterAnimRes
            ) : enterAnimResId;
            exitAnimResId = exitAnimResId == 0 ? (
                    overrideExitAnimRes == 0 ? (styleExitAnimResId != 0 ? styleExitAnimResId : R.anim.anim_dialogx_default_exit) : overrideExitAnimRes
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

    public PopTip setAnimResId(int enterResId, int exitResId) {
        this.enterAnimResId = enterResId;
        this.exitAnimResId = exitResId;
        return this;
    }

    public PopTip setEnterAnimResId(int enterResId) {
        this.enterAnimResId = enterResId;
        return this;
    }

    public PopTip setExitAnimResId(int exitResId) {
        this.exitAnimResId = exitResId;
        return this;
    }

    public PopTip setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    @Override
    protected void shutdown() {
        dismiss();
    }

    public PopTip setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public PopTip setMargin(int left, int top, int right, int bottom) {
        bodyMargin[0] = left;
        bodyMargin[1] = top;
        bodyMargin[2] = right;
        bodyMargin[3] = bottom;
        refreshUI();
        return this;
    }

    public PopTip setMarginLeft(int left) {
        bodyMargin[0] = left;
        refreshUI();
        return this;
    }

    public PopTip setMarginTop(int top) {
        bodyMargin[1] = top;
        refreshUI();
        return this;
    }

    public PopTip setMarginRight(int right) {
        bodyMargin[2] = right;
        refreshUI();
        return this;
    }

    public PopTip setMarginBottom(int bottom) {
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

    public PopTip iconSuccess() {
        setTintIcon(false);
        int resId = R.mipmap.ico_dialogx_success;
        if (getStyle().popTipSettings() != null && getStyle().popTipSettings().defaultIconSuccess() != 0) {
            resId = getStyle().popTipSettings().defaultIconSuccess();
        }
        setIconResId(resId);
        return this;
    }

    public PopTip iconWarning() {
        setTintIcon(false);
        int resId = R.mipmap.ico_dialogx_warning;
        if (getStyle().popTipSettings() != null && getStyle().popTipSettings().defaultIconWarning() != 0) {
            resId = getStyle().popTipSettings().defaultIconWarning();
        }
        setIconResId(resId);
        return this;
    }

    public PopTip iconError() {
        setTintIcon(false);
        int resId = R.mipmap.ico_dialogx_error;
        if (getStyle().popTipSettings() != null && getStyle().popTipSettings().defaultIconError() != 0) {
            resId = getStyle().popTipSettings().defaultIconError();
        }
        setIconResId(resId);
        return this;
    }

    public boolean isTintIcon() {
        if (tintIcon == null && getStyle().popTipSettings() != null) {
            return getStyle().popTipSettings().tintIcon();
        }
        return tintIcon == BOOLEAN.TRUE;
    }

    public PopTip setTintIcon(boolean tintIcon) {
        this.tintIcon = tintIcon ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }

    public PopTip setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public DialogXAnimInterface<PopTip> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public PopTip setDialogXAnimImpl(DialogXAnimInterface<PopTip> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public PopTip setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public PopTip setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new PopTip() {
     *
     * @param dialog self
     * @Override public void onShow(PopTip dialog) {
     * //...
     * }
     * }
     */
    protected void onShow(PopTip dialog) {

    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new PopTip() {
     *
     * @param dialog self
     * @Override public boolean onDismiss(PopTip dialog) {
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
    protected void onDismiss(PopTip dialog) {

    }

    @Override
    protected void cleanActivityContext() {
        super.cleanActivityContext();
        dismiss(getDialogView());
    }

    public PopTip setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public PopTip onShow(DialogXRunnable<PopTip> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public PopTip onDismiss(DialogXRunnable<PopTip> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public PopTip appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public PopTip setThisOrderIndex(int orderIndex) {
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

    public PopTip bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public PopTip setActionRunnable(int actionId, DialogXRunnable<PopTip> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public PopTip cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public PopTip cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss() {
        dismiss();
    }

    public PopTip bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }
}
