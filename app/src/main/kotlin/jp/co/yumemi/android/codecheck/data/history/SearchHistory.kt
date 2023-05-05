package jp.co.yumemi.android.codecheck.data.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val query: String,
    val searchedDate: Date,
)
