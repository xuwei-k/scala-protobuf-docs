# セットアップ

sbt-protoc自体は、あくまでも通常のsbt pluginなので、他のsbt pluginと同様、
まずは`project/plugin.sbt`[^plugin-sbt]に以下のように`addSbtPlugin`で追加します。
sbtのversionは、これを書いている現在の下記のversionでは、0.13.xと1.x系の両方をサポートしています。
ただし、scalapbとsbt-protocは独立しているため、scalapbのcompilerpluginのモジュールを、
libraryDependenciesとして追加するという、普通のsbt pluginとは違う少し変わった設定も必要です。

```tut:invisible
import sbt._, Keys._

import sbtprotoc.ProtocPlugin.autoImport._
```

```tut:silent
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.20")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.9.0-M3"
```

Windowsの場合、sbt-protoc 0.99.15より古いversionではローカルにPython(2.x系)のインストールが必要です。[^python-version]
MacやLinuxではPythonは必要ありません。
古いversionのsbt-protocを使う場合はPythonのインストール後、Pathを通すか、以下のように [`pythonExe`](https://github.com/thesamet/sbt-protoc/blob/v0.99.15/src/main/scala/sbtprotoc/ProtocPlugin.scala#L26) というkeyを設定してください。

```tut:silent
PB.pythonExe := "C:\\Python27\\Python.exe" // あくまで例なので、インストールした場所を指定
```

次に`build.sbt`への設定の説明をします。

```tut:silent
import scalapb.compiler.Version.protobufVersion

PB.targets in Compile := Seq(
  PB.gens.java(protobufVersion) -> ((sourceManaged in Compile).value / "protobuf-java"),
  scalapb.gen(javaConversions=true) -> ((sourceManaged in Compile).value / "protobuf-scala")
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
[^python-version]: Pythonのversion3系では動かない可能性があるため、2系を入れてください。どのversionで動くかの詳細は把握できていませんが、少なくとも2.7で動いた例があります。
