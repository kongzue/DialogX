package com.kongzue.dialogx.color;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;

import com.kongzue.dialogx.R;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
abstract class ColorPickerBaseSeekBar extends AppCompatSeekBar implements SeekBar.OnSeekBarChangeListener {
    private OnColorValueChangedListener listener;
    private ColorPickerType type;
    private LayerDrawable layerDrawable;
    private GradientDrawable drawable;
    private Drawable background;
    private int[] colors;
    private int value;

    public ColorPickerBaseSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public ColorPickerBaseSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerBaseSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setOrientation(GradientDrawable.Orientation.TL_BR);
        drawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.color_radius));
        drawable.setSize(-1, getResources().getDimensionPixelSize(R.dimen.color_height));
        drawable.setStroke(0, 0);

        setBackground(null);
        setPadding(0, 0, 0, 0);
        setThumb(ContextCompat.getDrawable(getContext(), R.drawable.color_picker_with_hole));
        setThumbOffset(getResources().getDimensionPixelSize(R.dimen.color_picker_offset));
        setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.color_picker_seekbar_progress));
        setOnSeekBarChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSplitTrack(false);
        }

        initialBasicInfo();
        updateProgressBackground();
    }

    abstract void initialBasicInfo();

    final void updateProgressBackground() {
        drawable.setColors(colors);
        if (layerDrawable == null) setProgressDrawable(drawable);
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layerDrawable.setDrawable(1, drawable);
            } else layerDrawable = new LayerDrawable(new Drawable[]{background, drawable});
            setProgressDrawable(layerDrawable);
        }
    }

    final void setType(ColorPickerType type) {
        this.type = type;
    }

    final void setListener(OnColorValueChangedListener listener) {
        this.listener = listener;
    }

    final void setLayerDrawable(Drawable background) {
        this.background = background;
        this.layerDrawable = new LayerDrawable(new Drawable[]{background, drawable});
    }

    final void setColors(int... colors) {
        this.colors = colors;
        updateProgressBackground();
    }

    void setValue(float value) {
        this.value = Math.round(value);
        setProgress(Math.round(value));
    }

    final int getValue() {
        return value;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            requestDisallowInterceptTouchEvent(true);
        else if (event.getAction() == MotionEvent.ACTION_UP)
            requestDisallowInterceptTouchEvent(false);

        return super.dispatchTouchEvent(event);
    }

    private void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        while (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
            parent = parent.getParent();
        }
    }

    @Override
    public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            value = progress;
            if (isHapticFeedbackEnabled())
                seekBar.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
            if (listener != null) listener.onColorValueChanged(type, value);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null) listener.onColorValueChanged(ColorPickerType.FINAL_COLOR, -1);
    }
}
