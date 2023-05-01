package jp.co.yumemi.android.codecheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import jp.co.yumemi.android.codecheck.espresso.SearchIdlingResource
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentIdlingResource
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [EspressoFakeTestModule::class],
)
object EspressoTestModule {

    @Provides
    @Singleton
    fun provideSearchFragmentCountingResource(): SearchFragmentIdlingResource {
        return SearchIdlingResource()
    }
}
