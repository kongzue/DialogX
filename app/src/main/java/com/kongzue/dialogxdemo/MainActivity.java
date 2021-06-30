package com.kongzue.dialogxdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;

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
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.KongzueStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Layout(R.layout.activity_main)
@DarkStatusBarTheme(true)
@DarkNavigationBarTheme(true)
@NavigationBarBackgroundColorRes(R.color.emptyNavBar)
public class MainActivity extends BaseActivity {
    
    private RelativeLayout boxTable;
    private LinearLayout boxTableChild;
    private LinearLayout btnBack;
    private ImageView btnShare;
    private TextView txtTitle;
    private LinearLayout boxBody;
    private RadioGroup grpStyle;
    private RadioButton rdoMaterial;
    private RadioButton rdoIos;
    private RadioButton rdoKongzue;
    private RadioButton rdoMiui;
    private RadioGroup grpTheme;
    private RadioButton rdoAuto;
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
    private TextView btnPoptip;
    private TextView btnPoptipBigMessage;
    private TextView btnBottomDialog;
    private TextView btnBottomMenu;
    private TextView btnBottomReply;
    private TextView btnBottomSelectMenu;
    private TextView btnBottomMultiSelectMenu;
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
        txtTitle = findViewById(R.id.txt_title);
        boxBody = findViewById(R.id.box_body);
        grpStyle = findViewById(R.id.grp_style);
        rdoMaterial = findViewById(R.id.rdo_material);
        rdoIos = findViewById(R.id.rdo_ios);
        rdoKongzue = findViewById(R.id.rdo_kongzue);
        rdoMiui = findViewById(R.id.rdo_miui);
        grpTheme = findViewById(R.id.grp_theme);
        rdoAuto = findViewById(R.id.rdo_auto);
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
        btnPoptip = findViewById(R.id.btn_poptip);
        btnPoptipBigMessage = findViewById(R.id.btn_poptip_bigMessage);
        btnBottomDialog = findViewById(R.id.btn_bottom_dialog);
        btnBottomMenu = findViewById(R.id.btn_bottom_menu);
        btnBottomReply = findViewById(R.id.btn_bottom_reply);
        btnBottomSelectMenu = findViewById(R.id.btn_bottom_select_menu);
        btnBottomMultiSelectMenu = findViewById(R.id.btn_bottom_multiSelect_menu);
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
        refreshUIMode();
        
        boolean showBreak = parameter.getBoolean("showBreak");
        if (showBreak) {
            txtTitle.setText("显示Dialog时关闭Activity演示");
            MessageDialog.show("提示", "接下来会直接运行一个 WaitDialog，2 秒后直接关闭 Activity，并回到原 Activity，保证程序不会出现 WindowLeaked 错误。\n\n" +
                    "Android 原生 AlertDialog 常出现因 Dialog 先于 Activity 关闭而导致此错误引发程序崩溃。\n\n" +
                    "而使用 DialogX 构建的对话框不仅仅不会出现此问题，还可避免因句柄持续持有导致的内存泄漏。", "开始测试", "取消")
                    .setOkButton(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            WaitDialog.show("请稍后...");
                            runDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();       //先结束掉本界面
                                }
                            }, 2000);
                            return false;
                        }
                    })
                    .setCancelButton(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            finish();
                            return false;
                        }
                    })
                    .setCancelable(false);
        }
    }
    
    //用于模拟进度提示
    private CycleRunner cycleRunner;
    private float progress = 0;
    private int waitId;
    
    private TextView btnReplyCommit;
    private EditText editReplyCommit;
    
    private TextView btnCancel;
    private TextView btnSubmit;
    private RelativeLayout boxUserName;
    private EditText editUserName;
    private RelativeLayout boxPassword;
    private EditText editPassword;
    
    private TextView btnClose;
    private WebView webView;
    
    private String[] singleSelectMenuText = new String[]{"拒绝", "询问", "始终允许", "仅在使用中允许"};
    private int selectMenuIndex;
    
    private String[] multiSelectMenuText = new String[]{"上海", "北京", "广州", "深圳"};
    private int[] selectMenuIndexArray;
    private String multiSelectMenuResultCache;
    
    @Override
    public void setEvents() {
        grpTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_auto:
                        DialogX.globalTheme = DialogX.THEME.AUTO;
                        break;
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
                DialogX.cancelButtonText = "取消";
                switch (checkedId) {
                    case R.id.rdo_material:
                        DialogX.globalStyle = MaterialStyle.style();
                        DialogX.cancelButtonText = "";
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
                MessageDialog.show("标题", "这里是正文内容。", "确定").setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog baseDialog, View v) {
                        PopTip.show("点击确定按钮");
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
                new InputDialog("标题", "正文内容", "确定", "取消", "正在输入的文字")
                        .setInputText("Hello World")
                        .setCancelable(false)
                        .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                            @Override
                            public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                                PopTip.show("输入的内容：" + inputStr);
                                return false;
                            }
                        })
                        .show();
            }
        });
        
        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show("Please Wait!").setOnBackPressedListener(new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        PopTip.show("按下返回");
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
                        PopTip.show("按下返回");
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
                        })
                        .show();
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
                                    PopTip.show(text);
                                    return false;
                                }
                            });
                } else {
                    BottomMenu.show(new String[]{"新标签页中打开", "稍后阅读", "复制链接网址"})
                            .setMessage("http://www.kongzue.com/DialogX")
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                @Override
                                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                    PopTip.show(text);
                                    dialog.setMenuList(new String[]{"test", "test2", "test3"});
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
                                PopTip.show("提交内容：\n" + editReplyCommit.getText().toString());
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
                MessageDialog.show("这里是标题", "此对话框演示的是自定义对话框内部布局的效果", "确定", "取消")
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
                InputDialog.show("这里是标题", "此对话框演示的是自定义对话框内部布局的效果", "确定", "取消")
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
                                PopTip.show(text);
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
                jump(MainActivity.class, new JumpParameter().put("showBreak", true));
            }
        });
        
        btnFullScreenDialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_full_login) {
                    @Override
                    public void onBind(FullScreenDialog dialog, View v) {
                        btnCancel = v.findViewById(R.id.btn_cancel);
                        btnSubmit = v.findViewById(R.id.btn_submit);
                        boxUserName = v.findViewById(R.id.box_userName);
                        editUserName = v.findViewById(R.id.edit_userName);
                        boxPassword = v.findViewById(R.id.box_password);
                        editPassword = v.findViewById(R.id.edit_password);
                        
                        initFullScreenLoginDemo(dialog);
                    }
                });
            }
        });
        
        btnFullScreenDialogWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_full_webview) {
                    @Override
                    public void onBind(final FullScreenDialog dialog, View v) {
                        btnClose = v.findViewById(R.id.btn_close);
                        webView = v.findViewById(R.id.webView);
                        
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setLoadWithOverviewMode(true);
                        webSettings.setUseWideViewPort(true);
                        webSettings.setSupportZoom(false);
                        webSettings.setAllowFileAccess(true);
                        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                        webSettings.setLoadsImagesAutomatically(true);
                        webSettings.setDefaultTextEncodingName("utf-8");
                        
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                            
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                            }
                        });
                        
                        webView.loadUrl("https://github.com/kongzue/DialogV3/");
                    }
                });
            }
        });
        
        btnCustomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.show(new OnBindView<CustomDialog>(R.layout.layout_custom_dialog) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        ImageView btnOk;
                        btnOk = v.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                        .setMaskColor(getResources().getColor(R.color.black30));
            }
        });
        
        btnPoptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopTip.show("这是一个提示");
            }
        });
        
        btnPoptipBigMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoIos.isChecked()) {
                    PopTip.show(R.mipmap.img_air_pods_pro, "AirPods Pro 已连接").setAutoTintIconInLightOrDarkMode(false).showLong();
                } else {
                    PopTip.show(R.mipmap.img_mail_line_white, "邮件已发送", "撤回").setButton(new OnDialogButtonClickListener<PopTip>() {
                        @Override
                        public boolean onClick(PopTip popTip, View v) {
                            //点击“撤回”按钮回调
                            toast("邮件已撤回");
                            return false;
                        }
                    }).showLong();
                }
            }
        });
        
        btnBottomSelectMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(singleSelectMenuText)
                        .setMessage("这里是权限确认的文本说明，这是一个演示菜单")
                        .setTitle("获得权限标题")
                        .setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                            @Override
                            public void onOneItemSelect(BottomMenu dialog, CharSequence text, int index, boolean select) {
                                selectMenuIndex = index;
                            }
                        })
                        .setCancelButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                            @Override
                            public boolean onClick(BottomDialog baseDialog, View v) {
                                PopTip.show("已选择：" + singleSelectMenuText[selectMenuIndex]);
                                return false;
                            }
                        })
                        .setSelection(selectMenuIndex);
            }
        });
        
        btnBottomMultiSelectMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(multiSelectMenuText)
                        .setMessage("这里是权限确认的文本说明，这是一个演示菜单")
                        .setTitle("获得权限标题")
                        .setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                            @Override
                            public void onMultiItemSelect(BottomMenu dialog, CharSequence[] text, int[] index) {
                                multiSelectMenuResultCache = "";
                                for (CharSequence c : text) {
                                    multiSelectMenuResultCache = multiSelectMenuResultCache + " " + c;
                                }
                                selectMenuIndexArray = index;
                            }
                        })
                        .setCancelButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                            @Override
                            public boolean onClick(BottomDialog baseDialog, View v) {
                                PopTip.show("已选择：" + multiSelectMenuResultCache);
                                return false;
                            }
                        })
                        .setSelection(selectMenuIndexArray);
            }
        });
    }

//    private void showSecondDialog1() {
//        SpannableString spannableString = new SpannableString("这里是一段文本，点击链接");
//        spannableString.setSpan(new ClickableSpan(){
//            @Override
//            public void onClick(@NonNull View widget) {
//                showSecondDialog2();
//            }
//        },9, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        MessageDialog.show("标题", spannableString, "确定");
//    }
//
//    private void showSecondDialog2() {
//        SpannableString spannableString = new SpannableString("这里是另一段一段文本，再次点击链接");
//        spannableString.setSpan(new ClickableSpan(){
//            @Override
//            public void onClick(@NonNull View widget) {
//                showSecondDialog1();
//            }
//        },14, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        MessageDialog.show("标题", spannableString, "确定");
//    }
    
    private void initFullScreenLoginDemo(final FullScreenDialog fullScreenDialog) {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenDialog.dismiss();
            }
        });
        
        btnCancel.setText("取消");
        btnSubmit.setText("下一步");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(editUserName.getText().toString().trim())) {
                    hideIME(null);
                    TipDialog.show("请输入账号", TipDialog.TYPE.WARNING);
                    return;
                }
                
                boxUserName.animate().x(-getDisplayWidth()).setDuration(300);
                boxPassword.setX(getDisplayWidth());
                boxPassword.setVisibility(View.VISIBLE);
                boxPassword.animate().x(0).setDuration(300);
                
                editPassword.setFocusable(true);
                editPassword.requestFocus();
                
                btnCancel.setText("上一步");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boxUserName.animate().x(0).setDuration(300);
                        boxPassword.animate().x(getDisplayWidth()).setDuration(300);
                        
                        editUserName.setFocusable(true);
                        editUserName.requestFocus();
                        
                        initFullScreenLoginDemo(fullScreenDialog);
                    }
                });
                
                btnSubmit.setText("登录");
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideIME(null);
                        if (isNull(editPassword.getText().toString().trim())) {
                            TipDialog.show("请输入密码", TipDialog.TYPE.WARNING);
                            return;
                        }
                        WaitDialog.show("登录中...");
                        runOnMainDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TipDialog.show("登录成功", TipDialog.TYPE.SUCCESS).setDialogLifecycleCallback(new DialogLifecycleCallback<WaitDialog>() {
                                    @Override
                                    public void onDismiss(WaitDialog dialog) {
                                        fullScreenDialog.dismiss();
                                    }
                                });
                            }
                        }, 2000);
                    }
                });
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
    
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshUIMode();
    }
    
    /**
     * 刷新亮暗色模式界面变化
     */
    private void refreshUIMode() {
        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO) {
            setDarkStatusBarTheme(true);
        } else {
            setDarkStatusBarTheme(false);
        }
    }
}