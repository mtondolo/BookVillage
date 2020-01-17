package com.example.android.bookvillage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;


@RunWith(AndroidJUnit4.class)
public class EditorActivityTest {
    @Rule
    public ActivityTestRule<CatalogActivity> mActivityTestRule = new
            ActivityTestRule<>(CatalogActivity.class);

    @Before
    public void selectViewItem(){
        onView(withId(R.id.list_books)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
    @Test
    public void clickIncreaseButton_IncreaseQuantity() {
        onView(withId(R.id.increase_text_view)).perform(click());
        onView(withId(R.id.edit_book_quantity)).check(matches(withText("6")));
    }

    @Test
    public void clickDecreaseButton_DecreaseQuantity() {
        onView(withId(R.id.decrease_text_view)).perform(click());
        onView(withId(R.id.edit_book_quantity)).check(matches(withText("4")));
    }
}
