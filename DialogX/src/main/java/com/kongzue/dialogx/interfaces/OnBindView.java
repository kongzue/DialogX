package com.kongzue.dialogx.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.DialogX;

import static com.kongzue.dialogx.DialogX.ERROR_INIT_TIPS;
import static com.kongzue.dialogx.interfaces.BaseDialog.log;

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
    OnViewLoadFinishListener onViewLoadFinishListener;
    
    public OnBindView(int layoutResId) {
        if (BaseDialog.getContext() == null) {
            DialogX.error(ERROR_INIT_TIPS);
            return;
        }
        this.layoutResId = layoutResId;
        customView = LayoutInflater.from(BaseDialog.getContext()).inflate(layoutResId, new RelativeLayout(BaseDialog.getContext()), false);
    }
    
    private boolean preLoading;
    
    public OnBindView(int layoutResId, boolean preLoading) {
        if (BaseDialog.getContext() == null) {
            DialogX.error(ERROR_INIT_TIPS);
            return;
        }
        this.layoutResId = layoutResId;
        this.preLoading = true;
        if (preLoading) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    customView = LayoutInflater.from(BaseDialog.getContext()).inflate(layoutResId, new RelativeLayout(BaseDialog.getContext()), false);
                    int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    customView.measure(widthSpec, heightSpec);
                    customView.invalidate();
                    
                    if (onViewLoadFinishListener!=null)onViewLoadFinishListener.onFinish(customView);
                }
            }.start();
        }else{
            customView = LayoutInflater.from(BaseDialog.getContext()).inflate(layoutResId, new RelativeLayout(BaseDialog.getContext()), false);
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
        if (preLoading && customView == null) {
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
    
    long loadStamp;
    
    public OnBindView<D> bindParent(ViewGroup parentView, BaseDialog dialog) {
        loadStamp = System.currentTimeMillis();
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
    
    public boolean isPreLoading() {
        return preLoading;
    }
    
    public interface OnViewLoadFinishListener{
        void onFinish(View view);
    }
    
    public OnBindView<D> setOnViewLoadFinishListener(OnViewLoadFinishListener onViewLoadFinishListener) {
        this.onViewLoadFinishListener = onViewLoadFinishListener;
        return this;
    }
}
