package jp.co.yumemi.android.codecheck.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * リポジトリ検索結果の格納用クラス
 *
 * Githubのリポジトリ検索結果はこのクラスの形で表現できる
 */
@JsonClass(generateAdapter = true)
data class RepositorySearchResult(
    @Json(name = "items")
    val searchedItemList: List<RepositoryProperty>,
    @Json(name = "total_count")
    val totalCount: Int,
)

/**
 * レポジトリを表現するためのクラス
 */
@Parcelize
@JsonClass(generateAdapter = false)
data class RepositoryProperty(
    @Json(name = "full_name")
    val name: String,
    @Json(name = "owner")
    val owner: RepositoryOwner,
    @Json(name = "language")
    val language: String?,
    @Json(name = "stargazers_count")
    val stargazersCount: Int,
    @Json(name = "watchers_count")
    val watchersCount: Int,
    @Json(name = "forks_count")
    val forksCount: Int,
    @Json(name = "open_issues_count")
    val openIssuesCount: Int,
) : Parcelable

/**
 * リポジトリの著者情報
 */
@Parcelize
@JsonClass(generateAdapter = false)
data class RepositoryOwner(
    @Json(name = "login")
    val ownerName: String,
    @Json(name = "html_url")
    val userPage: String,
    @Json(name = "avatar_url")
    val avatarIconUrl: String,
) : Parcelable
