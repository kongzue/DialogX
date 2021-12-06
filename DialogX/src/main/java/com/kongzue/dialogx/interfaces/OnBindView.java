package com.kongzue.dialogx.interfaces;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;

import static com.kongzue.dialogx.DialogX.ERROR_INIT_TIPS;

import androidx.appcompat.app.AppCompatActivity;

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
    
    public OnBindView(int layoutResId) {
        if (BaseDialog.getContext() == null) {
            DialogX.error(ERROR_INIT_TIPS);
            return;
        }
        this.layoutResId = layoutResId;
        customView = LayoutInflater.from(BaseDialog.getContext()).inflate(layoutResId, new RelativeLayout(BaseDialog.getContext()), false);
    }
    
    public OnBindView(View customView) {
        this.customView = customView;
    }
    
    private androidx.fragment.app.Fragment fragment;
    private android.app.Fragment supportFragment;
    
    public OnBindView(androidx.fragment.app.Fragment fragment) {
        if (BaseDialog.getContext() == null) return;
        this.customView = new FrameLayout(BaseDialog.getContext());
        this.customView.setId(R.id.id_frame_layout_custom);
        this.fragment = fragment;
        this.supportFragment = null;
    }
    
    public OnBindView(android.app.Fragment supportFragment) {
        if (BaseDialog.getContext() == null) return;
        this.customView = new FrameLayout(BaseDialog.getContext());
        this.customView.setId(R.id.id_frame_layout_custom);
        this.supportFragment = supportFragment;
        this.fragment = null;
    }
    
    public abstract void onBind(D dialog, View v);
    
    public void onFragmentBind(D dialog, View frameLayout, androidx.fragment.app.Fragment fragment, androidx.fragment.app.FragmentManager fragmentManager) {
    }
    
    public void onFragmentBind(D dialog, View frameLayout, android.app.Fragment fragment, android.app.FragmentManager fragmentManager) {
    }
    
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
            customView.setId(R.id.id_frame_layout_custom);
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
    
    @Deprecated
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
        if (fragment != null || supportFragment != null) getCustomView().post(new Runnable() {
            @Override
            public void run() {
                if (fragment != null && getCustomView() instanceof FrameLayout && BaseDialog.getContext() instanceof AppCompatActivity) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) BaseDialog.getContext();
                    androidx.fragment.app.FragmentTransaction transaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.id_frame_layout_custom, fragment);
                    transaction.commit();
                    onFragmentBind((D) dialog, getCustomView(), fragment, appCompatActivity.getSupportFragmentManager());
                }
                if (supportFragment != null && getCustomView() instanceof FrameLayout && BaseDialog.getContext() instanceof Activity) {
                    Activity activity = (Activity) BaseDialog.getContext();
                    android.app.FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                    transaction.add(R.id.id_frame_layout_custom, supportFragment);
                    transaction.commit();
                    onFragmentBind((D) dialog, getCustomView(), supportFragment, activity.getFragmentManager());
                }
            }
        });
        return this;
    }
}
