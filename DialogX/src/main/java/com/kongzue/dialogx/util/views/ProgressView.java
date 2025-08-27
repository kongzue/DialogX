package com.kongzue.dialogx.util.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.util.DialogXValueAnimator;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/27 16:16
 * @license: Apache License 2.0
 */
public class ProgressView extends View implements ProgressViewInterface {

    //提示动画持续时间
    public static long TIP_ANIMATOR_DURATION = 300;
    //进度动画中的逐渐跟随动画时长
    public static long PROGRESSING_ANIMATOR_DURATION = 1000;

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_WARNING = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_PROGRESSING = 4;

    private int status = STATUS_LOADING;

    private int width = dip2px(2);
    private int color = Color.WHITE;

    public ProgressView(Context context) {
        super(context);
        init(null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private DialogXValueAnimator rotateAnimator;
    private DialogXValueAnimator followAnimator;

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
        synchronized (ProgressView.class) {
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
                int refreshInterval = (int) calculateMillisPerFrame(getContext());

                halfSweepA = (halfSweepAMaxValue - halfSweepAMinValue) / 2;

                rotateAnimator = DialogXValueAnimator.ofFloat(0, 365);
                rotateAnimator.setDuration(1000);
                rotateAnimator.setInterpolator(new LinearInterpolator());
                rotateAnimator.setRepeatCount(-1);
                rotateAnimator.setRefreshInterval(refreshInterval);
                rotateAnimator.addUpdateListener(new DialogXValueAnimator.ValueUpdateListener() {
                    @Override
                    public void onValueUpdate(float animatedValue) {
                        if (!isAttachedToWindow()) {
                            return;
                        }
                        currentRotateDegrees = animatedValue;
                        invalidate();
                    }
                });

                followAnimator = DialogXValueAnimator.ofFloat(0, 365);
                followAnimator.setDuration(1500);
                followAnimator.setRefreshInterval(refreshInterval);
                followAnimator.setInterpolator(new LinearInterpolator());
                followAnimator.setRepeatCount(-1);
                followAnimator.addUpdateListener(new DialogXValueAnimator.ValueUpdateListener() {
                    @Override
                    public void onValueUpdate(float animatedValue) {
                        followRotateDegrees = animatedValue;
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

    private int successStep = 0;
    private float nowLoadingProgressValue;
    private float nowLoadingProgressEndAngle;
    private float changeStatusAngle;

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
        switch (status) {
            case STATUS_LOADING:
                float sweepAngle = (float) (halfSweepA * Math.sin(Math.toRadians(followRotateDegrees))) + halfSweepA + halfSweepAMinValue / 2;
                nowLoadingProgressValue = currentRotateDegrees - sweepAngle;
                if (nowLoadingProgressValue < 0) {
                    nowLoadingProgressValue = 360 + nowLoadingProgressValue;
                }
                nowLoadingProgressEndAngle = sweepAngle;
                changeStatusAngle = sweepAngle < 0 ? 360 - sweepAngle : sweepAngle;
                canvas.drawArc(oval, currentRotateDegrees, -sweepAngle, false, mPaint);
                break;
            case STATUS_SUCCESS:
            case STATUS_WARNING:
            case STATUS_ERROR:
                switch (successStep) {
                    case 0:
                        nowLoadingProgressEndAngle = nowLoadingProgressEndAngle + 5;
                        canvas.drawArc(oval, nowLoadingProgressValue, nowLoadingProgressEndAngle, false, mPaint);

                        if (nowLoadingProgressEndAngle - (360 - changeStatusAngle) >= nowLoadingProgressValue) {
                            successStep = 1;
                            if (waitArticulationAnimationRunnable != null) {
                                waitArticulationAnimationRunnable.run();
                                waitArticulationAnimationRunnable = null;
                            }
                        }
                        break;
                    case 1:
                        canvas.drawArc(oval, 0, 360, false, mPaint);
                        drawDoneMark(status, canvas);
                        break;
                }
                break;
            case STATUS_PROGRESSING:
                switch (successStep) {
                    case 0:
                        canvas.drawArc(oval, -90, currentRotateDegrees, false, mPaint);
                        if (currentRotateDegrees == 365) {
                            successStep = 1;
                            if (waitArticulationAnimationRunnable != null) {
                                waitArticulationAnimationRunnable.run();
                                waitArticulationAnimationRunnable = null;
                            }
                        }
                        break;
                    case 1:
                        canvas.drawArc(oval, 0, 360, false, mPaint);
                        drawDoneMark(status, canvas);
                        break;
                }
        }
    }

    private void drawDoneMark(int status, Canvas canvas) {
        if (rotateAnimator.getInterpolator() != interpolator) {
            rotateAnimator.setInterpolator(interpolator);
        }
        if (tickShowRunnable != null) {
            tickShowRunnable.run();
            tickShowRunnable = null;

            if (DialogX.useHaptic) {
                switch (status) {
                    case STATUS_SUCCESS:
                        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                        break;
                    case STATUS_WARNING:
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                            }
                        }, (long) (TIP_ANIMATOR_DURATION * 0.8f));
                        break;
                    case STATUS_ERROR:
                        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                            }
                        }, (long) (TIP_ANIMATOR_DURATION * 0.5f));
                        break;
                }
            }
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

    private ValueAnimator tickAnimator;
    private float tickAnimatorValue;

    //绘制对号
    private void showSuccessTick(Canvas canvas) {
        int verticalAxisOffset = (int) (mRadius / 20);       //纵轴向下偏移量
        int tickTurnLeftPoint = (int) (mCenterX - mRadius / 10 - verticalAxisOffset);       //转折点
        int startX = (int) (mCenterX - mRadius / 2);
        int startY = (int) (mCenterY + verticalAxisOffset);
        int endX = (int) (mCenterX + mRadius / 2);
        int tickAnimatorX = (int) (startX + ((endX - startX) * tickAnimatorValue));

        Path path = new Path();
        path.moveTo(startX, startY);
        if (tickAnimatorX < tickTurnLeftPoint) {
            line1X = tickAnimatorX;
            line1Y = (int) (startY + (tickAnimatorX - startX));
            path.lineTo(line1X, line1Y);
        } else {
            line1X = tickTurnLeftPoint;
            line1Y = (int) (startY + (line1X - startX));
            path.lineTo(line1X, line1Y);

            line2X = tickAnimatorX;
            line2Y = line1Y - (tickAnimatorX - line1X);
            path.lineTo(line2X, line2Y);
        }
        canvas.drawPath(path, mPaint);
    }

    //绘制感叹号
    private void showWarningTick(Canvas canvas) {
        int x = (int) mCenterX;
        int startY = (int) (mCenterY - mRadius * 1 / 2);
        int endY = (int) (mCenterY + mRadius * 1 / 8);
        int line2Y = (int) (mCenterY + mRadius * 3 / 7);

        if (tickAnimatorValue < 0.9f) {
            canvas.drawLine(x, startY, x, startY + (endY - startY) * tickAnimatorValue, mPaint);
        } else {
            canvas.drawLine(x, startY, x, endY, mPaint);
            canvas.drawLine(x, line2Y, x, line2Y + 1, mPaint);
        }
    }

    //绘制错误符号
    private void showErrorTick(Canvas canvas) {
        int start = (int) (mCenterY - mRadius * 4 / 10);
        int end = (int) (mCenterX + mRadius * 4 / 10);

        if (tickAnimatorValue < 0.5f) {
            line1X = (int) (start + (tickAnimatorValue * 2) * (end - start));
            line1Y = (int) (start + (tickAnimatorValue * 2) * (end - start));
            canvas.drawLine(start, start, line1X, line1Y, mPaint);
        } else {
            line1X = (int) (start + (tickAnimatorValue * 2) * (end - start));
            line1Y = (int) (start + (tickAnimatorValue * 2) * (end - start));
            canvas.drawLine(start, start, end, end, mPaint);

            line2X = (int) (end - ((tickAnimatorValue - 0.5f) * 2) * (end - start));
            line2Y = (int) (start + ((tickAnimatorValue - 0.5f) * 2) * (end - start));
            canvas.drawLine(end, start, line2X, line2Y, mPaint);
        }
    }

    private Interpolator interpolator;

    public void success() {
        if (status == STATUS_SUCCESS) return;
        if (status == STATUS_PROGRESSING) {
            progress(1f);
            waitArticulationAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    initTipAnimator(STATUS_SUCCESS, new AccelerateDecelerateInterpolator());
                }
            };
            return;
        }
        initTipAnimator(STATUS_SUCCESS, new AccelerateDecelerateInterpolator());
    }

    public void warning() {
        if (status == STATUS_WARNING) return;
        if (status == STATUS_PROGRESSING) {
            progress(1f);
            waitArticulationAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    initTipAnimator(STATUS_WARNING, new AccelerateInterpolator(2f));
                }
            };
            return;
        }
        initTipAnimator(STATUS_WARNING, new AccelerateInterpolator(2f));
    }

    public void error() {
        if (status == STATUS_ERROR) return;
        if (status == STATUS_PROGRESSING) {
            progress(1f);
            waitArticulationAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    initTipAnimator(STATUS_ERROR, new DecelerateInterpolator(2));
                }
            };
            return;
        }
        initTipAnimator(STATUS_ERROR, new DecelerateInterpolator(2));
    }

    Runnable waitArticulationAnimationRunnable;     //等待衔接完成后再执行

    private void initTipAnimator(int s, Interpolator i) {
        interpolator = i;
        status = s;
        if (successStep == 0) {
            waitArticulationAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    initTipAnimator(status, interpolator);
                }
            };
            return;
        }

        if (tickAnimator != null) {
            tickAnimator.cancel();
            tickAnimator = null;
        }
        tickAnimatorValue = 0;
        tickAnimator = ValueAnimator.ofFloat(0f, 1f);
        tickAnimator.setDuration(TIP_ANIMATOR_DURATION);
        tickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tickAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        tickAnimator.start();
    }

    public void progress(float progress) {
        if (rotateAnimator != null) rotateAnimator.cancel();
        if (followAnimator != null) followAnimator.cancel();
        if (status != STATUS_PROGRESSING) {
            currentRotateDegrees = 0;
        }
        noShowLoading = false;
        status = STATUS_PROGRESSING;
        rotateAnimator = DialogXValueAnimator.ofFloat(currentRotateDegrees, 365 * progress);
        rotateAnimator.setDuration(PROGRESSING_ANIMATOR_DURATION);
        rotateAnimator.setInterpolator(new DecelerateInterpolator(2));
        rotateAnimator.setRepeatCount(0);
        rotateAnimator.addUpdateListener(new DialogXValueAnimator.ValueUpdateListener() {
            @Override
            public void onValueUpdate(float animatedValue) {
                currentRotateDegrees = animatedValue;
                invalidate();
            }
        });
        rotateAnimator.start();
    }

    private Runnable tickShowRunnable;

    public ProgressView whenShowTick(Runnable runnable) {
        tickShowRunnable = runnable;
        return this;
    }

    public void loading() {
        if (status == STATUS_LOADING) return;
        noShowLoading = false;
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

    public ProgressView setStrokeWidth(int width) {
        this.width = width;
        if (mPaint != null) mPaint.setStrokeWidth(width);
        return this;
    }

    public int getColor() {
        return color;
    }

    public ProgressView setColor(int color) {
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

    private float getRefreshRate(Context context) {
        float refreshRate = 60.0f; // 默认值
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Display display = context.getSystemService(WindowManager.class).getDefaultDisplay();
            Display.Mode mode = display.getMode();
            refreshRate = mode.getRefreshRate();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            refreshRate = display.getRefreshRate();
        }

        return refreshRate;
    }

    private long calculateMillisPerFrame(Context context) {
        float refreshRate = getRefreshRate(context);

        if (refreshRate > 0) {
            return (long) (1000.0 / refreshRate);
        } else {
            return 16; // 获取刷新率失败
        }
    }
}
