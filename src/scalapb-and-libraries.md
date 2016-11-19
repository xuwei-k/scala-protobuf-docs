# ScalaPBと関連ライブラリやツール

ScalaPB自体の説明に入るまえに、関連する依存ライブラリやプロジェクトについて説明します。
できれば読んでおいたほうがいいですが、とにかくすぐ使いたい人は、このページをとばして次のセットアップのページに進んでください。


- [ScalaPB](https://github.com/scalapb/ScalaPB)
 - ScalaPB本体のリポジトリ
 - protocol bufferの公式のparserからASTを受け取って、Scalaコードを生成する本体
- [sbt-scalapb](https://github.com/scalapb/sbt-scalapb)
 - ScalaPBをsbtから使うためのplugin
 - ScalaPBを使う場合は、このpluginを設定することにより使うことになる
 - ScalaPBとsbt-scalapbは、リポジトリこそ別だが、作者も同じだし、リリースサイクルも基本的に同じようである[^scalapb-and-plugin]
- [Lenses](https://github.com/scalapb/Lenses)
 - ScalaPBが自動で生成するcase classがデフォルトで強制的に依存するライブラリ
 - Lensというのは、Haskell界隈などを中心に使われる関数型のテクニック
 - ネストしたcase classの一部を更新する場合などに便利
 - 実装は1ファイルだけで、百数十行程度でかなり少ない
 - 自動でLens関連のメソッドが追加されるが、**Lensを理解しないとScalaPBが使えないというわけではない**ので、あまり怖がる必要はない[^lens]
- [sbt-protobuf](https://github.com/sbt/sbt-protobuf)
 - sbt-scalapbが依存している
 - ScalaPBを普通に使う際には、このpluginそのものをあまり意識する必要はないはず
- [protoc-jar](https://github.com/os72/protoc-jar)
 - 環境ごとのprotocのバイナリを1つのjarにまとめたもの
 - これを使用せずにprotocをローカルにインストールしてもScalaPB自体は使用可能
   - しかし、makeなどの作業[^protoc]が必要になるため、基本これを使用したほうが楽になるはずなので、特に理由がなければこれを使用するべき
- [protobuf-scala-runtime](https://github.com/scalapb/protobuf-scala-runtime)
 - 主に[scala-js](http://www.scala-js.org/)対応のために、一部の公式のprotobuf-java[^protobuf-java]のclassをScalaで再実装したもの
 - scala-jsでない場合は、公式のprotobufを使わずにこちらを使うメリットは特にないと思われるので、scala-jsを使わないなら通常必要ない
- [fastparse](https://github.com/lihaoyi/fastparse)
 - version0.5.19以降実行時に依存
 - protobufには、text formatと呼ばれるデータの表現の形式があり、そのフォーマットをparseするためのもの
 - text formatを使用しない場合は関係ないので気にしなくてよい
 - ちなみにversion0.5.18以前では、一旦Javaのクラスのインスタンスを生成してScalaのクラスのインスタンスに変換していた。[^java-text-format-1] [^java-text-format-2]
- [protoc-bridge](https://github.com/scalapb/protoc-bridge)
 - version0.5.19くらいまでは[ScalaPBの本体リポジトリ](https://github.com/scalapb/ScalaPB)の中にあった部分
 - [多少汎用化されて別リポジトリになった](https://github.com/scalapb/ScalaPB/commit/ad6253260f474d)
 - 直接使うことはあまりないので、普通のユーザーは気にしなくてよい


[^scalapb-and-plugin]: 単にクロスビルドやScalaのversionの都合上、リポジトリが分かれていたほうがいい、という判断だと思われる
[^lens]: つまり、使う必要がなければLensは使わなくてよいです
[^protoc]: protocol bufferの本体はC++で書かれているため
[^protobuf-java]: これのこと http://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/ https://github.com/google/protobuf/tree/v3.0.0/java
[^java-text-format-1]: https://github.com/google/protobuf/blob/v3.0.0/java/core/src/main/java/com/google/protobuf/TextFormat.java
[^java-text-format-2]: https://github.com/scalapb/ScalaPB/commit/44af26eae9
