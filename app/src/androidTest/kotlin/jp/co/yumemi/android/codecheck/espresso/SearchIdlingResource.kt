package jp.co.yumemi.android.codecheck.espresso

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import jp.co.yumemi.android.codecheck.fragments.testutils.SearchFragmentIdlingResource

class SearchIdlingResource : SearchFragmentIdlingResource {
    private val idlingResource = CountingIdlingResource("Search")

    override fun increment() {
        idlingResource.increment()
    }

    override fun decrement() {
        idlingResource.decrement()
    }

    override fun getInstance(): IdlingResource = idlingResource
}
