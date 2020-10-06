package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.kongzue.dialogx.util.BottomDialogTouchEventInterceptor;
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
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        public DialogXBaseRelativeLayout boxRoot;
        public RelativeLayout boxBkg;
        public MaxRelativeLayout bkg;
        public ImageView imgTab;
        public TextView txtDialogTitle;
        public ScrollView scrollView;
        public LinearLayout boxContent;
        public TextView txtDialogTip;
        public RelativeLayout boxCustom;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            boxBkg = convertView.findViewById(R.id.box_bkg);
            bkg = convertView.findViewById(R.id.bkg);
            imgTab = convertView.findViewById(R.id.img_tab);
            txtDialogTitle = convertView.findViewById(R.id.txt_dialog_title);
            scrollView = convertView.findViewById(R.id.scrollView);
            boxContent = convertView.findViewById(R.id.box_content);
            txtDialogTip = convertView.findViewById(R.id.txt_dialog_tip);
            boxCustom = convertView.findViewById(R.id.box_custom);
            init();
            refreshView();
        }
        
        /**
         * 此值记录了BottomDialog启动后的位置
         * ·当内容高度大于屏幕安全区高度时，BottomDialog会以全屏方式启动，但一开始只会展开到 0.8×屏幕高度，
         * 此时可以再次上划查看全部内容。
         * ·当内容高度小于屏幕安全区高度时，BottomDialog会以内容高度启动。
         * <p>
         * 记录这个值的目的是，当用户向下滑动时，判断情况该回到这个位置还是关闭对话框，
         * 并阻止当内容高度已经完全显示时的继续向上滑动操作。
         */
        public float bkgEnterAimY;
        
        @Override
        public void init() {
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    
                    new BottomDialogTouchEventInterceptor(dialogImpl);
                    
                    boxContent.getViewTreeObserver().addOnGlobalLayoutListener(onContentViewLayoutChangeListener);
                    
                    getDialogLifecycleCallback().onShow(me);
                    
                    onDialogInit(dialogImpl);
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                }
            });
        }
        
        private ViewTreeObserver.OnGlobalLayoutListener onContentViewLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (boxContent != null) {
                    float oldY = bkgEnterAimY;
                    if (bkg.isChildScrollViewCanScroll()) {
                        bkgEnterAimY = boxBkg.getHeight() - bkg.getHeight() * 0.6f;
                    } else {
                        bkgEnterAimY = boxBkg.getHeight() - bkg.getHeight();
                    }
                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", oldY, bkgEnterAimY);
                    enterAnim.setDuration(300);
                    enterAnim.start();
                }
            }
        };
        
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
            
            if (boxContent != null)
                boxContent.getViewTreeObserver().removeOnGlobalLayoutListener(onContentViewLayoutChangeListener);
            
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
    
    protected void onDialogInit(DialogImpl dialog) {
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
