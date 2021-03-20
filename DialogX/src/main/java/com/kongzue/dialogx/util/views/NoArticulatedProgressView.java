package com.kongzue.dialogx.util.views;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/3/14 20:58
 */
public class NoArticulatedProgressView extends View implements ProgressViewInterface {
    public static final int STATUS_LOADING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_WARNING = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_PROGRESSING = 4;
    
    private int status = STATUS_LOADING;
    
    private int width = dip2px(2);
    private int color = Color.WHITE;
    
    public NoArticulatedProgressView(Context context) {
        super(context);
        init(null);
    }
    
    public NoArticulatedProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    
    public NoArticulatedProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    
    public NoArticulatedProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    
    private ValueAnimator rotateAnimator;
    private ValueAnimator followAnimator;
    
    private float currentRotateDegrees;
    private float followRotateDegrees;
    
    //跟随点度数做正弦值变化，halfSweepAMinValue为最低相较目标点度数差值，halfSweepAMaxValue为最大相较目标点度数差值
    private float halfSweepAMaxValue = 180;
    private float halfSweepAMinValue = 80;
    //正弦函数的半径
    private float halfSweepA;
    
    Paint mPaint = new Paint();
    
    private boolean isInited = false;
    
    private void init(AttributeSet attrs) {
        synchronized (NoArticulatedProgressView.class) {
            if (isInited) {
                return;
            }
            isInited = true;
            if (attrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
                width = a.getDimensionPixelSize(R.styleable.ProgressView_progressStrokeWidth, dip2px(2));
                color = a.getDimensionPixelSize(R.styleable.ProgressView_progressStrokeColor, color);
                
                a.recycle();
            }
            
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(width);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(color);
            
            if (!isInEditMode()) {
                halfSweepA = (halfSweepAMaxValue - halfSweepAMinValue) / 2;
                
                rotateAnimator = ValueAnimator.ofFloat(0, 365);
                rotateAnimator.setDuration(1000);
                rotateAnimator.setInterpolator(new LinearInterpolator());
                rotateAnimator.setRepeatCount(-1);
                rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currentRotateDegrees = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                
                followAnimator = ValueAnimator.ofFloat(0, 365);
                followAnimator.setDuration(1500);
                followAnimator.setInterpolator(new LinearInterpolator());
                followAnimator.setRepeatCount(-1);
                followAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        followRotateDegrees = (float) animation.getAnimatedValue();
                    }
                });
                
                followAnimator.start();
                rotateAnimator.start();
            }
        }
    }
    
    //旋转圆的中心坐标
    private float mCenterX;
    private float mCenterY;
    //半径
    private float mRadius = 100;
    private RectF oval;
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 1f / 2;
        mCenterY = h * 1f / 2;
        mRadius = Math.min(getWidth(), getHeight()) / 2 - width / 2;
        oval = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }
    
    protected float oldAnimAngle;
    private int successStep = 0;
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawArc(oval, 0, 365, false, mPaint);
            return;
        }
        if (noShowLoading) {
            canvas.drawArc(oval, 0, 365, false, mPaint);
            successStep = 2;
            drawDoneMark(status, canvas);
            return;
        }
        
        float sweepAngle = (float) (halfSweepA * Math.sin(Math.toRadians(followRotateDegrees))) + halfSweepA + halfSweepAMinValue / 2;
        switch (status) {
            case STATUS_LOADING:
                canvas.drawArc(oval, currentRotateDegrees, -sweepAngle, false, mPaint);
                break;
            case STATUS_SUCCESS:
            case STATUS_WARNING:
            case STATUS_ERROR:
                canvas.drawArc(oval, 0, 360, false, mPaint);
                drawDoneMark(status, canvas);
                break;
            case STATUS_PROGRESSING:
                canvas.drawArc(oval, -90, currentRotateDegrees, false, mPaint);
                if (waitProgressingRunnable != null) {
                    waitProgressingRunnable.run();
                    waitProgressingRunnable = null;
                }
                break;
        }
    }
    
    private void drawDoneMark(int status, Canvas canvas) {
        if (rotateAnimator.getInterpolator() != interpolator) {
            rotateAnimator.setInterpolator(interpolator);
        }
        if (tickShowRunnable != null) {
            tickShowRunnable.run();
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            tickShowRunnable = null;
        }
        switch (status) {
            case STATUS_SUCCESS:
                showSuccessTick(canvas);
                break;
            case STATUS_WARNING:
                showWarningTick(canvas);
                break;
            case STATUS_ERROR:
                showErrorTick(canvas);
                break;
        }
    }
    
    private int line1X = 0;
    private int line1Y = 0;
    private int line2X = 0;
    private int line2Y = 0;
    
    private int tickStep = 0;
    
    //绘制对号
    private void showSuccessTick(Canvas canvas) {
        int tickLeftPoint = (int) (mCenterX - mRadius * 1 / 2);
        int tickTurnLeftPoint = (int) (mCenterX - mRadius / 10);
        int tickRightPoint = (int) (mRadius * 0.99f);
        int speed = 2;
        switch (tickStep) {
            case 0:
                if (tickLeftPoint + line1X < tickTurnLeftPoint) {
                    line1X = line1X + speed;
                    line1Y = line1Y + speed;
                } else {
                    line2X = line1X;
                    line2Y = line1Y;
                    tickStep = 1;
                }
                break;
            case 1:
                if (line2X < tickRightPoint) {
                    line2X = line2X + 4;
                    line2Y = line2Y - 5;
                }
                break;
        }
        canvas.drawLine(tickLeftPoint, mCenterY, tickLeftPoint + line1X, mCenterY + line1Y, mPaint);
        canvas.drawLine(tickLeftPoint + line1X, mCenterY + line1Y, tickLeftPoint + line2X, mCenterY + line2Y, mPaint);
        
        postInvalidateDelayed(1);
    }
    
    //绘制感叹号
    private void showWarningTick(Canvas canvas) {
        int tickLeftPoint = (int) mCenterX;
        int line1StartY = (int) (mCenterY - mRadius * 1 / 2);
        int line1EndY = (int) (mCenterY + mRadius * 1 / 8);
        int line2StartY = (int) (mCenterY + mRadius * 3 / 7);
        int speed = 4;
        switch (tickStep) {
            case 0:
                if (line1Y < line1EndY - line1StartY) {
                    line1Y = line1Y + speed;
                } else {
                    line1Y = line1EndY - line1StartY;
                    tickStep = 1;
                }
                break;
            case 1:
                if (line2Y != line2StartY) {
                    canvas.drawLine(tickLeftPoint, line2StartY, tickLeftPoint, line2StartY + 1, mPaint);
                }
                break;
        }
        canvas.drawLine(tickLeftPoint, line1StartY, tickLeftPoint, line1StartY + line1Y, mPaint);
        postInvalidateDelayed(tickStep == 1 ? 100 : 1);
    }
    
    //绘制错误符号
    private void showErrorTick(Canvas canvas) {
        int tickLeftPoint = (int) (mCenterX - mRadius * 4 / 10);
        int tickRightPoint = (int) (mCenterX + mRadius * 4 / 10);
        int tickTopPoint = (int) (mCenterY - mRadius * 4 / 10);
        int speed = 4;
        
        switch (tickStep) {
            case 0:
                if (tickRightPoint - line1X > tickLeftPoint) {
                    line1X = line1X + speed;
                    line1Y = line1Y + speed;
                } else {
                    tickStep = 1;
                    canvas.drawLine(tickRightPoint, tickTopPoint, tickRightPoint - line1X, tickTopPoint + line1Y, mPaint);
                    postInvalidateDelayed(150);
                    return;
                }
                break;
            case 1:
                if (tickLeftPoint + line2X < tickRightPoint) {
                    line2X = line2X + speed;
                    line2Y = line2Y + speed;
                }
                canvas.drawLine(tickLeftPoint, tickTopPoint, tickLeftPoint + line2X, tickTopPoint + line2Y, mPaint);
                break;
        }
        canvas.drawLine(tickRightPoint, tickTopPoint, tickRightPoint - line1X, tickTopPoint + line1Y, mPaint);
        postInvalidateDelayed(1);
    }
    
    private TimeInterpolator interpolator;
    private Runnable waitProgressingRunnable;
    
    public void success() {
        if (status == STATUS_PROGRESSING) {
            progress(1f);
            waitProgressingRunnable = new Runnable() {
                @Override
                public void run() {
                    tickStep = 0;
                    successStep = 2;
                    interpolator = new AccelerateDecelerateInterpolator();
                    status = STATUS_SUCCESS;
                }
            };
            return;
        }
        tickStep = 0;
        interpolator = new AccelerateDecelerateInterpolator();
        status = STATUS_SUCCESS;
        invalidate();
    }
    
    public void warning() {
        if (status == STATUS_PROGRESSING) {
            progress(1f);
            waitProgressingRunnable = new Runnable() {
                @Override
                public void run() {
                    tickStep = 0;
                    successStep = 2;
                    interpolator = new DecelerateInterpolator(2);
                    status = STATUS_WARNING;
                }
            };
            return;
        }
        tickStep = 0;
        interpolator = new DecelerateInterpolator(2);
        status = STATUS_WARNING;
        invalidate();
    }
    
    public void error() {
        if (status == STATUS_PROGRESSING) {
            progress(1f);
            waitProgressingRunnable = new Runnable() {
                @Override
                public void run() {
                    tickStep = 0;
                    successStep = 2;
                    interpolator = new DecelerateInterpolator(2);
                    status = STATUS_ERROR;
                }
            };
            return;
        }
        tickStep = 0;
        interpolator = new DecelerateInterpolator(2);
        status = STATUS_ERROR;
        invalidate();
    }
    
    public void progress(float progress) {
        if (rotateAnimator != null) rotateAnimator.cancel();
        if (followAnimator != null) followAnimator.cancel();
        if (status != STATUS_PROGRESSING) {
            currentRotateDegrees = 0;
        }
        rotateAnimator = ValueAnimator.ofFloat(currentRotateDegrees, 365 * progress);
        rotateAnimator.setDuration(1000);
        rotateAnimator.setInterpolator(new DecelerateInterpolator(2));
        rotateAnimator.setRepeatCount(0);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentRotateDegrees = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.start();
        status = STATUS_PROGRESSING;
    }
    
    private Runnable tickShowRunnable;
    
    public NoArticulatedProgressView whenShowTick(Runnable runnable) {
        tickShowRunnable = runnable;
        return this;
    }
    
    public void loading() {
        noShowLoading = false;
        oldAnimAngle = 0;
        successStep = 0;
        line1X = 0;
        line1Y = 0;
        line2X = 0;
        line2Y = 0;
        status = STATUS_LOADING;
        if (rotateAnimator != null) rotateAnimator.cancel();
        if (followAnimator != null) followAnimator.cancel();
        isInited = false;
        init(null);
    }
    
    public int getStatus() {
        return status;
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
        }
        if (followAnimator != null) {
            followAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }
    
    public int getStrokeWidth() {
        return width;
    }
    
    public NoArticulatedProgressView setStrokeWidth(int width) {
        this.width = width;
        if (mPaint != null) mPaint.setStrokeWidth(width);
        return this;
    }
    
    public int getColor() {
        return color;
    }
    
    public NoArticulatedProgressView setColor(int color) {
        this.color = color;
        if (mPaint != null) mPaint.setColor(color);
        return this;
    }
    
    private boolean noShowLoading;
    
    public void noLoading() {
        noShowLoading = true;
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
