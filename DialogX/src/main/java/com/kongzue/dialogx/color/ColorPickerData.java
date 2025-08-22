package com.kongzue.dialogx.color;

import android.graphics.Color;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
class ColorPickerData {
    int hue;
    int saturation;
    int lightness = 10000;
    int alpha = 255;

    int HSVToColor() {
        return Color.HSVToColor(alpha, new float[]{(float) hue / 100, (float) saturation / 10000, (float) lightness / 10000});
    }
}
