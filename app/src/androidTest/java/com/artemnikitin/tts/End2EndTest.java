package com.artemnikitin.tts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class End2EndTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void onOpenAppDeviceLanguageShouldBeShown() {
        onView(withId(R.id.currentLanguageContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.currentLanguageContainer))
                .check(matches(withText(not(isEmptyString()))));
    }

    @Test
    public void mainFlowShouldWork() {
        onView(withId(R.id.input_text)).perform(click()).perform(typeText("Hello world!"));
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("English (United States) (en_US)")))
                .perform(click());
        onView(withId(R.id.speak_now)).perform(click());
        onView(withText("Speak: Hello world!")).inRoot(withDecorView(
                not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
