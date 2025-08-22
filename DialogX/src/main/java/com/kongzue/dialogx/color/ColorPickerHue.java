package com.kongzue.dialogx.color;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
public class ColorPickerHue extends ColorPickerBaseSeekBar {
    private OnHueValueChangedListener[] listeners;

    public ColorPickerHue(@NonNull Context context) {
        super(context);
    }

    public ColorPickerHue(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerHue(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void initialBasicInfo() {
        setType(ColorPickerType.HUE);
        setColors(
            Color.HSVToColor(new float[]{0, 1, 1}),
            Color.HSVToColor(new float[]{60, 1, 1}),
            Color.HSVToColor(new float[]{120, 1, 1}),
            Color.HSVToColor(new float[]{180, 1, 1}),
            Color.HSVToColor(new float[]{240, 1, 1}),
            Color.HSVToColor(new float[]{300, 1, 1}),
            Color.HSVToColor(new float[]{360, 1, 1})
        );
        setMax(36000);
        setProgress(0);
    }

    final void setListeners(OnHueValueChangedListener... listeners) {
        this.listeners = listeners;
    }


    @Override void setValue(@FloatRange(from = 0, to = 360) float value) {
        value = value * 100;
        super.setValue(value);
        callHueValueChanged();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (listeners != null && fromUser) callHueValueChanged();
    }

    private void callHueValueChanged() {
        for (OnHueValueChangedListener listener : listeners) {
            listener.onHueValueChanged(getValue() / 100);
        }
    }
}
