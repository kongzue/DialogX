package com.kongzue.dialogxdemo;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.MaterialStyle;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/21 18:01
 */
public class App extends BaseApp<App> {
    @Override
    public void init() {
        BaseFrameworkSettings.DEBUGMODE = BuildConfig.DEBUG;

        DialogX.init(this);
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW;
        DialogX.useHaptic = true;

        //演示仅覆写一个设置，不覆写其他设置：
        DialogX.globalStyle = new MaterialStyle() {
            @Override
            public PopTipSettings popTipSettings() {
                //DefaultPopTipSettings 是主题中默认的 PopTip 设置，以下演示仅覆写其中的 align 设置
                return new DefaultPopTipSettings() {
                    @Override
                    public ALIGN align() {
                        return ALIGN.BOTTOM;
                    }
                };
            }
        };

        DialogX.globalTheme = DialogX.THEME.AUTO;
        DialogX.onlyOnePopTip = false;
        DialogX.onlyOnePopNotification = false;
        DialogX.DEBUGMODE = BuildConfig.DEBUG;
        DialogX.defaultWaitDialogWaitingText = "hahah";
        DialogX.defaultTipDialogSuccessText = "okok!!!";

        // 全局背景遮罩层模糊范例（Android 12+）
//        DialogX.dialogLifeCycleListener = new DialogLifecycleCallback<BaseDialog>() {
//            @Override
//            public void onShow(BaseDialog dialog) {
//                super.onShow(dialog);
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && !(dialog instanceof NoTouchInterface) ) {
//                    RenderEffect blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP);
//                    ((ViewGroup) dialog.getOwnActivity().getWindow().getDecorView()).getChildAt(0).setRenderEffect(blurEffect);
//                }
//            }

//            @Override
//            public void onDismiss(BaseDialog dialog) {
//                super.onDismiss(dialog);
//                List<BaseDialog> sameActivityRunningDialog = BaseDialog.getRunningDialogList(dialog.getOwnActivity());
//                Iterator<BaseDialog> iterator = sameActivityRunningDialog.iterator();
//                while (iterator.hasNext()) {
//                    if (iterator.next() instanceof NoTouchInterface) {
//                        iterator.remove();
//                    }
//                }
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && !(dialog instanceof PopTip) && !(dialog instanceof PopNotification) && (sameActivityRunningDialog.isEmpty() || sameActivityRunningDialog.get(0) == dialog)) {
//                    ((ViewGroup) dialog.getOwnActivity().getWindow().getDecorView()).getChildAt(0).setRenderEffect(null);
//                }
//            }
//        };
        //DialogX.ignoreUnsafeInsetsHorizontal = true;

        //以下代码用于测试后台 Service 启动对话框
//        DialogX.implIMPLMode = DialogX.IMPL_MODE.FLOATING_ACTIVITY;
//        Intent serviceStartIntent = new Intent(this, TestBackgroundService.class);
//        startService(serviceStartIntent);
    }
}
