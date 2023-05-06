package jp.co.yumemi.android.codecheck.data.history.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import kotlinx.coroutines.flow.Flow
import java.util.Date

// 検索件数
private const val HISTORY_ITEMS = 10

/**
 * 履歴取得用のDAO
 */
@Dao
interface HistoryDao {
    // アノテーションで言いたいことを全部言っているためコメント省略。

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
        // ※Autogenerateの変数に0を指定すれば自動で設定してくれるらしい
        // 参考: https://developer.android.com/reference/androidx/room/PrimaryKey.html
        addHistory(SearchHistory(0, query, Date()))
    }
}
