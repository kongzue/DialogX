package com.kongzue.dialogx.impl;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
import static com.kongzue.dialogx.DialogX.error;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.NoTouchInterface;

import java.lang.ref.WeakReference;
import java.util.HashSet;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/7/28 15:45
 */
public class DialogFragmentImpl extends DialogFragment {

    public DialogFragmentImpl() {
    }

    private View dialogView;
    private BaseDialog baseDialog;

    public DialogFragmentImpl(BaseDialog baseDialog, View dialogView) {
        this.dialogView = dialogView;
        this.baseDialog = baseDialog;
        activityWeakReference = new WeakReference<>(baseDialog.getOwnActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return dialogView;
    }

    WeakReference<Activity> activityWeakReference = null;

    // Tracks the dialog root views on the current touch-forwarding chain.
    // When two or more dialogs are alive, each one forwards touches to the others;
    // without this guard a touch landing on an empty area ping-pongs A→B→A… until StackOverflowError.
    static final ThreadLocal<HashSet<View>> FORWARDING_CHAIN = new ThreadLocal<>();

    @Override
    public void onStart() {
        super.onStart();
      
        final Activity activity = activityWeakReference != null ? activityWeakReference.get() : null;
        if(activity == null){
          return;
        }

        if (getDialog() == null) return;
        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow == null) return;
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.dimAmount = 0f;
        lp.format = PixelFormat.TRANSPARENT;
        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HashSet<View> chain = FORWARDING_CHAIN.get();
                if (chain == null) {
                    chain = new HashSet<>();
                    FORWARDING_CHAIN.set(chain);
                }
                if (chain.contains(v)) {
                    // 该弹窗已在转发链上：再次到达即为回环，放弃转发，交回普通触摸处理
                    return false;
                }
                chain.add(v);
                try {
                    for (BaseDialog dialog : BaseDialog.getRunningDialogList()) {
                        if (dialog.getOwnActivity() == activity && dialog != baseDialog && dialog.getDialogImplMode() == baseDialog.getDialogImplMode()) {
                            if (!(dialog instanceof NoTouchInterface) && dialog.getDialogView() != null) {
                                dialog.getDialogView().dispatchTouchEvent(event);
                                return true;
                            }
                        }
                    }
                    if (baseDialog instanceof NoTouchInterface) {
                        return activity.dispatchTouchEvent(event);
                    } else {
                        return false;
                    }
                } finally {
                    chain.remove(v);
                    if (chain.isEmpty()) {
                        FORWARDING_CHAIN.remove();
                    }
                }
            }
        });
        dialogWindow.setAttributes(lp);
        dialogWindow.addFlags(FLAG_TRANSLUCENT_STATUS);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (activity != null) {
                if ((activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                    visibility = visibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            }
            dialogWindow.getDecorView().setSystemUiVisibility(visibility);
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            dialogWindow.setStatusBarColor(Color.TRANSPARENT);
            dialogWindow.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void show(FragmentManager manager, @Nullable String tag) {
        if (manager == null) {
            error("DialogX.DialogFragment 模式无法支持非 AppCompatActivity 启动。");
            return;
        }
        //super.show(manager, tag);

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        if (dialogView != null) {
            dialogView.setOnTouchListener(null);
            if (dialogView.getParent() instanceof ViewGroup) {
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialogView = null;
        baseDialog = null;
        if (activityWeakReference != null) {
            activityWeakReference.clear();
            activityWeakReference = null;
        }
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }
}
