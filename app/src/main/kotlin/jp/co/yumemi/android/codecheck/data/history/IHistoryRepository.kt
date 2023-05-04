package jp.co.yumemi.android.codecheck.data.history

import kotlinx.coroutines.flow.Flow

interface IHistoryRepository {
    /**
     * 新しい履歴を追加する
     */
    suspend fun appendHistory(query: String)

    /**
     * 現在の履歴一覧
     */
    fun history(): Flow<List<SearchHistory>>
}
