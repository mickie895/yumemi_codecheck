package jp.co.yumemi.android.codecheck.fragments.testutils

import androidx.test.espresso.IdlingResource

/**
 * UIテストで処理を待機させるときのために直接導入する必要が出てしまっている関数のインタフェース
 */
interface SearchFragmentIdlingResource {
    fun increment()
    fun decrement()
    fun getInstance(): IdlingResource
}

/**
 * 本番用の何もしない実装
 */
class SearchFragmentProductIdlingResource : SearchFragmentIdlingResource {
    override fun increment() {
    }

    override fun decrement() {
    }

    override fun getInstance(): IdlingResource {
        throw UnsupportedOperationException("本番コードでテストコードの呼び出しが行われています")
    }
}
