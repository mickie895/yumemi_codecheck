/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.AppendableRepositoryList
import jp.co.yumemi.android.codecheck.data.GithubApiRepository
import jp.co.yumemi.android.codecheck.data.SearchApiResponse
import jp.co.yumemi.android.codecheck.data.createDefaultResultList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 検索用画面のビューモデル
 */
@HiltViewModel
class SearchFragmentViewModel @Inject constructor(private val searchApi: GithubApiRepository) :
    ViewModel() {

    /**
     * 検索結果の実際の格納先
     */
    private val repositoryListSource: MutableLiveData<List<SearchResultItem>> = MutableLiveData(
        convertApiResponse(createDefaultResultList()),
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

    /**
     * エラーのUI反映が完了したことの通知
     * これを忘れると画面回転時などに複数回表示が出る
     */
    @MainThread
    fun errorMessageRecieved() {
        lastErrorSource.value = null
    }

    /**
     * リポジトリの検索をRepository層に依頼する
     */
    fun searchRepository(inputText: String): Job =
        searchStrategy { searchApi.searchQuery(inputText) }

    /**
     * 次のページのデータの取得をRepository層に依頼する
     */
    fun nextPage(): Job =
        searchStrategy { searchApi.nextPage() }

    /**
     * 検索APIを起動するときの共通処理
     */
    private fun searchStrategy(strategy: suspend () -> SearchApiResponse): Job =
        viewModelScope.launch(Dispatchers.IO) {
            when (val searchApiResponse = strategy()) {
                is SearchApiResponse.Error ->
                    lastErrorSource.postValue(searchApiResponse)
                is SearchApiResponse.Ok ->
                    repositoryListSource.postValue(convertApiResponse(searchApiResponse.result))
            }
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
}
