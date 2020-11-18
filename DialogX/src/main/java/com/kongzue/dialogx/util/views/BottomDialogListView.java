package com.kongzue.dialogx.util.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

import com.kongzue.dialogx.dialogs.BottomDialog;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/10/6 23:42
 */
public class BottomDialogListView extends ListView {
    public BottomDialogListView(Context context) {
        super(context);
    }
    
    public BottomDialogListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public BottomDialogListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    private BottomDialog.DialogImpl dialogImpl;
    
    public BottomDialogListView(BottomDialog.DialogImpl dialog, Context context) {
        super(context);
        dialogImpl = dialog;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
}
