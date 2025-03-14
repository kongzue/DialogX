package com.kongzue.dialogx.dialogs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.lifecycle.LifecycleOwner;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/8/19 16:35
 */
public class GuideDialog extends CustomDialog {

    public enum STAGE_LIGHT_TYPE {
        RECTANGLE,      //矩形
        SQUARE_OUTSIDE, //方形（外围）
        SQUARE_INSIDE,  //方形（内围）
        CIRCLE_OUTSIDE, //圆形（外围）
        CIRCLE_INSIDE,  //圆形（内围）
    }

    protected STAGE_LIGHT_TYPE stageLightType = STAGE_LIGHT_TYPE.CIRCLE_OUTSIDE;
    protected Drawable tipImage;
    protected float stageLightFilletRadius;     //舞台灯光部分的圆角
    protected Integer maskColor = null;
    protected OnDialogButtonClickListener<GuideDialog> onStageLightPathClickListener;
    protected int[] baseViewLocationCoordinateCompensation = new int[4];

    protected GuideDialog() {
        super();
        enterAnimResId = R.anim.anim_dialogx_alpha_enter;
        exitAnimResId = R.anim.anim_dialogx_default_exit;
        this.alignViewGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    }

    public static GuideDialog build() {
        return new GuideDialog();
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType) {
        this();
        this.baseView(baseView);
        this.stageLightType = stageLightType;
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, OnBindView<CustomDialog> onBindView, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.stageLightType = stageLightType;
        this.onBindView = onBindView;
        this.alignViewGravity = alignBaseViewGravity;
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, int tipImageResId, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.tipImage = getResources().getDrawable(tipImageResId);
        this.stageLightType = stageLightType;
        this.alignViewGravity = alignBaseViewGravity;
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, Bitmap tipImage, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
        this.stageLightType = stageLightType;
        this.alignViewGravity = alignBaseViewGravity;
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, Drawable tipImage, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.tipImage = tipImage;
        this.stageLightType = stageLightType;
        this.alignViewGravity = alignBaseViewGravity;
    }

    public GuideDialog(int tipImageResId) {
        this();
        this.tipImage = getResources().getDrawable(tipImageResId);
    }

    public GuideDialog(Bitmap tipImage) {
        this();
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
    }

    public GuideDialog(Drawable tipImage) {
        this();
        this.tipImage = tipImage;
    }

    public GuideDialog(int tipImageResId, ALIGN align) {
        this();
        this.tipImage = getResources().getDrawable(tipImageResId);
        this.align = align;
    }

    public GuideDialog(Bitmap tipImage, ALIGN align) {
        this();
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
        this.align = align;
    }

    public GuideDialog(Drawable tipImage, ALIGN align) {
        this();
        this.tipImage = tipImage;
        this.align = align;
    }

    public GuideDialog(OnBindView<CustomDialog> onBindView) {
        this();
        this.onBindView = onBindView;
    }

    public GuideDialog(OnBindView<CustomDialog> onBindView, ALIGN align) {
        this();
        this.onBindView = onBindView;
        this.align = align;
    }

    public GuideDialog(View baseView, int tipImageResId) {
        this();
        this.baseView(baseView);
        this.tipImage = getResources().getDrawable(tipImageResId);
    }

    public GuideDialog(View baseView, Bitmap tipImage) {
        this();
        this.baseView(baseView);
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
    }

    public GuideDialog(View baseView, Drawable tipImage) {
        this();
        this.baseView(baseView);
        this.tipImage = tipImage;
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, int tipImageResId) {
        this();
        this.baseView(baseView);
        this.stageLightType = stageLightType;
        this.tipImage = getResources().getDrawable(tipImageResId);
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, Bitmap tipImage) {
        this();
        this.baseView(baseView);
        this.stageLightType = stageLightType;
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
    }

    public GuideDialog(View baseView, STAGE_LIGHT_TYPE stageLightType, Drawable tipImage) {
        this();
        this.baseView(baseView);
        this.stageLightType = stageLightType;
        this.tipImage = tipImage;
    }

    public GuideDialog(View baseView, int tipImageResId, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.alignViewGravity = alignBaseViewGravity;
        this.tipImage = getResources().getDrawable(tipImageResId);
    }

    public GuideDialog(View baseView, Bitmap tipImage, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.alignViewGravity = alignBaseViewGravity;
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
    }

    public GuideDialog(View baseView, Drawable tipImage, int alignBaseViewGravity) {
        this();
        this.baseView(baseView);
        this.alignViewGravity = alignBaseViewGravity;
        this.tipImage = tipImage;
    }

    //静态方法
    public static GuideDialog show(OnBindView<CustomDialog> onBindView) {
        GuideDialog guideDialog = new GuideDialog(onBindView);
        guideDialog.show();
        return guideDialog;
    }

    public static GuideDialog show(OnBindView<CustomDialog> onBindView, ALIGN align) {
        GuideDialog guideDialog = new GuideDialog(onBindView);
        guideDialog.align = align;
        guideDialog.show();
        return guideDialog;
    }

    public static GuideDialog show(int tipImageResId) {
        return new GuideDialog(tipImageResId).show();
    }

    public static GuideDialog show(Bitmap tipImage) {
        return new GuideDialog(tipImage).show();
    }

    public static GuideDialog show(Drawable tipImage) {
        return new GuideDialog(tipImage).show();
    }

    public static GuideDialog show(int tipImageResId, ALIGN align) {
        GuideDialog guideDialog = new GuideDialog(tipImageResId, align);
        guideDialog.align = align;
        return guideDialog.show();
    }

    public static GuideDialog show(Bitmap tipImage, ALIGN align) {
        GuideDialog guideDialog = new GuideDialog(tipImage, align);
        guideDialog.align = align;
        return guideDialog.show();
    }

    public static GuideDialog show(Drawable tipImage, ALIGN align) {
        return new GuideDialog(tipImage, align).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType) {
        return new GuideDialog(baseView, stageLightType).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, OnBindView<CustomDialog> onBindView, int alignBaseViewGravity) {
        return new GuideDialog(baseView, stageLightType, onBindView, alignBaseViewGravity).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, int tipImageResId, int alignBaseViewGravity) {
        return new GuideDialog(baseView, stageLightType, tipImageResId, alignBaseViewGravity).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, Bitmap tipImage, int alignBaseViewGravity) {
        return new GuideDialog(baseView, stageLightType, tipImage, alignBaseViewGravity).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, Drawable tipImage, int alignBaseViewGravity) {
        return new GuideDialog(baseView, stageLightType, tipImage, alignBaseViewGravity).show();
    }

    public static GuideDialog show(View baseView, int tipImageResId) {
        return new GuideDialog(baseView, tipImageResId).show();
    }

    public static GuideDialog show(View baseView, Bitmap tipImage) {
        return new GuideDialog(baseView, tipImage).show();
    }

    public static GuideDialog show(View baseView, Drawable tipImage) {
        return new GuideDialog(baseView, tipImage).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, int tipImageResId) {
        return new GuideDialog(baseView, stageLightType, tipImageResId).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, Bitmap tipImage) {
        return new GuideDialog(baseView, stageLightType, tipImage).show();
    }

    public static GuideDialog show(View baseView, STAGE_LIGHT_TYPE stageLightType, Drawable tipImage) {
        return new GuideDialog(baseView, stageLightType, tipImage).show();
    }

    public static GuideDialog show(View baseView, int tipImageResId, int alignBaseViewGravity) {
        return new GuideDialog(baseView, tipImageResId, alignBaseViewGravity).show();
    }

    public static GuideDialog show(View baseView, Bitmap tipImage, int alignBaseViewGravity) {
        return new GuideDialog(baseView, tipImage, alignBaseViewGravity).show();
    }

    public static GuideDialog show(View baseView, Drawable tipImage, int alignBaseViewGravity) {
        return new GuideDialog(baseView, tipImage, alignBaseViewGravity).show();
    }

    //执行方法
    public GuideDialog show() {
        super.show();
        return this;
    }

    public GuideDialog show(Activity activity) {
        super.show(activity);
        return this;
    }

    @Override
    public String dialogKey() {
        return getClass().getSimpleName() + "(" + Integer.toHexString(hashCode()) + ")";
    }

    public GuideDialog setDialogLifecycleCallback(DialogLifecycleCallback<CustomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        if (isShow) dialogLifecycleCallback.onShow(me);
        return this;
    }

    public GuideDialog setOnBackPressedListener(OnBackPressedListener<CustomDialog> onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }

    public GuideDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }

    public GuideDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }

    public GuideDialog setCancelable(boolean cancelable) {
        this.privateCancelable = cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        refreshUI();
        return this;
    }

    public GuideDialog.DialogImpl getDialogImpl() {
        return dialogImpl;
    }

    public GuideDialog setCustomView(OnBindView<CustomDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }

    public GuideDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }

    public GuideDialog setEnterAnimResId(int enterAnimResId) {
        this.enterAnimResId = enterAnimResId;
        return this;
    }

    public GuideDialog setExitAnimResId(int exitAnimResId) {
        this.exitAnimResId = exitAnimResId;
        return this;
    }

    public GuideDialog setAnimResId(int enterAnimResId, int exitAnimResId) {
        this.enterAnimResId = enterAnimResId;
        this.exitAnimResId = exitAnimResId;
        return this;
    }

    public GuideDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }

    public GuideDialog setAutoUnsafePlacePadding(boolean autoUnsafePlacePadding) {
        super.setAutoUnsafePlacePadding(autoUnsafePlacePadding);
        return this;
    }

    public GuideDialog setFullScreen(boolean fullscreen) {
        super.setFullScreen(fullscreen);
        return this;
    }

    public GuideDialog setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        refreshUI();
        return this;
    }

    public GuideDialog setEnterAnimDuration(long enterAnimDuration) {
        this.enterAnimDuration = enterAnimDuration;
        return this;
    }

    public GuideDialog setExitAnimDuration(long exitAnimDuration) {
        this.exitAnimDuration = exitAnimDuration;
        return this;
    }

    public GuideDialog setDialogImplMode(DialogX.IMPL_MODE dialogImplMode) {
        this.dialogImplMode = dialogImplMode;
        return this;
    }

    public GuideDialog setBkgInterceptTouch(boolean bkgInterceptTouch) {
        this.bkgInterceptTouch = bkgInterceptTouch;
        return this;
    }

    public GuideDialog setAlignBaseViewGravity(View baseView, int alignGravity) {
        this.baseView(baseView);
        this.alignViewGravity = alignGravity;
        baseViewLoc = new int[4];
        baseView.getLocationInWindow(baseViewLoc);
        setFullScreen(true);
        return this;
    }

    public GuideDialog setAlignBaseViewGravity(View baseView) {
        this.baseView(baseView);
        baseViewLoc = new int[4];
        baseView.getLocationInWindow(baseViewLoc);
        setFullScreen(true);
        return this;
    }

    public GuideDialog setAlignBaseViewGravity(int alignGravity) {
        this.alignViewGravity = alignGravity;
        if (baseView() != null) {
            baseViewLoc = new int[4];
            baseView().getLocationInWindow(baseViewLoc);
        }
        setFullScreen(true);
        return this;
    }

    public GuideDialog setAlignBaseViewGravity(View baseView, int alignGravity, int marginLeft,
                                               int marginTop, int marginRight, int marginBottom) {
        this.marginRelativeBaseView = new int[]{marginLeft, marginTop, marginRight, marginBottom};
        return setAlignBaseViewGravity(baseView, alignGravity);
    }

    public GuideDialog setBaseViewMargin(int[] marginRelativeBaseView) {
        this.marginRelativeBaseView = marginRelativeBaseView;
        return this;
    }

    public GuideDialog setBaseViewMargin(int marginLeft, int marginTop,
                                         int marginRight, int marginBottom) {
        this.marginRelativeBaseView = new int[]{marginLeft, marginTop, marginRight, marginBottom};
        return this;
    }

    public GuideDialog setBaseViewMarginLeft(int marginLeft) {
        this.marginRelativeBaseView[0] = marginLeft;
        return this;
    }

    public GuideDialog setBaseViewMarginTop(int marginTop) {
        this.marginRelativeBaseView[1] = marginTop;
        return this;
    }

    public GuideDialog setBaseViewMarginRight(int marginRight) {
        this.marginRelativeBaseView[2] = marginRight;
        return this;
    }

    public GuideDialog setBaseViewMarginBottom(int marginBottom) {
        this.marginRelativeBaseView[3] = marginBottom;
        return this;
    }

    /**
     * 设置对话框 UI 宽度（单位：像素）
     *
     * @param width 宽度（像素）
     * @return CustomDialog实例
     */
    public GuideDialog setWidth(int width) {
        this.width = width;
        refreshUI();
        return this;
    }

    /**
     * 设置对话框 UI 高度（单位：像素）
     *
     * @param height 高度（像素）
     * @return CustomDialog实例
     */
    public GuideDialog setHeight(int height) {
        this.height = height;
        refreshUI();
        return this;
    }

    public GuideDialog setOnBackgroundMaskClickListener(OnBackgroundMaskClickListener<CustomDialog> onBackgroundMaskClickListener) {
        this.onBackgroundMaskClickListener = onBackgroundMaskClickListener;
        return this;
    }

    @Override
    protected void onDialogShow() {
        super.onDialogShow();
        if (baseView() == null) {
            super.setMaskColor(maskColor == null ? getColor(R.color.black50) : maskColor);
        }
    }

    View stageLightPathStub;

    @Override
    protected void onDialogRefreshUI() {
        super.onDialogRefreshUI();
        if (onBindView == null && tipImage != null) {
            getDialogImpl().boxCustom.setFocusable(false);
            getDialogImpl().boxCustom.setFocusableInTouchMode(false);
            getDialogImpl().boxCustom.setOnClickListener(null);
            getDialogImpl().boxCustom.setClickable(false);

            ImageView imageView = new ImageView(getOwnActivity());
            imageView.setImageDrawable(tipImage);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            onBindView = new OnBindView<CustomDialog>(imageView) {
                @Override
                public void onBind(CustomDialog dialog, View v) {

                }
            };
            onBindView.bindParent(getDialogImpl().boxCustom, me);
        }
        if (getOnStageLightPathClickListener() != null && baseView() != null) {
            stageLightPathStub = new View(getOwnActivity());
            stageLightPathStub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getOnStageLightPathClickListener().onClick(GuideDialog.this, v)) {
                        dismiss();
                    }
                }
            });
            getDialogImpl().boxRoot.addView(stageLightPathStub);
        } else {
            if (stageLightPathStub != null && stageLightPathStub.getParent() instanceof ViewGroup) {
                ((ViewGroup) stageLightPathStub.getParent()).removeView(stageLightPathStub);
            }
        }
    }

    int[] baseViewLocCache;

    @Override
    protected void onGetBaseViewLoc(int[] baseViewLoc) {
        if (Arrays.equals(baseViewLoc, baseViewLocCache)) {
            return;
        }
        if (getDialogImpl() == null) {
            return;
        }
        Bitmap bkg = Bitmap.createBitmap(getDialogImpl().boxRoot.getWidth(), getDialogImpl().boxRoot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bkg);

        int x = baseViewLoc[0] + baseViewLocationCoordinateCompensation[0];
        int y = baseViewLoc[1] + baseViewLocationCoordinateCompensation[1];
        int w = baseViewLoc[2] + baseViewLocationCoordinateCompensation[2];
        int h = baseViewLoc[3] + baseViewLocationCoordinateCompensation[3];
        int hW = w / 2;
        int hH = h / 2;

        if (stageLightPathStub != null && (stageLightPathStub.getX() != x || stageLightPathStub.getY() != y)) {
            RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) stageLightPathStub.getLayoutParams();
            if (rLp == null) {
                rLp = new RelativeLayout.LayoutParams(w, h);
            } else {
                rLp.width = w;
                rLp.height = h;
            }
            stageLightPathStub.setLayoutParams(rLp);
            stageLightPathStub.setX(x);
            stageLightPathStub.setY(y);
        }

        switch (stageLightType) {
            case CIRCLE_OUTSIDE: {
                int r = (int) Math.sqrt(hW * hW + hH * hH);
                canvas.drawCircle(x + hW, y + hH, r, getStageLightPaint());
            }
            break;
            case CIRCLE_INSIDE: {
                int r = Math.min(w, h) / 2;
                canvas.drawCircle(x + hW, y + hH, r, getStageLightPaint());
            }
            break;
            case RECTANGLE: {
                canvas.drawRoundRect(new RectF(x, y, x + w, y + h), stageLightFilletRadius, stageLightFilletRadius, getStageLightPaint());
            }
            break;
            case SQUARE_INSIDE: {
                int r = Math.min(w, h);
                canvas.drawRoundRect(new RectF(x + hW - r / 2, y + hH - r / 2, x + hW - r / 2 + r, y + hH - r / 2 + r), stageLightFilletRadius, stageLightFilletRadius, getStageLightPaint());
            }
            break;
            case SQUARE_OUTSIDE: {
                int r = Math.max(w, h);
                canvas.drawRoundRect(new RectF(x + hW - r / 2, y + hH - r / 2, x + hW - r / 2 + r, y + hH - r / 2 + r), stageLightFilletRadius, stageLightFilletRadius, getStageLightPaint());
            }
            break;
        }
        stageLightPaint.setXfermode(null);
        canvas.drawColor(maskColor == null ? getColor(R.color.black50) : maskColor, PorterDuff.Mode.SRC_OUT);

        BitmapDrawable bkgDrawable = new BitmapDrawable(getResources(), bkg);
        getDialogImpl().boxRoot.setBackground(bkgDrawable);
        baseViewLocCache = Arrays.copyOf(baseViewLoc, 4);
    }

    Paint stageLightPaint;

    private Paint getStageLightPaint() {
        if (stageLightPaint == null) {
            stageLightPaint = new Paint();
            stageLightPaint.setColor(Color.RED);
            stageLightPaint.setStyle(Paint.Style.FILL);
            stageLightPaint.setAntiAlias(true);
        }
        return stageLightPaint;
    }

    public STAGE_LIGHT_TYPE getStageLightType() {
        return stageLightType;
    }

    public GuideDialog setStageLightType(STAGE_LIGHT_TYPE stageLightType) {
        this.stageLightType = stageLightType;
        refreshUI();
        return this;
    }

    public Drawable getTipImage() {
        return tipImage;
    }

    public GuideDialog setTipImage(int tipImageResId) {
        this.tipImage = getResources().getDrawable(tipImageResId);
        refreshUI();
        return this;
    }

    public GuideDialog setTipImage(Bitmap tipImage) {
        this.tipImage = new BitmapDrawable(getResources(), tipImage);
        refreshUI();
        return this;
    }

    public GuideDialog setTipImage(Drawable tipImage) {
        this.tipImage = tipImage;
        refreshUI();
        return this;
    }

    public float getStageLightFilletRadius() {
        return stageLightFilletRadius;
    }

    public GuideDialog setStageLightFilletRadius(float stageLightFilletRadius) {
        this.stageLightFilletRadius = stageLightFilletRadius;
        refreshUI();
        return this;
    }

    public OnDialogButtonClickListener<GuideDialog> getOnStageLightPathClickListener() {
        return onStageLightPathClickListener;
    }

    public GuideDialog setOnStageLightPathClickListener(OnDialogButtonClickListener<GuideDialog> onStageLightPathClickListener) {
        this.onStageLightPathClickListener = onStageLightPathClickListener;
        refreshUI();
        return this;
    }

    public DialogXAnimInterface<CustomDialog> getDialogXAnimImpl() {
        return dialogXAnimImpl;
    }

    public GuideDialog setDialogXAnimImpl(DialogXAnimInterface<CustomDialog> dialogXAnimImpl) {
        this.dialogXAnimImpl = dialogXAnimImpl;
        return this;
    }

    public GuideDialog setRootPadding(int padding) {
        this.screenPaddings = new int[]{padding, padding, padding, padding};
        refreshUI();
        return this;
    }

    public GuideDialog setRootPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.screenPaddings = new int[]{paddingLeft, paddingTop, paddingRight, paddingBottom};
        refreshUI();
        return this;
    }

    public int[] getBaseViewLocationCoordinateCompensation() {
        return baseViewLocationCoordinateCompensation;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensation(int[] baseViewLocationCoordinateCompensation) {
        this.baseViewLocationCoordinateCompensation = baseViewLocationCoordinateCompensation;
        return this;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensation(int px) {
        this.baseViewLocationCoordinateCompensation = new int[]{px, px, px, px};
        refreshUI();
        return this;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensation(int pxX, int pxY, int pxR, int pxB) {
        this.baseViewLocationCoordinateCompensation = new int[]{pxX, pxY, pxR, pxB};
        refreshUI();
        return this;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensationLeft(int pxX) {
        this.baseViewLocationCoordinateCompensation[0] = pxX;
        refreshUI();
        return this;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensationTop(int pxY) {
        this.baseViewLocationCoordinateCompensation[1] = pxY;
        refreshUI();
        return this;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensationRight(int pxR) {
        this.baseViewLocationCoordinateCompensation[2] = pxR;
        refreshUI();
        return this;
    }

    public GuideDialog setBaseViewLocationCoordinateCompensationBottom(int pxB) {
        this.baseViewLocationCoordinateCompensation[3] = pxB;
        refreshUI();
        return this;
    }

    public int getBaseViewLocationCoordinateCompensationLeft() {
        return baseViewLocationCoordinateCompensation[0];
    }

    public int getBaseViewLocationCoordinateCompensationTop() {
        return baseViewLocationCoordinateCompensation[1];
    }

    public int getBaseViewLocationCoordinateCompensationRight() {
        return baseViewLocationCoordinateCompensation[2];
    }

    public int getBaseViewLocationCoordinateCompensationBottom() {
        return baseViewLocationCoordinateCompensation[3];
    }

    public GuideDialog setData(String key, Object obj) {
        if (data == null) data = new HashMap<>();
        data.put(key, obj);
        return this;
    }

    public GuideDialog onShow(DialogXRunnable<CustomDialog> dialogXRunnable) {
        onShowRunnable = dialogXRunnable;
        if (isShow() && onShowRunnable != null) {
            onShowRunnable.run(this);
        }
        return this;
    }

    public GuideDialog onDismiss(DialogXRunnable<CustomDialog> dialogXRunnable) {
        onDismissRunnable = dialogXRunnable;
        return this;
    }

    public GuideDialog setEnableImmersiveMode(boolean enableImmersiveMode) {
        this.enableImmersiveMode = enableImmersiveMode;
        refreshUI();
        return this;
    }

    public GuideDialog setThisOrderIndex(int orderIndex) {
        this.thisOrderIndex = orderIndex;
        if (getDialogView() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getDialogView().setTranslationZ(orderIndex);
            } else {
                error("DialogX: " + dialogKey() + " 执行 .setThisOrderIndex("+orderIndex+") 失败：系统不支持此方法，SDK-API 版本必须大于 21（LOLLIPOP）");
            }
        }
        return this;
    }

    public GuideDialog bringToFront() {
        setThisOrderIndex(getHighestOrderIndex());
        return this;
    }

    public GuideDialog setActionRunnable(int actionId, DialogXRunnable<CustomDialog> runnable) {
        dialogActionRunnableMap.put(actionId, runnable);
        return this;
    }

    public GuideDialog cleanAction(int actionId){
        dialogActionRunnableMap.remove(actionId);
        return this;
    }

    public GuideDialog cleanAllAction(){
        dialogActionRunnableMap.clear();
        return this;
    }

    // for BaseDialog use
    public void callDialogDismiss(){
        dismiss();
    }

    public GuideDialog bindDismissWithLifecycleOwner(LifecycleOwner owner){
        super.bindDismissWithLifecycleOwnerPrivate(owner);
        return this;
    }
}
