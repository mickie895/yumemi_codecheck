package jp.co.yumemi.android.codecheck.data

import jp.co.yumemi.android.codecheck.data.search.GithubApiRepository
import jp.co.yumemi.android.codecheck.data.search.SearchApiResponse
import jp.co.yumemi.android.codecheck.data.search.events.OnSearchResultRecievedListener
import jp.co.yumemi.android.codecheck.restapi.mock.getMockService
import jp.co.yumemi.android.codecheck.restapi.mock.getServerErrorMockService
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

/**
 * リポジトリ層に対するテスト
 */
class GithubApiRepositoryTest {

    private lateinit var queryResult: SearchApiResponse

    private val listener = object : OnSearchResultRecievedListener {
        override fun onSearchResultRecieved(response: SearchApiResponse) {
            queryResult = response
        }
    }

    /**
     * GithubAPIが正常なときの動作チェック
     */
    @Test
    fun normalQueryTest() = runTest {
        val apiService = getMockService(sampleApiResult)
        val repository = GithubApiRepository(apiService)
        repository.setSearchResultRecievedListener(listener)

        repository.searchQuery("git")
        Assert.assertTrue("正常判定", queryResult is SearchApiResponse.Ok)

        apiService.nextApiResult = emptyApiResult
        repository.searchQuery("git")
        Assert.assertTrue("空の結果も正常である", queryResult is SearchApiResponse.Ok)

        apiService.nextApiResult = sampleErrorResult
        repository.searchQuery("git")
        Assert.assertTrue("失敗時は対応する返答を返す", queryResult is SearchApiResponse.Error.ByQuery)
    }

    /**
     * エラーを吐いているときの確認
     */
    @Test
    fun errorQueryTest() = runTest {
        val repository = GithubApiRepository(
            getServerErrorMockService(sampleApiResult),
        )
        repository.setSearchResultRecievedListener(listener)

        repository.searchQuery("git")
        Assert.assertTrue("失敗時は対応する返答を返す", queryResult is SearchApiResponse.Error.ByQuery)
    }
}
