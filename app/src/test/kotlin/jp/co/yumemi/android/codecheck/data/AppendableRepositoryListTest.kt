package jp.co.yumemi.android.codecheck.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AppendableRepositoryListTest {
    // テスト対象のインスタンス
    private lateinit var testTarget: AppendableRepositoryList

    // テストで利用するParser
    private lateinit var jsonAdapter: JsonAdapter<RepositorySearchResult>

    private fun getResult(result: String) = jsonAdapter.fromJson(result)!!

    @Before
    fun setUp() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        jsonAdapter = moshi.adapter(RepositorySearchResult::class.java)
    }

    @Test
    fun checkAppendNormal() {
        testTarget = AppendableRepositoryList(getResult(sampleApiResult))
        Assert.assertTrue("適当なサイズの結果", testTarget.canAppendResult)
        Assert.assertEquals("次のページの確認", 2, testTarget.nextPage)

        testTarget.appendResult(getResult(sampleWith(61)))
        Assert.assertTrue("境界条件チェック1", testTarget.canAppendResult)
        Assert.assertEquals("次のページの確認", 3, testTarget.nextPage)

        testTarget.appendResult(getResult(sampleWith(90)))
        Assert.assertFalse("境界条件チェック2", testTarget.canAppendResult)
    }

    @Test
    fun checkAppendNormal2() {
        testTarget = AppendableRepositoryList(getResult(sampleWith(31)))
        Assert.assertTrue("境界条件チェック1", testTarget.canAppendResult)

        testTarget.appendResult(getResult(sampleWith(incompleteResults = true)))
        Assert.assertFalse("検索が間に合わなかったときのチェック", testTarget.canAppendResult)
    }

    @Test
    fun checkAbnormalState() {
        testTarget = AppendableRepositoryList(getResult(emptyApiResult))
        Assert.assertFalse("境界条件チェック：検索結果が空のときは次は検索できない", testTarget.canAppendResult)
    }
}
