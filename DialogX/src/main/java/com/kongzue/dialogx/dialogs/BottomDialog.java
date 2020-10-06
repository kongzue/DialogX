package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 15:17
 */
public class BottomDialog extends BaseDialog {
    
    private DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback;
    
    protected BottomDialog me;
    
    public BottomDialog() {
        me = this;
    }
    
    private View dialogView;
    
    public void show() {
        int layoutId = R.layout.layout_dialogx_bottom_material;
        
        dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        show(dialogView);
    }
    
    protected DialogImpl dialogImpl;
    
    class DialogImpl implements DialogConvertViewInterface {
        
        DialogXBaseRelativeLayout boxRoot;
        RelativeLayout boxBkg;
        MaxRelativeLayout bkg;
        ImageView imgTab;
        TextView txtDialogTitle;
        ScrollView scrollView;
        TextView txtDialogTip;
        RelativeLayout boxCustom;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            imgTab = convertView.findViewById(R.id.img_tab);
            txtDialogTitle = convertView.findViewById(R.id.txt_dialog_title);
            scrollView = convertView.findViewById(R.id.scrollView);
            txtDialogTip = convertView.findViewById(R.id.txt_dialog_tip);
            boxCustom = convertView.findViewById(R.id.box_custom);
            init();
            refreshView();
        }
        
        private boolean isBkgTouched = false;
        private float bkgTouchDownY;
        private boolean isSvTouched = false;
        private boolean isInterceptTouched = false;
        private float svTouchDownY;
        private float bkgEnterAimY;
        
        @Override
        public void init() {
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    svTouchDownY = event.getY();
                                    isSvTouched = true;
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isSvTouched) {
                                        if (scrollView.getScrollY() == 0) {
                                            bkgTouchDownY = svTouchDownY;
                                            bkg.setInterceptTouchEvent(true);
                                            bkg.onTouchEvent(event);
                                            isBkgTouched = true;
                                            isInterceptTouched = true;
                                            return true;
                                        } else {
                                            svTouchDownY = event.getY();
                                            isInterceptTouched = false;
                                        }
                                    }
                                    if (isInterceptTouched || bkg.getY() > 0) {
                                        return true;
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    isInterceptTouched = false;
                                    isSvTouched = false;
                                    break;
                            }
                            return false;
                        }
                    });
                    
                    bkg.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    bkgTouchDownY = event.getY();
                                    isBkgTouched = true;
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isBkgTouched) {
                                        float aimY = bkg.getY() + event.getY() - bkgTouchDownY;
                                        if (aimY < 0) {
                                            bkg.setInterceptTouchEvent(false);
                                            scrollView.onTouchEvent(event);
                                            bkg.setY(0);
                                        } else {
                                            if (bkg.isChildScrollViewCanScroll()) {
                                                bkg.setInterceptTouchEvent(true);
                                                bkg.setY(aimY);
                                            } else {
                                                if (aimY > bkgEnterAimY) {
                                                    bkg.setInterceptTouchEvent(true);
                                                    bkg.setY(aimY);
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    isBkgTouched = false;
                                    if (bkg.getY() > 0) {
                                        if (bkg.getY() < bkgEnterAimY  + dip2px(15)) {
                                            ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), bkgEnterAimY);
                                            enterAnim.setDuration(300);
                                            enterAnim.start();
                                        } else {
                                            doDismiss(boxRoot);
                                        }
                                    }
                                    break;
                            }
                            return true;
                        }
                    });
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            bkg.setY(boxBkg.getHeight());
                            if (bkg.isChildScrollViewCanScroll()) {
                                bkgEnterAimY = boxBkg.getHeight() - bkg.getHeight() * 0.8f;
                            } else {
                                bkgEnterAimY = boxBkg.getHeight() - bkg.getHeight();
                            }
                            ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", boxBkg.getHeight(), bkgEnterAimY);
                            enterAnim.setDuration(300);
                            enterAnim.start();
                            boxRoot.animate().setDuration(enterAnim.getDuration()).alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null);
                            
                            bkg.setInterceptTouchEvent(true);
                        }
                    });
                    
                    getDialogLifecycleCallback().onShow(me);
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                }
            });
        }
        
        @Override
        public void refreshView() {
            txtDialogTitle.getPaint().setFakeBoldText(true);
            if (cancelable) {
                boxRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss(v);
                    }
                });
            } else {
                boxRoot.setOnClickListener(null);
            }
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (cancelable) {
                        dismiss();
                    }
                    return false;
                }
            });
        }
        
        @Override
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            
            ObjectAnimator exitAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), boxBkg.getHeight());
            exitAnim.setDuration(300);
            exitAnim.start();
            
            boxRoot.animate().setDuration(300).alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(exitAnim.getDuration()).setListener(new AnimatorListenerEndCallBack() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismiss(dialogView);
                }
            });
        }
    }
    
    public void refreshUI() {
        if (dialogImpl == null) return;
        dialogImpl.refreshView();
    }
    
    public void dismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    public DialogLifecycleCallback<BottomDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<BottomDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public BottomDialog setDialogLifecycleCallback(DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        return this;
    }
    
    public BottomDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public BottomDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }
    
    public boolean isCancelable() {
        return cancelable;
    }
    
    public BottomDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        refreshUI();
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
}
