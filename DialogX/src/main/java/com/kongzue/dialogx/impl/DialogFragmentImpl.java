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

    @Override
    public void onStart() {
        super.onStart();
        final Activity activity = activityWeakReference.get();

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
                for (BaseDialog dialog : BaseDialog.getRunningDialogList()) {
                    if (dialog.getOwnActivity() == activity && dialog != baseDialog && dialog.getDialogImplMode() == baseDialog.getDialogImplMode()) {
                        if (!(dialog instanceof NoTouchInterface) && dialog.getDialogView() != null) {
                            Log.e(">>>", "onTouch: dialog=" + dialog + "  baseDialog="+baseDialog );
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
    public void dismiss() {
        dismissAllowingStateLoss();
    }
}
