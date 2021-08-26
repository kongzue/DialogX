package com.kongzue.dialogx.interfaces;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/22 14:09
 */
public abstract class DialogLifecycleCallback<T extends BaseDialog> implements LifecycleOwner {
    
    private LifecycleRegistry registry = new LifecycleRegistry(this);
    
    public void onShow(T dialog) {
        if (registry.getCurrentState()!= Lifecycle.State.CREATED){
            registry.setCurrentState(Lifecycle.State.CREATED);
        }
    }
    
    public void onDismiss(T dialog) {
        if (registry.getCurrentState()!= Lifecycle.State.DESTROYED){
            registry.setCurrentState(Lifecycle.State.DESTROYED);
        }
    }
    
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }
}
