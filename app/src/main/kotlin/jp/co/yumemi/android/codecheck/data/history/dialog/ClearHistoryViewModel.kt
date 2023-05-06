package jp.co.yumemi.android.codecheck.data.history.dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.data.history.IHistoryRepository
import jp.co.yumemi.android.codecheck.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClearHistoryViewModel @Inject constructor(
    private val historyRepository: IHistoryRepository,
    @ApplicationScope val scope: CoroutineScope,
) : ViewModel() {

    fun removeHistory(): Job = scope.launch(Dispatchers.IO) {
        // ダイアログが閉じられたときにフラグメント自体は消失するため大きめのスコープを使って履歴の消去を行う
        historyRepository.clearHistory()
    }
}
