package jp.co.yumemi.android.codecheck

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.co.yumemi.android.codecheck.data.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.fragments.search.SearchResultAdapter
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentIdlingResource
import jp.co.yumemi.android.codecheck.restapi.mock.MockedGithubApiService
import jp.co.yumemi.android.codecheck.restapi.mock.sampleApiResult
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainPathTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var idlingResource: SearchFragmentIdlingResource

    @Inject
    lateinit var githubApi: GithubApiService

    private lateinit var mockedApi: MockedGithubApiService

    @Before
    fun setup() {
        // このインスタンスに対してインジェクションを行う
        hiltRule.inject()

        // モック対象の検索出力を書き換えるための準備
        mockedApi = githubApi as MockedGithubApiService

        // 検索を行うときの非同期処理の待機を行うための準備
        IdlingRegistry.getInstance().register(idlingResource.getInstance())
    }

    @Test
    fun testScreenChange() {
        launchActivity<TopActivity>().use {
            mockedApi.setNextApiResult(sampleApiResult)

            Espresso.onView(ViewMatchers.withId(R.id.searchInputText)).perform(
                ViewActions.replaceText("awesome"),
                ViewActions.pressImeActionButton(),
                ViewActions.closeSoftKeyboard(),
            )
            Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.ViewHolder>(
                    0,
                    click(),
                ),
            )
            Espresso.onView(ViewMatchers.withId(R.id.repositoryName)).check(matches(isDisplayed()))
        }
    }
}
