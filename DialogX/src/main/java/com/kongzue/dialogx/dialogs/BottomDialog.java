package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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
import com.kongzue.dialogx.interfaces.OnBindView;
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
    
    protected OnBindView<BottomDialog> onBindView;
    protected CharSequence title;
    protected CharSequence message;
    protected CharSequence cancelText;
    protected boolean allowInterceptTouch = true;
    
    protected DialogLifecycleCallback<BottomDialog> dialogLifecycleCallback;
    
    protected BottomDialog me = this;
    
    protected BottomDialog() {
        super();
    }
    
    private View dialogView;
    
    public static BottomDialog build() {
        return new BottomDialog();
    }
    
    public BottomDialog(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
    }
    
    public static BottomDialog show(CharSequence title, CharSequence message) {
        BottomDialog bottomDialog = new BottomDialog(title, message);
        bottomDialog.show();
        return bottomDialog;
    }
    
    public BottomDialog(CharSequence title, CharSequence message, OnBindView<BottomDialog> onBindView) {
        this.title = title;
        this.message = message;
        this.onBindView = onBindView;
    }
    
    public static BottomDialog show(CharSequence title, CharSequence message, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(title, message, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }
    
    public BottomDialog(CharSequence title, OnBindView<BottomDialog> onBindView) {
        this.title = title;
        this.onBindView = onBindView;
    }
    
    public static BottomDialog show(CharSequence title, OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(title, onBindView);
        bottomDialog.show();
        return bottomDialog;
    }
    
    public BottomDialog(OnBindView<BottomDialog> onBindView) {
        this.onBindView = onBindView;
    }
    
    public static BottomDialog show(OnBindView<BottomDialog> onBindView) {
        BottomDialog bottomDialog = new BottomDialog(onBindView);
        bottomDialog.show();
        return bottomDialog;
    }
    
    public void show() {
        int layoutId = isLightTheme() ? R.layout.layout_dialogx_bottom_material : R.layout.layout_dialogx_bottom_material_dark;
        if (style.overrideBottomDialogRes() != null) {
            layoutId = style.overrideBottomDialogRes().overrideDialogLayout(isLightTheme());
        }
        
        dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        show(dialogView);
    }
    
    protected DialogImpl dialogImpl;
    
    public class DialogImpl implements DialogConvertViewInterface {
        
        private BottomDialogTouchEventInterceptor bottomDialogTouchEventInterceptor;
        
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
                    
                    boxContent.getViewTreeObserver().addOnGlobalLayoutListener(onContentViewLayoutChangeListener);
                    
                    getDialogLifecycleCallback().onShow(me);
                    
                    onDialogInit(dialogImpl);
                    
                    if (onBindView != null) onBindView.onBind(me, onBindView.getCustomView());
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                }
            });
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null && onBackPressedListener.onBackPressed()) {
                        dismiss();
                        return false;
                    }
                    if (cancelable) {
                        dismiss();
                    }
                    return false;
                }
            });
            
            bottomDialogTouchEventInterceptor = new BottomDialogTouchEventInterceptor(me, dialogImpl);
            
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    bkg.setY(boxBkg.getHeight());
                    if (bkg.isChildScrollViewCanScroll()) {
                        bkgEnterAimY = boxBkg.getHeight() - bkg.getHeight() * 0.6f;
                    } else {
                        bkgEnterAimY = boxBkg.getHeight() - bkg.getHeight();
                    }
                    ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", boxBkg.getHeight(), bkgEnterAimY);
                    enterAnim.setDuration(300);
                    enterAnim.start();
                    boxRoot.animate().setDuration(enterAnim.getDuration()).alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null);
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
            
            showText(txtDialogTitle, title);
            showText(txtDialogTip, message);
            
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
            
            if (onBindView != null) {
                if (onBindView.getCustomView() != null) {
                    if (onBindView.getCustomView().isAttachedToWindow()) {
                        boxCustom.removeView(onBindView.getCustomView());
                    }
                    ViewGroup.LayoutParams lp = boxCustom.getLayoutParams();
                    if (lp == null) {
                        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    boxCustom.addView(onBindView.getCustomView(), lp);
                }
            }
            
            if (isAllowInterceptTouch()) {
                if (imgTab != null) imgTab.setVisibility(View.VISIBLE);
            } else {
                if (imgTab != null) imgTab.setVisibility(View.GONE);
            }
            
            bottomDialogTouchEventInterceptor.refresh(me, this);
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
        
        public void preDismiss() {
            if (cancelable) {
                doDismiss(boxRoot);
            } else {
                ObjectAnimator enterAnim = ObjectAnimator.ofFloat(bkg, "y", bkg.getY(), bkgEnterAimY);
                enterAnim.setDuration(300);
                enterAnim.start();
            }
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
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public BottomDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
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
    
    public CharSequence getTitle() {
        return title;
    }
    
    public BottomDialog setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }
    
    public CharSequence getMessage() {
        return message;
    }
    
    public BottomDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }
    
    public CharSequence getCancelText() {
        return cancelText;
    }
    
    public BottomDialog setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }
    
    public BottomDialog setCustomView(OnBindView<BottomDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public BottomDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public boolean isAllowInterceptTouch() {
        if (style.overrideBottomDialogRes() == null) {
            return false;
        } else {
            return allowInterceptTouch && style.overrideBottomDialogRes().touchSlide();
        }
    }
    
    public BottomDialog setAllowInterceptTouch(boolean allowInterceptTouch) {
        this.allowInterceptTouch = allowInterceptTouch;
        refreshUI();
        return this;
    }
    
    public BottomDialog setDialogImpl(DialogImpl dialogImpl) {
        this.dialogImpl = dialogImpl;
        return this;
    }
}
