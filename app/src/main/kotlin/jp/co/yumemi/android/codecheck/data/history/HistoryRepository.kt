package jp.co.yumemi.android.codecheck.data.history

import jp.co.yumemi.android.codecheck.data.history.room.HistoryDao
import jp.co.yumemi.android.codecheck.data.history.room.HistoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    database: HistoryDatabase,
) : IHistoryRepository {
    private val dao: HistoryDao = database.historyDao()

    /**
     * 新しい履歴を追加する
     */
    override suspend fun appendHistory(query: String) {
        withContext(Dispatchers.IO) {
            dao.appendQueryHistory(query)
        }
    }

    /**
     * 現在の履歴一覧
     */
    override fun history(): Flow<List<SearchHistory>> = dao.getHistory()
}
