package com.whn946.parrottongue;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DwnttsActivity extends AppCompatActivity {
    private SysToolUtil sysToolUtil = new SysToolUtil();
    private Context dc = DwnttsActivity.this;
    private ImageView backBtn;
    private Button dwn_xa_tts_btn;
    private Button dwn_xf_tts_btn;
    private Button dwn_gg_tts_btn;
    private Button dwn_dm_tts_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwntts);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAPP();
    }

    private void init() {
        backBtn = findViewById(R.id.backBtn);
        dwn_xa_tts_btn = findViewById(R.id.dwn_xa_tts_btn);
        dwn_xf_tts_btn = findViewById(R.id.dwn_xf_tts_btn);
        dwn_gg_tts_btn = findViewById(R.id.dwn_gg_tts_btn);
        dwn_dm_tts_btn = findViewById(R.id.dwn_dm_tts_btn);
        //返回主界面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        dwn_xa_tts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                appControl(dwn_xa_tts_btn,"小爱语音引擎","com.xiaomi.mibrain.speech");
            }
        });
        dwn_xf_tts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                appControl(dwn_xf_tts_btn,"讯飞语音引擎","com.iflytek.speechcloud");
            }
        });
        dwn_gg_tts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                appControl(dwn_gg_tts_btn,"谷歌文字转语音引擎","com.google.android.tts");
            }
        });
        dwn_dm_tts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                appControl(dwn_dm_tts_btn,"度秘语音引擎","com.baidu.duersdk.opensdk");
            }
        });
    }

    public void checkAPP() {
        if (sysToolUtil.isAppExist("com.xiaomi.mibrain.speech", dc)) {
            onOffBtn(dwn_xa_tts_btn);
        }
        if (sysToolUtil.isAppExist("com.iflytek.speechcloud", dc)) {
            onOffBtn(dwn_xf_tts_btn);
        }
        if (sysToolUtil.isAppExist("com.google.android.tts", dc)) {
            onOffBtn(dwn_gg_tts_btn);
        }
        if (sysToolUtil.isAppExist("com.baidu.duersdk.opensdk", dc)) {
            onOffBtn(dwn_dm_tts_btn);
        }


    }

    public void onOffBtn(Button button) {
        button.setText("卸载");
    }
    public void appControl(Button button,String title,String pkgName) {
        if(button.getText().equals("下载")){
            sysToolUtil.dwnAlert("引擎下载","即将下载"+title,"下载","https://www.lanzous.com/b0e7fdy4d",dc);
        }else {
            sysToolUtil.uninstallAPP(pkgName, dc);
            DwnttsActivity.this.finish();
        }
    }


}

