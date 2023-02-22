package com.kongzue.dialogx.interfaces;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2023/2/22 16:47
 */
public interface MenuItemLayoutRefreshCallback<D> {
    
    void getView(D dialog, int position, View convertView, ViewGroup parent);
}
