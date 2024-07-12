package com.capstone.bloomy.ui.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.capstone.bloomy.R
import com.capstone.bloomy.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SignInActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(SignInActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login() {
        Espresso.onView(withId(R.id.et_email_sign_in))
            .perform(ViewActions.click(), ViewActions.typeText("sandhi@bloomy.com")).perform(
            ViewActions.closeSoftKeyboard()
        )
        Espresso.onView(withId(R.id.et_password_sign_in)).perform(
            ViewActions.click(),
            ViewActions.typeText("Sandhi1234")
        ).perform(ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btn_sign_in)).perform(ViewActions.click())

        Thread.sleep(6000)

        Espresso.onView(withId(R.id.bottom_navigation))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}