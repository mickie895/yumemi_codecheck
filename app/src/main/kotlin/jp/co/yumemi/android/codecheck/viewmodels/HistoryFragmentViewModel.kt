package jp.co.yumemi.android.codecheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.history.IHistoryRepository
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import jp.co.yumemi.android.codecheck.data.search.GithubApiRepository
import jp.co.yumemi.android.codecheck.data.search.events.OnSearchStateChangedListener
import jp.co.yumemi.android.codecheck.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryFragmentViewModel @Inject constructor(
    repository: IHistoryRepository,
    private val githubApiRepository: GithubApiRepository,
    @ApplicationScope private val externalScope: CoroutineScope,
) : OnSearchStateChangedListener, ViewModel() {
    val historyList: LiveData<List<SearchHistory>> = repository.history().asLiveData()

    private val searchingSource: MutableLiveData<Boolean>

    init {
        githubApiRepository.setSearchStateChangedListener(this)
        searchingSource = MutableLiveData(githubApiRepository.searching)
    }

    val searching: LiveData<Boolean> = searchingSource

    override fun onSearchStateChanged(searchStatus: Boolean) {
        searchingSource.postValue(searchStatus)
    }

    override fun onCleared() {
        // ライフサイクル管理されているこのインスタンスのほうが寿命は短いはずなので、
        // 使われなくなったらイベントの購読は解除しておく。
        githubApiRepository.removeSearchStateChangedListener(this)
        super.onCleared()
    }

    fun searchFromHistory(query: String) {
        githubApiRepository.startSearch()
        postSearch(query)
    }

    /**
     * 検索をリポジトリに依頼する
     */
    private fun postSearch(query: String): Job = externalScope.launch {
        // この関数の起動直後にフラグメントを離れるためか、viewModelScopeだと検索できなかった。
        // 広めのスコープを取る用に変更している。
        githubApiRepository.searchQuery(query)
    }
}
