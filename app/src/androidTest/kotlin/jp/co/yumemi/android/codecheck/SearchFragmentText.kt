package jp.co.yumemi.android.codecheck

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.di.launchFragmentInHiltContainer
import jp.co.yumemi.android.codecheck.fragments.search.SearchFragment
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentIdlingResource
import jp.co.yumemi.android.codecheck.restapi.mock.MockedGithubApiService
import jp.co.yumemi.android.codecheck.restapi.mock.sampleApiResult
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchFragmentText {
    /*
     * このテストは複数のテストガイドを組み合わせて動かしている。
     * それぞれかなりややこしいため、必要な情報一覧を提示しておく。
     *
     * HiltでUIテストを行うときのガイド
     * https://developer.android.com/training/dependency-injection/hilt-testing?hl=ja
     *
     * EspressoでUIを実行するとき、非同期処理が含まれている場合のガイド
     * https://developer.android.com/training/testing/espresso/idling-resource?hl=ja
     */

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var idlingResource: SearchFragmentIdlingResource

    @Inject
    lateinit var mockedApi: GithubApiService

    @Before
    fun setup() {
        // このインスタンスに対してインジェクションを行う
        hiltRule.inject()

        // フラグメントの起動準備（Hiltを使っているため特殊処理が入っている）
        launchFragmentInHiltContainer<SearchFragment>()

        // 検索を行うときの非同期処理の待機を行うための準備
        IdlingRegistry.getInstance().register(idlingResource.getInstance())
    }

    @Test
    fun testFragment() {
        (mockedApi as MockedGithubApiService).setNextApiResult(sampleApiResult)
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
