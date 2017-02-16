# sbtの設定のカスタマイズ

このページでは、基本的な使い方以外のsbtの設定について説明します。

## コンパイル対象のprotobufディレクトリを追加

以下のように `PB.protoSources` に追加します。

```tut:invisible
import sbt._, Keys._

import sbtprotoc.ProtocPlugin.autoImport._
```

```tut:silent
PB.protoSources in Compile += file("新たにコンパイル対象に追加したいディレクトリへのpath")
```


## 参照するprotobufディレクトリを追加

protocの`-I`の引数に相当するものです。それほど多くないパターンかもしれませんが、つまり

- すでにScalaPBによって生成済みのScalaコードと、そのScalaコードをコンパイルしたものが存在[^include]
- そのScalaコードの生成元となった`.proto`のメッセージの定義などを参照しつつ、それに依存したさらに別のメッセージやサービスを定義してScalaコード生成をしたい

というような場合です。設定方法は、以下のように`includePaths`というKeyに対してディレクトリを追加します。

```tut:silent
PB.includePaths in Compile += file("参照したいprotoファイルが置いてあるディレクトリ")
```

`includePaths`に限らず、おそらく他の`protoc`に渡されるkeyに関しても共通ですが、
古いScalaPBでは`.getCanonicalFile` の呼び出しが必要になる可能性があります[^getCanonicalFile]。

## コンパイルする`.proto`ファイルのフィルター

以下のように`excludeFilter`というkeyを設定すると、特定の`.proto`ファイルをコンパイル対象から除外することが可能です。
[^exclude]

```tut:silent
excludeFilter in PB.generate := {
  // ここにfilterの定義を書く
  "*Foo.proto"
}
```

`excludeFilter`は、Key自体はsbtの標準であり`SettingKey[FileFilter]`という型です。

文字列を書くと、正規表現のようなものと認識されるimplicit defがあったり
- <https://github.com/sbt/sbt/blob/v0.13.13/util/io/src/main/scala/sbt/NameFilter.scala#L96-L119>

それ以外にもいろいろな種類のfilterが書けます。詳細は以下のコードなどを参考にしてください。

- <https://github.com/sbt/sbt/blob/v0.13.13/util/io/src/main/scala/sbt/NameFilter.scala>
- <https://github.com/sbt/sbt/blob/v0.13.13/main/src/main/scala/sbt/Keys.scala#L93>

## jarの中に`.proto`ファイルを含める

ScalaPBでコード生成したものを含んだものをライブラリとして提供する場合や、
コード生成をするsbtのモジュールが複数あってそれらに依存関係があるときは、
以下のような設定[^resource-proto]を追加しておき、リソースとして`.proto`ファイルを含めておくとよいでしょう。

```tut:silent
unmanagedResourceDirectories in Compile ++= (PB.protoSources in Compile).value
```

## jarの中にある`.proto`ファイルを参照する

`sbt-protobuf`のpluginには、自動でそのための機能が存在します。以下のように、参照したい`.proto`ファイルが含まれているjarを`% "protobuf"`をつけて`libraryDependencies`を書くだけです。

```tut:silent
libraryDependencies += "com.example" %% "example" % "0.1.0" % "protobuf"
```


[^include]: 外部ライブラリとして、もしくはsbtのマルチプロジェクトの一部として、という意味
[^getCanonicalFile]: sbt plugin側で自動で呼び出せばいいと思ったので、pull requestしてmerge済みです。ScalaPB 0.5.21以降は必要ないはずです https://github.com/sbt/sbt-protobuf/pull/35
[^exclude]: ただしsbt-scalapbが依存しているsbt-protobufが、0.5.0以降である必要があります。sbt-scalapb 0.5.21以降対応済みのはずです https://github.com/scalapb/sbt-scalapb/pull/8 https://github.com/scalapb/ScalaPB/issues/24 https://github.com/sbt/sbt-protobuf/pull/29
[^resource-proto]: あくまで標準のprotoファイル用のディレクトリ以下のファイルを含める場合の設定例なので、標準以外の場所のprotoファイルを含めたい場合は設定を変えてください
