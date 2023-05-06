package jp.co.yumemi.android.codecheck.data.search.restapi

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.yumemi.android.codecheck.data.search.RepositorySearchResult
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * GithubAPIのインタフェース
 */
interface GithubApiService {
    @GET("search/repositories")
    suspend fun search(
        @Query(value = "q", encoded = true) searchText: String,
        @Query(value = "page") page: Int = 1,
        @Query(value = "per_page") count: Int = 30,
    ): RepositorySearchResult
}

/**
 * Githubの検索APIを起動するためのRetrofitオブジェクトの生成
 */
fun createRetrofit(): Retrofit {
    val moshi = Moshi.Builder().add(
        KotlinJsonAdapterFactory(),
    ).build()

    return Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                moshi,
            ),
        )
        .build()
}

private const val API_URL = "https://api.github.com/"
