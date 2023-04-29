package jp.co.yumemi.android.codecheck.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryProperty(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
