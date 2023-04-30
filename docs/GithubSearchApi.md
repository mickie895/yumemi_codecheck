# GithubApi調査

下記アドレスの「応答スキーマ」からパラメータの確認を行う
https://docs.github.com/ja/rest/search?apiVersion=2022-11-28#search-repositories

## リポジトリ検索結果要約

- total_count
  - 全件数
  - 一度にAPIで取得できる件数は限られている
  - 今まで取得できた件数より多かったとき、検索結果には続きがあることになる（追加する方法を考える）
- incomplete_results
  - 負荷がかかっていたときなどにtrueになる
  - 取得件数のカウントがずれるため、trueだったときは続きの取得は避けたほうが良さそうに思える
- items
  - 文字通り検索結果の中身

## items要約

- リポジトリ名
  - full_name
  - 文字列型のみ
- オーナーアイコン
  - owner.avatar_url
  - 文字列型のみ入ってくるため、必ず画像のURLが入っているとみなして良さそう
- プロジェクト言語
  - language
  - 文字列型、もしくはnullも入る
- Star数
  - stargazers_count
  - 数値型のみ
- Watcher数
  - watchers_count
  - 数値型のみ
- Fork数
  - forks_count
  - 数値型のみ
- Issue数
  - open_issues_count
  - 数値型のみ
  - （蛇足だがプルリクエストの数も入っている。）
