/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.GithubApiRepository
import jp.co.yumemi.android.codecheck.data.RepositoryProperty
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
     * リポジトリの検索をRepository層に依頼する
     */
    fun searchRepository(inputText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchApiResult = searchApi.searchQuery(inputText)
            repositoryListSource.postValue(searchApiResult.searchedItemList)
        }
    }
}
