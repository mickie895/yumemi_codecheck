package jp.co.yumemi.android.codecheck.room.mock

import jp.co.yumemi.android.codecheck.data.history.IHistoryRepository
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Integer.min
import java.util.Date

/**
 * 履歴機能のモック（一応同じ機能は持たせておく）
 */
class MockHistoryRepository : IHistoryRepository {
    private val histories: MutableList<SearchHistory> = mutableListOf()
    private val counter = 1

    override suspend fun appendHistory(query: String) {
        histories.add(SearchHistory(counter, query, Date()))
    }

    override fun history(): Flow<List<SearchHistory>> = flow {
        val allItems = histories.count()
        emit(histories.subList(min(0, allItems - 10), allItems + 1))
    }

    override suspend fun clearHistory() {
        histories.clear()
    }
}
