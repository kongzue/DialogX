package com.kongzue.dialogx.interfaces;

import static com.kongzue.dialogx.interfaces.BaseDialog.error;

import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;

public abstract class OnBindingView<D, VB extends ViewBinding> extends OnBindView<D> {

    protected VB binding;

    public OnBindingView(VB binding) {
        super(binding.getRoot());
    }

    public OnBindingView(Class viewBindingClass) {
        super(getViewBindingRootView(viewBindingClass.getSimpleName()));
    }

    public OnBindingView(String viewBindingClassName) {
        super(getViewBindingRootView(viewBindingClassName));
    }

    private static View getViewBindingRootView(String bindingClassName) {
        try {
            Class<?> bindingClass = Class.forName(bindingClassName);
            Method inflateMethod = bindingClass.getMethod("inflate", LayoutInflater.class);
            return ((ViewBinding) inflateMethod.invoke((Object)null, LayoutInflater.from(BaseDialog.getContext()))).getRoot();
        } catch (Exception var5) {
            error("DialogX: OnBindingView初始化异常，未能根据bindingClassName找到对应的ViewBinding，请尝试指定ViewBinding实例");
            var5.printStackTrace();
        }
        return new View(BaseDialog.getContext());
    }

    public OnBindingView(int layoutResId) {
        super(layoutResId);
    }

    public OnBindingView(int layoutResId, boolean async) {
        super(layoutResId, async);
    }

    public OnBindingView(View customView) {
        super(customView);
    }

    public OnBindingView(Fragment fragment) {
        super(fragment);
    }

    public OnBindingView(android.app.Fragment supportFragment) {
        super(supportFragment);
    }

    @Override
    public void onBind(D dialog, View v) {
        onBind(dialog, v, binding);
    }

    public abstract void onBind(D dialog, View v, VB binding);
}
