package jp.co.yumemi.android.codecheck.data.search.events

import jp.co.yumemi.android.codecheck.data.search.SearchApiResponse

/**
 * 検索の開始・終了が通知されるリスナ
 */
interface OnSearchStateChangedListener {
    /**
     * 検索状態の変動があったときのイベント
     *
     * @param searchStatus 検索中になった場合はtrue、終了した場合はfalse
     */
    fun onSearchStateChanged(searchStatus: Boolean)
}

/**
 * 検索結果を受け取るリスナ
 */
interface OnSearchResultRecievedListener {
    /**
     * 検索結果が更新されたときのイベント
     *
     * @param response OK、NGも含めた検索結果
     */
    fun onSearchResultRecieved(response: SearchApiResponse)
}
