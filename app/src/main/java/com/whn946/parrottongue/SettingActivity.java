package com.whn946.parrottongue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SettingActivity extends AppCompatActivity {
    private SysToolUtil sysToolUtil = new SysToolUtil();
    private Context sc = SettingActivity.this;
    private ImageView backBtn;
    private TextView toDwnBtn;
    private TextView qqGBtn;
    private TextView qqPBtn;
    private TextView viewHelpBtn;
    private TextView viewPrivacyBtn;
    private TextView checkUpdBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        init();
    }

    private void init() {
        backBtn = findViewById(R.id.backBtn);
        toDwnBtn = findViewById(R.id.toDwnBtn);
        qqGBtn = findViewById(R.id.qqGBtn);
        qqPBtn = findViewById(R.id.qqPBtn);
        viewHelpBtn = findViewById(R.id.viewHelpBtn);
        viewPrivacyBtn = findViewById(R.id.viewPrivacyBtn);
        checkUpdBtn = findViewById(R.id.checkUpdBtn);
        //返回主页面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        //跳转引擎下载页面
        toDwnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingActivity.this,
                        DwnttsActivity.class);
                startActivity(intent);
            }
        });
        //加群
        qqGBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                openQQ(1);
            }
        });
        //聊天
        qqPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                openQQ(0);
            }
        });
        //帮助文档
        viewHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingActivity.this,
                        WebActivity.class);
                intent.putExtra("openUrl","https://version.whn946.cn/help.html");
                startActivity(intent);
            }
        });
        //隐私协议
        viewPrivacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingActivity.this,
                        WebActivity.class);
                intent.putExtra("openUrl","https://version.whn946.cn/privacy.html");
                startActivity(intent);
            }
        });
        checkUpdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SettingActivity.this,
                        WebActivity.class);
                intent.putExtra("openUrl","https://version.whn946.cn/update.html?cversion=2.0");
                startActivity(intent);
            }
        });
    }


    /**
     * 加QQ群或聊天 0聊天 1加群
     *
     * @param ways
     * @return
     */
    public boolean openQQ(int ways) {
        Intent target = null;
        Intent g = new Intent().setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "7cxTd7Qin5IypYFecRx9zAzd_tV6LR7F"));
        Intent p = new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=525069428&version=1&src_type=web&web_src=oicqzone.com")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ways == 0) {
            target = p;
        } else if (ways == 1) {
            target = g;
        }
        try {
            startActivity(target);
            return true;
        } catch (Exception e) {
            sysToolUtil.msgAlert("打开失败", "未安装QQ或安装的版本不支持！", "确认", sc);
            return false;
        }
    }


}

