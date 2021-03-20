package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 14:09
 */
public abstract class DialogLifecycleCallback<T extends BaseDialog> {
    
    public void onShow(T dialog){
    
    }
    
    public void onDismiss(T dialog){
    
    }
}
