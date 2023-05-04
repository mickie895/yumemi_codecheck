package jp.co.yumemi.android.codecheck.data.history.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import javax.inject.Singleton

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {
    // シングルトンの管理はHiltに任せる
    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.databaseBuilder(context, HistoryDatabase::class.java, "product-database").build()
    }
}
