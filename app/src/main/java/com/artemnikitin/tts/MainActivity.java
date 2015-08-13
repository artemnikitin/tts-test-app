package com.artemnikitin.tts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import okio.BufferedSource;
import okio.Okio;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

public class MainActivity extends Activity {

    private String TAG = "TTS-test";
    private EditText text;
    private EditText locale;
    private TTSManager ttsManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale[] locales = Locale.getAvailableLocales();
        Log.d(TAG, Arrays.toString(locales));

        ttsManager = new TTSManager();
        ttsManager.init(this);

        text = (EditText) findViewById(R.id.input_text);
        locale = (EditText) findViewById(R.id.locale);
        Button speakNowButton = (Button) findViewById(R.id.speak_now);
        Button chooseFileButton = (Button) findViewById(R.id.choose_file);

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ru.bartwell.exfilepicker.ExFilePickerActivity.class);
                intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
                startActivityForResult(intent, 0);
            }
        });

        speakNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsManager.initQueue(text.getText().toString(),
                        new Locale(locale.getText().toString()));
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsManager.shutDown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            ExFilePickerParcelObject selectedFile = data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
            File file = new File(selectedFile.path + selectedFile.names.get(0));
            String text = "";
            try {
                BufferedSource source = Okio.buffer(Okio.source(file));
                text = source.readUtf8();
                source.close();
            } catch (IOException e) {
                Log.d(TAG, "okio can't process file " + file.getAbsolutePath());
            }
            this.text.setText(text);
        }
    }

}