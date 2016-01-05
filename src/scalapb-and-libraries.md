# ScalaPBと関連ライブラリやツール

ScalaPB自体の説明に入るまえに、関連する依存ライブラリやプロジェクトについて説明します。


- [ScalaPB](https://github.com/trueaccord/ScalaPB)
 - ScalaPB本体のリポジトリ
 - protocol bufferの公式のparserからASTを受け取って、Scalaコードを生成する本体
- [sbt-scalapb](https://github.com/trueaccord/sbt-scalapb)
 - ScalaPBをsbtから使うためのplugin
 - ScalaPBを使う場合は、このpluginを設定することにより使うことになる
 - ScalaPBとsbt-scalapbは、リポジトリこそ別だが、作者も同じだし、リリースサイクルも基本的に同じようである[^scalapb-and-plugin]
- [Lenses](https://github.com/trueaccord/Lenses)
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
- [protobuf-scala-runtime](https://github.com/trueaccord/protobuf-scala-runtime)
 - 主に[scala-js](http://www.scala-js.org/)対応のために、一部の公式のprotobuf-java[^protobuf-java]のclassをScalaで再実装したもの
 - scala-jsでない場合は、公式のprotobufを使わずにこちらを使うメリットは特にないと思われるので、scala-jsを使わないなら通常必要ない


[^scalapb-and-plugin]: 単にクロスビルドやScalaのversionの都合上、リポジトリが分かれていたほうがいい、という判断だと思われる
[^lens]: つまり、使う必要がなければLensは使わなくてよいです
[^protoc]: protocol bufferの本体はC++で書かれているため
[^protobuf-java]: これのこと http://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/ https://github.com/google/protobuf/tree/v3.0.0-beta-2/java
