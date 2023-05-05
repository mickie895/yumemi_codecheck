package jp.co.yumemi.android.codecheck.data.search.events

import jp.co.yumemi.android.codecheck.data.search.SearchApiResponse

/**
 * 検索の開始・終了が通知されるリスナ
 */
interface OnSearchStateChangedListener {
    fun onSearchStateChanged(searchStatus: Boolean)
}

/**
 * 検索結果を受け取るリスナ
 */
interface OnSearchResultRecievedListener {
    fun onSearchResultRecieved(response: SearchApiResponse)
}
