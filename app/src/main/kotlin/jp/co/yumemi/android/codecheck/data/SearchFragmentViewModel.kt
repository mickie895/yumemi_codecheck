/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.codecheck.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

/**
 * TwoFragment で使う
 */
class SearchFragmentViewModel : ViewModel() {
    // TODO: DIコンテナの導入時に注入させる
    private val searchApi: GithubApiRepository = GithubApiRepository()

    /**
     * 検索結果の実際の格納先
     */
    private val searchResultSource: MutableLiveData<List<RepositoryProperty>> = MutableLiveData()

    /**
     * UIで利用する検索結果の一覧のLiveData
     */
    val searchResult: LiveData<List<RepositoryProperty>> = searchResultSource

    /**
     * リポジトリの検索をRepository層に依頼する
     */
    fun searchRepository(inputText: String) {
        // TODO: DIか何かで値を共有するか、他の方法を考える
        lastSearchDate = Date()

        viewModelScope.launch(Dispatchers.IO) {
            val resultItems = RepositoryProperty.createFromJson(
                searchApi.searchQuery(inputText)
            )
            searchResultSource.postValue(resultItems)
        }
    }
}
