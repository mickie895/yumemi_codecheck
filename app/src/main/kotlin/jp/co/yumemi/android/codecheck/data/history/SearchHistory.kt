package jp.co.yumemi.android.codecheck.data.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 検索履歴保管用クラス
 */
@Entity
data class SearchHistory(
    // PrimaryKeyはRoomのライブラリとして必須だから設けている。
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val query: String,
    val searchedDate: Date,
)
