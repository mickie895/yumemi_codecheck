package jp.co.yumemi.android.codecheck.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.yumemi.android.codecheck.data.search.RepositorySearchResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * 動作確認を兼ねたテスト
 */
class MoshiParseTest {

    private lateinit var jsonAdapter: JsonAdapter<RepositorySearchResult>
    private lateinit var moshi: Moshi

    @Before
    fun prepareAdapter() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        jsonAdapter = moshi.adapter(RepositorySearchResult::class.java)
    }

    /**
     * ライブラリの動作確認用テスト
     */
    @Test
    fun parseTest() {
        val searchResult = jsonAdapter.fromJson(sampleApiResult)

        Assert.assertNotNull("正常時のアダプタの動作チェック", searchResult)

        // gitignoreは言語設定が無い
        val gitignoreRepository = searchResult!!.searchedItemList[0]
        Assert.assertNull("言語指定はnullが入る可能性がある", gitignoreRepository.language)

        // 適当な言語が設定されているリポジトリ
        val sampleRepository = searchResult.searchedItemList[1]
        Assert.assertNotNull("言語があることを確認", sampleRepository.language)

        val emptySearchResult = jsonAdapter.fromJson(emptyApiResult)
        Assert.assertEquals("空配列の確認", 0, emptySearchResult!!.searchedItemList.count())
    }

    @Test
    fun parseErrorTest() {
        Assert.assertThrows("不正な配列が入っていたときの動作確認", JsonDataException::class.java) {
            jsonAdapter.fromJson(sampleErrorResult)
        }
    }
}
