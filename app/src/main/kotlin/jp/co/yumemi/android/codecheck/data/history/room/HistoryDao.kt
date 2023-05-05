package jp.co.yumemi.android.codecheck.data.history.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import kotlinx.coroutines.flow.Flow
import java.util.Date

// 検索件数
private const val HISTORY_ITEMS = 10

@Dao
interface HistoryDao {
    @Query("SELECT * FROM SearchHistory ORDER BY id DESC LIMIT $HISTORY_ITEMS")
    fun getHistory(): Flow<List<SearchHistory>>

    @Insert
    fun addHistory(lastHistory: SearchHistory)

    @Query("DELETE FROM SearchHistory")
    fun deleteHistory()

    /**
     * 検索結果を追加する
     */
    fun appendQueryHistory(query: String) {
        // ※idに0を指定すれば自動で設定してくれるらしい
        addHistory(SearchHistory(0, query, Date()))
    }
}
