package com.kongzue.dialogx.dialogs;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.BaseOnDialogClickCallback;
import com.kongzue.dialogx.interfaces.BlurViewType;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuButtonClickListener;
import com.kongzue.dialogx.interfaces.ScrollController;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogScrollView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/21 17:08
 */
public class MessageDialog extends BaseDialog {

    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static int overrideEnterAnimRes = 0;
    public static int overrideExitAnimRes = 0;
    public static BOOLEAN overrideCancelable;
    protected boolean bkgInterceptTouch = true;
    protected OnBindView<MessageDialog> onBindView;
    protected MessageDialog me = this;
    protected BOOLEAN privateCancelable;
    protected int customEnterAnimResId;
    protected int customExitAnimResId;
    protected DialogXAnimInterface<MessageDialog> dialogXAnimImpl;
    protected OnBackPressedListener<MessageDialog> onBackPressedListener;
    protected BUTTON_SELECT_RESULT buttonSelectResult = BUTTON_SELECT_RESULT.NONE;

    protected DialogLifecycleCallback<MessageDialog> dialogLifecycleCallback;
    protected OnBackgroundMaskClickListener<MessageDialog> onBackgroundMaskClickListener;

    protected MessageDialog() {
        super();
    }

    protected CharSequence title;
    protected CharSequence message;
    protected CharSequence okText;
    protected CharSequence cancelText;
    protected CharSequence otherText;
    protected String inputText;
    protected String inputHintText;
    protected Integer maskColor = null;
    protected float backgroundRadius = DialogX.defaultMessageDialogBackgroundRadius;
    protected Drawable titleIcon;

    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo okTextInfo;
    protected TextInfo cancelTextInfo;
    protected TextInfo otherTextInfo;
    protected InputInfo inputInfo;

    protected BaseOnDialogClickCallback okButtonClickListener;
    protected BaseOnDialogClickCallback cancelButtonClickListener;
    protected BaseOnDialogClickCallback otherButtonClickListener;

    protected int buttonOrientation;

    public static MessageDialog build() {
        return new MessageDialog();
    }

    public static MessageDialog build(DialogXStyle style) {
        return new MessageDialog().setStyle(style);
    }

    public static MessageDialog build(OnBindView<MessageDialog> onBindView) {
        return new MessageDialog().setCustomView(onBindView);
    }

    public MessageDialog(CharSequence message) {
        this.message = message;
    }

    public MessageDialog(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
    }

    public MessageDialog(CharSequence title, CharSequence message, CharSequence okText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
    }

    public MessageDialog(int titleResId, int messageResId, int okTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
    }

    public MessageDialog(int titleResId, int messageResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }

    public static MessageDialog show(CharSequence title, CharSequence message, CharSequence okText) {
        MessageDialog messageDialog = new MessageDialog(title, message, okText);
        messageDialog.show();
        return messageDialog;
    }

    public static MessageDialog show(int titleResId, int messageResId, int okTextResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId, okTextResId);
        messageDialog.show();
        return messageDialog;
    }

    public static MessageDialog show(CharSequence title, CharSequence message) {
        MessageDialog messageDialog = new MessageDialog(title, message);
        messageDialog.show();
        return messageDialog;
    }

    public static MessageDialog show(int titleResId, int messageResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId);
        messageDialog.show();
        return messageDialog;
    }

    public MessageDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
    }

    public MessageDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
    }

    public static MessageDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        MessageDialog messageDialog = new MessageDialog(title, message, okText, cancelText);
        messageDialog.show();
        return messageDialog;
    }

    public static MessageDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId, okTextResId, cancelTextResId);
        messageDialog.show();
        return messageDialog;
    }

    public MessageDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
    }

    public MessageDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
        this.otherText = getString(otherTextResId);
    }

    public static MessageDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        MessageDialog messageDialog = new MessageDialog(title, message, okText, cancelText, otherText);
        messageDialog.show();
        return messageDialog;
    }

    public static MessageDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId, okTextResId, cancelTextResId, otherTextResId);
        messageDialog.show();
        return messageDialog;
    }

    protected DialogImpl dialogImpl;

    public MessageDialog show() {
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
            int layoutId = style.layout(isLightTheme());
            layoutId = layoutId == 0 ? (isLightTheme() ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark) : layoutId;

            View dialogView = createView(layoutId);
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
            int layoutId = style.layout(isLightTheme());
            layoutId = layoutId == 0 ? (isLightTheme() ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark) : layoutId;

            View dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
            show(activity, dialogView);
        } else {
            show(activity, getDialogView());
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

    public class DialogImpl implements DialogConvertViewInterface {

        private List<View> blurViews;

        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout bkg;
        public TextView txtDialogTitle;
        public ScrollController scrollView;
        public TextView txtDialogTip;
        public ViewGroup boxList;
        public RelativeLayout boxCustom;
        public EditText txtInput;
        public LinearLayout boxButton;
        public TextView btnSelectOther;
        public View spaceOtherButton;
        public View splitHorizontal;
        public TextView btnSelectNegative;
        public TextView btnSelectPositive;

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            txtDialogTitle = convertView.findViewById(R.id.txt_dialog_title);
            scrollView = convertView.findViewById(R.id.scrollView);
            txtDialogTip = convertView.findViewById(R.id.txt_dialog_tip);
            boxList = convertView.findViewById(R.id.box_list);
            boxCustom = convertView.findViewById(R.id.box_custom);
            txtInput = convertView.findViewById(R.id.txt_input);
            boxButton = convertView.findViewById(R.id.box_button);
            btnSelectOther = convertView.findViewById(R.id.btn_selectOther);
            spaceOtherButton = convertView.findViewById(R.id.space_other_button);
            splitHorizontal = convertView.findViewWithTag("split");
            btnSelectNegative = convertView.findViewById(R.id.btn_selectNegative);
            btnSelectPositive = convertView.findViewById(R.id.btn_selectPositive);

            blurViews = findAllBlurView(convertView);

            init();

            dialogImpl = this;
            refreshView();
        }

        public void init() {
            buttonSelectResult = BUTTON_SELECT_RESULT.NONE;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(getThisOrderIndex());
            }

            if (titleTextInfo == null) titleTextInfo = DialogX.titleTextInfo;
            if (messageTextInfo == null) messageTextInfo = DialogX.messageTextInfo;
            if (okTextInfo == null) okTextInfo = DialogX.okButtonTextInfo;
            if (okTextInfo == null) okTextInfo = DialogX.buttonTextInfo;
            if (cancelTextInfo == null) cancelTextInfo = DialogX.buttonTextInfo;
            if (otherTextInfo == null) otherTextInfo = DialogX.buttonTextInfo;
            if (inputInfo == null) inputInfo = DialogX.inputInfo;
            if (backgroundColor == null) backgroundColor = DialogX.backgroundColor;

            txtDialogTitle.getPaint().setFakeBoldText(true);
            btnSelectNegative.getPaint().setFakeBoldText(true);
            btnSelectPositive.getPaint().setFakeBoldText(true);
            btnSelectOther.getPaint().setFakeBoldText(true);

            txtDialogTip.setMovementMethod(LinkMovementMethod.getInstance());

            boxRoot.setBkgAlpha(0f);
            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    preShow = false;

                    setLifecycleState(Lifecycle.State.CREATED);

                    onDialogShow();
                    getDialogLifecycleCallback().onShow(me);
                    MessageDialog.this.onShow(me);

                    getDialogXAnimImpl().doShowAnim(me, bkg);

                    if (style.messageDialogBlurSettings() != null && style.messageDialogBlurSettings().blurBackground()) {
                        bkg.post(new Runnable() {
                            @Override
                            public void run() {
                                Integer blurFrontColor = null;
                                Float dialogXRadius = null;
                                if (style.messageDialogBlurSettings() != null) {
                                    blurFrontColor = getColorNullable(getIntStyleAttr(style.messageDialogBlurSettings().blurForwardColorRes(isLightTheme())));
                                    dialogXRadius = getFloatStyleAttr((float) style.messageDialogBlurSettings().blurBackgroundRoundRadiusPx());
                                }

                                if (blurViews != null) {
                                    for (View blurView : blurViews) {
                                        ((BlurViewType) blurView).setOverlayColor(backgroundColor == null ? blurFrontColor : backgroundColor);
                                        ((BlurViewType) blurView).setRadiusPx(dialogXRadius);
                                    }
                                }

                                setLifecycleState(Lifecycle.State.RESUMED);
                            }
                        });
                    }

                    if (autoShowInputKeyboard) {
                        txtInput.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (txtInput == null) return;
                                txtInput.requestFocus();
                                txtInput.setFocusableInTouchMode(true);
                                imeShow(txtInput, true);
                                txtInput.setSelection(txtInput.getText().length());
                                if (inputInfo != null && inputInfo.isSelectAllText()) {
                                    txtInput.selectAll();
                                }
                            }
                        }, 300);
                    } else {
                        if (inputInfo != null && inputInfo.isSelectAllText()) {
                            txtInput.clearFocus();
                            txtInput.requestFocus();
                            txtInput.selectAll();
                        }
                    }
                }

                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    MessageDialog.this.onDismiss(me);
                    setLifecycleState(Lifecycle.State.DESTROYED);
                    dialogLifecycleCallback = null;

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
            btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonSelectResult = BUTTON_SELECT_RESULT.BUTTON_OK;
                    if (txtInput != null) {
                        imeShow(txtInput, false);
                    }
                    haptic(v);
                    if (okButtonClickListener != null) {
                        if (okButtonClickListener instanceof OnInputDialogButtonClickListener) {
                            String s = txtInput == null ? "" : txtInput.getText().toString();
                            if (!((OnInputDialogButtonClickListener) okButtonClickListener).onClick(me, v, s)) {
                                doDismiss(v);
                            }
                        } else if (okButtonClickListener instanceof OnDialogButtonClickListener) {
                            if (!((OnDialogButtonClickListener) okButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        } else if (okButtonClickListener instanceof OnMenuButtonClickListener) {
                            if (!((OnMenuButtonClickListener) okButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
            btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonSelectResult = BUTTON_SELECT_RESULT.BUTTON_CANCEL;
                    if (txtInput != null) {
                        imeShow(txtInput, false);
                    }
                    haptic(v);
                    if (cancelButtonClickListener != null) {
                        if (cancelButtonClickListener instanceof OnInputDialogButtonClickListener) {
                            String s = txtInput == null ? "" : txtInput.getText().toString();
                            if (!((OnInputDialogButtonClickListener) cancelButtonClickListener).onClick(me, v, s)) {
                                doDismiss(v);
                            }
                        } else if (cancelButtonClickListener instanceof OnMenuButtonClickListener) {
                            if (!((OnMenuButtonClickListener) cancelButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        } else {
                            if (!((OnDialogButtonClickListener) cancelButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
            btnSelectOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonSelectResult = BUTTON_SELECT_RESULT.BUTTON_OTHER;
                    if (txtInput != null) {
                        imeShow(txtInput, false);
                    }
                    haptic(v);
                    if (otherButtonClickListener != null) {
                        if (otherButtonClickListener instanceof OnInputDialogButtonClickListener) {
                            String s = txtInput == null ? "" : txtInput.getText().toString();
                            if (!((OnInputDialogButtonClickListener) otherButtonClickListener).onClick(me, v, s)) {
                                doDismiss(v);
                            }
                        } else if (otherButtonClickListener instanceof OnMenuButtonClickListener) {
                            if (!((OnMenuButtonClickListener) otherButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        } else {
                            if (!((OnDialogButtonClickListener) otherButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });

            onDialogInit();
        }

        public void refreshView() {
            if (boxRoot == null || getOwnActivity() == null) {
                return;
            }

            boxRoot.setAutoUnsafePlacePadding(isEnableImmersiveMode());
            // 修改下划线颜色
            if (inputInfo != null && inputInfo.getBottomLineColor() != null) {
                txtInput.getBackground().mutate().setColorFilter(inputInfo.getBottomLineColor(), PorterDuff.Mode.SRC_ATOP);
            }
            // 修改光标颜色
            if (inputInfo != null && inputInfo.getCursorColor() != null) {
                int cursorColor = inputInfo.getCursorColor();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (txtInput.getTextCursorDrawable() != null) {
                        txtInput.getTextCursorDrawable().mutate().setColorFilter((new PorterDuffColorFilter(cursorColor, PorterDuff.Mode.SRC_ATOP)));
                    } else {
                        try {
                            @SuppressLint("SoonBlockedPrivateApi") Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                            field.setAccessible(true);
                            field.set(txtInput, R.drawable.rect_dialogx_defalut_edittxt_cursor);
                            txtInput.getTextCursorDrawable().mutate().setColorFilter((new PorterDuffColorFilter(cursorColor, PorterDuff.Mode.SRC_ATOP)));
                        } catch (Throwable throwable) {
                            log("DialogX: 在对话框" + dialogKey() + "中设置光标颜色时发生错误！");
                            if (DialogX.DEBUGMODE) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                } else {
                    // Thanks for @Jared Rummler https://stackoverflow.com/questions/11554078/set-textcursordrawable-programmatically/57555148#57555148
                    try {
                        Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                        fCursorDrawableRes.setAccessible(true);
                        int mCursorDrawableRes = fCursorDrawableRes.getInt(txtInput);
                        Field fEditor = TextView.class.getDeclaredField("mEditor");
                        fEditor.setAccessible(true);
                        Object editor = fEditor.get(txtInput);
                        Class<?> clazz = editor.getClass();
                        Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
                        fCursorDrawable.setAccessible(true);
                        Drawable[] drawables = new Drawable[2];
                        drawables[0] = txtInput.getContext().getResources().getDrawable(mCursorDrawableRes);
                        drawables[1] = txtInput.getContext().getResources().getDrawable(mCursorDrawableRes);
                        drawables[0].setColorFilter(cursorColor, PorterDuff.Mode.SRC_IN);
                        drawables[1].setColorFilter(cursorColor, PorterDuff.Mode.SRC_IN);
                        fCursorDrawable.set(editor, drawables);
                    } catch (Throwable throwable) {
                        log("DialogX: 在对话框" + dialogKey() + "中设置光标颜色时发生错误！");
                        if (DialogX.DEBUGMODE) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }

            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);
            if (backgroundColor != null) {
                tintColor(bkg, backgroundColor);
                if (style.tintButtonBackground()) {
                    tintColor(btnSelectOther, backgroundColor);
                    tintColor(btnSelectNegative, backgroundColor);
                    tintColor(btnSelectPositive, backgroundColor);
                }

                if (blurViews != null) {
                    log("#blurViews != null");
                    for (View blurView : blurViews) {
                        log("#blurView: " + blurView);
                        ((BlurViewType) blurView).setOverlayColor(backgroundColor);
                    }
                }
            }

            bkg.setMaxWidth(getMaxWidth());
            bkg.setMaxHeight(getMaxHeight());
            bkg.setMinimumWidth(getMinWidth());
            bkg.setMinimumHeight(getMinHeight());

            View inputBoxView = boxRoot.findViewWithTag("dialogx_editbox");
            if (me instanceof InputDialog) {
                if (inputBoxView != null) {
                    inputBoxView.setVisibility(View.VISIBLE);
                }
                txtInput.setVisibility(View.VISIBLE);
                boxRoot.bindFocusView(txtInput);
            } else {
                if (inputBoxView != null) {
                    inputBoxView.setVisibility(View.GONE);
                }
                txtInput.setVisibility(View.GONE);
            }
            boxRoot.setClickable(true);
            if (maskColor != null) {
                boxRoot.setBackgroundColor(maskColor);
            }
            if (backgroundRadius > -1) {
                // GradientDrawable gradientDrawable = (GradientDrawable) bkg.getBackground();
                // if (gradientDrawable != null) gradientDrawable.setCornerRadius(backgroundRadius);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

            showText(txtDialogTitle, title == null ? DialogX.defaultMessageDialogTitleText : title);
            showText(txtDialogTip, message);
            showText(btnSelectPositive, okText);
            showText(btnSelectNegative, cancelText);
            showText(btnSelectOther, otherText);

            txtInput.setText(inputText);
            txtInput.setHint(inputHintText);
            if (spaceOtherButton != null) {
                if (otherText == null) {
                    spaceOtherButton.setVisibility(View.GONE);
                } else {
                    spaceOtherButton.setVisibility(View.VISIBLE);
                }
            }

            useTextInfo(txtDialogTitle, titleTextInfo);
            useTextInfo(txtDialogTip, messageTextInfo);
            useTextInfo(btnSelectPositive, okTextInfo);
            useTextInfo(btnSelectNegative, cancelTextInfo);
            useTextInfo(btnSelectOther, otherTextInfo);

            if (boxButton != null) {
                boxButton.setVisibility((btnSelectNegative != null && btnSelectNegative.getVisibility() == View.VISIBLE) ||
                        (btnSelectOther != null && btnSelectOther.getVisibility() == View.VISIBLE) ||
                        (btnSelectPositive != null && btnSelectPositive.getVisibility() == View.VISIBLE) ?
                        View.VISIBLE : View.GONE);
            }
            if (titleIcon != null) {
                int size = (int) txtDialogTitle.getTextSize();
                titleIcon.setBounds(0, 0, size, size);
                txtDialogTitle.setCompoundDrawablePadding(dip2px(10));
                txtDialogTitle.setCompoundDrawables(titleIcon, null, null, null);
            }

            if (inputInfo != null) {
                int inputType = inputInfo.getInputType();
                if (inputInfo.getMAX_LENGTH() != -1) {
                    int inputClass = inputType & InputType.TYPE_MASK_CLASS;
                    if (inputClass != InputType.TYPE_CLASS_TEXT &&
                            inputClass != InputType.TYPE_CLASS_NUMBER &&
                            inputClass != InputType.TYPE_CLASS_PHONE &&
                            inputClass != InputType.TYPE_CLASS_DATETIME) {
                        inputType = (inputType & ~InputType.TYPE_MASK_CLASS) | InputType.TYPE_CLASS_TEXT;
                    }
                    txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                }
                if (inputInfo.isMultipleLines()) {
                    inputType = inputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                }
                txtInput.setInputType(inputType);
                if (inputInfo.getTextInfo() != null) {
                    useTextInfo(txtInput, inputInfo.getTextInfo());
                }
                if (inputInfo.getInputFilters() != null && inputInfo.getInputFilters().length > 0) {
                    txtInput.setFilters(inputInfo.getInputFilters());
                }
            }

            int visibleButtonCount = 0;
            if (!isNull(okText)) {
                visibleButtonCount = visibleButtonCount + 1;
            }
            if (!isNull(cancelText)) {
                visibleButtonCount = visibleButtonCount + 1;
            }
            if (!isNull(otherText)) {
                visibleButtonCount = visibleButtonCount + 1;
            }

            if (splitHorizontal != null) {
                splitHorizontal.setBackgroundColor(getColor(style.splitColorRes(isLightTheme())));
            }

            boxButton.setOrientation(buttonOrientation);
            if (buttonOrientation == LinearLayout.VERTICAL) {
                // 纵向
                if (style.verticalButtonOrder() != null && style.verticalButtonOrder().length != 0) {
                    boxButton.removeAllViews();
                    for (int buttonType : style.verticalButtonOrder()) {
                        switch (buttonType) {
                            case DialogXStyle.BUTTON_OK:
                                boxButton.addView(btnSelectPositive);
                                if (style.overrideVerticalButtonRes() != null) {
                                    btnSelectPositive.setBackgroundResource(style.overrideVerticalButtonRes().overrideVerticalOkButtonBackgroundRes(visibleButtonCount, isLightTheme()));
                                }
                                break;
                            case DialogXStyle.BUTTON_OTHER:
                                boxButton.addView(btnSelectOther);
                                if (style.overrideVerticalButtonRes() != null) {
                                    btnSelectOther.setBackgroundResource(style.overrideVerticalButtonRes().overrideVerticalOtherButtonBackgroundRes(visibleButtonCount, isLightTheme()));
                                }
                                break;
                            case DialogXStyle.BUTTON_CANCEL:
                                boxButton.addView(btnSelectNegative);
                                if (style.overrideVerticalButtonRes() != null) {
                                    btnSelectNegative.setBackgroundResource(style.overrideVerticalButtonRes().overrideVerticalCancelButtonBackgroundRes(visibleButtonCount, isLightTheme()));
                                }
                                break;
                            case DialogXStyle.SPACE:
                                Space space = new Space(getOwnActivity());
                                LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                spaceLp.weight = 1;
                                boxButton.addView(space, spaceLp);
                                break;
                            case DialogXStyle.SPLIT:
                                View splitView = new View(getOwnActivity());
                                splitView.setBackgroundColor(getResources().getColor(style.splitColorRes(isLightTheme())));
                                LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, style.splitWidthPx());
                                boxButton.addView(splitView, viewLp);
                                break;
                        }
                    }
                }
            } else {
                // 横向
                if (style.horizontalButtonOrder() != null && style.horizontalButtonOrder().length != 0) {
                    boxButton.removeAllViews();
                    for (int buttonType : style.horizontalButtonOrder()) {
                        switch (buttonType) {
                            case DialogXStyle.BUTTON_OK:
                                boxButton.addView(btnSelectPositive);
                                if (style.overrideHorizontalButtonRes() != null) {
                                    btnSelectPositive.setBackgroundResource(style.overrideHorizontalButtonRes().overrideHorizontalOkButtonBackgroundRes(visibleButtonCount, isLightTheme()));
                                }
                                break;
                            case DialogXStyle.BUTTON_OTHER:
                                boxButton.addView(btnSelectOther);
                                if (style.overrideHorizontalButtonRes() != null) {
                                    btnSelectOther.setBackgroundResource(style.overrideHorizontalButtonRes().overrideHorizontalOtherButtonBackgroundRes(visibleButtonCount, isLightTheme()));
                                }
                                break;
                            case DialogXStyle.BUTTON_CANCEL:
                                boxButton.addView(btnSelectNegative);
                                if (style.overrideHorizontalButtonRes() != null) {
                                    btnSelectNegative.setBackgroundResource(style.overrideHorizontalButtonRes().overrideHorizontalCancelButtonBackgroundRes(visibleButtonCount, isLightTheme()));
                                }
                                break;
                            case DialogXStyle.SPACE:
                                if (boxButton.getChildCount() >= 1) {
                                    if (boxButton.getChildAt(boxButton.getChildCount() - 1).getVisibility() == View.GONE) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                                Space space = new Space(getOwnActivity());
                                LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                spaceLp.weight = 1;
                                boxButton.addView(space, spaceLp);
                                break;
                            case DialogXStyle.SPLIT:
                                if (boxButton.getChildCount() >= 1) {
                                    if (boxButton.getChildAt(boxButton.getChildCount() - 1).getVisibility() == View.GONE) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                                View splitView = new View(getOwnActivity());
                                splitView.setBackgroundColor(getResources().getColor(style.splitColorRes(isLightTheme())));
                                LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(style.splitWidthPx(), ViewGroup.LayoutParams.MATCH_PARENT);
                                boxButton.addView(splitView, viewLp);
                                break;
                        }
                    }
                }
            }

            // Events
            if (bkgInterceptTouch) {
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
            } else {
                boxRoot.setClickable(false);
            }

            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
                boxCustom.setVisibility(View.VISIBLE);
                if (onBindView.getCustomView() instanceof ScrollController) {
                    if (scrollView instanceof DialogScrollView) {
                        ((DialogScrollView) scrollView).setVerticalScrollBarEnabled(false);
                    }
                    scrollView = (ScrollController) onBindView.getCustomView();
                } else {
                    View scrollController = onBindView.getCustomView().findViewWithTag("ScrollController");
                    if (scrollController instanceof ScrollController) {
                        if (scrollView instanceof DialogScrollView) {
                            ((DialogScrollView) scrollView).setVerticalScrollBarEnabled(false);
                        }
                        scrollView = (ScrollController) scrollController;
                    }
                }
            } else {
                boxCustom.setVisibility(View.GONE);
            }
            onDialogRefreshUI();
        }

        public void doDismiss(View v) {
            if (MessageDialog.this.preDismiss(MessageDialog.this)) {
                return;
            }
            if (v != null) v.setEnabled(false);
            if (getOwnActivity() == null) return;

            if (!dismissAnimFlag && getDialogXAnimImpl() != null) {
                dismissAnimFlag = true;
                getDialogXAnimImpl().doExitAnim(MessageDialog.this, bkg);
                runOnMainDelay(new Runnable() {
                    @Override
                    public void run() {
                        if (boxRoot != null) {
                            boxRoot.setVisibility(View.GONE);
                        }
                        dismiss(getDialogView());
                    }
                }, getExitAnimationDuration(null));
            }
        }

        protected DialogXAnimInterface<MessageDialog> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<MessageDialog>() {
                    @Override
                    public void doShowAnim(MessageDialog dialog, ViewGroup dialogBodyView) {
                        int enterAnimResId = style.enterAnimResId() == 0 ? R.anim.anim_dialogx_default_enter : style.enterAnimResId();
                        if (overrideEnterAnimRes != 0) {
                            enterAnimResId = overrideEnterAnimRes;
                        }
                        if (customEnterAnimResId != 0) {
                            enterAnimResId = customEnterAnimResId;
                        }
                        Animation enterAnim = AnimationUtils.loadAnimation(getOwnActivity(), enterAnimResId);
                        long enterAnimationDuration = getEnterAnimationDuration(enterAnim);
                        enterAnim.setDuration(enterAnimationDuration);
                        enterAnim.setInterpolator(new DecelerateInterpolator());
                        bkg.startAnimation(enterAnim);

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                        bkgAlpha.setDuration(enterAnimationDuration);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                            }
                        });
                        bkgAlpha.start();
                    }

                    @Override
                    public void doExitAnim(MessageDialog dialog, ViewGroup dialogBodyView) {
                        int exitAnimResId = style.exitAnimResId() == 0 ? R.anim.anim_dialogx_default_exit : style.exitAnimResId();
                        if (overrideExitAnimRes != 0) {
                            exitAnimResId = overrideExitAnimRes;
                        }
                        if (customExitAnimResId != 0) {
                            exitAnimResId = customExitAnimResId;
                        }
                        Animation exitAnim = AnimationUtils.loadAnimation(getOwnActivity(), exitAnimResId);
                        long exitAnimationDuration = getExitAnimationDuration(exitAnim);
                        exitAnim.setInterpolator(new AccelerateInterpolator());
                        exitAnim.setDuration(exitAnimationDuration);
                        bkg.startAnimation(exitAnim);

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1f, 0f);
                        bkgAlpha.setDuration(exitAnimationDuration);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
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
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    public void dismiss() {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                if (dialogImpl == null) return;
                dialogImpl.doDismiss(dialogImpl.bkg);
            }
        });
    }

    public DialogLifecycleCallback<MessageDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<MessageDialog>() {
        } : dialogLifecycleCallback;
    }

    public MessageDialog setDialogLifecycleCallback(DialogLifecycleCallback<MessageDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public MessageDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public MessageDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }

    public CharSequence getOkButton() {
        return okText;
    }

    public MessageDialog setOkButton(CharSequence okText) {
        this.okText = okText;
        refreshUI();
        return this;
    }

    public MessageDialog setOkButton(int okTextRedId) {
        this.okText = getString(okTextRedId);
        refreshUI();
        return this;
    }

    public MessageDialog setOkButton(OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public MessageDialog setOkButton(CharSequence okText, OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }

    public MessageDialog setOkButton(int okTextRedId, OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okText = getString(okTextRedId);
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelText;
    }

    public MessageDialog setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }

    public MessageDialog setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        refreshUI();
        return this;
    }

    public MessageDialog setCancelButton(OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public MessageDialog setCancelButton(CharSequence cancelText, OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }

    public MessageDialog setCancelButton(int cancelTextResId, OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }

    public CharSequence getOtherButton() {
        return otherText;
    }

    public MessageDialog setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        refreshUI();
        return this;
    }

    public MessageDialog setOtherButton(int otherTextResId) {
        this.otherText = getString(otherTextResId);
        refreshUI();
        return this;
    }

    public MessageDialog setOtherButton(OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public MessageDialog setOtherButton(CharSequence otherText, OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }

    public MessageDialog setOtherButton(int otherTextResId, OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }

    public OnDialogButtonClickListener<MessageDialog> getOkButtonClickListener() {
        return (OnDialogButtonClickListener<MessageDialog>) okButtonClickListener;
    }

    public MessageDialog setOkButtonClickListener(OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public OnDialogButtonClickListener<MessageDialog> getCancelButtonClickListener() {
        return (OnDialogButtonClickListener<MessageDialog>) cancelButtonClickListener;
    }

    public MessageDialog setCancelButtonClickListener(OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public OnDialogButtonClickListener<MessageDialog> getOtherButtonClickListener() {
        return (OnDialogButtonClickListener<MessageDialog>) otherButtonClickListener;
    }

    public MessageDialog setOtherButtonClickListener(OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public MessageDialog setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }

    public MessageDialog setTitle(int titleResId) {
        this.title = getString(titleResId);
        refreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public MessageDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }

    public MessageDialog setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public MessageDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public MessageDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getOkTextInfo() {
        return okTextInfo;
    }

    public MessageDialog setOkTextInfo(TextInfo okTextInfo) {
        this.okTextInfo = okTextInfo;
        refreshUI();
        return this;
    }

    public MessageDialog setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }

    public MessageDialog setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getOtherTextInfo() {
        return otherTextInfo;
    }

    public MessageDialog setOtherTextInfo(TextInfo otherTextInfo) {
        this.otherTextInfo = otherTextInfo;
        refreshUI();
        return this;
    }

    public int getButtonOrientation() {
        return buttonOrientation;
    }

    public MessageDialog setButtonOrientation(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshUI();
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

    public MessageDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }

    public OnBackPressedListener<MessageDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<MessageDialog>) onBackPressedListener;
    }

    public MessageDialog setOnBackPressedListener(OnBackPressedListener<MessageDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        return this;
    }

    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public MessageDialog setCustomView(OnBindView<MessageDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public MessageDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public MessageDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }

    public String getInputText() {
        if (dialogImpl.txtInput != null) {
            return dialogImpl.txtInput.getText().toString();
        } else {
            return "";
        }
    }

    public MessageDialog setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }

    public MessageDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public MessageDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public MessageDialog setExitAnimDuration(long exitAnimDuration) {
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
        if (getDialogImpl().boxList != null) {
            getDialogImpl().boxList.removeAllViews();
        }
        int layoutId = style.layout(isLightTheme());
        layoutId = layoutId == 0 ? (isLightTheme() ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark) : layoutId;

        enterAnimDuration = 0;
        View dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
    }

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
            }, getDialogImpl().getExitAnimationDuration(null));
        }
    }

    public MessageDialog setAnimResId(int enterResId, int exitResId) {
        customEnterAnimResId = enterResId;
        customExitAnimResId = exitResId;
        return this;
    }

    public MessageDialog setEnterAnimResId(int enterResId) {
        customEnterAnimResId = enterResId;
        return this;
    }

    public MessageDialog setExitAnimResId(int exitResId) {
        customExitAnimResId = exitResId;
        return this;
    }

    @Override
    protected void shutdown() {
        dismiss();
    }

    public MessageDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public MessageDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public MessageDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public MessageDialog setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public MessageDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public MessageDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<MessageDialog> getOnBackgroundMaskClickListener() {
        return (OnBackgroundMaskClickListener<MessageDialog>) onBackgroundMaskClickListener;
    }

    public MessageDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<MessageDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public MessageDialog setRadius(float radiusPx) {
        backgroundRadius = radiusPx;
        refreshUI();
        return this;
    }

    public float getRadius() {
        return backgroundRadius;
    }

    public Drawable getTitleIcon() {
        return titleIcon;
    }

    public MessageDialog setTitleIcon(Bitmap titleIcon) {
        this.titleIcon = new BitmapDrawable(getResources(), titleIcon);
        refreshUI();
        return this;
    }

    public MessageDialog setTitleIcon(int titleIconResId) {
        this.titleIcon = getResources().getDrawable(titleIconResId);
        refreshUI();
        return this;
    }

    public MessageDialog setTitleIcon(Drawable titleIcon) {
        this.titleIcon = titleIcon;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<MessageDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public MessageDialog setDialogXAnimImpl(DialogXAnimInterface<MessageDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public MessageDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public MessageDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public BUTTON_SELECT_RESULT getButtonSelectResult() {
        return buttonSelectResult;
    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new MessageDialog() {
     *
     * @param dialog self
     * @Override public void onShow(MessageDialog dialog) {
     * //...
     * }
     * }
     */
    protected void onShow(MessageDialog dialog) {

    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new MessageDialog() {
     *
     * @param dialog self
     * @Override public boolean onDismiss(MessageDialog dialog) {
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
    // 用于使用 new 构建实例时，override 的生命周期事件
    protected void onDismiss(MessageDialog dialog) {

    }

    public MessageDialog setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public MessageDialog onShow(DialogXRunnable<MessageDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public MessageDialog onDismiss(DialogXRunnable<MessageDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public MessageDialog setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public MessageDialog appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public MessageDialog setThisOrderIndex(int orderIndex) {
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

    public MessageDialog bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public MessageDialog setActionRunnable(int actionId, DialogXRunnable<MessageDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public MessageDialog cleanAction(int actionId){
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public MessageDialog cleanAllAction(){
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss(){
        dismiss();
    }

    public MessageDialog bindDismissWithLifecycleOwner(LifecycleOwner owner){
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }
}
