package jp.co.yumemi.android.codecheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.history.IHistoryRepository
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import jp.co.yumemi.android.codecheck.data.search.GithubApiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryFragmentViewModel @Inject constructor(
    repository: IHistoryRepository,
    private val githubApiRepository: GithubApiRepository,
) : ViewModel() {
    val historyList: LiveData<List<SearchHistory>> = repository.history().asLiveData()

    fun searchFromHistory(query: String): Job =
        viewModelScope.launch {
            // TODO: 検索画面との二重検索対応
            githubApiRepository.searchQuery(query)
        }
}
