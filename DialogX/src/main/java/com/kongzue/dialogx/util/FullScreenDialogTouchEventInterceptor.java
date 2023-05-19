package com.kongzue.dialogx.util;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.ScrollController;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/19 13:54
 */
public class FullScreenDialogTouchEventInterceptor {
    
    /**
     * 下边三个值用于判断触控过程，
     * isBkgTouched：标记是否已按下
     * bkgTouchDownY：记录起始触控位置
     * scrolledY：记录 ScrollView 已滚动过的距离，下次触控事件将接着上次的位置继续滑动
     * bkgOldY：记录按下时 bkg 的位置，用于区分松开手指时，bkg 移动的方向。
     */
    private boolean isBkgTouched = false;
    private float bkgTouchDownY;
    private float bkgOldY;
    
    public FullScreenDialogTouchEventInterceptor(FullScreenDialog me, FullScreenDialog.DialogImpl impl) {
        refresh(me, impl);
    }
    
    public void refresh(final FullScreenDialog me, final FullScreenDialog.DialogImpl impl) {
        if (me == null || impl == null || impl.bkg == null) {
            return;
        }
        if (me.isAllowInterceptTouch()) {
            View touchView = impl.boxCustom;
            if (impl.scrollView != null) {
                touchView = impl.bkg;
            }
            touchView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            bkgTouchDownY = event.getY();
                            isBkgTouched = true;
                            bkgOldY = impl.bkg.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isBkgTouched) {
                                float aimY = impl.bkg.getY() + event.getY() - bkgTouchDownY;
                                if (impl.scrollView != null && impl.scrollView.isCanScroll()) {
                                    if (aimY > me.getDialogImpl().getEnterY()) {
                                        if (impl.scrollView.getScrollDistance() == 0) {
                                            if (impl.scrollView instanceof ScrollController) {
                                                ((ScrollController) impl.scrollView).lockScroll(true);
                                            }
                                            impl.bkg.setY(aimY);
                                        } else {
                                            bkgTouchDownY = event.getY();
                                        }
                                    } else {
                                        if (impl.scrollView instanceof ScrollController) {
                                            ((ScrollController) impl.scrollView).lockScroll(false);
                                        }
                                        impl.bkg.setY(me.getDialogImpl().getEnterY());
                                    }
                                } else {
                                    if (aimY < me.getDialogImpl().getEnterY()) {
                                        aimY = me.getDialogImpl().getEnterY();
                                    }
                                    impl.bkg.setY(aimY);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            isBkgTouched = false;
                            if (bkgOldY == me.getDialogImpl().getEnterY()) {
                                if (impl.bkg.getY() < DialogX.touchSlideTriggerThreshold) {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), me.getDialogImpl().getEnterY());
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                } else if (impl.bkg.getY() > impl.getEnterY() + DialogX.touchSlideTriggerThreshold) {
                                    impl.preDismiss();
                                } else {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), impl.getEnterY());
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                }
                            } else {
                                if (impl.bkg.getY() < bkgOldY - DialogX.touchSlideTriggerThreshold) {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), me.getDialogImpl().getEnterY());
                                    enterAnim.setDuration(300);
                                    enterAnim.start();
                                } else if (impl.bkg.getY() > bkgOldY + DialogX.touchSlideTriggerThreshold) {
                                    impl.preDismiss();
                                } else {
                                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(impl.bkg, "y", impl.bkg.getY(), impl.getEnterY());
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
            View touchView = impl.boxCustom;
            if (impl.scrollView != null) {
                touchView = impl.bkg;
            }
            if (impl.scrollView instanceof ScrollController) {
                ((ScrollController) impl.scrollView).lockScroll(false);
            }
            touchView.setOnTouchListener(null);
        }
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
