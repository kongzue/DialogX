package com.kongzue.dialogx.interfaces;

import android.animation.ValueAnimator;
import android.view.View;

public abstract class PopMoveDisplacementInterceptor<D extends BaseDialog> {

    /**
     * 重置提示对话框新增时，旧的对话框让位位移的动画具体参数
     *
     * @param index        对话框索引
     * @param dialog       对话框
     * @param fromY        从哪来
     * @param toY          往哪去
     * @param dialogHeight 对话框本身的高度
     * @param allTipSize   提示框总数
     * @param moveBack     是否向后位移
     * @return 你要改为往哪去
     */
    public float resetAnimY(int index, D dialog, float fromY, float toY, int dialogHeight, int allTipSize, boolean moveBack) {
        return toY;
    }

    /**
     * 动画更新器
     *
     * @param index         对话框索引
     * @param dialog        对话框
     * @param dialogBody    对话框内容布局
     * @param fromY         从哪来
     * @param toY           往哪去
     * @param progress      动画进度（0f~1f）
     * @param animation     动画执行器
     * @param allTipSize    提示框总数
     * @param moveBack      是否向后位移
     * @return 返回true表示拦截处理，否则依然会执行原本的动画
     */
    public boolean animUpdater(int index, D dialog, View dialogBody, float fromY, float toY, float progress, ValueAnimator animation, int allTipSize, boolean moveBack) {
        return false;
    }
}
