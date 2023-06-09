package jp.co.yumemi.android.codecheck.data.search

/**
 * 検索結果を追記可能にした、検索結果格納用クラス
 */
class AppendableRepositoryList(private var searchResult: RepositorySearchResult) {
    /**
     * 継ぎ足しが行われるリスト
     */
    private val repositoryList: MutableList<RepositoryProperty> =
        searchResult.searchedItemList.toMutableList()

    /**
     * 呼び出し時点でのリポジトリのリスト
     */
    fun currentList(): List<RepositoryProperty> = repositoryList.toList()

    /**
     * 次の検索結果を得るために必要なページ番号
     */
    var nextPage: Int = 2

    /**
     * 一度に検索で得るリポジトリ数
     */
    val perPage = 30

    /**
     * 半端な数の結果が来ても対応しやすいようにページ数で今まで検索したリポジトリ数は検索する
     */
    private val searchedPage get() = nextPage - 1

    /**
     * 続きを検索できるかどうかの判断を行う
     * （何らかの負荷で検索が完了していない場合、安全側に倒しておく）
     */
    val canAppendResult: Boolean
        get() = searchedPage * perPage < searchResult.totalCount

    fun isEmpty(): Boolean {
        return repositoryList.isEmpty()
    }

    /**
     * 検索結果を現在のデータに追記する
     *
     * @param nextPageResult 追記する対象の検索結果
     */
    fun appendResult(nextPageResult: RepositorySearchResult) {
        searchResult = nextPageResult
        repositoryList.addAll(searchResult.searchedItemList)
        nextPage++
    }
}

/**
 * 主に初期状態を示すのに使う、空の検索結果のインスタンスを作成する
 */
fun createDefaultResultList(): AppendableRepositoryList {
    return AppendableRepositoryList(RepositorySearchResult(listOf(), 0))
}
