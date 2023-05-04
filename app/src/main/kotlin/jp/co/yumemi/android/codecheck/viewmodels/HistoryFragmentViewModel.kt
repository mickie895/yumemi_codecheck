package jp.co.yumemi.android.codecheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.history.HistoryRepository
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import javax.inject.Inject

@HiltViewModel
class HistoryFragmentViewModel @Inject constructor(repository: HistoryRepository) : ViewModel() {
    val historyList: LiveData<List<SearchHistory>> = repository.history().asLiveData()
}
