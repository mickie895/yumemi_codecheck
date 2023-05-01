package jp.co.yumemi.android.codecheck

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.co.yumemi.android.codecheck.di.launchFragmentInHiltContainer
import jp.co.yumemi.android.codecheck.fragments.search.SearchFragment
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchFragmentText {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var idlingResource: SearchFragmentIdlingResource

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragmentInHiltContainer<SearchFragment>()
        IdlingRegistry.getInstance().register(idlingResource.getInstance())
    }

    @Test
    fun testFragment() {
        onView(withId(R.id.searchInputText)).perform(
            replaceText("awesome"),
            pressImeActionButton(),
            closeSoftKeyboard(),
        )
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource.getInstance())
    }
}
