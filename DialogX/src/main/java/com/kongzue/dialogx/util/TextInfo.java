package com.kongzue.dialogx.util;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/10 22:01
 */
public class TextInfo {
    
    private int fontSize = -1;              //字号大小，值为-1时使用默认样式，单位：dp
    private int gravity = -1;               //对齐方式，值为-1时使用默认样式，取值可使用Gravity.CENTER等对齐方式
    private int fontColor = 1;              //文字颜色，值为1时使用默认样式，取值可以用Color.rgb(r,g,b)等方式获取
    private boolean bold = false;           //是否粗体
    private int maxLines = -1;              //最大行数
    private boolean showEllipsis = false;   //显示省略号
    
    public int getFontSize() {
        return fontSize;
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
    
    public TextInfo setFontColor(int fontColor) {
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
