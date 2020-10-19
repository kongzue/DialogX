package com.kongzue.dialogxdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.KongzueStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.TextInfo;

import java.util.ArrayList;

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
    private TextView btnBottomDialog;
    private TextView btnBottomMenu;
    private TextView btnBottomReply;
    private TextView btnCustomMessageDialog;
    private TextView btnCustomInputDialog;
    private TextView btnCustomBottomMenu;
    private TextView btnCustomDialog;
    private TextView btnFullScreenDialogWebPage;
    private TextView btnFullScreenDialogLogin;
    private TextView btnShowBreak;
    
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
        btnBottomDialog = findViewById(R.id.btn_bottom_dialog);
        btnBottomMenu = findViewById(R.id.btn_bottom_menu);
        btnBottomReply = findViewById(R.id.btn_bottom_reply);
        btnCustomMessageDialog = findViewById(R.id.btn_customMessageDialog);
        btnCustomInputDialog = findViewById(R.id.btn_customInputDialog);
        btnCustomBottomMenu = findViewById(R.id.btn_customBottomMenu);
        btnCustomDialog = findViewById(R.id.btn_customDialog);
        btnFullScreenDialogWebPage = findViewById(R.id.btn_fullScreenDialog_webPage);
        btnFullScreenDialogLogin = findViewById(R.id.btn_fullScreenDialog_login);
        btnShowBreak = findViewById(R.id.btn_showBreak);
    }
    
    @Override
    public void initDatas(JumpParameter parameter) {
        DialogX.globalStyle = IOSStyle.style();
        
        boolean showBreak = parameter.getBoolean("showBreak");
        if (showBreak){
            MessageDialog.show("提示","接下来会直接运行一个 WaitDialog，2 秒后直接关闭 Activity，并回到原 Activity，保证程序不会出现 WindowLeaked 错误。\n\n" +
                    "Android 原生 AlertDialog 常出现因 Dialog 先于 Activity 关闭而导致此错误引发程序崩溃。\n\n" +
                    "而使用 DialogX 构建的对话框不仅仅不会出现此问题，还可避免因句柄持续持有导致的内存泄漏。","开始测试","取消")
                    .setOkButton(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            WaitDialog.show("请稍后...");
                            runDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },2000);
                            return false;
                        }
                    })
                    .setCancelButton(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            finish();
                            return false;
                        }
                    });
        }
    }
    
    //用于模拟进度提示
    private CycleRunner cycleRunner;
    private float progress = 0;
    private int waitId;
    
    private TextView btnReplyCommit;
    private EditText editReplyCommit;
    
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
                MessageDialog.show("标题", "正文内容", "确定").setOkButton(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        toast("点击确定按钮");
                        return false;
                    }
                });
            }
        });
        
        btnSelectDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog("多选对话框", "移除App会将它从主屏幕移除并保留其所有数据。", "删除App", "取消", "移至App资源库")
                        .setButtonOrientation(LinearLayout.VERTICAL);
                if (!rdoMiui.isChecked()) {
                    messageDialog.setOkTextInfo(new TextInfo().setFontColor(Color.parseColor("#EB5545")));
                }
                messageDialog.show();
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
                WaitDialog.show("Please Wait!").setOnBackPressedListener(new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        toast("按下返回");
                        return false;
                    }
                });
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
                WaitDialog.show("Please Wait!").setOnBackPressedListener(new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        toast("按下返回");
                        return false;
                    }
                });
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
                waitId = 0;
                progress = 0;
                WaitDialog.show("假装连接...").setOnBackPressedListener(new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        MessageDialog.show("正在进行", "是否取消？", "是", "否").setOkButton(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                waitId = -1;
                                WaitDialog.dismiss();
                                return false;
                            }
                        });
                        return false;
                    }
                });
                runOnMainDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (waitId != 0) {
                            return;
                        }
                        cycleRunner = runOnMainCycle(new Runnable() {
                            @Override
                            public void run() {
                                if (waitId != 0) {
                                    cycleRunner.cancel();
                                    return;
                                }
                                progress = progress + 0.1f;
                                if (progress < 1f) {
                                    WaitDialog.show("假装加载" + ((int) (progress * 100)) + "%", progress);
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
                String s = rdoMaterial.isChecked() ? "你可以向下滑动来关闭这个对话框" : "你可以点击空白区域或返回键来关闭这个对话框";
                new BottomDialog("标题", "这里是对话框内容。\n" + s + "。\n底部对话框也支持自定义布局扩展使用方式。",
                        new OnBindView<BottomDialog>(R.layout.layout_custom_view) {
                            @Override
                            public void onBind(BottomDialog dialog, View v) {
                            
                            }
                        }).show();
            }
        });
        
        btnBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoMaterial.isChecked()) {
                    //Material 可滑动展开 BottomMenu 演示
                    BottomMenu.show(new String[]{"添加", "查看", "编辑", "删除", "分享", "评论", "下载", "收藏", "赞！", "不喜欢", "所属专辑", "复制链接", "类似推荐", "添加", "查看", "编辑", "删除", "分享", "评论", "下载", "收藏", "赞！", "不喜欢", "所属专辑", "复制链接", "类似推荐"})
                            .setOnIconChangeCallBack(new OnIconChangeCallBack(true) {
                                @Override
                                public int getIcon(BottomMenu bottomMenu, int index, String menuText) {
                                    switch (menuText) {
                                        case "添加":
                                            return R.mipmap.img_dialogx_demo_add;
                                        case "查看":
                                            return R.mipmap.img_dialogx_demo_view;
                                        case "编辑":
                                            return R.mipmap.img_dialogx_demo_edit;
                                        case "删除":
                                            return R.mipmap.img_dialogx_demo_delete;
                                        case "分享":
                                            return R.mipmap.img_dialogx_demo_share;
                                        case "评论":
                                            return R.mipmap.img_dialogx_demo_comment;
                                        case "下载":
                                            return R.mipmap.img_dialogx_demo_download;
                                        case "收藏":
                                            return R.mipmap.img_dialogx_demo_favorite;
                                        case "赞！":
                                            return R.mipmap.img_dialogx_demo_good;
                                        case "不喜欢":
                                            return R.mipmap.img_dialogx_demo_dislike;
                                        case "所属专辑":
                                            return R.mipmap.img_dialogx_demo_album;
                                        case "复制链接":
                                            return R.mipmap.img_dialogx_demo_link;
                                        case "类似推荐":
                                            return R.mipmap.img_dialogx_demo_recommend;
                                    }
                                    return 0;
                                }
                            })
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                @Override
                                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                    toast(text);
                                    return false;
                                }
                            });
                } else {
                    BottomMenu.show(new String[]{"新标签页中打开", "稍后阅读", "复制链接网址"})
                            .setMessage("http://www.kongzue.com/DialogX")
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                @Override
                                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                    toast(text);
                                    return false;
                                }
                            });
                }
            }
        });
        
        btnBottomReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.show(new OnBindView<BottomDialog>(rdoDark.isChecked() ? R.layout.layout_custom_reply_dark : R.layout.layout_custom_reply) {
                    @Override
                    public void onBind(final BottomDialog dialog, View v) {
                        btnReplyCommit = v.findViewById(R.id.btn_reply_commit);
                        editReplyCommit = v.findViewById(R.id.edit_reply_commit);
                        btnReplyCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                toast("提交内容：\n" + editReplyCommit.getText().toString());
                            }
                        });
                        editReplyCommit.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showIME(editReplyCommit);
                            }
                        }, 300);
                    }
                }).setAllowInterceptTouch(false);
            }
        });
        
        btnCustomMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog.show("这里是标题","此对话框演示的是自定义对话框内部布局的效果","确定","取消")
                        .setCustomView(new OnBindView<MessageDialog>(R.layout.layout_custom_view) {
                            @Override
                            public void onBind(MessageDialog dialog, View v) {
        
                            }
                        });
            }
        });
        
        btnCustomInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show("这里是标题","此对话框演示的是自定义对话框内部布局的效果","确定","取消")
                        .setCustomView(new OnBindView<MessageDialog>(R.layout.layout_custom_view) {
                            @Override
                            public void onBind(MessageDialog dialog, View v) {
                
                            }
                        });
            }
        });
        
        btnCustomBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(new String[]{"新标签页中打开", "稍后阅读", "复制链接网址"})
                        .setMessage("http://www.kongzue.com/DialogX")
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                toast(text);
                                return false;
                            }
                        })
                .setCustomView(new OnBindView<BottomDialog>(R.layout.layout_custom_view) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
        
                    }
                });
            }
        });
        
        btnShowBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(MainActivity.class,new JumpParameter().put("showBreak",true));
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        log("#MainActivity.onBackPressed");
        super.onBackPressed();
    }
    
    public void showIME(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}