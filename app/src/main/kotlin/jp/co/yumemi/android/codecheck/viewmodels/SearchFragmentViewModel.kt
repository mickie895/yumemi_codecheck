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
import jp.co.yumemi.android.codecheck.data.GithubApiRepository
import jp.co.yumemi.android.codecheck.data.RepositoryProperty
import jp.co.yumemi.android.codecheck.data.SearchApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 検索用画面のビューモデル
 */
@HiltViewModel
class SearchFragmentViewModel @Inject constructor(private val searchApi: GithubApiRepository) : ViewModel() {

    /**
     * 検索結果の実際の格納先
     */
    private val repositoryListSource: MutableLiveData<List<RepositoryProperty>> = MutableLiveData()

    /**
     * UIで利用する検索結果の一覧のLiveData
     */
    val searchedRepositoryList: LiveData<List<RepositoryProperty>> = repositoryListSource

    /**
     * 最後に発生したエラーの格納先
     */
    private val lastErrorSource: MutableLiveData<SearchApiResult.Error?> = MutableLiveData(null)

    /**
     * UIで利用する、エラーメッセージ受け渡し用のLiveData
     *
     * ※利用先が一つしかないことが保証できるため成り立っている。
     */
    val lastError: LiveData<SearchApiResult.Error?> = lastErrorSource

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
    fun searchRepository(inputText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val searchApiResult = searchApi.searchQuery(inputText)) {
                is SearchApiResult.Error ->
                    lastErrorSource.postValue(searchApiResult)
                is SearchApiResult.Ok ->
                    repositoryListSource.postValue(searchApiResult.result.searchedItemList)
            }
        }
    }
}
