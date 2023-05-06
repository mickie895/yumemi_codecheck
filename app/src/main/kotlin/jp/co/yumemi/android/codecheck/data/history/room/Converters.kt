package jp.co.yumemi.android.codecheck.data.history.room

import androidx.room.TypeConverter
import java.util.Date

/**
 * 時間のデータを保存できるようにするためのコンバータ
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time
}
