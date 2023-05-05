/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.viewmodels

import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.history.IHistoryRepository
import jp.co.yumemi.android.codecheck.data.search.AppendableRepositoryList
import jp.co.yumemi.android.codecheck.data.search.GithubApiRepository
import jp.co.yumemi.android.codecheck.data.search.SearchApiResponse
import jp.co.yumemi.android.codecheck.data.search.events.OnSearchResultRecievedListener
import jp.co.yumemi.android.codecheck.data.search.events.OnSearchStateChangedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 検索用画面のビューモデル
 */
@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val searchApi: GithubApiRepository,
    private val historyRepository: IHistoryRepository,
) :
    OnSearchStateChangedListener,
    OnSearchResultRecievedListener,
    ViewModel() {

    /**
     * 検索結果の実際の格納先
     */
    private val repositoryListSource: MutableLiveData<List<SearchResultItem>> = MutableLiveData(
        convertApiResponse(searchApi.lastSearchRepositories),
    )

    /**
     * UIで利用する検索結果の一覧のLiveData
     */
    val searchedRepositoryList: LiveData<List<SearchResultItem>> = repositoryListSource

    /**
     * 最後に発生したエラーの格納先
     */
    private val lastErrorSource: MutableLiveData<SearchApiResponse.Error?> = MutableLiveData(null)

    /**
     * UIで利用する、エラーメッセージ受け渡し用のLiveData
     *
     * ※利用先が一つしかないことが保証できるため成り立っている。
     */
    val lastError: LiveData<SearchApiResponse.Error?> = lastErrorSource

    private val searchingSource: MutableLiveData<Boolean>

    init {
        searchApi.setSearchStateChangedListener(this)
        searchApi.setSearchResultRecievedListener(this)
        searchingSource = MutableLiveData(searchApi.searching)
    }

    /**
     * 現在検索している状態かどうかの確認
     */
    val searching: LiveData<Boolean> = searchingSource

    override fun onCleared() {
        // ライフサイクル管理されているこのインスタンスのほうが寿命は短いはずなので、
        // 使われなくなったらイベントの購読は解除しておく。
        searchApi.removeSearchStateChangedListener(this)
        searchApi.removeSearchResultRecievedListener(this)
        super.onCleared()
    }

    /**
     * 検索APIから検索状態の変化が来たときの処理
     */
    override fun onSearchStateChanged(searchStatus: Boolean) {
        searchingSource.postValue(searchStatus)
    }

    /**
     * エラーのUI反映が完了したことの通知
     * これを忘れると画面回転時などに複数回表示が出る
     */
    @MainThread
    fun errorMessageRecieved() {
        lastErrorSource.value = null
    }

    fun search(inputText: String) {
        if (searchApi.searching) {
            return
        }
        searchApi.startSearch()
        postSearchJob(inputText)
    }

    /**
     * リポジトリの検索をRepository層に依頼する
     */
    @VisibleForTesting
    fun postSearchJob(inputText: String): Job =
        searchStrategy {
            // 新規検索時は履歴を追加する
            historyRepository.appendHistory(inputText)
            searchApi.searchQuery(inputText)
        }

    fun nextPage() {
        if (searchApi.searching) {
            return
        }
        searchApi.startSearch()
        postNextPageJob()
    }

    /**
     * 次のページのデータの取得をRepository層に依頼する
     */
    @VisibleForTesting
    fun postNextPageJob(): Job =
        searchStrategy { searchApi.nextPage() }

    /**
     * 検索APIを起動するときの共通処理
     */
    private fun searchStrategy(strategy: suspend () -> SearchApiResponse): Job =
        viewModelScope.launch(Dispatchers.IO) {
            strategy()
        }

    /**
     * Apiからの結果をもとに検索結果欄に表示させるリストを作成する
     */
    private fun convertApiResponse(response: AppendableRepositoryList): List<SearchResultItem> {
        val result: MutableList<SearchResultItem> =
            response.currentList().map { item -> SearchResultItem.Repository(item) }.toMutableList()

        if (response.canAppendResult) {
            result.add(SearchResultItem.SearchNextItem)
            return result
        }

        if (response.isEmpty()) {
            result.add(SearchResultItem.EmptyItem)
            return result
        }

        return result
    }

    /**
     * 値を受け取ったときの処理
     */
    override fun onSearchResultRecieved(response: SearchApiResponse) {
        when (response) {
            is SearchApiResponse.Error ->
                lastErrorSource.postValue(response)
            is SearchApiResponse.Ok ->
                repositoryListSource.postValue(convertApiResponse(response.result))
        }
    }
}
