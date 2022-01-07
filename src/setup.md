# セットアップ

sbt-protoc自体は、あくまでも通常のsbt pluginなので、他のsbt pluginと同様、
まずは`project/plugin.sbt`[^plugin-sbt]に以下のように`addSbtPlugin`で追加します。
sbtのversionは、これを書いている現在の下記のversionでは、0.13.xと1.x系の両方をサポートしています。
ただし、scalapbとsbt-protocは独立しているため、scalapbのcompilerpluginのモジュールを、
libraryDependenciesとして追加するという、普通のsbt pluginとは違う少し変わった設定も必要です。

```scala mdoc:invisible
import sbt._, Keys._

import sbtprotoc.ProtocPlugin.autoImport._
```

```scala mdoc:silent
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.6"
```

次に`build.sbt`への設定の説明をします。

```scala mdoc:silent
import scalapb.compiler.Version.protobufVersion

Compile / PB.targets := Seq(
  PB.gens.java(protobufVersion) -> ((Compile / sourceManaged).value / "protobuf-java"),
  scalapb.gen(javaConversions=true) -> ((Compile / sourceManaged).value / "protobuf-scala")
)
```

- sbt-protocは、[このような設定になっているため](https://github.com/thesamet/sbt-protoc/blob/v0.99.14/src/main/scala/sbtprotoc/ProtocPlugin.scala#L56) addSbtPlugin を設定しただけで、デフォルトで有効になります。
 - マルチプロジェクトの場合に、一部のプロジェクトだけで有効にしたい場合は、無効にしたいプロジェクトでdisablePluginsを呼び出してください
- sbt-protoc 0.99.5以前では、複数のScala versionでクロスビルドする場合に、微妙にバグがあるので注意してください <https://github.com/thesamet/sbt-protoc/pull/9>
- `PB.gens.java` は、Javaコードを生成する場合の設定です。Javaコードが必要ないならこの行はいりません
- `javaConversions=true` は、JavaのclassとScalaのclassの総合変換用メソッドを追加する場合です
- 古いversionとは色々と書き方が変わったので注意してください <https://scalapb.github.io/migrating.html>
- `(sourceManaged in Compile).value` だけでは、他のコード生成系のpluginと組み合わせた場合に不具合が生じるため、さらにサブディレクトリを指定しています <https://github.com/thesamet/sbt-protoc/issues/6>


[^plugin-sbt]: これはsbtの一般的な話ですが、`plugin.sbt`というファイル名は単なる慣習であり、`project/`ディレクトリの下で`.sbt`という拡張子ならば、`a.sbt`や`foo.sbt`など、どんな名前でも構いません
