package jp.co.yumemi.android.codecheck.restapi

import jp.co.yumemi.android.codecheck.data.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.data.restapi.createRetrofit
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RetrofitTest {
    /**
     * Retrofitの動作確認用テスト
     */
    @Test
    fun retrofitUseTest() = runTest {
        // TODO:動作確認が完了したらモックに差し替える
        val service = createRetrofit().create(GithubApiService::class.java)
        val searchResult = service.search("git", 1, 30)

        // "git"で検索したときに内容0件だったら何か別の問題が出ているはず
        Assert.assertTrue("動作チェック用の内容確認", searchResult.searchedItemList.isNotEmpty())
    }
}
