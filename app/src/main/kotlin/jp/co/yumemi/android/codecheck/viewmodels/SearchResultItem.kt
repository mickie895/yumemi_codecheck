package jp.co.yumemi.android.codecheck.viewmodels

import jp.co.yumemi.android.codecheck.data.search.RepositoryProperty

/**
 * 検索結果のリストに表示させることができる項目
 */
sealed interface SearchResultItem {
    /**
     * 検索結果のリポジトリ
     */
    class Repository(val repository: RepositoryProperty) : SearchResultItem

    /**
     * 続きを表示するための項目
     */
    object SearchNextItem : SearchResultItem

    /**
     * 空であることが示されている項目
     */
    object EmptyItem : SearchResultItem

    /**
     * 同じタイプのインスタンスかどうかのチェック
     */
    fun haveSameClass(other: SearchResultItem): Boolean {
        return when (this) {
            is Repository -> other is Repository
            else -> this == other
        }
    }

    /**
     * ２つの識別子が同じであることのチェック
     */
    fun isSameItem(other: SearchResultItem): Boolean {
        if (!haveSameClass(other)) {
            return false
        }

        if (this is Repository && other is Repository) {
            return this.repository.name == other.repository.name
        }

        // 消去法でtrueが確定する
        return true
    }
}
