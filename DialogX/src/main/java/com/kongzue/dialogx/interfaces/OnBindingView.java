package com.kongzue.dialogx.interfaces;

import static com.kongzue.dialogx.interfaces.BaseDialog.error;

import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.kongzue.dialogx.R;

import java.lang.reflect.Method;

public abstract class OnBindingView<D, VB extends ViewBinding> extends OnBindView<D> {

    protected VB binding;

    public OnBindingView(VB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public OnBindingView(Class viewBindingClass) {
        super(getBindingRootView(getViewBinding(viewBindingClass)));
        this.binding = (VB) getCustomView().getTag(R.id.dialogx_view_binding_tag_key);
    }

    public OnBindingView(String viewBindingClassName) {
        super(getBindingRootView(getViewBinding(viewBindingClassName)));
        this.binding = (VB) getCustomView().getTag(R.id.dialogx_view_binding_tag_key);
    }

    private static View getBindingRootView(ViewBinding viewBinding) {
        if (viewBinding == null) {
            return new View(BaseDialog.getContext());
        }
        View view = viewBinding.getRoot();
        view.setTag(R.id.dialogx_view_binding_tag_key, viewBinding);
        return view;
    }

    private static ViewBinding getViewBinding(String bindingClassName) {
        try {
            return getViewBinding(Class.forName(bindingClassName));
        } catch (ClassNotFoundException e) {
            error("DialogX: OnBindingView初始化异常，未能根据bindingClassName："+bindingClassName +"找到对应的ViewBinding，请尝试指定ViewBinding实例");
            throw new RuntimeException(e);
        }
    }

    private static ViewBinding getViewBinding(Class bindingClass) {
        try {
            Method inflateMethod = bindingClass.getMethod("inflate", LayoutInflater.class);
            return (ViewBinding) inflateMethod.invoke((Object) null, LayoutInflater.from(BaseDialog.getContext()));
        } catch (Exception var5) {
            error("DialogX: OnBindingView初始化异常，未能根据bindingClass找到对应的ViewBinding，请尝试指定ViewBinding实例");
            var5.printStackTrace();
        }
        return null;
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
