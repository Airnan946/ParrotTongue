package com.whn946.parrottongue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ConfigActivity extends AppCompatActivity {
    private ImageView backBtn;
    private EditText num_left_str;
    private EditText num_right_str;
    private TextView save_btn;
    private RadioGroup lan_sel_group;
    private RadioButton lan_china;
    private RadioButton lan_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        initView();
        readConfig();
    }

    private void initView() {
        backBtn = findViewById(R.id.backBtn);
        num_left_str = findViewById(R.id.num_left_str);
        num_right_str = findViewById(R.id.num_right_str);
        save_btn = findViewById(R.id.save_btn);
        lan_sel_group = findViewById(R.id.lan_sel_group);
        lan_china = findViewById(R.id.lan_china);
        lan_us = findViewById(R.id.lan_us);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveConfig();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        lan_sel_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                if (checkedId == lan_china.getId()) {
                    Toast.makeText(ConfigActivity.this, "切换至中文", Toast.LENGTH_SHORT).show();
                } else if (checkedId == lan_us.getId()) {
                    Toast.makeText(ConfigActivity.this, "切换至英文", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readConfig() {
        //读取语言首选项数据
        SharedPreferences tts_lan = getSharedPreferences("tts_lan", Context.MODE_PRIVATE);
        String lan_str = tts_lan.getString("tts_lan", "");
        if (lan_str.equals("CHINA")) {
            lan_china.setChecked(true);
            lan_us.setChecked(false);
        } else if (lan_str.equals("US")) {
            lan_china.setChecked(false);
            lan_us.setChecked(true);
        }
        //读取排号模式数据
        SharedPreferences config_num_mode = getSharedPreferences("config_num_mode", Context.MODE_PRIVATE);
        num_left_str.setText(config_num_mode.getString("leftstr", ""));
        num_right_str.setText(config_num_mode.getString("rightstr", ""));
    }

    private void saveConfig() {
        if (num_left_str.getText().toString().trim().equals("") || num_right_str.getText().toString().trim().equals("")) {
            Toast.makeText(this, "配置项有空值，保存失败！", Toast.LENGTH_LONG).show();
        } else {
            //保存语言首选项数据
            SharedPreferences tts_lan = getSharedPreferences("tts_lan", Context.MODE_PRIVATE);
            SharedPreferences.Editor tts_lan_editor = tts_lan.edit();
            if (lan_china.isChecked()) {
                tts_lan_editor.putString("tts_lan", "CHINA");
            } else if (lan_us.isChecked()) {
                tts_lan_editor.putString("tts_lan", "US");
            }
            tts_lan_editor.apply();
            //保存排号模式数据
            SharedPreferences config_num_mode = getSharedPreferences("config_num_mode", Context.MODE_PRIVATE);
            SharedPreferences.Editor config_num_mode_editor = config_num_mode.edit();
            config_num_mode_editor.putString("leftstr", num_left_str.getText().toString());
            config_num_mode_editor.putString("rightstr", num_right_str.getText().toString());
            config_num_mode_editor.apply();
            Toast.makeText(this, "配置已更新", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        saveConfig();
        super.onDestroy();
    }
}

