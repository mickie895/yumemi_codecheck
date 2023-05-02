package jp.co.yumemi.android.codecheck.data

/**
 * エラーハンドリング対応用のAPIの実行結果
 */
sealed class SearchApiResult {
    // Result型を実装するオープンソースのライブラリは存在するが、
    // カテゴリ分けのみが目的のため今回は導入しない

    /**
     * 正常終了
     */
    class Ok(val result: RepositorySearchResult) : SearchApiResult()

    /**
     * 異常終了
     */
    sealed class Error(val causedBy: Exception) : SearchApiResult() {
        /**
         * Githubのサーバのエラーや入力内容などのエラー発生
         */
        class ByQuery(causedBy: Exception) : Error(causedBy)

        /**
         * 機内モードONなどの、端末のネットワーク状況起因のエラー発生
         */
        class ByNetwork(causedBy: Exception) : Error(causedBy)

        /**
         * プログラミング時には想定できていなかったエラー発生
         */
        class ByUnknownSource(causedBy: Exception) : Error(causedBy)
    }
}

/**
 * エラー原因の振り分けを行う
 */
fun createFailedInstanceFrom(causedBy: Exception): SearchApiResult.Error {
    // ※実際に動かして見た結果なので見落としを発見したら都度追加すること
    return when (causedBy) {
        is retrofit2.HttpException -> SearchApiResult.Error.ByQuery(causedBy) // 検索失敗もここに含まれる
        is com.squareup.moshi.JsonDataException -> SearchApiResult.Error.ByQuery(causedBy) // APIの戻り値がエラーだったらこのエラーが帰ってくる
        is java.io.IOException -> SearchApiResult.Error.ByNetwork(causedBy) // 機内モード時の例外はそれはここに含まれた
        else -> SearchApiResult.Error.ByUnknownSource(causedBy) // 想定外の例外
    }
}
