package com.kongzue.dialogx.color;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.kongzue.dialogx.R;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
public class ColorPickerView extends LinearLayout implements OnColorValueChangedListener {
    private static final String TAG = "ColorPicker";
    private final ColorPickerData data = new ColorPickerData();
    private ColorPickerHue hue;
    private ColorPickerSaturation saturation;
    private ColorPickerLightness lightness;
    private ColorPickerAlpha alpha;
    private View colorView;
    private EditText editText;
    @ColorInt
    private int value;
    private OnColorValueChangedListener listener;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 8 && editText.hasFocus())
                setValue(Color.parseColor("#" + editable.toString()));
        }
    };

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_dialogx_color_picker_adapter, this, true);
        setOrientation(VERTICAL);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        hue = findViewById(R.id.color_hue);
        saturation = findViewById(R.id.color_saturation);
        lightness = findViewById(R.id.color_lightness);
        alpha = findViewById(R.id.color_alpha);
        colorView = findViewById(R.id.color_view);
        editText = findViewById(R.id.color_edit);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.color_radius));
        colorView.setBackground(drawable);

        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefABCDEF"));
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        apply();
    }

    private void apply() {
        hue.setListener(this);
        saturation.setListener(this);
        lightness.setListener(this);
        alpha.setListener(this);

        hue.setListeners(saturation, lightness, alpha);
        value = data.HSVToColor();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        editText.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        editText.removeTextChangedListener(textWatcher);
    }

    public void setValue(@ColorInt int value) {
        if (this.value == value) return;
        this.value = value;
        updateContent(ColorPickerType.FINAL_COLOR, true);
    }

    public void setListener(OnColorValueChangedListener listener) {
        this.listener = listener;
    }

    @ColorInt
    public int getValue() {
        return value;
    }

    @Override
    public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        hue.setHapticFeedbackEnabled(hapticFeedbackEnabled);
        saturation.setHapticFeedbackEnabled(hapticFeedbackEnabled);
        lightness.setHapticFeedbackEnabled(hapticFeedbackEnabled);
        alpha.setHapticFeedbackEnabled(hapticFeedbackEnabled);
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled);
    }

    @Override
    public void onColorValueChanged(ColorPickerType type, int value) {
        if (type == ColorPickerType.HUE) data.hue = value;
        else if (type == ColorPickerType.SATURATION) data.saturation = value;
        else if (type == ColorPickerType.LIGHTNESS) data.lightness = value;
        else if (type == ColorPickerType.ALPHA) data.alpha = value;

        this.value = data.HSVToColor();
        if (type == ColorPickerType.FINAL_COLOR) updateContent(ColorPickerType.FINAL_COLOR, false);
        else updateContent(ColorPickerType.INSTANT_COLOR, false);
    }

    private void updateContent(ColorPickerType type, boolean shouldUpdateValue) {
        ((GradientDrawable) colorView.getBackground()).setColor(value);
        if (!editText.hasFocus()) editText.setText(formatColor(value));
        if (listener != null) listener.onColorValueChanged(type, value);

        if (shouldUpdateValue) {
            float[] hsv = new float[3];
            Color.colorToHSV(value, hsv);

            hue.setValue(Math.round(hsv[0]));
            saturation.setValue(Math.round(hsv[1]));
            lightness.setValue(Math.round(hsv[2]));
            alpha.setValue(Color.alpha(value));

            data.hue = Math.round(hsv[0] * 100);
            data.saturation = Math.round(hsv[1] * 10000);
            data.lightness = Math.round(hsv[2] * 10000);
            data.alpha = Color.alpha(value);
        }
    }

    public String formatColor(int argb) {
        return String.format("%08X", argb);
    }
}
