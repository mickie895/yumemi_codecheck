package jp.co.yumemi.android.codecheck.data.search

import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import javax.inject.Inject

/**
 * GitHubAPIからデータを取得するためのクラス
 */
class GithubApiRepository @Inject constructor(private val apiService: GithubApiService) {
    // 現在検索を行っている検索状況
    private lateinit var lastSearchRepositories: AppendableRepositoryList

    // 検索状況に対応する検索ワード
    private lateinit var lastSearchQuery: String

    /**
     * 与えられた文字列をもとにリポジトリの検索を行う
     */
    suspend fun searchQuery(query: String): SearchApiResponse {
        return searchStrategy {
            val result = apiService.search(query)
            lastSearchRepositories = AppendableRepositoryList(result)
            lastSearchQuery = query
        }
    }

    /**
     * 次のページの検索を行う
     */
    suspend fun nextPage(): SearchApiResponse {
        return searchStrategy {
            val result = apiService.search(lastSearchQuery, lastSearchRepositories.nextPage, lastSearchRepositories.perPage)
            lastSearchRepositories.appendResult(result)
        }
    }

    /**
     * リポジトリ検索の一連の流れを共通化するための関数
     */
    private suspend fun searchStrategy(work: suspend () -> Unit): SearchApiResponse {
        return try {
            work()
            SearchApiResponse.Ok(lastSearchRepositories)
        } catch (e: Exception) {
            createFailedInstanceFrom(e)
        }
    }
}
