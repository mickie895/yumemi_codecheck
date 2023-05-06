package jp.co.yumemi.android.codecheck.viewmodel.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.co.yumemi.android.codecheck.data.emptyApiResult
import jp.co.yumemi.android.codecheck.data.sampleApiResult
import jp.co.yumemi.android.codecheck.data.sampleErrorResult
import jp.co.yumemi.android.codecheck.data.search.GithubApiRepository
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentProductIdlingResource
import jp.co.yumemi.android.codecheck.restapi.mock.MockedGithubApiService
import jp.co.yumemi.android.codecheck.restapi.mock.getMockService
import jp.co.yumemi.android.codecheck.room.mock.MockHistoryRepository
import jp.co.yumemi.android.codecheck.util.MainCoroutineRule
import jp.co.yumemi.android.codecheck.viewmodels.SearchFragmentViewModel
import jp.co.yumemi.android.codecheck.viewmodels.SearchResultItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    // テスト対象
    private lateinit var viewModel: SearchFragmentViewModel

    // 動作を調整しながら確認するためのモック
    private lateinit var apiService: MockedGithubApiService

    private val historyRepository = MockHistoryRepository()

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        apiService = getMockService(sampleApiResult)
        viewModel = SearchFragmentViewModel(
            GithubApiRepository(apiService),
            historyRepository,
            SearchFragmentProductIdlingResource(),
        )
    }

    /**
     * 現時点での格納されている値を取得
     */
    private fun takeCurrentList(): List<SearchResultItem> = viewModel.searchedRepositoryList.value!!

    @Test
    fun checkRepositoryList() = runTest {
        Assert.assertNull("開始時にエラーは無し", viewModel.lastError.value)

        viewModel.postSearchJob("git").join()
        Assert.assertEquals("通常の検索成功時の状況", 4, takeCurrentList().count())
        Assert.assertEquals(
            "最後に続き検索用の項目が入っている",
            SearchResultItem.SearchNextItem,
            takeCurrentList().last(),
        )

        Assert.assertNull("正常時にエラーは出ない", viewModel.lastError.value)

        viewModel.postNextPageJob().join()
        Assert.assertEquals("続きを検索したときの挙動", 7, takeCurrentList().count())
        Assert.assertEquals(
            "最後に続き検索用の項目が入っている",
            SearchResultItem.SearchNextItem,
            takeCurrentList().last(),
        )

        apiService.nextApiResult = sampleErrorResult
        viewModel.postSearchJob("git").join()
        Assert.assertEquals(
            "エラーが発生しているとき、前回の検索結果を保持している",
            7,
            takeCurrentList().count(),
        )

        Assert.assertNotNull("エラー時はそれを示すメッセージが格納される", viewModel.lastError.value)

        viewModel.errorMessageRecieved()

        Assert.assertNull("エラー処理完了後はリセットされている", viewModel.lastError.value)

        apiService.nextApiResult = emptyApiResult
        viewModel.postSearchJob("git").join()
        Assert.assertEquals(
            "検索結果がなかったときはなかったということがわかる",
            1,
            takeCurrentList().count(),
        )
        Assert.assertEquals(
            "空であることを示すメッセージが入っている",
            SearchResultItem.EmptyItem,
            takeCurrentList().first(),
        )
    }
}
