package com.kongzue.dialogx.interfaces;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.DialogX;

import static com.kongzue.dialogx.DialogX.ERROR_INIT_TIPS;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/8 17:00
 */
public abstract class OnBindView<D> {
    
    int layoutResId;
    View customView;
    Runnable privateRunnable;
    
    public OnBindView(int resId) {
        createView(resId, false);
    }
    
    public OnBindView(int resId, boolean async) {
        if (async) {
            new Thread() {
                @Override
                public void run() {
                    createView(resId, true);
                }
            }.start();
        } else {
            createView(resId, false);
        }
    }
    
    public void createView(int resId, boolean async) {
        if (BaseDialog.getContext() == null) {
            DialogX.error(ERROR_INIT_TIPS);
            return;
        }
        layoutResId = resId;
        customView = LayoutInflater.from(BaseDialog.getContext()).inflate(layoutResId, new RelativeLayout(BaseDialog.getContext()), false);
    
        runSuccessRunnable(async, createViewSuccessRunnable());
        runSuccessRunnable(async, privateRunnable);
    }
    
    private void runSuccessRunnable(boolean async, Runnable runnable) {
        if (runnable != null) {
            if (async) {
                BaseDialog.runOnMain(runnable);
            } else {
                runnable.run();
            }
        }
    }
    
    public OnBindView(View customView) {
        this.customView = customView;
    }
    
    public abstract void onBind(D dialog, View v);
    
    public int getLayoutResId() {
        return layoutResId;
    }
    
    public OnBindView<D> setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
        return this;
    }
    
    public View getCustomView() {
        if (customView == null) {
            customView = LayoutInflater.from(BaseDialog.getContext()).inflate(layoutResId, new RelativeLayout(BaseDialog.getContext()), false);
        }
        return customView;
    }
    
    public OnBindView<D> setCustomView(View customView) {
        this.customView = customView;
        return this;
    }
    
    public void clean() {
        layoutResId = 0;
        customView = null;
    }
    
    public OnBindView<D> bindParent(ViewGroup parentView) {
        if (getCustomView() == null) return this;
        if (getCustomView().getParent() != null) {
            if (getCustomView().getParent() == parentView) {
                return this;
            }
            ((ViewGroup) getCustomView().getParent()).removeView(getCustomView());
        }
        ViewGroup.LayoutParams lp = parentView.getLayoutParams();
        if (lp == null) {
            lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        parentView.addView(getCustomView(), lp);
        return this;
    }
    
    public OnBindView<D> bindParent(ViewGroup parentView, BaseDialog dialog) {
        if (getCustomView() == null) return this;
        if (getCustomView().getParent() != null) {
            if (getCustomView().getParent() == parentView) {
                return this;
            }
            ((ViewGroup) getCustomView().getParent()).removeView(getCustomView());
        }
        ViewGroup.LayoutParams lp = getCustomView().getLayoutParams();
        if (lp == null) {
            lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        parentView.addView(getCustomView(), lp);
        onBind((D) dialog, getCustomView());
        return this;
    }
    
    public Runnable createViewSuccessRunnable() {
        return null;
    }
    
    /**
     * @hide 内部方法，请勿使用
     */
    public OnBindView<D> setPrivateRunnable(Runnable privateRunnable) {
        if (customView != null) {
            runSuccessRunnable(false, privateRunnable);
        } else {
            this.privateRunnable = privateRunnable;
        }
        return this;
    }
}
