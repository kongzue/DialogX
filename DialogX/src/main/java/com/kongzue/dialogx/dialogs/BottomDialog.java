package com.kongzue.dialogx.dialogs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.BaseOnDialogClickCallback;
import com.kongzue.dialogx.interfaces.BlurViewType;
import com.kongzue.dialogx.interfaces.BottomDialogSlideEventLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXBaseBottomDialog;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuButtonClickListener;
import com.kongzue.dialogx.interfaces.ScrollController;
import com.kongzue.dialogx.util.BottomDialogTouchEventInterceptor;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.DialogScrollView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 15:17
 */
public class BottomDialog extends BaseDialog implements DialogXBaseBottomDialog {

    public static int overrideEnterDuration = -1;
    public static int overrideExitDuration = -1;
    public static BOOLEAN overrideCancelable;
    protected OnBindView<BottomDialog> onBindView;
    protected CharSequence title;
    protected CharSequence message;
    protected CharSequence cancelText;
    protected CharSequence okText;
    protected CharSequence otherText;
    protected boolean allowInterceptTouch = true;
    protected boolean bottomNonSafetyAreaBySelf = false;
    protected Integer maskColor = null;
    protected BaseOnDialogClickCallback cancelButtonClickListener;
    protected BaseOnDialogClickCallback okButtonClickListener;
    protected BaseOnDialogClickCallback otherButtonClickListener;
    protected OnBackgroundMaskClickListener<BottomDialog> onBackgroundMaskClickListener;
    protected OnBackPressedListener<BottomDialog> onBackPressedListener;
    protected BOOLEAN privateCancelable;
    protected boolean bkgInterceptTouch = true;
    protected float backgroundRadius = DialogX.defaultBottomDialogBackgroundRadius;
    protected Drawable titleIcon;
    protected DialogXAnimInterface<BottomDialog> dialogXAnimImpl;
    protected BUTTON_SELECT_RESULT buttonSelectResult = BUTTON_SELECT_RESULT.NONE;
    protected boolean scrollableWhenContentLargeThanVisibleRange = true;

    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo menuTextInfo;
    protected TextInfo cancelTextInfo = new TextInfo().setBold(true);
    protected TextInfo okTextInfo = new TextInfo().setBold(true);
    protected TextInfo otherTextInfo = new TextInfo().setBold(true);

    /**
     * 此值用于，当禁用滑动时（style.overrideBottomDialogRes.touchSlide = false时）的最大显示高度。
     * 0：不限制，最大显示到屏幕可用高度。
     */
    protected float bottomDialogMaxHeight = 0f;

    protected DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback;

    protected BottomDialog me = this;

    protected BottomDialog() {
        super();
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    public static BottomDialog build() {
        return new BottomDialog();
    }

    public static BottomDialog build(DialogXStyle style) {
        return new BottomDialog().setStyle(style);
    }

    public static BottomDialog build(OnBindView<BottomDialog> onBindView) {
        return new BottomDialog().setCustomView(onBindView);
    }

    public static BottomDialog show(CharSequence title, CharSequence message) {
        BottomDialog bottomDialog = new BottomDialog(title, message);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, int messageResId) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, messageResId);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(CharSequence title, CharSequence message, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(title, message, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, int messageResId, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, messageResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(CharSequence title, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(title, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, int messageResId, int okTextResId) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, messageResId, okTextResId);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, messageResId, okTextResId, cancelTextResId);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, int messageResId, int okTextResId, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, messageResId, okTextResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public static BottomDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(titleResId, messageResId, okTextResId, cancelTextResId, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }

    public BottomDialog(OnBindView<BottomDialog> onBindView) {
        this.onBindView = onBindView;
    }

    public BottomDialog(CharSequence title, OnBindView<BottomDialog> onBindView) {
        this.title = title;
        this.onBindView = onBindView;
    }

    public BottomDialog(int titleResId, OnBindView<BottomDialog> onBindView) {
        this.title = getString(titleResId);
        this.onBindView = onBindView;
    }

    public BottomDialog(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
    }

    public BottomDialog(int titleResId, int messageResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }

    public BottomDialog(CharSequence title, CharSequence message, OnBindView<BottomDialog> onBindView) {
        this.title = title;
        this.message = message;
        this.onBindView = onBindView;
    }

    public BottomDialog(int titleResId, int messageResId, OnBindView<BottomDialog> onBindView) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.onBindView = onBindView;
    }

    public BottomDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
    }

    public BottomDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
    }

    public BottomDialog(int titleResId, int messageResId, int okTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
    }

    public BottomDialog(CharSequence title, CharSequence message, CharSequence okText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
    }

    public BottomDialog(int titleResId, int messageResId, int okTextResId, OnBindView<BottomDialog> onBindView) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.onBindView = onBindView;
    }

    public BottomDialog(CharSequence title, CharSequence message, CharSequence okText, OnBindView<BottomDialog> onBindView) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.onBindView = onBindView;
    }

    public BottomDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId, OnBindView<BottomDialog> onBindView) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
        this.onBindView = onBindView;
    }

    public BottomDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, OnBindView<BottomDialog> onBindView) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.onBindView = onBindView;
    }

    public BottomDialog show() {
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
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            if (style.overrideBottomDialogRes() != null) {
                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
            }

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
            int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
            if (style.overrideBottomDialogRes() != null) {
                layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
            }

            View dialogView = createView(layoutId);
            dialogImpl = new DialogImpl(dialogView);
            if (dialogView != null) dialogView.setTag(me);
            show(activity, dialogView);
        } else {
            show(activity, getDialogView());
        }
    }

    protected DialogImpl dialogImpl;

    public class DialogImpl implements DialogConvertViewInterface {

        private BottomDialogTouchEventInterceptor bottomDialogTouchEventInterceptor;

        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxBkg;
        public MaxRelativeLayout bkg;
        public ImageView imgTab;
        public ViewGroup boxBody;
        public TextView txtDialogTitle;
        public ScrollController scrollView;
        public LinearLayout boxContent;
        public TextView txtDialogTip;
        public View imgSplit;
        public ViewGroup boxList;
        public RelativeLayout boxCustom;
        public ViewGroup boxCancel;
        public ImageView splitSelectPositive;
        public ImageView splitSelectOther;

        public LinearLayout boxButton;
        public TextView btnSelectNegative;
        public TextView btnSelectOther;
        public TextView btnSelectPositive;

        private List<View> blurViews;

        public DialogImpl(View convertView) {
            if (convertView == null) return;
            setDialogView(convertView);
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            bkg = convertView.findViewById(R.id.bkg);
            imgTab = convertView.findViewById(R.id.img_tab);
            boxBody = convertView.findViewById(R.id.box_body);
            txtDialogTitle = convertView.findViewById(R.id.txt_dialog_title);
            scrollView = convertView.findViewById(R.id.scrollView);
            boxContent = convertView.findViewById(R.id.box_content);
            txtDialogTip = convertView.findViewById(R.id.txt_dialog_tip);
            imgSplit = convertView.findViewWithTag("split");
            boxList = convertView.findViewById(R.id.box_list);
            boxCustom = convertView.findViewById(R.id.box_custom);

            if (!scrollableWhenContentLargeThanVisibleRange) {
                ViewGroup bodyContent = (ViewGroup) txtDialogTitle.getParent();
                ((ViewGroup) boxContent.getParent()).removeView(boxContent);
                bodyContent.addView(boxContent, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            boxCancel = convertView.findViewWithTag("cancelBox");

            boxButton = convertView.findViewById(R.id.box_button);
            btnSelectNegative = convertView.findViewById(R.id.btn_selectNegative);
            btnSelectOther = convertView.findViewById(R.id.btn_selectOther);
            btnSelectPositive = convertView.findViewById(R.id.btn_selectPositive);
            splitSelectPositive = convertView.findViewWithTag("imgPositiveButtonSplit");
            splitSelectOther = convertView.findViewWithTag("imgOtherButtonSplit");

            blurViews = findAllBlurView(convertView);

            init();
            dialogImpl = this;
            refreshView();
        }

        public void reBuild() {
            init();
            dialogImpl = this;
            refreshView();
        }

        /**
         * 此值记录了BottomDialog启动后的位置
         * ·当内容高度大于屏幕安全区高度时，BottomDialog会以全屏方式启动，但一开始只会展开到 0.8×屏幕高度，
         * 此时可以再次上划查看全部内容。
         * ·当内容高度小于屏幕安全区高度时，BottomDialog会以内容高度启动。
         * <p>
         * 记录这个值的目的是，当用户向下滑动时，判断情况该回到这个位置还是关闭对话框，
         * 并阻止当内容高度已经完全显示时的继续向上滑动操作。
         */
        public float bkgEnterAimY = -1;

        @Override
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
            if (backgroundColor == null) backgroundColor = DialogX.backgroundColor;
            if (cancelText == null) cancelText = DialogX.cancelButtonText;

            if (txtDialogTitle != null) txtDialogTitle.getPaint().setFakeBoldText(true);
            if (btnSelectNegative != null) btnSelectNegative.getPaint().setFakeBoldText(true);
            if (btnSelectPositive != null) btnSelectPositive.getPaint().setFakeBoldText(true);
            if (btnSelectOther != null) btnSelectOther.getPaint().setFakeBoldText(true);

            boxBkg.setY(getRootFrameLayout() == null ? Resources.getSystem().getDisplayMetrics().heightPixels : getRootFrameLayout().getMeasuredHeight());

            boxRoot.setParentDialog(me);
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {

                    isShow = true;
                    preShow = false;

                    setLifecycleState(Lifecycle.State.CREATED);
                    getDialogLifecycleCallback().onShow(me);
                    BottomDialog.this.onShow(me);

                    onDialogShow();

                    refreshUI();
                }

                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                    BottomDialog.this.onDismiss(me);
                    setLifecycleState(Lifecycle.State.DESTROYED);
                    dialogImpl = null;
                    bottomDialogTouchEventInterceptor = null;
                    dialogLifecycleCallback = null;
                    System.gc();
                }
            });

            if (btnSelectNegative != null) {
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        haptic(v);
                        buttonSelectResult = BUTTON_SELECT_RESULT.BUTTON_CANCEL;
                        if (cancelButtonClickListener != null) {
                            if (cancelButtonClickListener instanceof OnDialogButtonClickListener) {
                                if (!((OnDialogButtonClickListener) cancelButtonClickListener).onClick(me, v)) {
                                    dismiss();
                                }
                            } else if (cancelButtonClickListener instanceof OnMenuButtonClickListener) {
                                if (!((OnMenuButtonClickListener) cancelButtonClickListener).onClick(me, v)) {
                                    dismiss();
                                }
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
            }
            if (btnSelectOther != null) {
                btnSelectOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        haptic(v);
                        buttonSelectResult = BUTTON_SELECT_RESULT.BUTTON_OTHER;
                        if (otherButtonClickListener != null) {
                            if (otherButtonClickListener instanceof OnDialogButtonClickListener) {
                                if (!((OnDialogButtonClickListener) otherButtonClickListener).onClick(me, v)) {
                                    dismiss();
                                }
                            } else if (otherButtonClickListener instanceof OnMenuButtonClickListener) {
                                if (!((OnMenuButtonClickListener) otherButtonClickListener).onClick(me, v)) {
                                    dismiss();
                                }
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
            }
            if (btnSelectPositive != null) {
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        haptic(v);
                        buttonSelectResult = BUTTON_SELECT_RESULT.BUTTON_OK;
                        if (okButtonClickListener != null) {
                            if (okButtonClickListener instanceof OnDialogButtonClickListener) {
                                if (!((OnDialogButtonClickListener) okButtonClickListener).onClick(me, v)) {
                                    dismiss();
                                }
                            } else if (okButtonClickListener instanceof OnMenuButtonClickListener) {
                                if (!((OnMenuButtonClickListener) okButtonClickListener).onClick(me, v)) {
                                    dismiss();
                                }
                            }
                        } else {
                            dismiss();
                        }
                    }
                });
            }

            if (imgSplit != null) {
                int dividerRes = style.overrideBottomDialogRes().overrideMenuDividerDrawableRes(isLightTheme());
                int dividerHeight = style.overrideBottomDialogRes().overrideMenuDividerHeight(isLightTheme());
                if (dividerRes != 0) imgSplit.setBackgroundResource(dividerRes);
                if (dividerHeight != 0) {
                    ViewGroup.LayoutParams lp = imgSplit.getLayoutParams();
                    lp.height = dividerHeight;
                    imgSplit.setLayoutParams(lp);
                }
            }

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

            boxBkg.post(new Runnable() {
                @Override
                public void run() {
                    getDialogXAnimImpl().doShowAnim(BottomDialog.this, bkg);

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
                }
            });

            runOnMainDelay(new Runnable() {
                @Override
                public void run() {
                    bottomDialogTouchEventInterceptor = new BottomDialogTouchEventInterceptor(me, dialogImpl);
                }
            }, getEnterAnimationDuration());

            onDialogInit();
        }

        @Override
        public void refreshView() {
            if (boxRoot == null || getOwnActivity() == null) {
                return;
            }

            bkg.setMaxWidth(getMaxWidth());
            bkg.setMaxHeight(getMaxHeight());
            bkg.setMinimumWidth(getMinWidth());
            bkg.setMinimumHeight(getMinHeight());

            boxRoot.setAutoUnsafePlacePadding(isEnableImmersiveMode());
            boxRoot.setRootPadding(screenPaddings[0], screenPaddings[1], screenPaddings[2], screenPaddings[3]);
            if (backgroundColor != null) {
                tintColor(bkg, backgroundColor);
                if (style.tintButtonBackground()) {
                    tintColor(btnSelectOther, backgroundColor);
                    tintColor(btnSelectNegative, backgroundColor);
                    tintColor(btnSelectPositive, backgroundColor);
                }

                if (blurViews != null) {
                    for (View blurView : blurViews) {
                        ((BlurViewType) blurView).setOverlayColor(backgroundColor);
                    }
                }
            }

            showText(txtDialogTitle, title);
            showText(txtDialogTip, message);

            useTextInfo(txtDialogTitle, titleTextInfo);
            useTextInfo(txtDialogTip, messageTextInfo);
            useTextInfo(btnSelectNegative, cancelTextInfo);
            useTextInfo(btnSelectOther, otherTextInfo);
            useTextInfo(btnSelectPositive, okTextInfo);

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
            boxBkg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boxRoot.callOnClick();
                }
            });
            if (backgroundRadius > -1) {
                if (bkg.getBackground() instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) bkg.getBackground();
                    if (gradientDrawable != null)
                        gradientDrawable.setCornerRadii(new float[]{backgroundRadius, backgroundRadius, backgroundRadius, backgroundRadius, 0, 0, 0, 0});
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bkg.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, view.getWidth(), (int) (view.getHeight() + backgroundRadius), backgroundRadius);
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

            if (maskColor != null) {
                boxRoot.setBackground(new ColorDrawable(maskColor));
            }

            if (onBindView != null && onBindView.getCustomView() != null) {
                onBindView.bindParent(boxCustom, me);
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
            }

            if (isAllowInterceptTouch() && isCancelable()) {
                if (imgTab != null) imgTab.setVisibility(View.VISIBLE);
            } else {
                if (imgTab != null) imgTab.setVisibility(View.GONE);
            }

            if (bottomDialogTouchEventInterceptor != null) {
                bottomDialogTouchEventInterceptor.refresh(me, this);
            }

            if (imgSplit != null) {
                if (txtDialogTitle.getVisibility() == View.VISIBLE || txtDialogTip.getVisibility() == View.VISIBLE) {
                    imgSplit.setVisibility(View.VISIBLE);
                } else {
                    imgSplit.setVisibility(View.GONE);
                }
            }

            if (boxCancel != null) {
                if (isNull(cancelText)) {
                    boxCancel.setVisibility(View.GONE);
                } else {
                    boxCancel.setVisibility(View.VISIBLE);
                }
            }

            showText(btnSelectPositive, okText);
            showText(btnSelectNegative, cancelText);
            showText(btnSelectOther, otherText);
            if (splitSelectPositive != null) {
                splitSelectPositive.setVisibility(btnSelectPositive.getVisibility());
            }
            if (splitSelectOther != null) {
                splitSelectOther.setVisibility(btnSelectOther.getVisibility());
            }

            onDialogRefreshUI();
        }

        @Override
        public void doDismiss(View v) {
            if (BottomDialog.this.preDismiss(BottomDialog.this)) {
                return;
            }
            if (v != null) v.setEnabled(false);
            if (getOwnActivity() == null) return;

            if (!dismissAnimFlag && getDialogXAnimImpl() != null) {
                dismissAnimFlag = true;

                getDialogXAnimImpl().doExitAnim(BottomDialog.this, bkg);

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
                if (getDialogLifecycleCallback() instanceof BottomDialogSlideEventLifecycleCallback) {
                    if (!((BottomDialogSlideEventLifecycleCallback) getDialogLifecycleCallback()).onSlideClose(me)) {
                        doDismiss(boxRoot);
                    }
                    return;
                }
                doDismiss(boxRoot);
            } else {
                long exitAnimDurationTemp = 300;
                if (overrideExitDuration >= 0) {
                    exitAnimDurationTemp = overrideExitDuration;
                }
                if (exitAnimDuration >= 0) {
                    exitAnimDurationTemp = exitAnimDuration;
                }
                ObjectAnimator exitAnim = ObjectAnimator.ofFloat(boxBkg, "y", boxBkg.getY(), boxRoot.getUnsafePlace().top);
                exitAnim.setDuration(exitAnimDurationTemp);
                exitAnim.start();
            }
        }

        protected DialogXAnimInterface<BottomDialog> getDialogXAnimImpl() {
            if (dialogXAnimImpl == null) {
                dialogXAnimImpl = new DialogXAnimInterface<BottomDialog>() {
                    @Override
                    public void doShowAnim(BottomDialog dialog, ViewGroup dialogBodyView) {
                        long enterAnimDurationTemp = getEnterAnimationDuration();

                        float customDialogTop = 0;
                        if (dialog.isAllowInterceptTouch()) {
                            if (bottomDialogMaxHeight > 0 && bottomDialogMaxHeight <= 1) {
                                customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight * boxBkg.getHeight();
                            } else if (bottomDialogMaxHeight > 1) {
                                customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight;
                            }
                        } else {
                            if (bottomDialogMaxHeight > 0 && bottomDialogMaxHeight <= 1) {
                                customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight * boxBkg.getHeight();
                            } else if (bottomDialogMaxHeight > 1) {
                                customDialogTop = boxBkg.getHeight() - bottomDialogMaxHeight;
                            }
                            boxBkg.setPadding(0, 0, 0, (int) customDialogTop);
                        }

                        // 上移动画
                        ObjectAnimator enterAnim = ObjectAnimator.ofFloat(boxBkg, "y", getRootFrameLayout() == null ? Resources.getSystem().getDisplayMetrics().heightPixels : getRootFrameLayout().getMeasuredHeight(), bkgEnterAimY = boxRoot.getUnsafePlace().top + customDialogTop);
                        enterAnim.setDuration(enterAnimDurationTemp);
                        enterAnim.setAutoCancel(true);
                        enterAnim.setInterpolator(new DecelerateInterpolator(2f));
                        enterAnim.start();

                        // 遮罩层动画
                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
                        bkgAlpha.setDuration(enterAnimDurationTemp);
                        bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
                            }
                        });
                        bkgAlpha.start();
                    }

                    @Override
                    public void doExitAnim(BottomDialog dialog, ViewGroup dialogBodyView) {
                        long exitAnimDurationTemp = getExitAnimationDuration();

                        ObjectAnimator exitAnim = ObjectAnimator.ofFloat(boxBkg, "y", boxBkg.getY(), boxBkg.getHeight());
                        exitAnim.setDuration(exitAnimDurationTemp);
                        exitAnim.start();

                        ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1f, 0f);
                        bkgAlpha.setDuration(exitAnimDurationTemp);
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

        public BottomDialogTouchEventInterceptor getBottomDialogTouchEventInterceptor() {
            return bottomDialogTouchEventInterceptor;
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

    public DialogLifecycleCallback<BottomDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<BottomDialog>() {
        } : dialogLifecycleCallback;
    }

    public BottomDialog setDialogLifecycleCallback(DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public OnBackPressedListener<BottomDialog> getOnBackPressedListener() {
        return (OnBackPressedListener<BottomDialog>) onBackPressedListener;
    }

    public BottomDialog setOnBackPressedListener(OnBackPressedListener<BottomDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }

    public BottomDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public BottomDialog setTheme(DialogX.THEME theme) {
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

    public BottomDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }

    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public CharSequence getTitle() {
        return title;
    }

    public BottomDialog setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }

    public BottomDialog setTitle(int titleResId) {
        this.title = getString(titleResId);
        refreshUI();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public BottomDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }

    public BottomDialog setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelText;
    }

    public BottomDialog setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }

    public BottomDialog setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        refreshUI();
        return this;
    }

    public BottomDialog setCancelButton(OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public BottomDialog setCancelButton(CharSequence cancelText, OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }

    public BottomDialog setCancelButton(int cancelTextResId, OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }

    public BottomDialog setCustomView(OnBindView<BottomDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }

    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }

    public BottomDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }

    public boolean isAllowInterceptTouch() {
        if (style.overrideBottomDialogRes() == null) {
            return false;
        } else {
            return allowInterceptTouch && style.overrideBottomDialogRes().touchSlide();
        }
    }

    public BottomDialog setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        return this;
    }

    public OnDialogButtonClickListener<BottomDialog> getCancelButtonClickListener() {
        return (OnDialogButtonClickListener<BottomDialog>) cancelButtonClickListener;
    }

    public BottomDialog setCancelButtonClickListener(OnDialogButtonClickListener<BottomDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public BottomDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public BottomDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }

    public BottomDialog setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        refreshUI();
        return this;
    }

    public TextInfo getOkTextInfo() {
        return okTextInfo;
    }

    public BottomDialog setOkTextInfo(TextInfo okTextInfo) {
        this.okTextInfo = okTextInfo;
        return this;
    }

    public TextInfo getOtherTextInfo() {
        return otherTextInfo;
    }

    public BottomDialog setOtherTextInfo(TextInfo otherTextInfo) {
        this.otherTextInfo = otherTextInfo;
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public BottomDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }

    public BottomDialog setBackgroundColorRes(@ColorRes int backgroundRes) {
        this.backgroundColor = getColor(backgroundRes);
        refreshUI();
        return this;
    }

    public CharSequence getOkButton() {
        return okText;
    }

    public BottomDialog setOkButton(CharSequence okText) {
        this.okText = okText;
        refreshUI();
        return this;
    }

    public BottomDialog setOkButton(int OkTextResId) {
        this.okText = getString(OkTextResId);
        refreshUI();
        return this;
    }

    public BottomDialog setOkButton(OnDialogButtonClickListener<BottomDialog> OkButtonClickListener) {
        this.okButtonClickListener = OkButtonClickListener;
        return this;
    }

    public BottomDialog setOkButton(CharSequence OkText, OnDialogButtonClickListener<BottomDialog> OkButtonClickListener) {
        this.okText = OkText;
        this.okButtonClickListener = OkButtonClickListener;
        refreshUI();
        return this;
    }

    public BottomDialog setOkButton(int OkTextResId, OnDialogButtonClickListener<BottomDialog> OkButtonClickListener) {
        this.okText = getString(OkTextResId);
        this.okButtonClickListener = OkButtonClickListener;
        refreshUI();
        return this;
    }

    public CharSequence getOtherButton() {
        return otherText;
    }

    public BottomDialog setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        refreshUI();
        return this;
    }

    public BottomDialog setOtherButton(int OtherTextResId) {
        this.otherText = getString(OtherTextResId);
        refreshUI();
        return this;
    }

    public BottomDialog setOtherButton(OnDialogButtonClickListener<BottomDialog> OtherButtonClickListener) {
        this.otherButtonClickListener = OtherButtonClickListener;
        return this;
    }

    public BottomDialog setOtherButton(CharSequence OtherText, OnDialogButtonClickListener<BottomDialog> OtherButtonClickListener) {
        this.otherText = OtherText;
        this.otherButtonClickListener = OtherButtonClickListener;
        refreshUI();
        return this;
    }

    public BottomDialog setOtherButton(int OtherTextResId, OnDialogButtonClickListener<BottomDialog> OtherButtonClickListener) {
        this.otherText = getString(OtherTextResId);
        this.otherButtonClickListener = OtherButtonClickListener;
        refreshUI();
        return this;
    }

    public BottomDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }

    public long getEnterAnimDuration() {
        return enterAnimDuration;
    }

    public BottomDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public long getExitAnimDuration() {
        return exitAnimDuration;
    }

    public BottomDialog setExitAnimDuration(long exitAnimDuration) {
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
        int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
        if (style.overrideBottomDialogRes() != null) {
            layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
        }

        enterAnimDuration = 0;
        View dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        if (dialogView != null) dialogView.setTag(me);
        show(dialogView);
    }

    protected boolean isHide;

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

    public float getBottomDialogMaxHeight() {
        return bottomDialogMaxHeight;
    }

    public BottomDialog setBottomDialogMaxHeight(float bottomDialogMaxHeight) {
        this.bottomDialogMaxHeight = bottomDialogMaxHeight;
        return this;
    }

    public BottomDialog setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        refreshUI();
        return this;
    }

    public BottomDialog setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        refreshUI();
        return this;
    }

    public BottomDialog setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        refreshUI();
        return this;
    }

    public BottomDialog setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        refreshUI();
        return this;
    }

    public BottomDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public boolean isBkgInterceptTouch() {
        return bkgInterceptTouch;
    }

    public BottomDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public OnBackgroundMaskClickListener<BottomDialog> getOnBackgroundMaskClickListener() {
        return (OnBackgroundMaskClickListener<BottomDialog>) onBackgroundMaskClickListener;
    }

    public BottomDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<BottomDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    public BottomDialog setRadius(float radiusPx) {
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

    public BottomDialog setTitleIcon(Bitmap titleIcon) {
        this.titleIcon = new BitmapDrawable(getResources(), titleIcon);
        refreshUI();
        return this;
    }

    public BottomDialog setTitleIcon(int titleIconResId) {
        this.titleIcon = getResources().getDrawable(titleIconResId);
        refreshUI();
        return this;
    }

    public BottomDialog setTitleIcon(Drawable titleIcon) {
        this.titleIcon = titleIcon;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<BottomDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public BottomDialog setDialogXAnimImpl(DialogXAnimInterface<BottomDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public BottomDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public BottomDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
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
     * new BottomDialog() {
     *
     * @param dialog self
     * @Override public void onShow(BottomDialog dialog) {
     * //...
     * }
     * }
     */
    protected void onShow(BottomDialog dialog) {

    }

    /**
     * 用于使用 new 构建实例时，override 的生命周期事件
     * 例如：
     * new BottomDialog() {
     *
     * @param dialog self
     * @Override public boolean onDismiss(BottomDialog dialog) {
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
    protected void onDismiss(BottomDialog dialog) {

    }

    public boolean isBottomNonSafetyAreaBySelf() {
        return bottomNonSafetyAreaBySelf;
    }

    public BottomDialog setBottomNonSafetyAreaBySelf(boolean bottomNonSafetyAreaBySelf) {
        this.bottomNonSafetyAreaBySelf = bottomNonSafetyAreaBySelf;
        return this;
    }

    public boolean isScrollableWhenContentLargeThanVisibleRange() {
        return scrollableWhenContentLargeThanVisibleRange;
    }

    public BottomDialog setScrollableWhenContentLargeThanVisibleRange(boolean scrollableWhenContentLargeThanVisibleRange) {
        this.scrollableWhenContentLargeThanVisibleRange = scrollableWhenContentLargeThanVisibleRange;
        return this;
    }

    public BottomDialog setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public BottomDialog setHapticFeedbackEnabled(boolean isHapticFeedbackEnabled) {
        this.isHapticFeedbackEnabled = isHapticFeedbackEnabled ? 1 : 0;
        return this;
    }

    public BottomDialog onShow(DialogXRunnable<BottomDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public BottomDialog onDismiss(DialogXRunnable<BottomDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public BottomDialog setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public BottomDialog appendMessage(CharSequence message) {
        this.message = TextUtils.concat(this.message, message);
        refreshUI();
        return this;
    }

    public BottomDialog setThisOrderIndex(int orderIndex) {
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

    public BottomDialog bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public BottomDialog setActionRunnable(int actionId, DialogXRunnable<BottomDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public BottomDialog cleanAction(int actionId) {
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public BottomDialog cleanAllAction() {
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss() {
        dismiss();
    }

    public BottomDialog bindDismissWithLifecycleOwner(LifecycleOwner owner) {
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }
}
