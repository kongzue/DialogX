package com.kongzue.dialogx.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;

public class ItemDivider {

    private int left;       //左边距（dp）
    private int right;      //右边距（dp）
    private int width = 1;  //分割线宽度（px）
    private int[] color = {0xFFDFE1E5, 0xFF3A3A3A};     //颜色

    public ItemDivider() {
    }

    public ItemDivider(int left, int right, int width) {
        this.left = left;
        this.right = right;
        this.width = width;
    }

    public int getLeft() {
        return left;
    }

    public ItemDivider setLeft(int left) {
        this.left = left;
        return this;
    }

    public int getRight() {
        return right;
    }

    public ItemDivider setRight(int right) {
        this.right = right;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public ItemDivider setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getColor(boolean light) {
        return light ? color[0] : color[1];
    }

    public ItemDivider setColor(boolean light, int color) {
        if (light) {
            this.color[0] = color;
        } else {
            this.color[1] = color;
        }
        return this;
    }

    public ItemDivider setColor(int color) {
        this.color = new int[]{color, color};
        return this;
    }

    @Override
    public String toString() {
        return "ItemDivider{" +
                "left(dp)=" + left +
                ", right(dp)=" + right +
                ", width(px)=" + width +
                ", color(light)=" + String.format("#%06X", (0xFFFFFF & color[0])) +
                ", color(night)=" + String.format("#%06X", (0xFFFFFF & color[1])) +
                '}';
    }

    public Drawable createDividerDrawable(Context c, boolean light) {
        GradientDrawable dividerShape = new GradientDrawable();
        dividerShape.setShape(GradientDrawable.RECTANGLE);
        dividerShape.setColor(getColor(light));

        InsetDrawable insetDivider = new InsetDrawable(
                dividerShape,
                dip2px(c, left), 0, dip2px(c, right), 0
        );
        return insetDivider;
    }

    private int dip2px(Context c, float dpValue) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
