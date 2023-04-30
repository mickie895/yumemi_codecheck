package jp.co.yumemi.android.codecheck.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * 動作確認を兼ねたテスト
 */
class MoshiParseTest {

    private lateinit var jsonAdapter: JsonAdapter<RepositorySearchResult>
    private lateinit var moshi: Moshi

    @Before
    fun prepareAdapter() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        jsonAdapter = moshi.adapter(RepositorySearchResult::class.java)
    }

    /**
     * ライブラリの動作確認用テスト
     */
    @Test
    fun parseTest() {
        val searchResult = jsonAdapter.fromJson(sampleApiResult)

        Assert.assertNotNull("正常時のアダプタの動作チェック", searchResult)

        // gitignoreは言語設定が無い
        val gitignoreRepository = searchResult!!.searchedItemList[0]
        Assert.assertNull("言語指定はnullが入る可能性がある", gitignoreRepository.language)

        // 適当な言語が設定されているリポジトリ
        val sampleRepository = searchResult.searchedItemList[1]
        Assert.assertNotNull("言語があることを確認", sampleRepository.language)

        val emptySearchResult = jsonAdapter.fromJson(emptyApiResult)
        Assert.assertEquals("空配列の確認", 0, emptySearchResult!!.searchedItemList.count())
    }

    @Test
    fun parseErrorTest() {
        Assert.assertThrows("不正な配列が入っていたときの動作確認", JsonDataException::class.java) {
            jsonAdapter.fromJson(sampleErrorResult)
        }
    }
}

// 適当なタイミングで「git」で調べたときの検索結果
const val sampleApiResult = """
        {
  "total_count": 7653556,
  "incomplete_results": true,
  "items": [
    {
      "id": 1062897,
      "node_id": "MDEwOlJlcG9zaXRvcnkxMDYyODk3",
      "name": "gitignore",
      "full_name": "github/gitignore",
      "private": false,
      "owner": {
        "login": "github",
        "id": 9919,
        "node_id": "MDEyOk9yZ2FuaXphdGlvbjk5MTk=",
        "avatar_url": "https://avatars.githubusercontent.com/u/9919?v=4",
        "gravatar_id": "",
        "url": "https://api.github.com/users/github",
        "html_url": "https://github.com/github",
        "followers_url": "https://api.github.com/users/github/followers",
        "following_url": "https://api.github.com/users/github/following{/other_user}",
        "gists_url": "https://api.github.com/users/github/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/github/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/github/subscriptions",
        "organizations_url": "https://api.github.com/users/github/orgs",
        "repos_url": "https://api.github.com/users/github/repos",
        "events_url": "https://api.github.com/users/github/events{/privacy}",
        "received_events_url": "https://api.github.com/users/github/received_events",
        "type": "Organization",
        "site_admin": false
      },
      "html_url": "https://github.com/github/gitignore",
      "description": "A collection of useful .gitignore templates",
      "fork": false,
      "url": "https://api.github.com/repos/github/gitignore",
      "forks_url": "https://api.github.com/repos/github/gitignore/forks",
      "keys_url": "https://api.github.com/repos/github/gitignore/keys{/key_id}",
      "collaborators_url": "https://api.github.com/repos/github/gitignore/collaborators{/collaborator}",
      "teams_url": "https://api.github.com/repos/github/gitignore/teams",
      "hooks_url": "https://api.github.com/repos/github/gitignore/hooks",
      "issue_events_url": "https://api.github.com/repos/github/gitignore/issues/events{/number}",
      "events_url": "https://api.github.com/repos/github/gitignore/events",
      "assignees_url": "https://api.github.com/repos/github/gitignore/assignees{/user}",
      "branches_url": "https://api.github.com/repos/github/gitignore/branches{/branch}",
      "tags_url": "https://api.github.com/repos/github/gitignore/tags",
      "blobs_url": "https://api.github.com/repos/github/gitignore/git/blobs{/sha}",
      "git_tags_url": "https://api.github.com/repos/github/gitignore/git/tags{/sha}",
      "git_refs_url": "https://api.github.com/repos/github/gitignore/git/refs{/sha}",
      "trees_url": "https://api.github.com/repos/github/gitignore/git/trees{/sha}",
      "statuses_url": "https://api.github.com/repos/github/gitignore/statuses/{sha}",
      "languages_url": "https://api.github.com/repos/github/gitignore/languages",
      "stargazers_url": "https://api.github.com/repos/github/gitignore/stargazers",
      "contributors_url": "https://api.github.com/repos/github/gitignore/contributors",
      "subscribers_url": "https://api.github.com/repos/github/gitignore/subscribers",
      "subscription_url": "https://api.github.com/repos/github/gitignore/subscription",
      "commits_url": "https://api.github.com/repos/github/gitignore/commits{/sha}",
      "git_commits_url": "https://api.github.com/repos/github/gitignore/git/commits{/sha}",
      "comments_url": "https://api.github.com/repos/github/gitignore/comments{/number}",
      "issue_comment_url": "https://api.github.com/repos/github/gitignore/issues/comments{/number}",
      "contents_url": "https://api.github.com/repos/github/gitignore/contents/{+path}",
      "compare_url": "https://api.github.com/repos/github/gitignore/compare/{base}...{head}",
      "merges_url": "https://api.github.com/repos/github/gitignore/merges",
      "archive_url": "https://api.github.com/repos/github/gitignore/{archive_format}{/ref}",
      "downloads_url": "https://api.github.com/repos/github/gitignore/downloads",
      "issues_url": "https://api.github.com/repos/github/gitignore/issues{/number}",
      "pulls_url": "https://api.github.com/repos/github/gitignore/pulls{/number}",
      "milestones_url": "https://api.github.com/repos/github/gitignore/milestones{/number}",
      "notifications_url": "https://api.github.com/repos/github/gitignore/notifications{?since,all,participating}",
      "labels_url": "https://api.github.com/repos/github/gitignore/labels{/name}",
      "releases_url": "https://api.github.com/repos/github/gitignore/releases{/id}",
      "deployments_url": "https://api.github.com/repos/github/gitignore/deployments",
      "created_at": "2010-11-08T20:17:14Z",
      "updated_at": "2023-02-04T16:41:36Z",
      "pushed_at": "2023-02-01T18:33:03Z",
      "git_url": "git://github.com/github/gitignore.git",
      "ssh_url": "git@github.com:github/gitignore.git",
      "clone_url": "https://github.com/github/gitignore.git",
      "svn_url": "https://github.com/github/gitignore",
      "homepage": "",
      "size": 2356,
      "stargazers_count": 143783,
      "watchers_count": 143783,
      "language": null,
      "has_issues": false,
      "has_projects": false,
      "has_downloads": true,
      "has_wiki": false,
      "has_pages": false,
      "has_discussions": false,
      "forks_count": 81984,
      "mirror_url": null,
      "archived": false,
      "disabled": false,
      "open_issues_count": 376,
      "license": {
        "key": "cc0-1.0",
        "name": "Creative Commons Zero v1.0 Universal",
        "spdx_id": "CC0-1.0",
        "url": "https://api.github.com/licenses/cc0-1.0",
        "node_id": "MDc6TGljZW5zZTY="
      },
      "allow_forking": true,
      "is_template": false,
      "web_commit_signoff_required": false,
      "topics": [
        "git",
        "gitignore"
      ],
      "visibility": "public",
      "forks": 81984,
      "open_issues": 376,
      "watchers": 143783,
      "default_branch": "main",
      "score": 1.0
    },
    {
      "id": 817345,
      "node_id": "MDEwOlJlcG9zaXRvcnk4MTczNDU=",
      "name": "git-extras",
      "full_name": "tj/git-extras",
      "private": false,
      "owner": {
        "login": "tj",
        "id": 25254,
        "node_id": "MDQ6VXNlcjI1MjU0",
        "avatar_url": "https://avatars.githubusercontent.com/u/25254?v=4",
        "gravatar_id": "",
        "url": "https://api.github.com/users/tj",
        "html_url": "https://github.com/tj",
        "followers_url": "https://api.github.com/users/tj/followers",
        "following_url": "https://api.github.com/users/tj/following{/other_user}",
        "gists_url": "https://api.github.com/users/tj/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/tj/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/tj/subscriptions",
        "organizations_url": "https://api.github.com/users/tj/orgs",
        "repos_url": "https://api.github.com/users/tj/repos",
        "events_url": "https://api.github.com/users/tj/events{/privacy}",
        "received_events_url": "https://api.github.com/users/tj/received_events",
        "type": "User",
        "site_admin": false
      },
      "html_url": "https://github.com/tj/git-extras",
      "description": "GIT utilities -- repo summary, repl, changelog population, author commit percentages and more",
      "fork": false,
      "url": "https://api.github.com/repos/tj/git-extras",
      "forks_url": "https://api.github.com/repos/tj/git-extras/forks",
      "keys_url": "https://api.github.com/repos/tj/git-extras/keys{/key_id}",
      "collaborators_url": "https://api.github.com/repos/tj/git-extras/collaborators{/collaborator}",
      "teams_url": "https://api.github.com/repos/tj/git-extras/teams",
      "hooks_url": "https://api.github.com/repos/tj/git-extras/hooks",
      "issue_events_url": "https://api.github.com/repos/tj/git-extras/issues/events{/number}",
      "events_url": "https://api.github.com/repos/tj/git-extras/events",
      "assignees_url": "https://api.github.com/repos/tj/git-extras/assignees{/user}",
      "branches_url": "https://api.github.com/repos/tj/git-extras/branches{/branch}",
      "tags_url": "https://api.github.com/repos/tj/git-extras/tags",
      "blobs_url": "https://api.github.com/repos/tj/git-extras/git/blobs{/sha}",
      "git_tags_url": "https://api.github.com/repos/tj/git-extras/git/tags{/sha}",
      "git_refs_url": "https://api.github.com/repos/tj/git-extras/git/refs{/sha}",
      "trees_url": "https://api.github.com/repos/tj/git-extras/git/trees{/sha}",
      "statuses_url": "https://api.github.com/repos/tj/git-extras/statuses/{sha}",
      "languages_url": "https://api.github.com/repos/tj/git-extras/languages",
      "stargazers_url": "https://api.github.com/repos/tj/git-extras/stargazers",
      "contributors_url": "https://api.github.com/repos/tj/git-extras/contributors",
      "subscribers_url": "https://api.github.com/repos/tj/git-extras/subscribers",
      "subscription_url": "https://api.github.com/repos/tj/git-extras/subscription",
      "commits_url": "https://api.github.com/repos/tj/git-extras/commits{/sha}",
      "git_commits_url": "https://api.github.com/repos/tj/git-extras/git/commits{/sha}",
      "comments_url": "https://api.github.com/repos/tj/git-extras/comments{/number}",
      "issue_comment_url": "https://api.github.com/repos/tj/git-extras/issues/comments{/number}",
      "contents_url": "https://api.github.com/repos/tj/git-extras/contents/{+path}",
      "compare_url": "https://api.github.com/repos/tj/git-extras/compare/{base}...{head}",
      "merges_url": "https://api.github.com/repos/tj/git-extras/merges",
      "archive_url": "https://api.github.com/repos/tj/git-extras/{archive_format}{/ref}",
      "downloads_url": "https://api.github.com/repos/tj/git-extras/downloads",
      "issues_url": "https://api.github.com/repos/tj/git-extras/issues{/number}",
      "pulls_url": "https://api.github.com/repos/tj/git-extras/pulls{/number}",
      "milestones_url": "https://api.github.com/repos/tj/git-extras/milestones{/number}",
      "notifications_url": "https://api.github.com/repos/tj/git-extras/notifications{?since,all,participating}",
      "labels_url": "https://api.github.com/repos/tj/git-extras/labels{/name}",
      "releases_url": "https://api.github.com/repos/tj/git-extras/releases{/id}",
      "deployments_url": "https://api.github.com/repos/tj/git-extras/deployments",
      "created_at": "2010-08-04T16:32:07Z",
      "updated_at": "2023-02-04T09:50:03Z",
      "pushed_at": "2023-02-03T04:22:09Z",
      "git_url": "git://github.com/tj/git-extras.git",
      "ssh_url": "git@github.com:tj/git-extras.git",
      "clone_url": "https://github.com/tj/git-extras.git",
      "svn_url": "https://github.com/tj/git-extras",
      "homepage": "",
      "size": 1913,
      "stargazers_count": 16139,
      "watchers_count": 16139,
      "language": "Shell",
      "has_issues": true,
      "has_projects": true,
      "has_downloads": true,
      "has_wiki": true,
      "has_pages": true,
      "has_discussions": false,
      "forks_count": 1215,
      "mirror_url": null,
      "archived": false,
      "disabled": false,
      "open_issues_count": 128,
      "license": {
        "key": "mit",
        "name": "MIT License",
        "spdx_id": "MIT",
        "url": "https://api.github.com/licenses/mit",
        "node_id": "MDc6TGljZW5zZTEz"
      },
      "allow_forking": true,
      "is_template": false,
      "web_commit_signoff_required": false,
      "topics": [
        "git"
      ],
      "visibility": "public",
      "forks": 1215,
      "open_issues": 128,
      "watchers": 16139,
      "default_branch": "master",
      "score": 1.0
    },
    {
      "id": 1614410,
      "node_id": "MDEwOlJlcG9zaXRvcnkxNjE0NDEw",
      "name": "FFmpeg",
      "full_name": "FFmpeg/FFmpeg",
      "private": false,
      "owner": {
        "login": "FFmpeg",
        "id": 729418,
        "node_id": "MDEyOk9yZ2FuaXphdGlvbjcyOTQxOA==",
        "avatar_url": "https://avatars.githubusercontent.com/u/729418?v=4",
        "gravatar_id": "",
        "url": "https://api.github.com/users/FFmpeg",
        "html_url": "https://github.com/FFmpeg",
        "followers_url": "https://api.github.com/users/FFmpeg/followers",
        "following_url": "https://api.github.com/users/FFmpeg/following{/other_user}",
        "gists_url": "https://api.github.com/users/FFmpeg/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/FFmpeg/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/FFmpeg/subscriptions",
        "organizations_url": "https://api.github.com/users/FFmpeg/orgs",
        "repos_url": "https://api.github.com/users/FFmpeg/repos",
        "events_url": "https://api.github.com/users/FFmpeg/events{/privacy}",
        "received_events_url": "https://api.github.com/users/FFmpeg/received_events",
        "type": "Organization",
        "site_admin": false
      },
      "html_url": "https://github.com/FFmpeg/FFmpeg",
      "description": "Mirror of https://git.ffmpeg.org/ffmpeg.git",
      "fork": false,
      "url": "https://api.github.com/repos/FFmpeg/FFmpeg",
      "forks_url": "https://api.github.com/repos/FFmpeg/FFmpeg/forks",
      "keys_url": "https://api.github.com/repos/FFmpeg/FFmpeg/keys{/key_id}",
      "collaborators_url": "https://api.github.com/repos/FFmpeg/FFmpeg/collaborators{/collaborator}",
      "teams_url": "https://api.github.com/repos/FFmpeg/FFmpeg/teams",
      "hooks_url": "https://api.github.com/repos/FFmpeg/FFmpeg/hooks",
      "issue_events_url": "https://api.github.com/repos/FFmpeg/FFmpeg/issues/events{/number}",
      "events_url": "https://api.github.com/repos/FFmpeg/FFmpeg/events",
      "assignees_url": "https://api.github.com/repos/FFmpeg/FFmpeg/assignees{/user}",
      "branches_url": "https://api.github.com/repos/FFmpeg/FFmpeg/branches{/branch}",
      "tags_url": "https://api.github.com/repos/FFmpeg/FFmpeg/tags",
      "blobs_url": "https://api.github.com/repos/FFmpeg/FFmpeg/git/blobs{/sha}",
      "git_tags_url": "https://api.github.com/repos/FFmpeg/FFmpeg/git/tags{/sha}",
      "git_refs_url": "https://api.github.com/repos/FFmpeg/FFmpeg/git/refs{/sha}",
      "trees_url": "https://api.github.com/repos/FFmpeg/FFmpeg/git/trees{/sha}",
      "statuses_url": "https://api.github.com/repos/FFmpeg/FFmpeg/statuses/{sha}",
      "languages_url": "https://api.github.com/repos/FFmpeg/FFmpeg/languages",
      "stargazers_url": "https://api.github.com/repos/FFmpeg/FFmpeg/stargazers",
      "contributors_url": "https://api.github.com/repos/FFmpeg/FFmpeg/contributors",
      "subscribers_url": "https://api.github.com/repos/FFmpeg/FFmpeg/subscribers",
      "subscription_url": "https://api.github.com/repos/FFmpeg/FFmpeg/subscription",
      "commits_url": "https://api.github.com/repos/FFmpeg/FFmpeg/commits{/sha}",
      "git_commits_url": "https://api.github.com/repos/FFmpeg/FFmpeg/git/commits{/sha}",
      "comments_url": "https://api.github.com/repos/FFmpeg/FFmpeg/comments{/number}",
      "issue_comment_url": "https://api.github.com/repos/FFmpeg/FFmpeg/issues/comments{/number}",
      "contents_url": "https://api.github.com/repos/FFmpeg/FFmpeg/contents/{+path}",
      "compare_url": "https://api.github.com/repos/FFmpeg/FFmpeg/compare/{base}...{head}",
      "merges_url": "https://api.github.com/repos/FFmpeg/FFmpeg/merges",
      "archive_url": "https://api.github.com/repos/FFmpeg/FFmpeg/{archive_format}{/ref}",
      "downloads_url": "https://api.github.com/repos/FFmpeg/FFmpeg/downloads",
      "issues_url": "https://api.github.com/repos/FFmpeg/FFmpeg/issues{/number}",
      "pulls_url": "https://api.github.com/repos/FFmpeg/FFmpeg/pulls{/number}",
      "milestones_url": "https://api.github.com/repos/FFmpeg/FFmpeg/milestones{/number}",
      "notifications_url": "https://api.github.com/repos/FFmpeg/FFmpeg/notifications{?since,all,participating}",
      "labels_url": "https://api.github.com/repos/FFmpeg/FFmpeg/labels{/name}",
      "releases_url": "https://api.github.com/repos/FFmpeg/FFmpeg/releases{/id}",
      "deployments_url": "https://api.github.com/repos/FFmpeg/FFmpeg/deployments",
      "created_at": "2011-04-14T14:12:38Z",
      "updated_at": "2023-02-04T16:13:01Z",
      "pushed_at": "2023-02-04T17:15:05Z",
      "git_url": "git://github.com/FFmpeg/FFmpeg.git",
      "ssh_url": "git@github.com:FFmpeg/FFmpeg.git",
      "clone_url": "https://github.com/FFmpeg/FFmpeg.git",
      "svn_url": "https://github.com/FFmpeg/FFmpeg",
      "homepage": "https://ffmpeg.org/",
      "size": 344322,
      "stargazers_count": 33913,
      "watchers_count": 33913,
      "language": "C",
      "has_issues": false,
      "has_projects": false,
      "has_downloads": true,
      "has_wiki": false,
      "has_pages": false,
      "has_discussions": false,
      "forks_count": 10518,
      "mirror_url": null,
      "archived": false,
      "disabled": false,
      "open_issues_count": 3,
      "license": {
        "key": "other",
        "name": "Other",
        "spdx_id": "NOASSERTION",
        "url": null,
        "node_id": "MDc6TGljZW5zZTA="
      },
      "allow_forking": true,
      "is_template": false,
      "web_commit_signoff_required": false,
      "topics": [
        "audio",
        "c",
        "ffmpeg",
        "fft",
        "hevc",
        "hls",
        "matroska",
        "mp4",
        "mpeg",
        "multimedia",
        "rtmp",
        "rtsp",
        "streaming",
        "video",
        "webm"
      ],
      "visibility": "public",
      "forks": 10518,
      "open_issues": 3,
      "watchers": 33913,
      "default_branch": "master",
      "score": 1.0
    }
    ]
  }
    """

// 空文字列で検索させ、強制的にエラーを吐かせたときの検索結果
const val sampleErrorResult = """
{
  "message": "Validation Failed",
  "errors": [
    {
      "resource": "Search",
      "field": "q",
      "code": "missing"
    }
  ],
  "documentation_url": "https://docs.github.com/v3/search"
}
"""

// 検索結果が存在しなかったときの検索結果（※自作）
const val emptyApiResult = """
        {
  "total_count": 0,
  "incomplete_results": false,
  "items": [
    ]
  }
    """
