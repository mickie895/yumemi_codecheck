package jp.co.yumemi.android.codecheck.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.data.history.HistoryRepository
import jp.co.yumemi.android.codecheck.data.history.IHistoryRepository
import jp.co.yumemi.android.codecheck.data.history.room.HistoryDatabase
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.data.search.restapi.createRetrofit
import javax.inject.Singleton

// GithubAPIを利用するために必要なモジュールの準備を行うファイル
// メンテナンス性のために一つのファイルに纏めている

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {

    @Provides
    @Singleton
    fun provideApiService(): GithubApiService {
        return createRetrofit().create(GithubApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.databaseBuilder(context, HistoryDatabase::class.java, "product-database").build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {
    @Binds
    @Singleton
    fun bindHistoryRepository(historyRepository: HistoryRepository): IHistoryRepository
}
