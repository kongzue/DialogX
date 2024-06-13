package com.kongzue.dialogxdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogxdemo.R;

/**
 * AppCompatActivity 试验场，完成一些原生 UI 相关测试
 */
public class AppCompatActivityTest extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_compat_test);
    }
    
    public void btnTextClick(View view) {
        String s =  "你可以点击空白区域或返回键来关闭这个对话框";
        new BottomDialog("标题", "这里是对话框内容。\n" + s + "。\n底部对话框也支持自定义布局扩展使用方式。",
                new OnBindView<BottomDialog>(R.layout.layout_custom_reply) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                WaitDialog.show("please wait...");
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppCompatActivityTest.this.finish();
                                    }
                                },2000);
                            }
                        });
                    }
                })
                .show();
    }
}