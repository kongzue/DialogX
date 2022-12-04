package com.kongzue.dialogx.util;

import android.util.TypedValue;

import androidx.annotation.ColorInt;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/10 22:01
 */
public class TextInfo {
    
    private int fontSize = -1;              //字号大小，值为-1时使用默认样式，单位：dp
    private FONT_SIZE_UNIT fontSizeUnit = FONT_SIZE_UNIT.DP;  //字号单位
    private int gravity = -1;               //对齐方式，值为-1时使用默认样式，取值可使用Gravity.CENTER等对齐方式
    private int fontColor = 1;              //文字颜色，值为1时使用默认样式，取值可以用Color.rgb(r,g,b)等方式获取
    private boolean bold = false;           //是否粗体
    private int maxLines = -1;              //最大行数
    private boolean showEllipsis = false;   //显示省略号
    
    public enum FONT_SIZE_UNIT {
        DP,
        PX,
        SP
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public int getFontSizeComplexUnit() {
        if (fontSizeUnit == null) {
            return TypedValue.COMPLEX_UNIT_DIP;
        }
        switch (fontSizeUnit) {
            case PX:
                return TypedValue.COMPLEX_UNIT_PX;
            case SP:
                return TypedValue.COMPLEX_UNIT_SP;
            default:
                return TypedValue.COMPLEX_UNIT_DIP;
        }
    }
    
    public TextInfo setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }
    
    public int getGravity() {
        return gravity;
    }
    
    public TextInfo setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }
    
    public int getFontColor() {
        return fontColor;
    }
    
    public TextInfo setFontColor(@ColorInt int fontColor) {
        this.fontColor = fontColor;
        return this;
    }
    
    public boolean isBold() {
        return bold;
    }
    
    public TextInfo setBold(boolean bold) {
        this.bold = bold;
        return this;
    }
    
    public int getMaxLines() {
        return maxLines;
    }
    
    public TextInfo setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        return this;
    }
    
    public boolean isShowEllipsis() {
        return showEllipsis;
    }
    
    public TextInfo setShowEllipsis(boolean showEllipsis) {
        this.showEllipsis = showEllipsis;
        return this;
    }
    
    public FONT_SIZE_UNIT getFontSizeUnit() {
        return fontSizeUnit;
    }
    
    public TextInfo setFontSizeUnit(FONT_SIZE_UNIT fontSizeUnit) {
        this.fontSizeUnit = fontSizeUnit;
        return this;
    }
    
    @Override
    public String toString() {
        return "TextInfo{" +
                "fontSize=" + fontSize +
                ", gravity=" + gravity +
                ", fontColor=" + fontColor +
                ", bold=" + bold +
                ", maxLines=" + maxLines +
                ", showEllipsis=" + showEllipsis +
                '}';
    }
}
