package com.kongzue.dialogx.util;

import android.os.Handler;
import android.os.Looper;
import android.view.animation.Interpolator;

public class DialogXValueAnimator {

    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int INFINITE = -1;

    Handler handler = new Handler(Looper.getMainLooper());

    private long duration;
    private long startTime;
    private boolean isRunning = false;
    private ValueUpdateListener listener;
    private Interpolator interpolator;
    private float startValue;
    private float endValue;
    private int repeatCount = 0;
    private int currentRepeatCount = 0;
    private int refreshInterval = 16;       // 控制更新频率，默认 60 FPS

    public static DialogXValueAnimator ofFloat(float start, float end){
        return new DialogXValueAnimator(start, end);
    }

    public DialogXValueAnimator(float startValue, float endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public void setFloatValues(float start, float end) {
        startValue = start;
        endValue = end;
    }

    public void addUpdateListener(ValueUpdateListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        startTime = System.currentTimeMillis();

        // 开启一个线程用于更新动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    long currentTime = System.currentTimeMillis();
                    long elapsed = currentTime - startTime;

                    if (elapsed < duration) {
                        float fraction = (float) elapsed / duration;

                        // 使用插值器处理动画进度
                        if (interpolator != null) {
                            fraction = interpolator.getInterpolation(fraction);
                        }

                        float animatedValue = startValue + fraction * (endValue - startValue);

                        // 通知监听器
                        if (listener != null) {
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onValueUpdate(animatedValue);
                                }
                            });
                        }
                    } else {
                        // 动画结束
                        isRunning = false;

                        onAnimationEnd();

                        // 处理重复播放
                        if (repeatCount == INFINITE || currentRepeatCount < repeatCount) {
                            currentRepeatCount++;
                            startTime = System.currentTimeMillis();
                            isRunning = true;
                        }
                    }

                    try {
                        Thread.sleep(refreshInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler getHandler() {
        if (handler==null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    private void onAnimationEnd() {

    }

    public long getDuration() {
        return duration;
    }

    public DialogXValueAnimator setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public DialogXValueAnimator setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public ValueUpdateListener getListener() {
        return listener;
    }

    public DialogXValueAnimator setListener(ValueUpdateListener listener) {
        this.listener = listener;
        return this;
    }

    public float getStartValue() {
        return startValue;
    }

    public DialogXValueAnimator setStartValue(float startValue) {
        this.startValue = startValue;
        return this;
    }

    public float getEndValue() {
        return endValue;
    }

    public DialogXValueAnimator setEndValue(float endValue) {
        this.endValue = endValue;
        return this;
    }

    public void cancel() {
        isRunning = false;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    // 监听器接口
    public interface ValueUpdateListener {
        void onValueUpdate(float animatedValue);
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getCurrentRepeatCount() {
        return currentRepeatCount;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public DialogXValueAnimator setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
        return this;
    }
}
