package com.kongzue.dialogxdemo.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogxdemo.R;

public class TestMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        }
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        findViewById(R.id.btn_showDialog).setOnClickListener(view -> {
            BottomDialog.show("标题", "这里是对话框内容。")
                    .setCancelButton("取消", (dialog, v) -> false)
                    .setOkButton("确定", (dialog, v) -> false);
        });
    }
}
