# sbtの設定のカスタマイズ

このページでは、基本的な使い方以外のsbtの設定について説明します。

## コンパイル対象のprotobufディレクトリを追加

以下のように `sourceDirectories in PB.protobufConfig` に追加します。[^src-dir-def]

```tut:invisible
import sbt._, Keys._
```

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

sourceDirectories in PB.protobufConfig += file("新たにコンパイル対象に追加したいディレクトリへのpath")
```


## 参照するprotobufディレクトリを追加

protocの`-I`の引数に相当するものです。それほど多くないパターンかもしれませんが、つまり

- すでにScalaPBによって生成済みのScalaコードと、そのScalaコードをコンパイルしたものが存在[^include]
- そのScalaコードの生成元となった`.proto`のメッセージの定義などを参照しつつ、それに依存したさらに別のメッセージやサービスを定義してScalaコード生成をしたい

というような場合です。設定方法は、以下のように`includePaths`というKeyに対してディレクトリを追加します。

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

PB.includePaths in PB.protobufConfig += file("参照したいprotoファイルが置いてあるディレクトリ")
```

`includePaths`に限らず、おそらく他の`protoc`に渡されるkeyに関しても共通ですが、
古いScalaPBでは`.getCanonicalFile` の呼び出しが必要になる可能性があります[^getCanonicalFile]。

## コンパイルする`.proto`ファイルのフィルター

以下のように`excludeFilter`というkeyを設定すると、特定の`.proto`ファイルをコンパイル対象から除外することが可能です。
[^exclude]

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

excludeFilter in PB.protobufConfig := {
  // ここにfilterの定義を書く
  "*Foo.proto"
}
```

`excludeFilter`は`protobufConfig`のスコープになっていますが、Key自体はsbtの標準であり`SettingKey[FileFilter]`という型です。

文字列を書くと、正規表現のようなものと認識されるimplicit defがあったり
- <https://github.com/sbt/sbt/blob/v0.13.12/util/io/src/main/scala/sbt/NameFilter.scala#L96-L119>

それ以外にもいろいろな種類のfilterが書けます。詳細は以下のコードなどを参考にしてください。

- <https://github.com/sbt/sbt/blob/v0.13.12/util/io/src/main/scala/sbt/NameFilter.scala>
- <https://github.com/sbt/sbt/blob/v0.13.12/main/src/main/scala/sbt/Keys.scala#L92>


## Javaのclassとの相互変換

以下のオプションを設定することで、`toJavaProto`, `fromJavaProto` などの、Javaのclassとの相互変換のためのメソッドがScalaのコンパニオンobjectに追加されます。
デフォルトはoffです。
もし何らかの理由でJavaのclassとの相互変換が必要になった場合は設定してください。

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

PB.javaConversions in PB.protobufConfig := true
```

## jarの中に`.proto`ファイルを含める

ScalaPBでコード生成したものを含んだものをライブラリとして提供する場合や、
コード生成をするsbtのモジュールが複数あってそれらに依存関係があるときは、
以下のような設定[^resource-proto]を追加しておき、リソースとして`.proto`ファイルを含めておくとよいでしょう。

```tut:silent
unmanagedResourceDirectories in Compile += (sourceDirectory in PB.protobufConfig).value
```

## jarの中にある`.proto`ファイルを参照する

`sbt-protobuf`のpluginには、自動でそのための機能が存在します。以下のように、参照したい`.proto`ファイルが含まれているjarを`% "protobuf"`をつけて`libraryDependencies`を書くだけです。

```tut:silent
libraryDependencies += "com.example" %% "example" % "0.1.0" % "protobuf"
```


[^src-dir-def]: 関連するsbt-protobufの定義場所 https://github.com/sbt/sbt-protobuf/blob/v0.5.1/src/main/scala/sbtprotobuf/ProtobufPlugin.scala#L22-L23
[^include]: 外部ライブラリとして、もしくはsbtのマルチプロジェクトの一部として、という意味
[^getCanonicalFile]: sbt plugin側で自動で呼び出せばいいと思ったので、pull requestしてmerge済みです。ScalaPB 0.5.21以降は必要ないはずです https://github.com/sbt/sbt-protobuf/pull/35
[^exclude]: ただしsbt-scalapbが依存しているsbt-protobufが、0.5.0以降である必要があります。sbt-scalapb 0.5.21以降対応済みのはずです https://github.com/trueaccord/sbt-scalapb/pull/8 https://github.com/trueaccord/ScalaPB/issues/24 https://github.com/sbt/sbt-protobuf/pull/29
[^resource-proto]: あくまで標準のprotoファイル用のディレクトリ以下のファイルを含める場合の設定例なので、標準以外の場所のprotoファイルを含めたい場合は設定を変えてください
