package com.whn946.parrottongue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map tts_map = new HashMap();
    private InternetUtil internetUtil = new InternetUtil();
    private SysToolUtil sysToolUtil = new SysToolUtil();
    private Context mc = MainActivity.this;
    private EditText aet;
    private TextToSpeech tts;
    private EditText et;
    private TextView tb;
    private Button speakBtn;
    private Button stopBtn;
    private Button trimBtn;
    private Button saveBtn;
    private TextView textNum;
    private Spinner ttsListSp;
    private TextView tv_pitch;//音调tv
    private TextView tv_speechRate;//语速tv
    private SeekBar pitchBar;//音调bar
    private SeekBar speechRateBar;//语速bar
    private float pitchNum = 1.0f;
    private float speechRateNum = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        init();
    }



    private void init() {
        //检查权限
        pms_check();
        //检测更新
        checkVersion();
        //初始化控件
        tb = findViewById(R.id.top_bar);
        et = findViewById(R.id.textarea);
        speakBtn = findViewById(R.id.speakBtn);
        stopBtn = findViewById(R.id.stopBtn);
        trimBtn = findViewById(R.id.trimBtn);
        saveBtn = findViewById(R.id.saveBtn);
        textNum = findViewById(R.id.tv_text_num);
        tv_pitch = findViewById(R.id.tv_pitch);
        tv_speechRate = findViewById(R.id.tv_speechRate);
        pitchBar = findViewById(R.id.pitchBar);
        speechRateBar = findViewById(R.id.speechRateBar);
        ttsListSp = findViewById(R.id.ttsListSp);
        //加载历史数据并更新控件状态
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                speakBtn.setVisibility(View.VISIBLE);
                stopBtn.setVisibility(View.GONE);
                historyText("read");
            }
        });


        ttsListSp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tts.setEngineByPackageName(tts_map.get(ttsListSp.getSelectedItem().toString()).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //跳转关于界面
        findViewById(R.id.toSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this,
                        SettingActivity.class);
                startActivity(intent);
            }
        });

        //开始朗读
        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                tts.setPitch(pitchNum);
                tts.setSpeechRate(speechRateNum);
                String str = et.getText().toString();
                if (str.trim().equals("")) {
                    sysToolUtil.msgAlert("提示", "朗读内容不能为空！", "好的", mc);
                } else {
                    tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, getString(R.string.app_name));
                }

            }
        });
        //停止朗读
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (tts.isSpeaking()) {
                    tts.stop();
                    setTextViewWord(tb, "文转音");
                    //开启滑块
                    onOffSeekBar(true);
                }

            }
        });
        //输入框监听时间
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                historyText("write");
            }
        });
        //音调监听事件
        pitchBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //滑动滑块时触发
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            //滑动开始时触发
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //滑动停止时触发
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getId() == R.id.pitchBar) {
                    pitchNum = pitchBar.getProgress() * 0.1f;
                    tts.setSpeechRate(pitchNum);
                }
            }
        });
        //语速监听事件
        speechRateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //滑动滑块时触发
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            //滑动开始时触发
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //滑动停止时触发
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getId() == R.id.speechRateBar) {
                    speechRateNum = speechRateBar.getProgress() * 0.1f;
                    tts.setSpeechRate(speechRateNum);
                }
            }
        });

        //清空文本
        trimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                et.setText("");
                if (tts.isSpeaking()) {
                    tts.stop();
                    setTextViewWord(tb, "文转音");
                    //开启滑块
                    onOffSeekBar(true);
                }
            }
        });
        //保存音频
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                aet = new EditText(mc);
                aet.setHeight(150);
                aet.setPadding(30, 0, 30, 0);
                aet.setHint("文件名...");
                if (pms_check()) {
                    String str = et.getText().toString();
                    if (str.trim().equals("")) {
                        sysToolUtil.msgAlert("提示", "朗读内容不能为空！", "好的", mc);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mc).setTitle("请输入文件名").setView(aet)
                                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //按下确定键后的事件
                                        if (aet.getText().toString().trim().equals("")) {
                                            sysToolUtil.msgAlert("提示", "文件名不能为空！", "好的", mc);
                                        } else {
                                            File audioDir = null;
                                            try {
                                                audioDir = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/文转音");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (!audioDir.exists()) audioDir.mkdir();
                                            HashMap<String, String> myHashRender = new HashMap<String, String>();
                                            myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, et.getText().toString());
                                            String fileName = aet.getText().toString() + "-" + getDate() + ".mp3";
                                            int r = tts.synthesizeToFile(et.getText().toString(), myHashRender, audioDir + "/" + fileName);
                                            if (r == TextToSpeech.SUCCESS) {
                                                sysToolUtil.msgAlert("保存成功", "文件路径：sdcard/文转音/" + fileName, "OK", mc);
                                            } else {
                                                sysToolUtil.msgAlert("保存失败", "输出流异常！", "是", mc);
                                            }
                                        }
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    }
                }
            }
        });
    }

    private boolean pms_check() {
        int writeCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int internetCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        if (writeCheck < 0 || internetCheck < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("权限申请")
                    .setMessage("即将申请手机储存读写权限，该权限仅用于保存并导出音频文件。")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 2);
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


    /**
     * 格式化当前日期作为文件名后缀
     *
     * @return
     */
    public String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }


    /**
     * 开启或关闭滑块
     * 参数：state
     * 参数值：true开启，false关闭
     *
     * @param state
     */
    public void onOffSeekBar(final boolean state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pitchBar.setClickable(state);
                pitchBar.setEnabled(state);
                pitchBar.setSelected(state);
                pitchBar.setFocusable(state);
                speechRateBar.setClickable(state);
                speechRateBar.setEnabled(state);
                speechRateBar.setSelected(state);
                speechRateBar.setFocusable(state);
                ttsListSp.setEnabled(state);
                et.setEnabled(state);
                if (state) {
                    speakBtn.setVisibility(View.VISIBLE);
                    stopBtn.setVisibility(View.GONE);
                } else {
                    speakBtn.setVisibility(View.GONE);
                    stopBtn.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    /**
     * 设置TextView文字
     * 参数：view_id，text_value
     * 参数值：view_id控件成员变量名，text_value要设置的文本内容
     *
     * @param view_id
     * @param text_value
     */
    public void setTextViewWord(final TextView view_id, final String text_value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view_id.setText(text_value);
            }
        });
    }

    /**
     * 读取历史记录
     *
     * @param ways
     */
    public void historyText(String ways) {
        SharedPreferences history_text = getSharedPreferences("history_text", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = history_text.edit();
        if (ways.equals("read")) {
            String text = history_text.getString("history_text", "");
            et.setText(text);

        } else if (ways.equals("write")) {
            editor.putString("history_text", et.getText().toString());
            editor.commit();
        }
        setTextViewWord(textNum, "字数：" + et.getText().toString().length() + "/4000");
    }

    /**
     * 启动检测更新
     */
    public void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    internetUtil.checkData(internetUtil.checkUpdate(MainActivity.this), true, MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mc).setTitle("确认要退出吗?")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tts.shutdown();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void getTtsListData() {
        //获取原始列表
        List<TextToSpeech.EngineInfo> tts_list = AudioUtil.getTtsList(mc);
        Collections.reverse(tts_list);
        String[] tts_spinner_list = new String[tts_list.size()];
        for (int i = 0; i < tts_list.size(); i++) {
            tts_spinner_list[i] = tts_list.get(i).label;
            tts_map.put(tts_list.get(i).label, tts_list.get(i).name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tts_spinner_list);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        ttsListSp.setAdapter(adapter);
        tts = AudioUtil.initSysTTS(mc, tts_map.get(ttsListSp.getSelectedItem().toString()).toString());
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                setTextViewWord(tb, "文转音 - 朗读中...");
                //关闭滑块
                onOffSeekBar(false);
            }

            @Override
            public void onDone(String utteranceId) {
                tts.stop();
                //开启滑块
                onOffSeekBar(true);
                setTextViewWord(tb, "文转音");
            }

            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onError(String utteranceId, int errorCode) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化TTS引擎
        getTtsListData();

    }
}
