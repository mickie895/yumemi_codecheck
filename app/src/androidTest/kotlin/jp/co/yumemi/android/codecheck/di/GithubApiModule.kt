package jp.co.yumemi.android.codecheck.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import jp.co.yumemi.android.codecheck.data.history.room.HistoryDatabase
import jp.co.yumemi.android.codecheck.data.search.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.data.search.restapi.createRetrofit
import jp.co.yumemi.android.codecheck.restapi.mock.MockedGithubApiService
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import javax.inject.Singleton

/**
 * リポジトリ層をモックのAPI返答用サービスに差し替え
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class],
)
abstract class MockedApiModule {
    @Singleton
    @Binds
    abstract fun bindApiService(githubApiService: MockedGithubApiService): GithubApiService
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HistoryModule::class],
)
object InMemoryHistoryModule {
    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java).build()
    }
}

/**
 * ネットワークの挙動の設定
 */
@Module
@InstallIn(SingletonComponent::class)
object DelegateModule {

    @Provides
    @Singleton
    fun createDelegateFrom(): BehaviorDelegate<GithubApiService> {
        val networkBehavior: NetworkBehavior = NetworkBehavior.create().apply {
            this.setVariancePercent(0)
            this.setFailurePercent(0)
            this.setErrorPercent(0)
        }

        val retrofit = createRetrofit()
        return MockRetrofit.Builder(retrofit).networkBehavior(networkBehavior).build()
            .create(GithubApiService::class.java)
    }
}
