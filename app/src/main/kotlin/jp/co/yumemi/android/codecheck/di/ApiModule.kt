package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.data.restapi.GithubApiService
import jp.co.yumemi.android.codecheck.data.restapi.createRetrofit
import javax.inject.Singleton

// GithubAPIを利用するために必要なモジュールの準備を行うクラス

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): GithubApiService {
        return createRetrofit().create(GithubApiService::class.java)
    }
}
