package jp.co.yumemi.android.codecheck.restapi.mock

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.yumemi.android.codecheck.data.search.RepositorySearchResult
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import retrofit2.mock.BehaviorDelegate
import javax.inject.Inject

/**
 * 戻り値などを指定できるモックされた検索結果取得用サービス
 *
 * ※ ユニットテストにも同じようなコードが存在しているが、下記アドレスを参考にソースを共有しようとしても
 * Android Studioがどちらか片方のコードとしてしか認識しなかったためこちらにも残してある。
 * https://developer.android.com/studio/build/gradle-tips?hl=ja#change-default-source-set-configurations
 */
class MockedGithubApiService @Inject constructor(
    var delegate: BehaviorDelegate<GithubApiService>,
) : GithubApiService {

    private val jsonAdapter: JsonAdapter<RepositorySearchResult>
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private lateinit var nextApiResult: String

    init {
        jsonAdapter = moshi.adapter(RepositorySearchResult::class.java)
    }

    fun setNextApiResult(source: String) {
        nextApiResult = source
    }

    override suspend fun search(searchText: String, page: Int, count: Int): RepositorySearchResult {
        val githubSearchResult = jsonAdapter.fromJson(nextApiResult)!!
        return delegate.returningResponse(githubSearchResult).search(searchText)
    }
}
