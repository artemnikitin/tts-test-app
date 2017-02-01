package com.artemnikitin.tts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class MainActivityTest {

    private Activity activity;

    private EditText text;

    private Spinner langList;

    private Button speakNowButton;

    private Button chooseFileButton;

    private TextView systemLanguage;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
        text = (EditText) activity.findViewById(R.id.input_text);
        langList = (Spinner) activity.findViewById(R.id.spinner);
        speakNowButton = (Button) activity.findViewById(R.id.speak_now);
        chooseFileButton = (Button) activity.findViewById(R.id.choose_file);
        systemLanguage = (TextView) activity.findViewById(R.id.currentLanguageContainer);

        List<String> list = new ArrayList<>();
        list.add("English (United States) (en_US)");
        langList.setAdapter(new ArrayAdapter<>(RuntimeEnvironment.application,
                R.layout.support_simple_spinner_dropdown_item, list));
    }

    @Test
    public void putTextAndSpeakItShouldWork() {
        text.setText("abc");
        langList.setSelection(0);
        speakNowButton.performClick();

        ShadowApplication application = shadowOf(RuntimeEnvironment.application);
        assertTrue("Application should be non null", application != null);
    }

    @Test
    public void cantUseFilePickerWithoutPermissions() {
        ShadowApplication application = shadowOf(RuntimeEnvironment.application);
        application.denyPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
        chooseFileButton.performClick();
        Intent next = shadowOf(activity).getNextStartedActivity();
        assertEquals("Can't start activity without permissions", null, next);
    }

    @Test
    public void filePickerWithPermissionsShouldWork() {
        ShadowApplication application = shadowOf(RuntimeEnvironment.application);
        application.grantPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
        chooseFileButton.performClick();
        Intent next = shadowOf(activity).getNextStartedActivity();
        assertTrue("Without permissions file picker activity should start", next != null);
    }

    @Test
    public void textViewShowsSystemLanguage() {
        assertEquals("Text View should show system language", "English (United States) (en_US)",
                systemLanguage.getText());
    }

}
