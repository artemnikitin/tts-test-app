package com.artemnikitin.tts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okio.BufferedSource;
import okio.Okio;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

public class MainActivity extends Activity {

    private final static String TAG = "TTS-MainActivity";
    private EditText text;
    private TTSManager tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner langSelect = (Spinner) findViewById(R.id.spinner);

        tts = new TTSManager();
        tts.init(this, langSelect);

        text = (EditText) findViewById(R.id.input_text);
        Button speakNowButton = (Button) findViewById(R.id.speak_now);
        Button chooseFileButton = (Button) findViewById(R.id.choose_file);

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ru.bartwell.exfilepicker.ExFilePickerActivity.class);
                intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
                startActivityForResult(intent, 0);
            }
        });

        speakNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.initQueue(text.getText().toString(),
                        new Locale(langSelect.getSelectedItem().toString()));
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tts.shutDown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            ExFilePickerParcelObject selectedFile = data.getParcelableExtra(
                    ExFilePickerParcelObject.class.getCanonicalName());
            File file = new File(selectedFile.path + selectedFile.names.get(0));
            String text = "";
            try {
                BufferedSource source = Okio.buffer(Okio.source(file));
                text = source.readUtf8();
                source.close();
            } catch (IOException e) {
                Log.d(TAG, "okio can't process file " + file.getAbsolutePath());
                Toast.makeText(getApplicationContext(), "Can't process file", Toast.LENGTH_SHORT)
                        .show();
            }
            this.text.setText(text);
        }
    }

}