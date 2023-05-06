package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentIdlingResource
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentProductIdlingResource
import javax.inject.Singleton

/**
 * UIテストを行うために必要なモジュール
 */
@Module
@InstallIn(SingletonComponent::class)
object EspressoFakeTestModule {

    @Provides
    @Singleton
    fun provideSearchFragmentCountingResource(): SearchFragmentIdlingResource {
        return SearchFragmentProductIdlingResource()
    }
}
