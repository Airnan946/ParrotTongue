package com.whn946.parrottongue;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class AudioUtil {
    static TextToSpeech tts;


    /**
     * 封装系统TTS列表数据
     *
     * @param context
     * @return
     */
    public static List<TextToSpeech.EngineInfo> getTtsList(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });
        String[] res = new String[tts.getEngines().size()];
        List<TextToSpeech.EngineInfo> list = tts.getEngines();
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i).name;
        }
        return tts.getEngines();
    }

    /**
     * 初始化TTS服务
     */
    static TextToSpeech mTextToSpeech;

    public static TextToSpeech initTTS(final Context context, final String engine) {
        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                int supported = 0;
                if (status == TextToSpeech.SUCCESS) {
                    SharedPreferences tts_lan = context.getSharedPreferences("tts_lan", Context.MODE_PRIVATE);
                    String lan_str = tts_lan.getString("tts_lan", "");
                    //设置朗读语言
                    if (lan_str.equals("CHINA")) {
                        supported = mTextToSpeech.setLanguage(Locale.CHINA);
                    } else if (lan_str.equals("US")) {
                        supported = mTextToSpeech.setLanguage(Locale.US);
                    }
                    if ((supported != TextToSpeech.LANG_AVAILABLE) && (supported != TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                        Toast.makeText(context, "不支持当前语言！", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, engine);
        return mTextToSpeech;
    }

}
