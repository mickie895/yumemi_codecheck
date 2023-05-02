package jp.co.yumemi.android.codecheck.restapi.mock

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.yumemi.android.codecheck.data.RepositorySearchResult
import jp.co.yumemi.android.codecheck.data.sampleApiResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class MockRetrofitTest {
    /**
     * Retrofit(モック)の動作確認用テスト
     */
    @Test
    fun mockedServiceTest() = runTest {
        // モックの検索結果
        val service = getMockService(sampleApiResult)
        val searchResult = service.search("git", 1, 30)

        // 値を直接変換した、等価なはずのモックの検索結果
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(RepositorySearchResult::class.java)

        Assert.assertEquals("Retrofitの動作確認", searchResult, jsonAdapter.fromJson(sampleApiResult))
    }
}
