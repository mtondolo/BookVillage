package com.example.android.bookvillage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CatalogActivityBasicTest {

    @Rule
    public ActivityTestRule<CatalogActivity> mActivityTestRule = new
            ActivityTestRule<>(CatalogActivity.class);

    @Test
    public void selectRecyclerViewItem_OpenViewItem() {
        onView(withId(R.id.list_books)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.edit_book_name)).check(matches(withText("A+")));
        onView(withId(R.id.edit_book_quantity)).check(matches(withText("5")));
        onView(withId(R.id.edit_book_price)).check(matches(withText("75.55")));
    }


    @Test
    public void clickFabButton_OpenEmptyForm() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.edit_book_name)).check(matches(withText("")));
        onView(withId(R.id.edit_book_quantity)).check(matches(withText("0")));
        onView(withId(R.id.edit_book_price)).check(matches(withText("")));
    }
}
