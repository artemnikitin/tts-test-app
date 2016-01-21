package com.artemnikitin.tts;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class TTSManager {

    private String TAG = "TTS-TTSManager";
    private TextToSpeech mTts = null;
    private boolean isLoaded = false;

    public void init(Context context) {
        try {
            mTts = new TextToSpeech(context, onInitListener);
        } catch (Exception e) {
            Log.e(TAG, Arrays.toString(e.getStackTrace()));
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
            int available = mTts.isLanguageAvailable(locale);
            if (available >= 0) {
                mTts.setLanguage(locale);
                mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        else
            Log.e(TAG, "TTS Not Initialized");
    }

    public Locale[] getSupportedLanguages() {
        int api = Build.VERSION.SDK_INT;
        if (api >= 21) {
            Set<Locale> lang = mTts.getAvailableLanguages();
            if (lang == null) {
                Log.e(TAG, "Can't retrieve list of supported languages");
                return new Locale[0];
            }
            Log.d(TAG, "List of supported languages: " + lang.size());
            return lang.toArray(new Locale[lang.size()]);
        } else
            return new Locale[0];
    }

}
