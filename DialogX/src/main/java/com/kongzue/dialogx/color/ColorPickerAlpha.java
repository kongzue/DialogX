package com.kongzue.dialogx.color;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kongzue.dialogx.R;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
public class ColorPickerAlpha extends ColorPickerBaseSeekBar implements OnHueValueChangedListener {
    public ColorPickerAlpha(@NonNull Context context) {
        super(context);
    }

    public ColorPickerAlpha(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerAlpha(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void initialBasicInfo() {
        setType(ColorPickerType.ALPHA);
        setColors(
            Color.HSVToColor(0, new float[]{0, 1, 1}),
            Color.HSVToColor(255, new float[]{0, 1, 1})
        );
        setMax(255);
        setProgress(255);
        setLayerDrawable(ContextCompat.getDrawable(getContext(), R.drawable.color_picker_alpha_bg));
    }

    @Override void setValue(@FloatRange(from = 0, to = 255) float value) {
        super.setValue(value);
    }

    @Override
    public void onHueValueChanged(int value) {
        setColors(
            Color.HSVToColor(0, new float[]{value, 1, 1}),
            Color.HSVToColor(255, new float[]{value, 1, 1})
        );
    }
}
