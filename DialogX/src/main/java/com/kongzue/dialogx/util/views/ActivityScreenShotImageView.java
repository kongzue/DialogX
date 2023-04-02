package com.kongzue.dialogx.util.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kongzue.dialogx.impl.ActivityLifecycleImpl;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.util.DialogXFloatingWindowActivity;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/11/17 23:53
 */
@SuppressLint("AppCompatCustomView")
public class ActivityScreenShotImageView extends ImageView {

    float width, height, mRadius;

    public ActivityScreenShotImageView(Context context) {
        super(context);
        init(null);
    }

    public ActivityScreenShotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ActivityScreenShotImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width != getWidth() || height != getHeight()) {
            refreshImage();
        }
        width = getWidth();
        height = getHeight();
    }

    boolean readyDraw = false;

    @Override
    protected void onDraw(Canvas canvas) {
        if (!readyDraw) {
            super.onDraw(canvas);
        }
        if (width >= mRadius && height > mRadius) {
            if (isScreenshotSuccess) {
                canvas.drawColor(Color.BLACK);
            }
            Path path = new Path();
            path.moveTo(mRadius, 0);
            path.lineTo(width - mRadius, 0);
            path.quadTo(width, 0, width, mRadius);
            path.lineTo(width, height - mRadius);
            path.quadTo(width, height, width - mRadius, height);
            path.lineTo(mRadius, height);
            path.quadTo(0, height, 0, height - mRadius);
            path.lineTo(0, mRadius);
            path.quadTo(0, 0, mRadius, 0);

            canvas.clipPath(path);
        }
        try {
            canvas.drawColor(Color.WHITE);
            super.onDraw(canvas);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isScreenshotSuccess) refreshImage();
    }

    private int screenWidth, screenHeight;

    private void refreshImage() {
        if (!isAttachedToWindow()) {
            return;
        }
        if (screenWidth != getMeasuredWidth() || screenHeight != getMeasuredHeight()) {
            screenWidth = getMeasuredWidth();
            screenHeight = getMeasuredHeight();
            doScreenshotActivityAndZoom();
        }
    }

    private void doScreenshotActivityAndZoom() {
        ViewGroup decorView = getDecorView();
        if (decorView == null) return;
        drawViewImage(decorView);
        setVisibility(VISIBLE);
        inited = true;
    }

    private ViewGroup getDecorView() {
        if (dialog != null) {
            Activity ownActivity = dialog.getOwnActivity();
            return (ViewGroup) ownActivity.getWindow().getDecorView();
        }
        Activity topActivity = ActivityLifecycleImpl.getTopActivity();
        if (topActivity != null) {
            if (topActivity instanceof DialogXFloatingWindowActivity) {
                return (ViewGroup) ((DialogXFloatingWindowActivity) topActivity).getFromActivity().getWindow().getDecorView();
            }
            return (ViewGroup) topActivity.getWindow().getDecorView();
        }
        return null;
    }

    private boolean inited = false;
    private boolean isScreenshotSuccess;
    private WeakReference<View> contentView;
    public static boolean hideContentView = false;

    private void drawViewImage(View view) {
        if (view.getWidth() == 0 || view.getHeight() == 0) return;
        dialog.getDialogView().setVisibility(GONE);
        setContentViewVisibility(true);
        view.buildDrawingCache();
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        view.setDrawingCacheEnabled(true);
        setImageBitmap(Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getWidth(), view.getHeight()));
        view.destroyDrawingCache();
        setContentViewVisibility(false);
        isScreenshotSuccess = true;
        dialog.getDialogView().setVisibility(VISIBLE);
        dialog.getDialogView().requestFocus();
    }

    protected void setContentViewVisibility(boolean show) {
        if (hideContentView) {
            if (show) {
                if (contentView != null && contentView.get() != null) {
                    contentView.get().setVisibility(VISIBLE);
                }
            } else {
                View userContentView = Objects.requireNonNull(getDecorView()).getChildAt(0);
                if (userContentView != null) {
                    userContentView.setVisibility(GONE);
                    contentView = new WeakReference<>(userContentView);
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setContentViewVisibility(true);
        if (contentView != null) {
            contentView.clear();
        }
    }

    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
        readyDraw = true;
    }

    BaseDialog dialog;

    public void bindDialog(BaseDialog dialog) {
        this.dialog = dialog;
    }
}
