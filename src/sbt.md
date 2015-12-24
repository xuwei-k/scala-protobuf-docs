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

PB.includePaths in PB.protobufConfig += file("参照したいprotoファイルが置いてあるディレクトリ").getCanonicalFile
```

`includePaths`に限らず、おそらく他の`protoc`に渡されるkeyに関しても共通ですが、
`getCanonicalFile`を呼び出しておかないと`protoc`がうまく処理できないようなので注意してください[^getCanonicalFile]。


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
コード生成をするsbtのモジュールが複数あってそれらに依存関係がある場数などは、
以下のような設定[^resource-proto]を追加しておき、リソースとして`.proto`ファイルを含めておくとよいでしょう。

```tut:silent
unmanagedResourceDirectories in Compile += (sourceDirectory in PB.protobufConfig).value
```

## jarの中にある`.proto`ファイルを参照する

`sbt-protobuf`のpluginには、自動でそのための機能が存在します。以下のように、参照したい`.proto`ファイルが含まれているjarを`% "protobuf"`をつけて`libraryDependencies`を書くだけです。

```tut:silent
libraryDependencies += "com.example" %% "example" % "0.1.0" % "protobuf"
```


[^src-dir-def]: 関連するsbt-protobufの定義場所 https://github.com/sbt/sbt-protobuf/blob/v0.3.3/src/main/scala/sbtprotobuf/ProtobufPlugin.scala#L21-L22
[^include]: 外部ライブラリとして、もしくはsbtのマルチプロジェクトの一部として、という意味
[^getCanonicalFile]: sbt plugin側で自動で呼び出せばいいと思ったので、現在pull request中 https://github.com/sbt/sbt-protobuf/pull/35
[^resource-proto]: あくまで標準のprotoファイル用のディレクトリ以下のファイルを含める場合の設定例なので、標準以外の場所のprotoファイルを含めたい場合は設定を変えてください
