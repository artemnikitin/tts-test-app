package com.artemnikitin.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

public class TTSManager {

    private TextToSpeech mTts = null;
    private boolean isLoaded = false;

    public void init(Context context) {
        try {
            mTts = new TextToSpeech(context, onInitListener);
        } catch (Exception e) {
            Log.e("", Arrays.toString(e.getStackTrace()));
        }
    }

    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.US);
            isLoaded = true;
        }
        }
    };

    public void shutDown() {
        mTts.shutdown();
    }

    public void initQueue(String text, Locale locale) {
        if (isLoaded && text != null && locale != null) {
            mTts.setLanguage(locale);
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        else
            Log.e("TTS-test", "TTS Not Initialized");
    }

}
