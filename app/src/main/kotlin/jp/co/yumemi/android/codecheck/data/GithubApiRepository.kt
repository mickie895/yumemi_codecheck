package jp.co.yumemi.android.codecheck.data

import jp.co.yumemi.android.codecheck.data.restapi.GithubApiService
import javax.inject.Inject

/**
 * GitHubAPIからデータを取得するためのクラス
 */
class GithubApiRepository @Inject constructor(private val apiService: GithubApiService) {
    /**
     * 与えられた文字列をもとに検索を行う
     */
    suspend fun searchQuery(query: String): SearchApiResult {
        return try {
            SearchApiResult.Ok(apiService.search(query))
        } catch (e: Exception) {
            SearchApiResult.Error.ByUnknownSource(e)
        }
    }
}
