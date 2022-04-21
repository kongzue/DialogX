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
    
    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    
    public void onShow(T dialog) {
        try {
            //概率性报 no event down from INITIALIZED，目前尚不清楚为何
            if (registry.getCurrentState() != Lifecycle.State.CREATED) {
                registry.setCurrentState(Lifecycle.State.CREATED);
            }
        } catch (Exception e) {
        }
    }
    
    public void onDismiss(T dialog) {
        try {
            if (registry.getCurrentState() != Lifecycle.State.DESTROYED) {
                //概率性报 no event down from INITIALIZED，目前尚不清楚为何
                registry.setCurrentState(Lifecycle.State.DESTROYED);
            }
        } catch (Exception e) {
        }
    }
    
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }
}
