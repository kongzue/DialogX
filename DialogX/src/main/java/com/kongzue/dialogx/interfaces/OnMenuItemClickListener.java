package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/10 6:26
 */
public interface OnMenuItemClickListener<D> {
    boolean onClick(D dialog, CharSequence text, int index);
}
