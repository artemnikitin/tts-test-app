package com.artemnikitin.tts;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okio.BufferedSource;
import okio.Okio;

public class MainActivity extends Activity implements OnRequestPermissionsResultCallback {

    private final static String TAG = "TTS-MainActivity";

    private final static int READ_ACCESS = 0;

    private EditText text;

    private TtsManager tts;

    private TextView systemLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner langSelect = (Spinner) findViewById(R.id.spinner);
        tts = new TtsManager(this, langSelect);
        text = (EditText) findViewById(R.id.input_text);
        systemLanguage = (TextView) findViewById(R.id.currentLanguageContainer);
        Button speakNowButton = (Button) findViewById(R.id.speak_now);
        Button chooseFileButton = (Button) findViewById(R.id.choose_file);

        systemLanguage.setText(Locale.getDefault().getDisplayName() + "(" + Locale.getDefault().toString() + ")");

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_ACCESS);
                } else {
                    new MaterialFilePicker().withActivity(MainActivity.this).withRequestCode(111)
                            .withFilterDirectories(false).withHiddenFiles(true).start();
                }
            }
        });

        speakNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.say(text.getText().toString(),
                        processSelection(langSelect.getSelectedItem().toString()));
            }
        });

    }

    @Override
    public void onDestroy() {
        tts.shutDown();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (filePath != null) {
                File file = new File(filePath);
                String text = "";
                try {
                    BufferedSource source = Okio.buffer(Okio.source(file));
                    text = source.readUtf8();
                    source.close();
                } catch (IOException e) {
                    Log.d(TAG, "Can't process file " + file.getAbsolutePath());
                    Toast.makeText(getApplicationContext(), "Can't process file",
                            Toast.LENGTH_SHORT).show();
                }
                this.text.setText(text);
            }
        }
    }

    private Locale processSelection(String text) {
        int first = text.lastIndexOf("(");
        if (first != -1) {
            String loc = text.substring(first + 1, text.length());
            return new Locale(loc);
        } else {
            return Locale.US;
        }
    }

}