package com.kongzue.dialogxdemo.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBindView;
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
//            BottomDialog.show("标题", "这里是对话框内容。")
//                    .setCancelButton("取消", (dialog, v) -> false)
//                    .setOkButton("确定", (dialog, v) -> false);

            FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_full_webview) {
                private TextView btnClose;
                private WebView webView;
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
            });
        });
    }
}
