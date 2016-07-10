package com.artemnikitin.tts;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TtsManagerTest {

    @Mock
    private TextToSpeech engine;

    private TtsManager tts;

    @Before
    public void setUp() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
        when(engine.isLanguageAvailable(Locale.US)).thenReturn(0);
        when(engine.setLanguage(Locale.US)).thenReturn(0);
        when(engine.setSpeechRate(1.0f)).thenReturn(0);
        when(engine.speak("hello", TextToSpeech.QUEUE_FLUSH, null, "random string - 2d22332"))
                .thenReturn(0);
        tts = new TtsManager(engine);
    }

    @After
    public void tearDown() {
        tts.shutDown();
    }

    @Test
    public void sayText() {
        boolean result = tts.say("hello", Locale.US);
        assertTrue("Engine should process text correctly", result);
    }

    @Test
    public void sayTextWithNullLocale() {
        boolean result = tts.say("hello", null);
        assertFalse("Engine should return false", result);
    }

    @Test
    public void sayTextWithNullInput() {
        boolean result = tts.say(null, Locale.US);
        assertFalse("Engine should return false", result);
    }

    @Test
    public void sayTextWithUnavailableLocale() {
        when(engine.isLanguageAvailable(Locale.CANADA)).thenReturn(-2);
        boolean result = tts.say("hello", Locale.CANADA);
        assertFalse("Should return false for unsupported locale", result);
    }

}
