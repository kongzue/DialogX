package com.kongzue.dialogxdemo.activity;

import static com.kongzue.dialogx.dialogs.PopTip.tip;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
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
import com.kongzue.dialogx.dialogs.GuideDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.MessageMenu;
import com.kongzue.dialogx.dialogs.PopMenu;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.BottomDialogSlideEventLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXAnimInterface;
import com.kongzue.dialogx.interfaces.DialogXRunnable;
import com.kongzue.dialogx.interfaces.MenuIconAdapter;
import com.kongzue.dialogx.interfaces.MenuItemTextInfoInterceptor;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBackgroundMaskClickListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnBindingView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.kongzue.dialogx.interfaces.OnSafeInsetsChangeListener;
import com.kongzue.dialogx.interfaces.PopMoveDisplacementInterceptor;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.KongzueStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.ItemDivider;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogxdemo.BuildConfig;
import com.kongzue.dialogxdemo.R;
import com.kongzue.dialogxdemo.custom.recycleview.CustomRecycleViewAdapter;
import com.kongzue.dialogxdemo.fragment.CustomFragment;
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Layout(R.layout.activity_main)
@DarkStatusBarTheme(true)
@DarkNavigationBarTheme(true)
@NavigationBarBackgroundColorRes(R.color.emptyNavBar)
public class MainActivity extends BaseActivity {

    private ConstraintLayout boxTitle;
    private TextView txtTitle;
    private ImageView btnShare;
    private ImageView splitBody;
    private LinearLayout boxBody;
    private MaterialButtonToggleGroup grpStyle;
    private MaterialButton rdoMaterial;
    private MaterialButton rdoIos;
    private MaterialButton rdoKongzue;
    private MaterialButton rdoMiui;
    private MaterialButton rdoMaterialYou;
    private MaterialButtonToggleGroup grpTheme;
    private MaterialButton rdoAuto;
    private MaterialButton rdoLight;
    private MaterialButton rdoDark;
    private MaterialButtonToggleGroup grpMode;
    private MaterialButton rdoModeView;
    private MaterialButton rdoModeWindow;
    private MaterialButton rdoModeDialogFragment;
    private MaterialButton rdoModeFloatingActivity;
    private MaterialButton btnMessageDialog;
    private MaterialButton btnSelectDialog;
    private MaterialButton btnInputDialog;
    private MaterialButton btnSelectMessageMenu;
    private MaterialButton btnMutiSelectMessageMenu;
    private MaterialButton btnWaitDialog;
    private MaterialButton btnWaitAndTipDialog;
    private MaterialButton btnTipSuccess;
    private MaterialButton btnTipWarning;
    private MaterialButton btnTipError;
    private MaterialButton btnTipProgress;
    private MaterialButton btnPoptip;
    private MaterialButton btnPoptipBigMessage;
    private MaterialButton btnPoptipSuccess;
    private MaterialButton btnPoptipWarning;
    private MaterialButton btnPoptipError;
    private MaterialButton btnPopnotification;
    private MaterialButton btnPopnotificationBigMessage;
    private MaterialButton btnPopnotificationOverlay;
    private MaterialButton btnBottomDialog;
    private MaterialButton btnBottomMenu;
    private MaterialButton btnBottomReply;
    private MaterialButton btnBottomSelectMenu;
    private MaterialButton btnBottomMultiSelectMenu;
    private MaterialButton btnBottomCustomRecycleView;
    private MaterialButton btnCustomMessageDialog;
    private MaterialButton btnCustomInputDialog;
    private MaterialButton btnCustomBottomMenu;
    private MaterialButton btnCustomDialog;
    private MaterialButton btnCustomDialogAlign;
    private MaterialButton btnFullScreenDialogWebPage;
    private MaterialButton btnFullScreenDialogLogin;
    private MaterialButton btnFullScreenDialogFragment;
    private MaterialButton btnContextMenu;
    private TextView btnSelectMenu;
    private MaterialButton btnShowGuide;
    private MaterialButton btnShowGuideBaseView;
    private MaterialButton btnShowGuideBaseViewRectangle;
    private MaterialButton btnShowBreak;
    private MaterialButton btnListDialog;
    private TextView txtVer;

    @Override
    public void initViews() {
        boxTitle = findViewById(R.id.box_title);
        txtTitle = findViewById(R.id.txt_title);
        btnShare = findViewById(R.id.btn_share);
        splitBody = findViewById(R.id.split_body);
        boxBody = findViewById(R.id.box_body);
        grpStyle = findViewById(R.id.grp_style);
        rdoMaterial = findViewById(R.id.rdo_material);
        rdoIos = findViewById(R.id.rdo_ios);
        rdoKongzue = findViewById(R.id.rdo_kongzue);
        rdoMiui = findViewById(R.id.rdo_miui);
        rdoMaterialYou = findViewById(R.id.rdo_material_you);
        grpTheme = findViewById(R.id.grp_theme);
        rdoAuto = findViewById(R.id.rdo_auto);
        rdoLight = findViewById(R.id.rdo_light);
        rdoDark = findViewById(R.id.rdo_dark);
        grpMode = findViewById(R.id.grp_mode);
        rdoModeView = findViewById(R.id.rdo_mode_view);
        rdoModeWindow = findViewById(R.id.rdo_mode_window);
        rdoModeDialogFragment = findViewById(R.id.rdo_mode_dialogFragment);
        rdoModeFloatingActivity = findViewById(R.id.rdo_mode_floatingActivity);
        btnMessageDialog = findViewById(R.id.btn_messageDialog);
        btnSelectDialog = findViewById(R.id.btn_selectDialog);
        btnInputDialog = findViewById(R.id.btn_inputDialog);
        btnSelectMessageMenu = findViewById(R.id.btn_select_menu);
        btnMutiSelectMessageMenu = findViewById(R.id.btn_multiSelect_menu);
        btnWaitDialog = findViewById(R.id.btn_waitDialog);
        btnWaitAndTipDialog = findViewById(R.id.btn_waitAndTipDialog);
        btnTipSuccess = findViewById(R.id.btn_tipSuccess);
        btnTipWarning = findViewById(R.id.btn_tipWarning);
        btnTipError = findViewById(R.id.btn_tipError);
        btnTipProgress = findViewById(R.id.btn_tipProgress);
        btnPoptip = findViewById(R.id.btn_poptip);
        btnPoptipBigMessage = findViewById(R.id.btn_poptip_bigMessage);
        btnPoptipSuccess = findViewById(R.id.btn_poptip_success);
        btnPoptipWarning = findViewById(R.id.btn_poptip_warning);
        btnPoptipError = findViewById(R.id.btn_poptip_error);
        btnPopnotification = findViewById(R.id.btn_popnotification);
        btnPopnotificationBigMessage = findViewById(R.id.btn_popnotification_bigMessage);
        btnPopnotificationOverlay = findViewById(R.id.btn_popnotification_overlay);
        btnBottomDialog = findViewById(R.id.btn_bottom_dialog);
        btnBottomMenu = findViewById(R.id.btn_bottom_menu);
        btnBottomReply = findViewById(R.id.btn_bottom_reply);
        btnBottomSelectMenu = findViewById(R.id.btn_bottom_select_menu);
        btnBottomMultiSelectMenu = findViewById(R.id.btn_bottom_multiSelect_menu);
        btnBottomCustomRecycleView = findViewById(R.id.btn_bottom_custom_recycleView);
        btnCustomMessageDialog = findViewById(R.id.btn_customMessageDialog);
        btnCustomInputDialog = findViewById(R.id.btn_customInputDialog);
        btnCustomBottomMenu = findViewById(R.id.btn_customBottomMenu);
        btnCustomDialog = findViewById(R.id.btn_customDialog);
        btnCustomDialogAlign = findViewById(R.id.btn_customDialogAlign);
        btnFullScreenDialogWebPage = findViewById(R.id.btn_fullScreenDialog_webPage);
        btnFullScreenDialogLogin = findViewById(R.id.btn_fullScreenDialog_login);
        btnFullScreenDialogFragment = findViewById(R.id.btn_fullScreenDialog_fragment);
        btnContextMenu = findViewById(R.id.btn_contextMenu);
        btnSelectMenu = findViewById(R.id.btn_selectMenu);
        btnShowGuide = findViewById(R.id.btn_showGuide);
        btnShowGuideBaseView = findViewById(R.id.btn_showGuideBaseView);
        btnShowGuideBaseViewRectangle = findViewById(R.id.btn_showGuideBaseViewRectangle);
        btnShowBreak = findViewById(R.id.btn_showBreak);
        btnListDialog = findViewById(R.id.btn_listDialog);
        txtVer = findViewById(R.id.txt_ver);
    }

    @Override
    public void initDatas(JumpParameter parameter) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    boxTitle.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
                    boxBody.setPadding(dip2px(15), dip2px(15), dip2px(15), insets.getSystemWindowInsetBottom() + dip2px(250));
                    return insets;
                }
            });
        } else {
            getRootView().setFitsSystemWindows(true);
        }

        refreshUIMode();
        boolean showBreak = parameter.getBoolean("showBreak");
        if (showBreak) {
            txtTitle.setText("显示Dialog时关闭Activity演示");
            MessageDialog.show("提示", "接下来会直接运行一个 WaitDialog，2 秒后直接关闭 Activity，并回到原 Activity，保证程序不会出现 WindowLeaked 错误。\n\n" + "Android 原生 AlertDialog 常出现因 Activity 先于 Dialog 关闭而导致此错误引发程序崩溃。\n\n" + "而使用 DialogX 构建的对话框不仅仅不会出现此问题，还可避免因句柄持续持有导致的内存泄漏。", "开始测试", "取消").setOkButton(new OnDialogButtonClickListener() {
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
            }).setCancelButton(new OnDialogButtonClickListener() {
                @Override
                public boolean onClick(BaseDialog baseDialog, View v) {
                    finish();
                    return false;
                }
            }).setCancelable(false);
        }

        switch (DialogX.implIMPLMode) {
            case VIEW:
                rdoModeView.setChecked(true);
                break;
            case WINDOW:
                rdoModeWindow.setChecked(true);
                break;
            case DIALOG_FRAGMENT:
                rdoModeDialogFragment.setChecked(true);
                break;
            case FLOATING_ACTIVITY:
                rdoModeFloatingActivity.setChecked(true);
                break;
        }

        txtVer.setText("当前版本：" + BuildConfig.VERSION_NAME);

        checkAndroid14InDebugMode();

//        //合并处理演示，在 onDismiss 中获取用户选择进行统一处理，以防止编写大量可能在不同选择下都要处理的重复代码
//        MessageDialog.show("Title", "Ask Question", "OK", "NO", "OTHER").setDialogLifecycleCallback(new DialogLifecycleCallback<MessageDialog>() {
//            @Override
//            public void onDismiss(MessageDialog dialog) {
//                /**
//                 * dialog.getButtonSelectResult() 支持 MessageDialog 和 BottomDialog
//                 * 两种具有选择功能的对话框。
//                 * 包含四种状态：
//                 * NONE,           //未做出选择
//                 * BUTTON_OK,      //选择了确定按钮
//                 * BUTTON_CANCEL,  //选择了取消按钮
//                 * BUTTON_OTHER    //选择了其他按钮
//                 */
//                if (dialog.getButtonSelectResult() == BaseDialog.BUTTON_SELECT_RESULT.BUTTON_OK) {
//                    MessageDialog.show("Title", "You Select OK Button!", "OK");
//                } else {
//                    TipDialog.show("Other Select!", WaitDialog.TYPE.WARNING);
//                }
//            }
//        });

//        //复写事件演示
//        new MessageDialog() {
//            @Override
//            public void onShow(MessageDialog dialog) {
//                //...
//                tip("onShow");
//            }
//
//            @Override
//            public void onDismiss(MessageDialog dialog) {
//                WaitDialog.show("Please Wait...");
//                if (dialog.getButtonSelectResult() == BUTTON_SELECT_RESULT.BUTTON_OK) {
//                    //点击了OK的情况
//                    //...
//                } else {
//                    //其他按钮点击、对话框dismiss的情况
//                    //...
//                }
//                tip("onDismiss");
//            }
//        }
//                .setTitle("Title")
//                .setMessage("message")
//                .setOkButton("OK")
//                .setCancelButton("Cancel")
//                .show();
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
    private TextView btnLicense;

    private TextView btnClose;
    private WebView webView;

    private String[] singleSelectMenuText = new String[]{"拒绝", "询问", "始终允许", "仅在使用中允许"};
    private int selectMenuIndex;

    private String[] multiSelectMenuText = new String[]{"上海", "北京", "广州", "深圳"};
    private int[] selectMenuIndexArray;
    private String multiSelectMenuResultCache;

    @Override
    public void setEvents() {
        grpMode.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                BaseDialog.cleanAll();
                switch (checkedId) {
                    case R.id.rdo_mode_view:
                        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW;
                        break;
                    case R.id.rdo_mode_window:
                        DialogX.implIMPLMode = DialogX.IMPL_MODE.WINDOW;
                        break;
                    case R.id.rdo_mode_dialogFragment:
                        DialogX.implIMPLMode = DialogX.IMPL_MODE.DIALOG_FRAGMENT;
                        break;
                    case R.id.rdo_mode_floatingActivity:
                        DialogX.implIMPLMode = DialogX.IMPL_MODE.FLOATING_ACTIVITY;
                        break;
                }
            }
        });

        grpTheme.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
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

        grpStyle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                DialogX.cancelButtonText = "取消";
                DialogX.titleTextInfo = null;
                DialogX.buttonTextInfo = null;
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
                    case R.id.rdo_material_you:
                        DialogX.globalStyle = MaterialYouStyle.style();
                        break;
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://github.com/kongzue/DialogX");
            }
        });

        btnContextMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopMenu.show("添加", "编辑", "删除", "分享")
                        .disableMenu("编辑", "删除")
                        .setIconResIds(R.mipmap.img_dialogx_demo_add, R.mipmap.img_dialogx_demo_edit, R.mipmap.img_dialogx_demo_delete, R.mipmap.img_dialogx_demo_share)
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<PopMenu>() {
                            @Override
                            public boolean onClick(PopMenu dialog, CharSequence text, int index) {
                                if (index == 0) {
                                    dialog.enableAllMenu();
                                    dialog.setMenuList(new String[]{"产品A", "产品B", "产品C"});
                                    return true;
                                }
                                return false;
                            }
                        });
            }
        });

        btnSelectMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopMenu.show(view, new String[]{"选项1", "选项2", "选项3"})
                        .setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER))
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<PopMenu>() {
                            @Override
                            public boolean onClick(PopMenu dialog, CharSequence text, int index) {
                                btnSelectMenu.setText(text);
                                return false;
                            }
                        }).setItemDivider(new ItemDivider(15, 15, 1));
            }
        });

        btnFullScreenDialogFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomFragment customFragment = new CustomFragment().setAddButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnFullScreenDialogFragment.callOnClick();
                    }
                });
                FullScreenDialog.build(new OnBindView<FullScreenDialog>(customFragment) {
                    @Override
                    public void onBind(FullScreenDialog dialog, View v) {

                    }
                }).hideActivityContentView(true).show();
            }
        });

        btnMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageDialog.show("标题", "这里是正文内容。", "确定")
                        .setDialogLifecycleCallback(new DialogLifecycleCallback() {
                            @Override
                            public void onShow(BaseDialog dialog) {
                                tip("onShow");
                                super.onShow(dialog);
                            }

                            @Override
                            public void onDismiss(BaseDialog dialog) {
                                tip("onDismiss");
                                super.onDismiss(dialog);
                            }
                        })
//                        .onShow(new DialogXRunnable<MessageDialog>() {
//                            @Override
//                            public void run(MessageDialog dialog) {
//                                tip("onShow");
//                            }
//                        }).onDismiss(new DialogXRunnable<MessageDialog>() {
//                            @Override
//                            public void run(MessageDialog dialog) {
//                                tip("onDismiss");
//                            }
//                        })
                        .setTitleIcon(R.mipmap.img_demo_avatar)
                        .setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog dialog, View v) {
                                PopTip.show("点击确定按钮");
                                return true;
                            }
                        });
            }
        });

        btnSelectDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog("多选对话框", "移除App会将它从主屏幕移除并保留其所有数据。", "删除App", "取消", "移至App资源库").setButtonOrientation(LinearLayout.VERTICAL);
                if (!rdoMiui.isChecked()) {
                    messageDialog.setOkTextInfo(new TextInfo().setFontColor(Color.parseColor("#EB5545")).setBold(true));
                }
                messageDialog.show();
            }
        });

        btnInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputDialog("标题", "正文内容", "确定", "取消", "正在输入的文字").setInputText("Hello World").setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                    @Override
                    public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                        PopTip.show("输入的内容：" + inputStr);
                        return false;
                    }
                }).show();
            }
        });

        btnSelectMessageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageMenu.show(singleSelectMenuText).setShowSelectedBackgroundTips(rdoMiui.isChecked()).setMessage("这里是权限确认的文本说明，这是一个演示菜单").setTitle("获得权限标题").setOnMenuItemClickListener(new OnMenuItemSelectListener<MessageMenu>() {
                    @Override
                    public void onOneItemSelect(MessageMenu dialog, CharSequence text, int index, boolean select) {
                        selectMenuIndex = index;
                    }
                }).setCancelButton("确定", new OnMenuButtonClickListener<MessageMenu>() {
                    @Override
                    public boolean onClick(MessageMenu baseDialog, View v) {
                        PopTip.show("已选择：" + singleSelectMenuText[selectMenuIndex]);
                        return false;
                    }
                }).setSelection(selectMenuIndex);
            }
        });
        btnMutiSelectMessageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageMenu.show(multiSelectMenuText).setMessage("这里是选择城市的模拟范例，这是一个演示菜单").setTitle("请选择城市").setOnMenuItemClickListener(new OnMenuItemSelectListener<MessageMenu>() {
                            @Override
                            public void onMultiItemSelect(MessageMenu dialog, CharSequence[] text, int[] index) {
                                multiSelectMenuResultCache = "";
                                for (CharSequence c : text) {
                                    multiSelectMenuResultCache = multiSelectMenuResultCache + " " + c;
                                }
                                selectMenuIndexArray = index;
                            }
                        }).setOkButton("确定", new OnMenuButtonClickListener<MessageMenu>() {
                            @Override
                            public boolean onClick(MessageMenu dialog, View v) {
                                PopTip.show("已选择：" + multiSelectMenuResultCache);
                                return false;
                            }
                        })
//                        .setCancelButton("确定", new OnDialogButtonClickListener<MessageDialog>() {
//                            @Override
//                            public boolean onClick(MessageDialog baseDialog, View v) {
//                                PopTip.show("已选择：" + multiSelectMenuResultCache);
//                                return false;
//                            }
//                        })
                        .setSelection(selectMenuIndexArray);
            }
        });

        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show("Please Wait!").setOnBackPressedListener(new OnBackPressedListener<WaitDialog>() {
                    @Override
                    public boolean onBackPressed(WaitDialog dialog) {
                        PopTip.show("按下返回").setButton("取消", new OnDialogButtonClickListener<PopTip>() {
                            @Override
                            public boolean onClick(PopTip dialog, View v) {
                                WaitDialog.dismiss();
                                return false;
                            }
                        });
                        return false;
                    }
                });
                WaitDialog.dismiss(3000);
            }
        });

        btnWaitAndTipDialog.setOnClickListener(new View.OnClickListener() {

            boolean closeFlag = false;

            @Override
            public void onClick(View v) {
                closeFlag = false;
                WaitDialog.show("Please Wait!").setOnBackPressedListener(new OnBackPressedListener<WaitDialog>() {
                    @Override
                    public boolean onBackPressed(WaitDialog dialog) {
                        PopTip.show("按下返回", "关闭").setButton(new OnDialogButtonClickListener<PopTip>() {
                            @Override
                            public boolean onClick(PopTip baseDialog, View v) {
                                closeFlag = true;
                                WaitDialog.dismiss();
                                return false;
                            }
                        });
                        return false;
                    }
                }).setMinWidth(dip2px(200));
                if (!closeFlag) runDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!closeFlag) TipDialog.show("完成！", WaitDialog.TYPE.SUCCESS);
                    }
                }, 1500 + new Random().nextInt(1000));
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
                WaitDialog.show("假装连接...").setOnBackPressedListener(new OnBackPressedListener<WaitDialog>() {
                    @Override
                    public boolean onBackPressed(WaitDialog dialog) {
                        MessageDialog.show("正在进行", "是否取消？", "是", "否").setOkButton(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                waitId = -1;
                                if (cycleRunner != null) {
                                    cycleRunner.cancel();
                                }
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
                new BottomDialog("标题", "这里是对话框内容。\n" + s + "。\n底部对话框也支持自定义布局扩展使用方式。", new OnBindView<BottomDialog>(R.layout.layout_custom_view) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                PopTip.show("Click Custom View");
                            }
                        });
                    }
                }).setDialogLifecycleCallback(new BottomDialogSlideEventLifecycleCallback<BottomDialog>() {
                    @Override
                    public boolean onSlideClose(BottomDialog dialog) {
                        log("#onSlideClose");
                        return super.onSlideClose(dialog);
                    }

                    @Override
                    public boolean onSlideTouchEvent(BottomDialog dialog, View v, MotionEvent event) {
                        log("#onSlideTouchEvent: action=" + event.getAction() + " y=" + event.getY());
                        return super.onSlideTouchEvent(dialog, v, event);
                    }
                }).show();
            }
        });

        btnBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoMaterial.isChecked()) {
                    //Material 可滑动展开 BottomMenu 演示
                    BottomMenu.build()
                            .setTitle("title")
                            .setMessage("message")
                            .setBottomDialogMaxHeight(0.6f)
                            .setMenuList(new String[]{"添加", "查看", "编辑", "删除", "分享", "评论", "下载", "收藏", "赞！", "不喜欢", "所属专辑", "复制链接", "类似推荐", "添加", "查看", "编辑", "删除", "分享", "评论", "下载", "收藏", "赞！", "不喜欢", "所属专辑", "复制链接", "类似推荐"}).setOnIconChangeCallBack(new OnIconChangeCallBack<BottomMenu>(true) {

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
                            }).setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                @Override
                                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                    PopTip.show(text);
                                    return false;
                                }
                            }).show();

//                      测试用代码
//                    BottomMenu.show("添加", "查看", "编辑")
//                            .setIconResIds(R.mipmap.img_dialogx_demo_add,
//                                    R.mipmap.img_dialogx_demo_view,
//                                    R.mipmap.img_dialogx_demo_edit
//                            );
                } else {
                    BottomMenu.show(new String[]{"新标签页中打开", "稍后阅读", "复制链接网址"}).setMessage("http://www.kongzue.com/DialogX").setMenuItemTextInfoInterceptor(new MenuItemTextInfoInterceptor<BottomMenu>() {
                        @Override
                        public TextInfo menuItemTextInfo(BottomMenu dialog, int index, String menuText) {
                            if (index == 2) {
                                return new TextInfo().setFontColor(Color.RED).setBold(true);
                            }
                            return null;
                        }
                    }).setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                        @Override
                        public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                            log("点击了菜单：" + index + " 文本：" + text);
                            PopTip.show(text);
                            try {
                                throw new RuntimeException("test");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    }).setOnIconChangeCallBack(new MenuIconAdapter<BottomMenu>(false) {

                        String[] urls = {
                                "http://www.kongzue.com/test/res/dialogx/ic_menu_add.png",
                                "http://www.kongzue.com/test/res/dialogx/ic_menu_read_later.png",
                                "http://www.kongzue.com/test/res/dialogx/ic_menu_link.png"
                        };

                        @Override
                        public boolean applyIcon(BottomMenu dialog, int index, String menuText, ImageView iconImageView) {
                            Glide.with(MainActivity.this).load(urls[index]).into(iconImageView);
                            return true;
                        }
                    });//.setIconResIds(R.mipmap.img_dialogx_demo_add, R.mipmap.img_dialogx_demo_view, R.mipmap.img_dialogx_demo_link);
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
                MessageDialog.show("这里是标题", "此对话框演示的是自定义对话框内部布局的效果", "确定", "取消").setDialogLifecycleCallback(new BottomDialogSlideEventLifecycleCallback<MessageDialog>() {
                    @Override
                    public void onShow(MessageDialog dialog) {
                        super.onShow(dialog);
                        dialog.getDialogImpl().txtDialogTip.setPadding(0, dip2px(20), 0, 0);
                    }
                }).setCustomView(new OnBindView<MessageDialog>(R.layout.layout_custom_view) {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {

                    }
                }).setMaskColor(Color.TRANSPARENT);
            }
        });

        btnCustomInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show("这里是标题", "此对话框演示的是自定义对话框内部布局的效果", "确定", "取消").setCustomView(new OnBindView<MessageDialog>(R.layout.layout_custom_view) {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {

                    }
                });
            }
        });

        //仅使用渐变动画的实现示例
        DialogXAnimInterface<BottomDialog> alphaDialogAnimation = new DialogXAnimInterface<BottomDialog>() {

            @Override
            //入场动画
            public void doShowAnim(BottomDialog dialog, ViewGroup dialogBodyView) {
                //设置好位置，将默认预设会在屏幕底外部恢复为屏幕内
                dialog.getDialogImpl().boxBkg.setY(dialog.getDialogImpl().boxRoot.getUnsafePlace().top);
                //创建 0f~1f 的数值动画
                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        //修改背景遮罩透明度
                        dialog.getDialogImpl().boxRoot.setBkgAlpha(value);
                        //修改内容透明度
                        dialog.getDialogImpl().bkg.setAlpha(value);
                    }
                });
                //使用真正的动画时长
                animator.setDuration(dialog.getDialogImpl().getEnterAnimationDuration());
                animator.start();
            }

            @Override
            //出场动画
            public void doExitAnim(BottomDialog dialog, ViewGroup dialogBodyView) {
                ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        //这里可以直接修改整体透明度淡出
                        dialog.getDialogImpl().boxRoot.setAlpha(value);
                    }
                });
                animator.setDuration(dialog.getDialogImpl().getExitAnimationDuration());
                animator.start();
            }
        };

        btnCustomBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(new String[]{"新标签页中打开", "稍后阅读", "复制链接网址"}).setMessage("http://www.kongzue.com/DialogX").setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                PopTip.show(text);
                                return false;
                            }
                        })
                        //.setDialogXAnimImpl(alphaDialogAnimation)
                        .setCustomView(new OnBindView<BottomDialog>(R.layout.layout_custom_view) {
                            @Override
                            public void onBind(BottomDialog dialog, View v) {

                            }
                        });
            }
        });

        btnShowGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideDialog.show(R.mipmap.img_guide_tip);
            }
        });

        btnShowGuideBaseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideDialog.show(btnFullScreenDialogLogin, R.mipmap.img_tip_login).setBaseViewMarginTop(-dip2px(30));
            }
        });

        btnShowGuideBaseViewRectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideDialog.show(btnCustomDialogAlign, GuideDialog.STAGE_LIGHT_TYPE.RECTANGLE, R.mipmap.img_tip_login_clicktest).setStageLightFilletRadius(dip2px(5)).setBaseViewMarginTop(-dip2px(30)).setOnBackgroundMaskClickListener(new OnBackgroundMaskClickListener<CustomDialog>() {
                    @Override
                    public boolean onClick(CustomDialog dialog, View v) {
                        toast("点击了外围遮罩");
                        return false;
                    }
                }).setOnStageLightPathClickListener(new OnDialogButtonClickListener<GuideDialog>() {
                    @Override
                    public boolean onClick(GuideDialog dialog, View v) {
                        toast("点击了原按钮");
                        btnCustomDialogAlign.callOnClick();
                        return false;
                    }
                });
            }
        });

        btnShowBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(MainActivity.class, new JumpParameter().put("showBreak", true).put("fromActivity", getInstanceKey()));
            }
        });

        btnListDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogX.showDialogList(MessageDialog.build().setTitle("提示").setMessage("这是一组消息对话框队列").setOkButton("开始").setCancelButton("取消").setCancelButton(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog dialog, View v) {
                        dialog.cleanDialogList();
                        return false;
                    }
                }), PopTip.build().setMessage("每个对话框会依次显示"), PopNotification.build().setTitle("通知提示").setMessage("直到上一个对话框消失"), InputDialog.build().setTitle("请注意").setMessage("你必须使用 .build() 方法构建，并保证不要自己执行 .show() 方法").setInputText("输入文字").setOkButton("知道了"), TipDialog.build().setMessageContent("准备结束...").setTipType(WaitDialog.TYPE.SUCCESS), BottomDialog.build().setTitle("结束").setMessage("下滑以结束旅程，祝你编码愉快！").setCustomView(new OnBindView<BottomDialog>(R.layout.layout_custom_dialog) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        ImageView btnOk;
                        btnOk = v.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }));
            }
        });

        btnFullScreenDialogLogin.setOnClickListener(new View.OnClickListener() {

            /**
             * 采用异步加载布局防止卡顿测试
             */

            OnBindView<FullScreenDialog> onBindView;

            @Override
            public void onClick(View v) {
                onBindView = new OnBindView<FullScreenDialog>(R.layout.layout_full_login, true) {
                    @Override
                    public void onBind(FullScreenDialog dialog, View v) {
                        btnCancel = v.findViewById(R.id.btn_cancel);
                        btnSubmit = v.findViewById(R.id.btn_submit);
                        boxUserName = v.findViewById(R.id.box_userName);
                        editUserName = v.findViewById(R.id.edit_userName);
                        boxPassword = v.findViewById(R.id.box_password);
                        editPassword = v.findViewById(R.id.edit_password);
                        btnLicense = v.findViewById(R.id.btn_license);

                        initFullScreenLoginDemo(dialog);
                    }
                };
                FullScreenDialog.show(onBindView);
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

                        webView.loadUrl("https://github.com/kongzue/DialogX");
                    }
                }).setBottomNonSafetyAreaBySelf(false);
            }
        });

        btnCustomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //创建一个自定义 View，这里拿 TextView 举例
//                TextView customView = new TextView(me);
//                customView.setText("Custom View");
//                customView.setGravity(Gravity.CENTER);
//                customView.setBackgroundColor(Color.WHITE);
//                //宽度 200dp，满高度
//                customView.setLayoutParams(new ViewGroup.LayoutParams(dip2px(200), ViewGroup.LayoutParams.MATCH_PARENT));
//
//                //创建对话框
//                CustomDialog.show(new OnBindView<CustomDialog>(customView) {
//                            @Override
//                            public void onBind(CustomDialog dialog, View v) {
//                                //处理沉浸式非安全区（如果需要）
//                                dialog.getDialogImpl().boxRoot.setOnSafeInsetsChangeListener(new OnSafeInsetsChangeListener() {
//                                    @Override
//                                    public void onChange(Rect unsafeRect) {
//                                        v.setPadding(unsafeRect.left, unsafeRect.top, unsafeRect.right, unsafeRect.bottom);
//                                    }
//                                });
//                            }
//                        })
//                        //从屏幕左侧显示
//                        .setAlign(CustomDialog.ALIGN.LEFT)
//                        //关闭默认安全区沉浸式，让自定义 view 能够显示到非安全区外
//                        .setEnableImmersiveMode(false)
//                        //设置背景遮罩 30% 透明黑
//                        .setMaskColor(getResources().getColor(com.kongzue.dialogx.iostheme.R.color.black30));

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
                        }).setMaskColor(getColorS(com.kongzue.dialogx.R.color.black50)).setOnBackgroundMaskClickListener(new OnBackgroundMaskClickListener<CustomDialog>() {
                            @Override
                            public boolean onClick(CustomDialog dialog, View v) {
                                log("点击遮罩层");
                                return false;
                            }
                        }).setMaskColor(getResources().getColor(com.kongzue.dialogx.iostheme.R.color.black30))
                        //实验性，RenderEffect实现的背景模糊效果
                        .setDialogLifecycleCallback(new DialogLifecycleCallback<CustomDialog>() {
                            @Override
                            public void onShow(CustomDialog dialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    RenderEffect blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP);
                                    ((ViewGroup) getWindow().getDecorView()).getChildAt(0).setRenderEffect(blurEffect);
                                }
                            }

                            @Override
                            public void onDismiss(CustomDialog dialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    ((ViewGroup) getWindow().getDecorView()).getChildAt(0).setRenderEffect(null);
                                }
                            }
                        })
//                        .setAlign(CustomDialog.ALIGN.LEFT)
                //.setAnimResId(R.anim.anim_right_in, R.anim.anim_right_out)


                //实现完全自定义动画效果
//                        .setDialogXAnimImpl(new DialogXAnimInterface<CustomDialog>() {
//                            @Override
//                            public void doShowAnim(CustomDialog customDialog, ViewGroup dialogBodyView) {
//                                Animation enterAnim;
//
//                                int enterAnimResId = com.kongzue.dialogx.R.anim.anim_dialogx_top_enter;
//                                enterAnim = AnimationUtils.loadAnimation(me, enterAnimResId);
//                                enterAnim.setInterpolator(new DecelerateInterpolator(2f));
//
//                                long enterAnimDurationTemp = enterAnim.getDuration();
//
//                                enterAnim.setDuration(enterAnimDurationTemp);
//                                customDialog.getDialogImpl().boxCustom.startAnimation(enterAnim);
//
//                                ValueAnimator bkgAlpha = ValueAnimator.ofFloat(0f, 1f);
//                                bkgAlpha.setDuration(enterAnimDurationTemp);
//                                bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                    @Override
//                                    public void onAnimationUpdate(ValueAnimator animation) {
//                                        if (customDialog.getDialogImpl() == null || customDialog.getDialogImpl().boxRoot == null) {
//                                            return;
//                                        }
//                                        customDialog.getDialogImpl().boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
//                                    }
//                                });
//                                bkgAlpha.start();
//                            }
//
//                            @Override
//                            public void doExitAnim(CustomDialog customDialog, ViewGroup dialogBodyView) {
//                                int exitAnimResIdTemp = com.kongzue.dialogx.R.anim.anim_dialogx_default_exit;
//
//                                Animation exitAnim = AnimationUtils.loadAnimation(me, exitAnimResIdTemp);
//                                customDialog.getDialogImpl().boxCustom.startAnimation(exitAnim);
//
//                                ValueAnimator bkgAlpha = ValueAnimator.ofFloat(1f, 0f);
//                                bkgAlpha.setDuration(exitAnim.getDuration());
//                                bkgAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                    @Override
//                                    public void onAnimationUpdate(ValueAnimator animation) {
//                                        if (customDialog.getDialogImpl() == null || customDialog.getDialogImpl().boxRoot == null) {
//                                            return;
//                                        }
//                                        customDialog.getDialogImpl().boxRoot.setBkgAlpha((Float) animation.getAnimatedValue());
//                                    }
//                                });
//                                bkgAlpha.start();
//                            }
//                        })
                ;
            }
        });

        btnCustomDialogAlign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.show(new OnBindView<CustomDialog>(R.layout.layout_custom_dialog_align) {

                    private TextView btnSelectPositive;

                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        btnSelectPositive = v.findViewById(R.id.btn_selectPositive);
                        btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopTip.show("我知道了");
                                dialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false).setMaskColor(getResources().getColor(com.kongzue.dialogx.iostheme.R.color.black30)).setEnterAnimResId(R.anim.anim_custom_pop_enter).setExitAnimResId(R.anim.anim_custom_pop_exit).setAlignBaseViewGravity(btnCustomDialogAlign, Gravity.TOP | Gravity.CENTER_HORIZONTAL).setBaseViewMarginBottom(-dip2px(45)).show();
            }
        });

        btnPoptip.setOnClickListener(new View.OnClickListener() {
            int index;

            @Override
            public void onClick(View v) {
                index++;
                PopTip.show("任务 " + index + " 已完成处理", "撤销").setEnterAnimDuration(500).iconSuccess();
            }
        });

        btnPoptipBigMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoIos.isChecked()) {
                    PopTip.show(R.mipmap.img_air_pods_pro, "AirPods Pro 已连接").setTintIcon(false).showLong();
                } else {
                    PopTip.show(R.mipmap.img_mail_line_white, "邮件已发送", "撤回").setButton(new OnDialogButtonClickListener<PopTip>() {
                        @Override
                        public boolean onClick(PopTip popTip, View v) {
                            //点击“撤回”按钮回调
                            toast("邮件已撤回");
                            return false;
                        }
                    }).setTintIcon(true).showLong();
                }
            }
        });

        btnPoptipSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopTip.show("操作已完成").iconSuccess();
            }
        });

        btnPoptipWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopTip.show("存储空间不足").setButton("立即清理", new OnDialogButtonClickListener<PopTip>() {
                    @Override
                    public boolean onClick(PopTip baseDialog, View v) {
                        toast("点击了立即清理");
                        return false;
                    }
                }).iconWarning();
            }
        });

        btnPoptipError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopTip.show("无法连接网络").iconError();
            }
        });

        btnBottomSelectMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(singleSelectMenuText).setShowSelectedBackgroundTips(rdoMiui.isChecked()).setMessage("这里是权限确认的文本说明，这是一个演示菜单").setTitle("获得权限标题").setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                    @Override
                    public void onOneItemSelect(BottomMenu dialog, CharSequence text, int index, boolean select) {
                        selectMenuIndex = index;
                    }
                }).setCancelButton("确定", new OnMenuButtonClickListener<BottomMenu>() {
                    @Override
                    public boolean onClick(BottomMenu baseDialog, View v) {
                        PopTip.show("已选择：" + singleSelectMenuText[selectMenuIndex]);
                        return false;
                    }
                }).setSelection(selectMenuIndex);
            }
        });

        btnBottomMultiSelectMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(multiSelectMenuText).setMessage("这里是选择城市的模拟范例，这是一个演示菜单").setTitle("请选择城市").setOnMenuItemClickListener(new OnMenuItemSelectListener<BottomMenu>() {
                            @Override
                            public void onMultiItemSelect(BottomMenu dialog, CharSequence[] text, int[] index) {
                                multiSelectMenuResultCache = "";
                                for (CharSequence c : text) {
                                    multiSelectMenuResultCache = multiSelectMenuResultCache + " " + c;
                                }
                                selectMenuIndexArray = index;
                            }
                        }).setOkButton("确定", new OnMenuButtonClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, View v) {
                                PopTip.show("已选择：" + multiSelectMenuResultCache);
                                return false;
                            }
                        })
//                        .setCancelButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
//                            @Override
//                            public boolean onClick(BottomDialog baseDialog, View v) {
//                                PopTip.show("已选择：" + multiSelectMenuResultCache);
//                                return false;
//                            }
//                        })
                        .setSelection(selectMenuIndexArray);
            }
        });

        btnBottomCustomRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.build().setCustomView(new OnBindView<BottomDialog>(R.layout.layout_custom_recycleview) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        List<CustomRecycleViewAdapter.Data> dataArrayList = new ArrayList<>();
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 1"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 2"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 3"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 4"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 5"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 6"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 7"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 8"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 9"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 10"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 11"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 12"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 13"));
                        dataArrayList.add(new CustomRecycleViewAdapter.Data("Item Text 14"));
                        RecyclerView recyclerView = (RecyclerView) v;
                        LinearLayoutManager layoutManager = new LinearLayoutManager(me);
                        recyclerView.setLayoutManager(layoutManager);
                        CustomRecycleViewAdapter adapter = new CustomRecycleViewAdapter(dataArrayList);
                        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                PopTip.show("点击了第 " + position + " 个");
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }).setScrollableWhenContentLargeThanVisibleRange(false).show();
            }
        });

        btnPopnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationIndex++;
                PopNotification.build().setMessage("这是一条消息 " + notificationIndex).setOnPopNotificationClickListener(new OnDialogButtonClickListener<PopNotification>() {
                    @Override
                    public boolean onClick(PopNotification dialog, View v) {
                        tip("点击了通知" + dialog.dialogKey());
                        return true;
                    }
                }).show();
            }
        });

        btnPopnotificationBigMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.img_demo_avatar);
                notificationIndex++;
                PopNotification.show("这是一条消息 " + notificationIndex, "吃了没？\uD83E\uDD6A").setIcon(icon).setButton("回复", new OnDialogButtonClickListener<PopNotification>() {
                    @Override
                    public boolean onClick(PopNotification baseDialog, View v) {
                        toast("点击回复按钮");
                        return false;
                    }
                });
            }
        });

        btnPopnotificationOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogX.globalHoverWindow = true;
                //悬浮窗权限检查
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(me)) {
                        Toast.makeText(me, "使用 DialogX.globalHoverWindow 必须开启悬浮窗权限", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    }
                }

                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.img_demo_avatar);
                notificationIndex++;
                Toast.makeText(me, "会在1秒后显示悬浮窗！", Toast.LENGTH_LONG).show();

                //跳转到桌面
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //等待一秒后显示
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PopNotification.build().setDialogImplMode(DialogX.IMPL_MODE.WINDOW).setTitle("这是一条消息 " + notificationIndex).setIcon(icon).setButton("回复", new OnDialogButtonClickListener<PopNotification>() {
                            @Override
                            public boolean onClick(PopNotification baseDialog, View v) {
                                Intent intent = new Intent(me, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                return false;
                            }
                        }).showLong();
                    }
                }, 1000);
            }
        });
    }

    int notificationIndex;

    private void initFullScreenLoginDemo(final FullScreenDialog fullScreenDialog) {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenDialog.dismiss();
            }
        });

        btnCancel.setText("取消");
        btnSubmit.setText("下一步");

        btnLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopTip.show("点击用户服务条款");
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("MainActivity#onDestroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
    }

    // 检查是否处于debug模式且系统版本为Android 14
    private void checkAndroid14InDebugMode() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT == 34) {
            String fullText = "当前系统版本在debug模式下可能存在卡顿现象，属于系统故障，程序编译为release版本后将恢复正常，具体原因请参阅：《Android14 设备上 debug 调试 app 出现卡顿的问题及临时修复办法》";
            String linkText = "《Android14 设备上 debug 调试 app 出现卡顿的问题及临时修复办法》";
            String url = "https://xiaozhuanlan.com/topic/1023694578";

            SpannableString spannableString = new SpannableString(fullText);
            int start = fullText.indexOf(linkText);
            int end = start + linkText.length();
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // 点击后跳转到指定链接
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE); // 设置链接颜色
                    ds.setUnderlineText(true); // 添加下划线
                }
            };
            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            MessageDialog.build()
                    .setTitle("警告")
                    .setMessage(spannableString)
                    .setOkButton("知道了", new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog dialog, View v) {
                            return false;
                        }
                    })
                    .show();
        }
    }
}