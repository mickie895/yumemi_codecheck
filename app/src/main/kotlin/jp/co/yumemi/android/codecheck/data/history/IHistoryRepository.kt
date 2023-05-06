package jp.co.yumemi.android.codecheck.data.history

import kotlinx.coroutines.flow.Flow

interface IHistoryRepository {
    // 複数フラグメントで連携が必要な場合はSingletonを指定すること。

    /**
     * 新しい履歴を追加する
     * 検索時間はこの関数の起動時を想定している
     *
     * @param query 検索に使用した文字列
     */
    suspend fun appendHistory(query: String)

    /**
     * 現在の履歴一覧
     */
    fun history(): Flow<List<SearchHistory>>

    /**
     * 履歴の削除
     */
    suspend fun clearHistory()
}
