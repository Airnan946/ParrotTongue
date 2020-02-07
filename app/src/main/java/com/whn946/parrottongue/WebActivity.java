package com.whn946.parrottongue;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    private ImageView backBtn;
    private WebView wb;
    private TextView wb_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        init();

    }


    private void init() {
        String openUrl = this.getIntent().getStringExtra("openUrl");
        wb = findViewById(R.id.wb);
        wb_title = findViewById(R.id.wb_title);
        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wb.loadUrl(openUrl);
        wb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                wb_title.setText(title);
            }
        });
        backBtn = findViewById(R.id.backBtn);
        //返回主界面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        wb.clearCache(true);
        super.onDestroy();
    }


}

