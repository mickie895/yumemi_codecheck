package jp.co.yumemi.android.codecheck.data.search

import androidx.annotation.MainThread
import jp.co.yumemi.android.codecheck.data.search.events.OnSearchResultRecievedListener
import jp.co.yumemi.android.codecheck.data.search.events.OnSearchStateChangedListener
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * GitHubAPIからデータを取得するためのクラス
 */
@Singleton
class GithubApiRepository @Inject constructor(private val apiService: GithubApiService) {
    // ※複数のビューモデルとイベント駆動でやり取りするのでシングルトンである必要がある

    // 現在検索を行っている検索状況
    var lastSearchRepositories: AppendableRepositoryList =
        createDefaultResultList()
        private set

    // 検索状況に対応する検索ワード
    private lateinit var lastSearchQuery: String

    var searching: Boolean = false
        private set(value) {
            if (field == value) {
                return
            }
            field = value
            searchStateChangedListeners.forEach {
                it.onSearchStateChanged(value)
            }
        }

    /**
     * 検索開始を通知する
     * ※二重検索などによるバグの防止の為、UIスレッドからの起動を強制する
     */
    @MainThread
    fun startSearch() {
        searching = true
    }

    // 検索状況をやり取りするためのリスナ郡
    // 今のところイベントの多重購読の必要はない

    private val searchStateChangedListeners: MutableSet<OnSearchStateChangedListener> =
        mutableSetOf()

    fun setSearchStateChangedListener(listener: OnSearchStateChangedListener) {
        searchStateChangedListeners.add(listener)
    }

    fun removeSearchStateChangedListener(listener: OnSearchStateChangedListener) {
        searchStateChangedListeners.remove(listener)
    }

    private val searchResultRecievedListeners: MutableSet<OnSearchResultRecievedListener> =
        mutableSetOf()

    fun setSearchResultRecievedListener(listener: OnSearchResultRecievedListener) {
        searchResultRecievedListeners.add(listener)
    }

    fun removeSearchResultRecievedListener(listener: OnSearchResultRecievedListener) {
        searchResultRecievedListeners.remove(listener)
    }

    /**
     * 与えられた文字列をもとにリポジトリの検索を行う
     */
    suspend fun searchQuery(query: String): SearchApiResponse {
        return searchStrategy {
            val result = apiService.search(query)
            lastSearchRepositories = AppendableRepositoryList(result)
            lastSearchQuery = query
            searching = false
        }
    }

    /**
     * 次のページの検索を行う
     */
    suspend fun nextPage(): SearchApiResponse {
        return searchStrategy {
            val result = apiService.search(
                lastSearchQuery,
                lastSearchRepositories.nextPage,
                lastSearchRepositories.perPage,
            )
            lastSearchRepositories.appendResult(result)
            searching = false
        }
    }

    private fun onSearchResultRecieved(result: SearchApiResponse) {
        searchResultRecievedListeners.forEach {
            it.onSearchResultRecieved(result)
        }
    }

    /**
     * リポジトリ検索の一連の流れを共通化するための関数
     */
    private suspend fun searchStrategy(work: suspend () -> Unit): SearchApiResponse {
        val result =
            try {
                work()
                SearchApiResponse.Ok(lastSearchRepositories)
            } catch (e: Exception) {
                createFailedInstanceFrom(e)
            }
        onSearchResultRecieved(result)
        return result
    }
}
