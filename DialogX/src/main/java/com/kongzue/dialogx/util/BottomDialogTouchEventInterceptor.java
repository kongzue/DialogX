package com.kongzue.dialogx.util;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.util.views.BottomDialogScrollView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/7 4:01
 */
public class BottomDialogTouchEventInterceptor {
    
    /**
     * 下边三个值用于判断触控过程，
     * isBkgTouched：标记是否已按下
     * bkgTouchDownY：记录起始触控位置
     * scrolledY：记录 ScrollView 已滚动过的距离，下次触控事件将接着上次的位置继续滑动
     * bkgOldY：记录按下时 bkg 的位置，用于区分松开手指时，bkg 移动的方向。
     */
    private boolean isBkgTouched = false;
    private float bkgTouchDownY;
    private float scrolledY;
    private float bkgOldY;
    /**
     * 0：bkg接收触控事件，-1：scrollView进行滚动
     * 此标记的意义在于，当从 [scrollView滚动] 与 [bkg接收触控事件] 状态切换时，
     * 需要对bkgTouchDownY、scrolledY的值进行刷新，否则触控连续过程会出现闪跳。
     */
    private int oldMode;
    
    public BottomDialogTouchEventInterceptor(BottomDialog me, BottomDialog.DialogImpl impl) {
        refresh(me, impl);
    }
    
    public void refresh(final BottomDialog me, final BottomDialog.DialogImpl impl) {
        if (me == null || impl == null || impl.bkg == null || impl.scrollView == null) {
            return;
        }
        /**
         * BottomDialog 触控事件说明：
         * bkg 将拦截并接管所有触控操作。
         * BottomDialog 的启动方式依据是内容布局高度是否大于可显示安全区域的高度。
         * bkg 会在合适的时机，直接接管控制 ScrollView 的滚动。
         * 因此，请确保内容布局的高度计算方式一定是按照内容高度计算，
         * 即，请重写 onMeasure 方法：
         * @Override
         * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         *     int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
         *     super.onMeasure(widthMeasureSpec, expandSpec);
         * }
         */
        if (me.isAllowInterceptTouch()) {
            impl.bkg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //这里 return 什么实际上无关紧要，重点在于 MaxRelativeLayout.java(dispatchTouchEvent:184) 的事件分发会独立触发此处的额外滑动事件
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            bkgTouchDownY = event.getY();
                            isBkgTouched = true;
                            bkgOldY = impl.bkg.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isBkgTouched) {
                                float aimY = impl.bkg.getY() + event.getY() - bkgTouchDownY;
                                if (impl.bkg.isChildScrollViewCanScroll()) {
                                    if (aimY > 0) {
                                        if (impl.scrollView.getScrollY() == 0) {
                                            if (impl.scrollView instanceof BottomDialogScrollView) {
                                                ((BottomDialogScrollView) impl.scrollView).lockScroll(true);
                                            }
                                            impl.bkg.setY(aimY);
                                        } else {
                                            bkgTouchDownY = event.getY();
                                        }
                                    } else {
                                        if (impl.scrollView instanceof BottomDialogScrollView) {
                                            ((BottomDialogScrollView) impl.scrollView).lockScroll(false);
                                        }
                                        impl.bkg.setY(0);
                                    }
                                } else {
                                    if (aimY > impl.bkgEnterAimY) {
                                        impl.bkg.setY(aimY);
                                    } else {
                                        impl.bkg.setY(impl.bkgEnterAimY);
                                    }
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            scrolledY = impl.scrollView.getScrollY();
                            isBkgTouched = false;
                            if (bkgOldY == 0) {
                                if (impl.bkg.getY() < dip2px(35)) {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), 0);
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                } else if (impl.bkg.getY() > impl.bkgEnterAimY + dip2px(35)) {
                                    impl.preDismiss();
                                } else {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), impl.bkgEnterAimY);
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                }
                            } else {
                                if (impl.bkg.getY() < bkgOldY - dip2px(35)) {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), 0);
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                } else if (impl.bkg.getY() > bkgOldY + dip2px(35)) {
                                    impl.preDismiss();
                                } else {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), impl.bkgEnterAimY);
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                }
                            }
                            break;
                    }
                    return false;
                }
            });
        } else {
            impl.bkg.setOnTouchListener(null);
        }
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
