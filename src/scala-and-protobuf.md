# ScalaにおけるProtocol Buffer事情

この資料では[ScalaPB](https://github.com/scalapb/ScalaPB)というsbt-pluginを使用します。
現在、Scalaでは、いくつかのprotocol-bufferに関するsbt pluginやライブラリがあります。

- https://github.com/sbt/sbt-scalabuff
- https://github.com/sbt/sbt-protobuf
- https://github.com/scalapb/sbt-scalapb
- https://github.com/finagle/finagle-protobuf
- https://github.com/SandroGrzicic/ScalaBuff
- https://github.com/Atry/sbt-cppp


なぜ他のものではなくScalaPBがオススメなのか?というのを簡単に説明します。
このページの内容そのものは、とにかくすぐにScalaPBを使いたい！という人にとっては必要ないものなので、
そういう人はこのページは読み飛ばしてもらって構いません。

まずScalaPB以外のものについてですが

- [sbt-scalabuff](https://github.com/sbt/sbt-scalabuff)
 - メンテされてない？
 - 名前ややこしいけれど、sbt-protobufとは別物です

- [finagle-protobuf](https://github.com/finagle/finagle-protobuf)
 - finagleと組み合わせないかぎり必要無さそう
 - そもそもメンテが活発ではない？

- [ScalaBuff](https://github.com/SandroGrzicic/ScalaBuff)
 - parserが独自である[^ScalaBuff-parser]
   - これは欠点でも利点でもあるかもしれない [^protobuf-parser]
   - ScalaPBは、公式のparser[^protobuf-parser-cpp]を使用している
 - protocol bufferのversion 3の対応状況が不明(詳細未調査)
 - serviceに対応していない(?)
   - 機能追加するには、おそらくparserそのものから書かないといけない

- [sbt-protobuf](https://github.com/sbt/sbt-protobuf)
 - `protoc`[^protoc]を呼びだす薄いラッパー
 - これ単体ではScalaのクラスのコード生成機能はない
   - つまりこれ単体で使う場合は、Javaコードを生成して使う、ということ
 - しかし、ScalaPBはこれに依存している(後述)
 - 最近[@xuwei-k](https://github.com/xuwei-k)はこれの[コミッターになりました](http://d.hatena.ne.jp/xuwei/20160114/1452790299)

- [sbt-cppp](https://github.com/Atry/sbt-cppp)
 - これもprotocを呼び出すのみで、Scalaコード生成機能なし
 - それほどメンテ活発ではない？

- [scala-protobuf](https://github.com/jeffplaisance/scala-protobuf)
 - メンテされてない

- [protobuf-scala](https://code.google.com/p/protobuf-scala)
 - メンテされてない

一方ScalaPBですが

- IDLをparseする際に独自のparserを使用していない
 - protobufにはpluginという仕組みがあり、任意の言語でparse済みのASTのようなものを受け取れる仕組み[^protobuf-plugin]があり、ScalaPBはそれを利用している
 - parser部分のバグや互換性を気にしなくてすむという利点
- version3に対応している
- いくつかの便利なオプション 
 - Javaとの相互変換のメソッド生成機能
 - 継承するtraitを追加する機能
 - 独自のclassに自動でマッピングする機能
- ScalaCheck[^scalacheck]でテストされている
- Lensが自動生成される
- [gRPCに対応している](https://github.com/scalapb/ScalaPB/issues/44)

というような理由から、これ以降はScalaPB前提で話を進めます


[^ScalaBuff-parser]: 具体的には、普通にScala標準のparser combinatorを使っている https://github.com/SandroGrzicic/ScalaBuff/blob/1.4.0/project/ScalaBuffBuild.scala#L52
[^protobuf-parser]: protocol bufferの仕様のページはあまり厳密に書かれておらず、ちゃんと実装しようとするとC++の実装コードを読まないとならないため、必要ない限り独自にparserを書くべきではないと思われる https://developers.google.com/protocol-buffers/docs/reference/proto3-spec http://d.hatena.ne.jp/xuwei/20151012/1444617489
[^protobuf-parser-cpp]: 公式のparserはC++で書かれている https://github.com/google/protobuf/blob/v3.0.0/src/google/protobuf/compiler/parser.cc
[^protoc]: ここで言う`protoc`とは、protocol bufferの公式のコンパイラのこと。C++で書かれており、通常はmakeなどを使ってローカルにインストールしないといけない。
[^protobuf-plugin]: protocol bufferのpluginに関しての詳細はここを参照 https://github.com/google/protobuf/blob/v3.0.0/src/google/protobuf/compiler/plugin.proto CodeGeneratorRequestを標準入力から受け取り、CodeGeneratorResponseを返す、というインターフェイス
[^scalacheck]: https://www.scalacheck.org/ property based testingと呼ばれる、テストデータを自動生成してテストする方式。あくまで、ScalaPB自体のテストにつかわれているだけなので、ScalaPBを単に使う側からすると直接関係はない。
