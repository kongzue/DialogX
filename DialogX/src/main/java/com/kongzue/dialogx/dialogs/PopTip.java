package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.graphics.Rect;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/20 11:59
 */
public class PopTip extends BaseDialog {
    
    public static final int TIME_NO_AUTO_DISMISS_DELAY = -1;
    
    protected static WeakReference<PopTip> oldInstance;
    protected OnBindView<PopTip> onBindView;
    protected DialogLifecycleCallback<PopTip> dialogLifecycleCallback;
    protected PopTip me = this;
    protected DialogImpl dialogImpl;
    protected int enterAnimResId = R.anim.anim_dialogx_default_enter;
    protected int exitAnimResId = R.anim.anim_dialogx_default_exit;
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
    
    public static PopTip build() {
        return new PopTip();
    }
    
    public PopTip(OnBindView<PopTip> onBindView) {
        super();
        this.onBindView = onBindView;
    }
    
    public PopTip(CharSequence message) {
        super();
        this.message = message;
    }
    
    public PopTip(int iconResId, CharSequence message) {
        super();
        this.iconResId = iconResId;
        this.message = message;
    }
    
    public PopTip(int iconResId, CharSequence message, CharSequence buttonText) {
        super();
        this.iconResId = iconResId;
        this.message = message;
        this.buttonText = buttonText;
    }
    
    public PopTip(CharSequence message, CharSequence buttonText) {
        super();
        this.message = message;
        this.buttonText = buttonText;
    }
    
    public PopTip(CharSequence message, OnBindView<PopTip> onBindView) {
        super();
        this.message = message;
        this.onBindView = onBindView;
    }
    
    public PopTip(int iconResId, CharSequence message, OnBindView<PopTip> onBindView) {
        super();
        this.iconResId = iconResId;
        this.message = message;
        this.onBindView = onBindView;
    }
    
    public PopTip(int iconResId, CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        super();
        this.iconResId = iconResId;
        this.message = message;
        this.buttonText = buttonText;
        this.onBindView = onBindView;
    }
    
    public PopTip(CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        super();
        this.message = message;
        this.buttonText = buttonText;
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
    
    public static PopTip show(CharSequence message, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(message, onBindView);
        popTip.show();
        return popTip;
    }
    
    public static PopTip show(CharSequence message, CharSequence buttonText) {
        PopTip popTip = new PopTip(message, buttonText);
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
    
    public static PopTip show(CharSequence message, CharSequence buttonText, OnBindView<PopTip> onBindView) {
        PopTip popTip = new PopTip(message, buttonText, onBindView);
        popTip.show();
        return popTip;
    }
    
    public void show() {
        super.beforeShow();
        if (DialogX.onlyOnePopTip) {
            if (oldInstance != null && oldInstance.get() != null) {
                oldInstance.get().dismiss();
            }
        }
        oldInstance = new WeakReference<>(this);
        int layoutResId = isLightTheme() ? R.layout.layout_dialogx_poptip_material : R.layout.layout_dialogx_poptip_material_dark;
        if (style.popTipSettings() != null) {
            if (style.popTipSettings().layout(isLightTheme()) != 0) {
                layoutResId = style.popTipSettings().layout(isLightTheme());
            }
            align = style.popTipSettings().align();
            if (align == null) align = DialogXStyle.PopTipSettings.ALIGN.BOTTOM;
            enterAnimResId = style.popTipSettings().enterAnimResId(isLightTheme()) != 0 ? style.popTipSettings().enterAnimResId(isLightTheme()) : R.anim.anim_dialogx_default_enter;
            exitAnimResId = style.popTipSettings().exitAnimResId(isLightTheme()) != 0 ? style.popTipSettings().exitAnimResId(isLightTheme()) : R.anim.anim_dialogx_default_exit;
        }
        dialogView = createView(layoutResId);
        dialogImpl = new DialogImpl(dialogView);
        show(dialogView);
    }
    
    protected Timer autoDismissTimer;
    
    public PopTip autoDismiss(long delay) {
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
    
    public PopTip showShort() {
        autoDismiss(2000);
        return this;
    }
    
    public PopTip showLong() {
        autoDismiss(3500);
        return this;
    }
    
    public PopTip noAutoDismiss() {
        autoDismiss(TIME_NO_AUTO_DISMISS_DELAY);
        return this;
    }
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        DialogXBaseRelativeLayout boxRoot;
        LinearLayout boxBody;
        ImageView imgDialogxPopIcon;
        TextView txtDialogxPopText;
        RelativeLayout boxCustom;
        TextView txtDialogxButton;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBody = convertView.findViewById(R.id.box_body);
            imgDialogxPopIcon = convertView.findViewById(R.id.img_dialogx_pop_icon);
            txtDialogxPopText = convertView.findViewById(R.id.txt_dialogx_pop_text);
            boxCustom = convertView.findViewById(R.id.box_custom);
            txtDialogxButton = convertView.findViewById(R.id.txt_dialogx_button);
            
            init();
            refreshView();
        }
        
        @Override
        public void init() {
            boxRoot.setFocusable(false);
            boxRoot.setFocusableInTouchMode(false);
            boxRoot.setAutoUnsafePlacePadding(false);
            
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    
                    getDialogLifecycleCallback().onShow(me);
                    
                    if (onBindView != null) onBindView.onBind(me, onBindView.getCustomView());
                    
                    if (autoDismissTimer == null) {
                        showShort();
                    }
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    if (oldInstance.get() == me) oldInstance.clear();
                    getDialogLifecycleCallback().onDismiss(me);
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
            
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    
                    Animation enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                    enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                    boxBody.startAnimation(enterAnim);
                    
                    boxRoot.animate().setDuration(enterAnim.getDuration()).alpha(1f).setInterpolator(new DecelerateInterpolator()).setListener(null);
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
            if (backgroundColor != -1) {
                tintColor(boxBody, backgroundColor);
            }
            
            if (onBindView != null) {
                if (onBindView.getCustomView() != null) {
                    boxCustom.removeView(onBindView.getCustomView());
                    ViewGroup.LayoutParams lp = onBindView.getCustomView().getLayoutParams();
                    if (lp == null) {
                        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    boxCustom.addView(onBindView.getCustomView(), lp);
                    boxCustom.setVisibility(View.VISIBLE);
                }else{
                    boxCustom.setVisibility(View.GONE);
                }
            }
            
            showText(txtDialogxPopText, message);
            showText(txtDialogxButton, buttonText);
            
            useTextInfo(txtDialogxPopText, messageTextInfo);
            useTextInfo(txtDialogxButton, buttonTextInfo);
            
            if (iconResId != 0) {
                imgDialogxPopIcon.setVisibility(View.VISIBLE);
                imgDialogxPopIcon.setImageResource(iconResId);
                if (autoTintIconInLightOrDarkMode) {
                    imgDialogxPopIcon.setImageTintList(txtDialogxPopText.getTextColors());
                } else {
                    imgDialogxPopIcon.setImageTintList(null);
                }
            } else {
                imgDialogxPopIcon.setVisibility(View.GONE);
            }
            
            if (onPopTipClickListener != null) {
                boxBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!onPopTipClickListener.onClick(me, v)){
                            dismiss();
                        }
                    }
                });
            }else{
                boxBody.setOnClickListener(null);
                boxBody.setClickable(false);
            }
        }
        
        @Override
        public void doDismiss(final View v) {
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    if (v != null) v.setEnabled(false);
                    
                    Animation exitAnim = AnimationUtils.loadAnimation(getContext(), exitAnimResId);
                    boxBody.startAnimation(exitAnim);
                    
                    boxRoot.animate().alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(exitAnim.getDuration()).setListener(new AnimatorListenerEndCallBack() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dismiss(dialogView);
                        }
                    });
                }
            });
        }
    }
    
    public void refreshUI() {
        if (dialogImpl == null) return;
        dialogImpl.refreshView();
    }
    
    public void dismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    public DialogLifecycleCallback<PopTip> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<PopTip>() {
        } : dialogLifecycleCallback;
    }
    
    public PopTip setDialogLifecycleCallback(DialogLifecycleCallback<PopTip> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
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
    
    public CharSequence getButtonText() {
        return buttonText;
    }
    
    public PopTip setButton(CharSequence buttonText) {
        this.buttonText = buttonText;
        refreshUI();
        return this;
    }
    
    public PopTip setButton(CharSequence buttonText, OnDialogButtonClickListener<PopTip> onButtonClickListener) {
        this.buttonText = buttonText;
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
}
