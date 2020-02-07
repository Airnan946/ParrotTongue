package com.whn946.parrottongue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class NumModeActivity extends AppCompatActivity {
    private ImageView backBtn;
    private Button num_0_btn;
    private Button num_1_btn;
    private Button num_2_btn;
    private Button num_3_btn;
    private Button num_4_btn;
    private Button num_5_btn;
    private Button num_6_btn;
    private Button num_7_btn;
    private Button num_8_btn;
    private Button num_9_btn;
    private Button num_clear_btn;
    private Button num_play_btn;
    private Button num_next_btn;
    private Button num_config_btn;
    private String num_tmp = "";
    private String num_str = "";
    private TextView num_view;
    private int num_int;
    private TextToSpeech tts;
    private float pitchNum = 1.0f;
    private float speechRateNum = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nummode);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        initView();
    }

    private void initView() {
        num_config_btn = findViewById(R.id.num_config_btn);
        backBtn = findViewById(R.id.backBtn);
        num_0_btn = findViewById(R.id.num_0_btn);
        num_1_btn = findViewById(R.id.num_1_btn);
        num_2_btn = findViewById(R.id.num_2_btn);
        num_3_btn = findViewById(R.id.num_3_btn);
        num_4_btn = findViewById(R.id.num_4_btn);
        num_5_btn = findViewById(R.id.num_5_btn);
        num_6_btn = findViewById(R.id.num_6_btn);
        num_7_btn = findViewById(R.id.num_7_btn);
        num_8_btn = findViewById(R.id.num_8_btn);
        num_9_btn = findViewById(R.id.num_9_btn);
        num_clear_btn = findViewById(R.id.num_clear_btn);
        num_play_btn = findViewById(R.id.num_play_btn);
        num_next_btn = findViewById(R.id.num_next_btn);
        num_view = findViewById(R.id.num_view);
        num_config_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(NumModeActivity.this,
                        ConfigActivity.class);
                startActivity(intent);

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        num_0_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("0");
            }
        });
        num_1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("1");
            }
        });
        num_2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("2");
            }
        });
        num_3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("3");
            }
        });
        num_4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("4");
            }
        });
        num_5_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("5");
            }
        });
        num_6_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("6");
            }
        });
        num_7_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("7");
            }
        });
        num_8_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("8");
            }
        });
        num_9_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputNum("9");
            }
        });
        num_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                num_tmp = "0000";
                num_str = "0000";
                num_int = 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        num_view.setText("0000");

                    }
                });
            }
        });
        num_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                playNum(String.valueOf(Integer.valueOf(num_view.getText().toString())));
            }
        });
        num_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                nextNum();
                playNum(String.valueOf(Integer.valueOf(num_view.getText().toString())));
            }
        });
        readConfig("read");
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                    }
                }
            }
        });
        tts.setPitch(pitchNum);
        tts.setSpeechRate(speechRateNum);
    }

    private void inputNum(String way) {
        int n =Integer.valueOf(num_view.getText().toString());
        num_tmp=String.valueOf(n);
        if((n+1)>9999){
        } else {
            if(num_tmp.equals("0000")||num_tmp.equals("000")||num_tmp.equals("00")||num_tmp.equals("0")){
                num_tmp=way;
            }else {
                num_tmp += way;
            }
            if (num_tmp.length() == 1) {
                num_str = "000" + num_tmp;
            }
            if (num_tmp.length() == 2) {
                num_str = "00" + num_tmp;
            }
            if (num_tmp.length() == 3) {
                num_str = "0" + num_tmp;
            }
            if (num_tmp.length() == 4) {
                num_str = num_tmp;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    num_view.setText(num_str);
                }
            });
        }
        readConfig("write");
    }

    private void nextNum() {
        num_int = Integer.valueOf(num_str);
        num_int++;
        num_tmp = String.valueOf(num_int);
        if (num_tmp == "9999") {
            return;
        } else {
            if (num_tmp.length() == 1) {
                num_str = "000" + num_tmp;
            }
            if (num_tmp.length() == 2) {
                num_str = "00" + num_tmp;
            }
            if (num_tmp.length() == 3) {
                num_str = "0" + num_tmp;
            }
            if (num_tmp.length() == 4) {
                num_str = num_tmp;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    num_view.setText(num_str);

                }
            });
        }
        readConfig("write");
    }

    private void playNum(String num) {
        SharedPreferences config_num_mode = getSharedPreferences("config_num_mode", Context.MODE_PRIVATE);
        String str_left = config_num_mode.getString("leftstr", "");
        String str_right = config_num_mode.getString("rightstr", "");
        String numAndStr=str_left+num+str_right;
        tts.speak(numAndStr, TextToSpeech.QUEUE_FLUSH, null, getString(R.string.app_name));

    }

    /**
     * 读取历史记录
     *
     * @param ways
     */
    public void readConfig(String ways) {
        SharedPreferences config_num_mode = getSharedPreferences("config_num_mode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = config_num_mode.edit();
        if (ways.equals("read")) {
            String text = config_num_mode.getString("number", "");
            if (text == null || text.length() == 0) {
                text = "0001";
            }
            num_view.setText(text);
            num_tmp = text;
            num_str = text;
        } else if (ways.equals("write")) {
            editor.putString("number", num_view.getText().toString());
            editor.apply();
        }
    }

}
