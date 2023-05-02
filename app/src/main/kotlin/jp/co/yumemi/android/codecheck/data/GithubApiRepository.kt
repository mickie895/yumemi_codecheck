package jp.co.yumemi.android.codecheck.data

import jp.co.yumemi.android.codecheck.data.restapi.GithubApiService
import javax.inject.Inject

/**
 * GitHubAPIからデータを取得するためのクラス
 */
class GithubApiRepository @Inject constructor(private val apiService: GithubApiService) {
    /**
     * 与えられた文字列をもとにリポジトリの検索を行う
     */
    suspend fun searchQuery(query: String): SearchApiResponse {
        return try {
            SearchApiResponse.Ok(apiService.search(query))
        } catch (e: Exception) {
            createFailedInstanceFrom(e)
        }
    }
}
