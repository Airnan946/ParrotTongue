package com.whn946.parrottongue;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {
    private Button to_memo_btn;//查看说明按钮
    private ImageView to_mode_def;//进入默认模式按钮
    private ImageView to_mode_num;//进入排号模式按钮
    private ImageView num_config_btn;
    private ImageView toSetting;
    private ImageView home_to_money_btn;
    private ImageView home_to_share_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        initView();
        initMethod();
        pms_check();
    }


    private void initView() {
        to_memo_btn = findViewById(R.id.to_memo_btn);
        to_mode_def = findViewById(R.id.to_mode_def);
        to_mode_num = findViewById(R.id.to_mode_num);
        toSetting = findViewById(R.id.toSetting);
        num_config_btn = findViewById(R.id.num_config_btn);
        home_to_share_btn = findViewById(R.id.home_to_share_btn);
        home_to_money_btn= findViewById(R.id.home_to_money_btn);
    }

    private void initMethod() {
        SharedPreferences app_run = getSharedPreferences("app_run", Context.MODE_PRIVATE);
        Boolean ar = app_run.getBoolean("runtime",false);
        if (!ar || ar == null) {
            SysToolUtil.setDefConfig(HomeActivity.this);
        }
        num_config_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this,
                        ConfigActivity.class);
                startActivity(intent);

            }
        });
        toSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this,
                        SettingActivity.class);
                startActivity(intent);

            }
        });
        home_to_money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this,
                        WebActivity.class);
                intent.putExtra("openUrl","https://version.whn946.cn/money.html");
                startActivity(intent);

            }
        });
        home_to_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"助力微餐饮行业，文转音V2.0全新出发。下载地址：https://projects.whn946.cn/wzy/index.html");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"分享文转音"));

            }
        });
        to_memo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this,
                        WebActivity.class);
                intent.putExtra("openUrl","https://version.whn946.cn/help.html");
                startActivity(intent);

            }
        });

        to_mode_def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this,
                        DefaultModeActivity.class);
                startActivity(intent);

            }
        });

        to_mode_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this,
                        NumModeActivity.class);
                startActivity(intent);

            }
        });


    }

    private boolean pms_check() {
        int writeCheck = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int internetCheck = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.INTERNET);
        if (writeCheck < 0 || internetCheck < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("权限申请")
                    .setMessage("即将申请手机储存读写权限，该权限仅用于保存并导出音频文件。")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 2);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            return false;
        } else {
            return true;
        }
    }

}
