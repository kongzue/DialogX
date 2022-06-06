package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/8/5 8:39
 */
public interface ScrollController {
    
    /**
     * 返回已滚动的距离
     * 若该距离为 0，BottomDialog、FullScreenDialog 将直接接管和衔接触控事件
     * 若距离不为 0，由本控件处理触控事件
     *
     * @return 已滚动的距离
     */
    int getScrollDistance();
    
    /**
     * 设置是否可以滑动
     * 若可以滑动，BottomDialog、FullScreenDialog 将尝试接管和衔接触控事件，
     * 若不可以滑动，BottomDialog、FullScreenDialog 将直接拦截触控事件
     *
     * @return 是否可以滑动
     */
    boolean isCanScroll();
    
    /**
     * BottomDialog、FullScreenDialog 在接管触控事件时会通过此方法传入 lockScroll，
     * 若 lockScroll 为真，请勿处理任何触摸事件。
     *
     * 举例：建议增加以下代码：
     * @Override
     * public boolean onTouchEvent(MotionEvent event) {
     *     if (lockScroll) return false;
     *     return super.onTouchEvent(event);
     * }
     *
     * @param lockScroll 是否锁定滑动
     */
    void lockScroll(boolean lockScroll);
    
    boolean isLockScroll();
}
