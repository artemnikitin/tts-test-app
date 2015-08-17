package com.artemnikitin.tts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    private TTSManager ttsManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale[] locales = Locale.getAvailableLocales();
        Filter filter = new Filter(locales);
        ArrayAdapter<Locale> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, filter.getListOfLocales());

        ttsManager = new TTSManager();
        ttsManager.init(this);

        text = (EditText) findViewById(R.id.input_text);
        Button speakNowButton = (Button) findViewById(R.id.speak_now);
        Button chooseFileButton = (Button) findViewById(R.id.choose_file);
        final Spinner langSelect = (Spinner) findViewById(R.id.spinner);
        langSelect.setAdapter(adapter);

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
                        new Locale(langSelect.getSelectedItem().toString()));
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