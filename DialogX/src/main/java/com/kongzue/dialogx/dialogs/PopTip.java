package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.IdRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
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
public class PopTip extends BaseDialog {
    
    public static final int TIME_NO_AUTO_DISMISS_DELAY = -1;
    protected static List<PopTip> popTipList;
    
    public static long overrideEnterDuration = -1;
    public static long overrideExitDuration = -1;
    public static int overrideEnterAnimRes = 0;
    public static int overrideExitAnimRes = 0;
    
    protected OnBindView<PopTip> onBindView;
    protected DialogLifecycleCallback<PopTip> dialogLifecycleCallback;
    protected PopTip me = this;
    protected DialogImpl dialogImpl;
    protected int enterAnimResId = 0;
    protected int exitAnimResId = 0;
    private View dialogView;
    protected DialogXStyle.PopTipSettings.ALIGN align;
    protected OnDialogButtonClickListener<PopTip> onButtonClickListener;
    protected OnDialogButtonClickListener<PopTip> onPopTipClickListener;
    protected boolean autoTintIconInLightOrDarkMode = true;
    
    protected int iconResId;
    protected CharSequence message;
    protected CharSequence buttonText;
    
    protected TextInfo messageTextInfo;
    protected TextInfo buttonTextInfo = new TextInfo().setBold(true);
    
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
                    for (int i = 0; i < popTipList.size(); i++) {
                        PopTip popInstance = popTipList.get(i);
                        popInstance.moveUp();
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
                align = style.popTipSettings().align();
                if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
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
            dialogView = createView(layoutResId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(dialogView);
        return this;
    }
    
    public PopTip show(Activity activity) {
        super.beforeShow();
        if (dialogView != null) {
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
                    for (int i = 0; i < popTipList.size(); i++) {
                        PopTip popInstance = popTipList.get(i);
                        popInstance.moveUp();
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
                align = style.popTipSettings().align();
                if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
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
            dialogView = createView(layoutResId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
        }
        show(activity, dialogView);
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
        return this;
    }
    
    public PopTip showLong() {
        autoDismiss(3500);
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
        
        public DialogImpl(View convertView) {
            if (convertView == null) return;
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            imgDialogxPopIcon = convertView.findViewById(R.id.img_dialogx_pop_icon);
            txtDialogxPopText = convertView.findViewById(R.id.txt_dialogx_pop_text);
            boxCustom = convertView.findViewById(R.id.box_custom);
            txtDialogxButton = convertView.findViewById(R.id.txt_dialogx_button);
            
            init();
            dialogImpl = this;
            refreshView();
        }
        
        @Override
        public void init() {
            if (messageTextInfo == null) messageTextInfo = DialogX.popTextInfo;
            if (buttonTextInfo == null) buttonTextInfo = DialogX.buttonTextInfo;
            if (backgroundColor == -1) backgroundColor = DialogX.backgroundColor;
            
            if (autoDismissTimer == null) {
                showShort();
            }
            
            boxRoot.setParentDialog(me);
            boxRoot.setAutoUnsafePlacePadding(false);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    
                    getDialogLifecycleCallback().onShow(me);
                }
                
                @Override
                public void onDismiss() {
                    if (popTipList != null) popTipList.remove(PopTip.this);
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    dialogImpl = null;
                    System.gc();
                }
            });
            
            RelativeLayout.LayoutParams rlp;
            rlp = ((RelativeLayout.LayoutParams) boxBody.getLayoutParams());
            if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
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
                    if (align == DialogXStyle.PopTipSettings.ALIGN.TOP) {
                        boxBody.setY(unsafeRect.top);
                    } else if (align == DialogXStyle.PopTipSettings.ALIGN.TOP_INSIDE) {
                        boxBody.setPadding(0, unsafeRect.top, 0, 0);
                    }
                }
            });
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    return false;
                }
            });
            
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    Animation enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId == 0 ? R.anim.anim_dialogx_default_enter : enterAnimResId);
                    enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                    if (enterAnimDuration != -1) {
                        enterAnim.setDuration(enterAnimDuration);
                    }
                    enterAnim.setFillAfter(true);
                    boxBody.startAnimation(enterAnim);
                    
                    boxRoot.animate()
                            .setDuration(enterAnimDuration == -1 ? enterAnim.getDuration() : enterAnimDuration)
                            .alpha(1f)
                            .setInterpolator(new DecelerateInterpolator())
                            .setListener(null);
                }
            });
            
            txtDialogxButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClickListener != null) {
                        if (!onButtonClickListener.onClick(me, v)) {
                            doDismiss(v);
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
        }
        
        @Override
        public void refreshView() {
            if (boxRoot == null || getContext() == null) {
                return;
            }
            if (backgroundColor != -1) {
                tintColor(boxBody, backgroundColor);
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
                    if (autoTintIconInLightOrDarkMode) {
                        imgDialogxPopIcon.setImageTintList(txtDialogxPopText.getTextColors());
                    } else {
                        imgDialogxPopIcon.setImageTintList(null);
                    }
                }
            } else {
                imgDialogxPopIcon.setVisibility(View.GONE);
            }
            
            if (onPopTipClickListener != null) {
                boxBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!onPopTipClickListener.onClick(me, v)) {
                            dismiss();
                        }
                    }
                });
            } else {
                boxBody.setOnClickListener(null);
                boxBody.setClickable(false);
            }
        }
        
        @Override
        public void doDismiss(final View v) {
            if (v != null) v.setEnabled(false);
    
            if (!dismissAnimFlag) {
                dismissAnimFlag = true;
                boxRoot.post(new Runnable() {
                    @Override
                    public void run() {
            
                        Animation exitAnim = AnimationUtils.loadAnimation(getContext() == null ? boxRoot.getContext() : getContext(), exitAnimResId == 0 ? R.anim.anim_dialogx_default_exit : exitAnimResId);
                        if (exitAnimDuration != -1) {
                            exitAnim.setDuration(exitAnimDuration);
                        }
                        exitAnim.setFillAfter(true);
                        boxBody.startAnimation(exitAnim);
            
                        boxRoot.animate()
                                .alpha(0f)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
            
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                waitForDismiss();
                            }
                        }, exitAnimDuration == -1 ? exitAnim.getDuration() : exitAnimDuration);
                    }
                });
            }
        }
    }
    
    protected boolean preRecycle = false;
    
    /**
     * 之所以这样处理，在较为频繁的启停 PopTip 时可能存在 PopTip 关闭动画位置错误无法计算的问题
     * 使用 preRecycle 标记记录是否需要回收，而不是立即销毁
     * 等待所有 PopTip 处于待回收状态时一并回收可以避免此问题
     */
    private void waitForDismiss() {
        preRecycle = true;
        if (popTipList != null) {
            for (PopTip popTip : popTipList) {
                if (!popTip.preRecycle) {
                    return;
                }
            }
            for (PopTip popTip : new CopyOnWriteArrayList<>(popTipList)) {
                dismiss(popTip.dialogView);
            }
        }
    }
    
    private void moveUp() {
        if (getDialogImpl() != null && getDialogImpl().boxBody != null) {
            getDialogImpl().boxBody.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getDialogImpl() == null || getDialogImpl().boxBody == null) return;
                    if (style.popTipSettings() != null) align = style.popTipSettings().align();
                    if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
                    float moveAimTop = 0;
                    switch (align) {
                        case TOP:
                            moveAimTop = getDialogImpl().boxBody.getY() + getDialogImpl().boxBody.getHeight() * 1.3f;
                            break;
                        case TOP_INSIDE:
                            moveAimTop = getDialogImpl().boxBody.getY() + getDialogImpl().boxBody.getHeight() - getDialogImpl().boxBody.getPaddingTop();
                            break;
                        case CENTER:
                        case BOTTOM:
                        case BOTTOM_INSIDE:
                            moveAimTop = getDialogImpl().boxBody.getY() - getDialogImpl().boxBody.getHeight() * 1.3f;
                            break;
                    }
                    getDialogImpl().boxBody.animate()
                            .y(moveAimTop)
                            .setDuration(enterAnimDuration == -1 ? 300 : enterAnimDuration)
                            .setInterpolator(new DecelerateInterpolator(2f))
                    ;
                }
            },150);
        }
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
    
    public boolean isAutoTintIconInLightOrDarkMode() {
        return autoTintIconInLightOrDarkMode;
    }
    
    public PopTip setAutoTintIconInLightOrDarkMode(boolean autoTintIconInLightOrDarkMode) {
        this.autoTintIconInLightOrDarkMode = autoTintIconInLightOrDarkMode;
        refreshUI();
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
        if (dialogView != null) {
            dismiss(dialogView);
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
                for (int i = 0; i < popTipList.size(); i++) {
                    PopTip popInstance = popTipList.get(i);
                    popInstance.moveUp();
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
            align = style.popTipSettings().align();
            if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
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
        dialogView = createView(layoutResId);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
    }
    
    public void hide() {
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
    
    @Override
    protected void shutdown() {
        dismiss();
    }
    
    public PopTip setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }
}
