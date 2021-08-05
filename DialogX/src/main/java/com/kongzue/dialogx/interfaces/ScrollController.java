package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/5 8:39
 */
public interface ScrollController {
    
    boolean isLockScroll();
    
    void lockScroll(boolean lockScroll);
    
    int getScrollDistance();
    
    boolean isCanScroll();
}
