package com.kongzue.dialogx.util;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;

import com.kongzue.dialogx.dialogs.FullScreenDialog;

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
        
        impl.boxCustom.setOnTouchListener(new View.OnTouchListener() {
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
                            if (aimY < 0) {
                                aimY = 0;
                            }
                            impl.bkg.setY(aimY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
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
                return true;
            }
        });
    }
    
    private int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
