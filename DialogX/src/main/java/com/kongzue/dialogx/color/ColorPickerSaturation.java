package com.kongzue.dialogx.color;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
public class ColorPickerSaturation extends ColorPickerBaseSeekBar implements OnHueValueChangedListener {
    public ColorPickerSaturation(@NonNull Context context) {
        super(context);
    }

    public ColorPickerSaturation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerSaturation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void initialBasicInfo() {
        setType(ColorPickerType.SATURATION);
        setColors(
            Color.HSVToColor(new float[]{0, 0, 1}),
            Color.HSVToColor(new float[]{0, 1, 1})
        );
        setMax(10000);
        setProgress(0);
    }

    @Override
    void setValue(@FloatRange(from = 0, to = 1) float value) {
        value = value * 10000;
        super.setValue(value);
    }

    @Override
    public void onHueValueChanged(int value) {
        setColors(
            Color.HSVToColor(new float[]{value, 0, 1}),
            Color.HSVToColor(new float[]{value, 1, 1})
        );
    }
}
