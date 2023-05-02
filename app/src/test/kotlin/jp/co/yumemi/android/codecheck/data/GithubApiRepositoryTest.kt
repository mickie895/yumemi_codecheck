package jp.co.yumemi.android.codecheck.data

import jp.co.yumemi.android.codecheck.restapi.mock.getMockService
import jp.co.yumemi.android.codecheck.restapi.mock.getServerErrorMockService
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GithubApiRepositoryTest {
    /**
     * GithubAPIが正常なときの動作チェック
     */
    @Test
    fun normalQueryTest() = runTest {
        val apiService = getMockService(sampleApiResult)
        val repository = GithubApiRepository(apiService)

        val queryResult = repository.searchQuery("git")
        Assert.assertTrue("正常判定", queryResult is SearchApiResult.Ok)

        apiService.nextApiResult = emptyApiResult
        val emptyResult = repository.searchQuery("git")
        Assert.assertTrue("空の結果も正常である", emptyResult is SearchApiResult.Ok)

        apiService.nextApiResult = sampleErrorResult
        val errorResult = repository.searchQuery("git")
        Assert.assertTrue("失敗を受け取れていることの確認", errorResult is SearchApiResult.Error)
        Assert.assertTrue("失敗時は対応する返答を返す", errorResult is SearchApiResult.Error.ByQuery)
    }

    /**
     * エラーを吐いているときの確認
     */
    @Test
    fun errorQueryTest() = runTest {
        val repository = GithubApiRepository(
            getServerErrorMockService(sampleApiResult),
        )
        val errorResult = repository.searchQuery("git")
        Assert.assertTrue("失敗を受け取れていることの確認", errorResult is SearchApiResult.Error)
        Assert.assertTrue("失敗時は対応する返答を返す", errorResult is SearchApiResult.Error.ByQuery)
    }
}
