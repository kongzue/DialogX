package com.kongzue.dialogxdemo;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.util.CycleRunner;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.KongzueStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.TextInfo;

@Layout(R.layout.activity_main)
@DarkStatusBarTheme(true)
@DarkNavigationBarTheme(true)
@NavigationBarBackgroundColorRes(R.color.colorNavbarBkg)
public class MainActivity extends BaseActivity {
    
    private RelativeLayout boxTable;
    private LinearLayout boxTableChild;
    private LinearLayout btnBack;
    private ImageView btnShare;
    private LinearLayout boxBody;
    private RadioGroup grpStyle;
    private RadioButton rdoIos;
    private RadioButton rdoMaterial;
    private RadioButton rdoKongzue;
    private RadioButton rdoMiui;
    private RadioGroup grpTheme;
    private RadioButton rdoLight;
    private RadioButton rdoDark;
    private TextView btnMessageDialog;
    private TextView btnSelectDialog;
    private TextView btnInputDialog;
    private TextView btnWaitDialog;
    private TextView btnWaitAndTipDialog;
    private TextView btnTipSuccess;
    private TextView btnTipWarning;
    private TextView btnTipError;
    private TextView btnTipProgress;
    private TextView btnShowBreak;
    private TextView btnBottomDialog;
    private TextView btnBottomMenu;
    private TextView btnCustomMessageDialog;
    private TextView btnCustomInputDialog;
    private TextView btnCustomBottomMenu;
    private TextView btnCustomNotification;
    private TextView btnCustomDialog;
    private TextView btnFullScreenDialogWebPage;
    private TextView btnFullScreenDialogLogin;
    
    @Override
    public void initViews() {
        boxTable = findViewById(R.id.box_table);
        boxTableChild = findViewById(R.id.box_table_child);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
        boxBody = findViewById(R.id.box_body);
        grpStyle = findViewById(R.id.grp_style);
        rdoIos = findViewById(R.id.rdo_ios);
        rdoMaterial = findViewById(R.id.rdo_material);
        rdoKongzue = findViewById(R.id.rdo_kongzue);
        rdoMiui = findViewById(R.id.rdo_miui);
        grpTheme = findViewById(R.id.grp_theme);
        rdoLight = findViewById(R.id.rdo_light);
        rdoDark = findViewById(R.id.rdo_dark);
        btnMessageDialog = findViewById(R.id.btn_messageDialog);
        btnSelectDialog = findViewById(R.id.btn_selectDialog);
        btnInputDialog = findViewById(R.id.btn_inputDialog);
        btnWaitDialog = findViewById(R.id.btn_waitDialog);
        btnWaitAndTipDialog = findViewById(R.id.btn_waitAndTipDialog);
        btnTipSuccess = findViewById(R.id.btn_tipSuccess);
        btnTipWarning = findViewById(R.id.btn_tipWarning);
        btnTipError = findViewById(R.id.btn_tipError);
        btnTipProgress = findViewById(R.id.btn_tipProgress);
        btnShowBreak = findViewById(R.id.btn_showBreak);
        btnBottomDialog = findViewById(R.id.btn_bottom_dialog);
        btnBottomMenu = findViewById(R.id.btn_bottom_menu);
        btnCustomMessageDialog = findViewById(R.id.btn_customMessageDialog);
        btnCustomInputDialog = findViewById(R.id.btn_customInputDialog);
        btnCustomBottomMenu = findViewById(R.id.btn_customBottomMenu);
        btnCustomNotification = findViewById(R.id.btn_customNotification);
        btnCustomDialog = findViewById(R.id.btn_customDialog);
        btnFullScreenDialogWebPage = findViewById(R.id.btn_fullScreenDialog_webPage);
        btnFullScreenDialogLogin = findViewById(R.id.btn_fullScreenDialog_login);
    }
    
    @Override
    public void initDatas(JumpParameter parameter) {
        DialogX.globalStyle = IOSStyle.style();
    }
    
    //用于模拟进度提示
    private CycleRunner cycleRunner;
    private float progress = 0;
    
    @Override
    public void setEvents() {
        grpTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_light:
                        DialogX.globalTheme = DialogX.THEME.LIGHT;
                        break;
                    case R.id.rdo_dark:
                        DialogX.globalTheme = DialogX.THEME.DARK;
                        break;
                }
            }
        });
        
        grpStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_material:
                        DialogX.globalStyle = MaterialStyle.style();
                        break;
                    case R.id.rdo_kongzue:
                        DialogX.globalStyle = KongzueStyle.style();
                        break;
                    case R.id.rdo_ios:
                        DialogX.globalStyle = IOSStyle.style();
                        break;
                    case R.id.rdo_miui:
                        DialogX.globalStyle = MIUIStyle.style();
                        break;
                }
            }
        });
        
        btnMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MessageDialog("标题", "正文内容", "确定").show();
            }
        });
        
        btnSelectDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MessageDialog("多选对话框", "移动App会将它从主屏幕移除并保留其所有数据。", "删除App", "取消", "移至App资源库")
                        .setButtonOrientation(LinearLayout.VERTICAL)
                        .setOkTextInfo(new TextInfo().setFontColor(Color.parseColor("#EB5545")))
                        .show();
            }
        });
        
        btnInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputDialog("标题", "正文内容", "确定", "取消", "正在输入的文字").setCancelable(false).show();
            }
        });
        
        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show("Please Wait!");
                runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WaitDialog.dismiss();
                    }
                }, 2000);
            }
        });
        
        btnWaitAndTipDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show("Please Wait!");
                runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TipDialog.show("Success!", WaitDialog.TYPE.SUCCESS);
                    }
                }, 2000);
            }
        });
        
        btnTipSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show("Success!", WaitDialog.TYPE.SUCCESS);
            }
        });
        
        btnTipWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show("Warning!", WaitDialog.TYPE.WARNING);
            }
        });
        
        btnTipError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show("Error!", WaitDialog.TYPE.ERROR);
            }
        });
        
        btnTipProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = 0;
                WaitDialog.show("连接服务器...");
                runOnMainDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cycleRunner = runOnMainCycle(new Runnable() {
                            @Override
                            public void run() {
                                progress = progress + 0.1f;
                                if (progress < 1f) {
                                    WaitDialog.show("正在加载" + ((int) (progress * 100)) + "%", progress);
                                } else {
                                    TipDialog.show("加载完成", WaitDialog.TYPE.SUCCESS);
                                    cycleRunner.cancel();
                                }
                            }
                        }, 1000, 1000);
                    }
                }, 3000);
            }
        });
    
        btnBottomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomDialog().show();
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        log("#MainActivity.onBackPressed");
        super.onBackPressed();
    }
}