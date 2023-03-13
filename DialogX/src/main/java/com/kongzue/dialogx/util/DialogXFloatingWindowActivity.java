package com.kongzue.dialogx.util;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialogx.R;
import com.kongzue.dialogx.interfaces.BaseDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/12/20 14:58
 */
public class DialogXFloatingWindowActivity extends AppCompatActivity {

    static WeakReference<DialogXFloatingWindowActivity> dialogXFloatingWindowActivity;
    int fromActivityHashCode;
    List<String> shownDialogXList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        dialogXFloatingWindowActivity = new WeakReference<>(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialogx_empty);

        int fromActivityUiStatus = getIntent().getIntExtra("fromActivityUiStatus", 0);
        if (fromActivityUiStatus == 0) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(fromActivityUiStatus | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        setFromActivityHashCode(getIntent().getIntExtra("from", 0));
        String dialogXKey;
        ActivityRunnable activityRunnable = BaseDialog.getActivityRunnable(dialogXKey = getIntent().getStringExtra("dialogXKey"));
        if (activityRunnable == null) {
            finish();
        } else {
            shownDialogXList.add(dialogXKey);
            activityRunnable.run(this);
        }

        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_CANCEL && getFromActivity() != null) {
                    if (getFromActivity() != null && !(getFromActivity() instanceof DialogXFloatingWindowActivity)) {
                        return getFromActivity().dispatchTouchEvent(event);
                    }
                }
                return false;
            }
        });
    }

    public boolean isSameFrom(int fromActivityHashCode) {
        return fromActivityHashCode == this.fromActivityHashCode;
    }

    public void showDialogX(String dialogXKey) {
        ActivityRunnable activityRunnable = BaseDialog.getActivityRunnable(dialogXKey);
        if (activityRunnable != null) {
            shownDialogXList.add(dialogXKey);
            activityRunnable.run(this);
        }
    }

    public int getFromActivityHashCode() {
        return fromActivityHashCode;
    }

    public DialogXFloatingWindowActivity setFromActivityHashCode(int fromActivityHashCode) {
        this.fromActivityHashCode = fromActivityHashCode;
        return this;
    }

    public static DialogXFloatingWindowActivity getDialogXFloatingWindowActivity() {
        if (dialogXFloatingWindowActivity == null) return null;
        return dialogXFloatingWindowActivity.get();
    }

    public void finish(String dialogXKey) {
        shownDialogXList.remove(dialogXKey);
        if (shownDialogXList.isEmpty()) {
            if (dialogXFloatingWindowActivity != null) {
                dialogXFloatingWindowActivity.clear();
            }
            dialogXFloatingWindowActivity = null;
            super.finish();
            int version = Integer.valueOf(Build.VERSION.SDK_INT);
            if (version > 5) {
                overridePendingTransition(0, 0);
            }
        }
    }

    public void finish() {
        if (dialogXFloatingWindowActivity != null) {
            dialogXFloatingWindowActivity.clear();
        }
        dialogXFloatingWindowActivity = null;
        super.finish();
        int version = Integer.valueOf(Build.VERSION.SDK_INT);
        if (version > 5) {
            overridePendingTransition(0, 0);
        }
    }

    boolean isScreenshot;

    public boolean isScreenshot() {
        return isScreenshot;
    }

    public DialogXFloatingWindowActivity setScreenshot(boolean screenshot) {
        isScreenshot = screenshot;
        return this;
    }

    /**
     * 原始用户操作界面
     */
    WeakReference<Activity> fromActivity;

    public Activity getFromActivity() {
        return fromActivity == null ? null : fromActivity.get();
    }

    public DialogXFloatingWindowActivity setFromActivity(Activity fromActivity) {
        this.fromActivity = new WeakReference<>(fromActivity);
        return this;
    }
}
