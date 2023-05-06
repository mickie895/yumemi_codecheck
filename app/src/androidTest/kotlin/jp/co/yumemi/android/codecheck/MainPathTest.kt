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
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
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

    /**
     * 基本的な動作チェック
     */
    @Test
    fun testScreenChange() {
        // ※ 基本的にLiveDataを始めとする、外部のライブラリをほぼそのまま使っているため、
        // UI側のチェックする必要は少ない。
        launchActivity<TopActivity>().use {
            mockedApi.setNextApiResult(sampleApiResult)

            // 最初は「検索が空」の画面のはずなので、クリックしても何もしない。
            // (なにかしてたらこのあとのテストが続かないはず)
            Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                    0,
                    click(),
                ),
            )

            // 適当な検索を行わせる。（※検索結果は差し替えられている）
            Espresso.onView(ViewMatchers.withId(R.id.searchInputText)).perform(
                ViewActions.replaceText("awesome"),
                ViewActions.pressImeActionButton(),
                ViewActions.closeSoftKeyboard(),
            )

            // 今度は最初の項目クリックで次画面へ移動する
            Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                    0,
                    click(),
                ),
            )

            // 表記の確認。
            Espresso.onView(ViewMatchers.withId(R.id.repositoryName)).check(matches(isDisplayed()))
        }
    }
}
