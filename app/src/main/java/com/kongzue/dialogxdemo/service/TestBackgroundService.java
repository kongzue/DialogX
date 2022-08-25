package com.kongzue.dialogxdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogxdemo.activity.MainActivity;

/**
 * 测试从 Service 中启动
 *
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/8/25 18:18
 */
public class TestBackgroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(">>>", "TestService#onStartCommand: ");
        MainActivity mainActivity = MainActivity.getActivity(MainActivity.class);
        if (mainActivity != null) {
            mainActivity.finish();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MessageDialog.show("Title", "Message", "OK");
        return super.onStartCommand(intent, flags, startId);
    }
}
