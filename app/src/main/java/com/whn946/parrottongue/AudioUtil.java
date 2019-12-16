package com.whn946.parrottongue;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.List;
import java.util.Locale;

public class AudioUtil {
    static TextToSpeech tts;

    public static TextToSpeech initSysTTS(final Context context,final String engins) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                        //SysToolUtil.msgAlert("提示", "啊哦！被我捕捉到一个TTS环境异常，具体原因不明，若播放正常可忽略！", "好吧", context);
                    }
                }
            }
        },engins);
        return tts;
    }

    /**
     * 封装系统TTS列表数据
     * @param context
     * @return
     */
    public static List<TextToSpeech.EngineInfo> getTtsList(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) { }
        });
        String[] res=new String[tts.getEngines().size()];
        List<TextToSpeech.EngineInfo> list=tts.getEngines();
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            res[i]=list.get(i).name;
        }
        return tts.getEngines();
    }


}
