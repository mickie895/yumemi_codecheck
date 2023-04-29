package jp.co.yumemi.android.codecheck.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

/**
 * GitHubAPIからデータを取得するためのクラス
 */
class GithubApiRepository {
    /**
     * 与えられた文字列をもとに検索を行う
     */
    suspend fun searchQuery(query: String): String {
        // TODO: RESTクライアントライブラリの利用
        // TODO: エラーチェック（ネットワーク・GithubAPIの両方）
        val client = HttpClient(Android)

        val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", query)
        }

        return response.body()
    }
}