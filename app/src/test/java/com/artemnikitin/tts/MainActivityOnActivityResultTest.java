package com.artemnikitin.tts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import static junit.framework.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityOnActivityResultTest {

    private Activity activity;

    private EditText text;

    private Button chooseFileButton;

    private Intent start;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
        text = (EditText) activity.findViewById(R.id.input_text);
        chooseFileButton = (Button) activity.findViewById(R.id.choose_file);
        ShadowApplication application = shadowOf(RuntimeEnvironment.application);
        application.grantPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
        chooseFileButton.performClick();
        start = shadowOf(activity).getNextStartedActivity();
    }

    @Test
    public void receiveCorrectIntent() {
        Intent result = new Intent();
        result.putExtra("result_file_path", RuntimeEnvironment.application.getPackageResourcePath()
                + "/src/test/assets/file.txt");

        shadowOf(activity).receiveResult(start, Activity.RESULT_OK, result);
        assertEquals("OnActivityResult should produce correct text",
                "This is test text from a file", text.getText().toString());
    }

    @Test
    public void receiveIntentWithFailedResult() {
        Intent result = new Intent();
        result.putExtra("result_file_path", RuntimeEnvironment.application.getPackageResourcePath()
                + "/src/test/assets/file.txt");

        shadowOf(activity).receiveResult(start, Activity.RESULT_CANCELED, result);
        assertEquals("EditText should remain empty", "", text.getText().toString());
    }

    @Test
    public void receiveIntentWithoutExtra() {
        shadowOf(activity).receiveResult(start, Activity.RESULT_OK, new Intent());
        assertEquals("EditText should remain empty", "", text.getText().toString());
    }

    @Test
    public void receiveIntentWithUnexistedPathToFile() {
        Intent result = new Intent();
        result.putExtra("result_file_path", "file.txt");

        shadowOf(activity).receiveResult(start, Activity.RESULT_OK, result);
        assertEquals("EditText should remain empty", "", text.getText().toString());
    }

    @Test
    public void receiveIntentWithNull() {
        Intent result = new Intent();
        result.putExtra("result_file_path", (String) null);

        shadowOf(activity).receiveResult(start, Activity.RESULT_OK, result);
        assertEquals("EditText should remain empty", "", text.getText().toString());
    }

    @Test
    public void receiveNullInsteadOfIntent() {
        shadowOf(activity).receiveResult(start, Activity.RESULT_OK, null);
        assertEquals("EditText should remain empty", "", text.getText().toString());
    }

}
