package jp.co.yumemi.android.codecheck.restapi.mock

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.yumemi.android.codecheck.data.search.RepositorySearchResult
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.data.search.restapi.createRetrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

/**
 * 戻り値などを指定できるモックされた検索結果取得用サービス
 */
class MockedGithubApiService(
    var delegate: BehaviorDelegate<GithubApiService>,
    var nextApiResult: String,
) : GithubApiService {

    private val jsonAdapter: JsonAdapter<RepositorySearchResult>
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    init {
        jsonAdapter = moshi.adapter(RepositorySearchResult::class.java)
    }

    override suspend fun search(searchText: String, page: Int, count: Int): RepositorySearchResult {
        val githubSearchResult = jsonAdapter.fromJson(nextApiResult)!!
        return delegate.returningResponse(githubSearchResult).search(searchText)
    }
}

object NetworkBehaviorFactory {
    /**
     * 失敗を発生させない挙動
     */
    fun alwaysSuccessBehavior(): NetworkBehavior {
        return NetworkBehavior.create().apply {
            this.setVariancePercent(0)
            this.setFailurePercent(0)
            this.setErrorPercent(0)
        }
    }

    /**
     * 失敗が発生する挙動
     */
    fun alwaysFailBehavior(): NetworkBehavior {
        return NetworkBehavior.create().apply {
            this.setVariancePercent(0)
            this.setFailurePercent(0)
            this.setErrorPercent(100)
        }
    }
}

/**
 * 必ず戻り値が返ってくるときのモックの取得
 */
fun getMockService(apiResult: String): MockedGithubApiService {
    return MockedGithubApiService(createDelegateFrom(NetworkBehaviorFactory.alwaysSuccessBehavior()), apiResult)
}

/**
 * サーバのえらーが返ってくるときのモックの取得
 */
fun getServerErrorMockService(apiResult: String): MockedGithubApiService {
    return MockedGithubApiService(createDelegateFrom(NetworkBehaviorFactory.alwaysFailBehavior()), apiResult)
}

/**
 * 素直に返答するときの振る舞い
 */
private fun createDelegateFrom(networkBehavior: NetworkBehavior): BehaviorDelegate<GithubApiService> {
    val retrofit = createRetrofit()
    return MockRetrofit.Builder(retrofit).networkBehavior(networkBehavior).build()
        .create(GithubApiService::class.java)
}
