package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExtendChildLayoutParamsFrameLayout extends FrameLayout {
    public ExtendChildLayoutParamsFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ExtendChildLayoutParamsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendChildLayoutParamsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        setLayoutParams(new RelativeLayout.LayoutParams(params.width, params.height));
        super.addView(child, index, params);
    }
}
