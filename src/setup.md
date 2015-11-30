# セットアップ

sbt-scalapb自体は、あくまでも通常のsbt pluginなので、他のsbt pluginと同様、
まずは`project/plugin.sbt`[^plugin-sbt]に以下のように`addSbtPlugin`で追加します。

```tut:invisible
import sbt._, Keys._
```

```tut:silent
addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.15")
```


ただし、[ScalaPBと関連ライブラリやツール](scalapb-and-libraries.html)のページで説明したように、
[protoc-jar](https://github.com/os72/protoc-jar)を使用したほうが便利なため、
protoc-jarの設定も`project/plugin.sbt`に追加します。


```tut:silent
addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.15")

libraryDependencies += "com.github.os72" % "protoc-jar" % "3.0.0-b1"
```


次に`build.sbt`への設定の説明をします。

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

PB.protobufSettings

PB.runProtoc in PB.protobufConfig := { args =>
  com.github.os72.protocjar.Protoc.runProtoc("-v300" +: args.toArray)
}

watchSources ++= (((sourceDirectory in Compile).value / "protobuf") ** "*.proto").get
```

- importの`ScalaPbPlugin => PB`の部分は、ScalaPBのpluginが別のsbt pluginに依存している[^scalapb-sbt-key]という少し変わった構成になっている都合上、そのままでは参照できないため必要
- `PB.protobufSettings`は、必ず必要な設定
- `PB.runProtoc in PB.protobufConfig`は、protoc-jarを使用する場合に必要な設定。ローカルにprotocをインストールして、それを使用する場合は必要ない
- `"-v300"`というのはprotocol bufferのversion 3を使用する、という意味。その他のversionを使用したい場合はprotoc-jarの公式ドキュメントを参照してください。 https://github.com/os72/protoc-jar
- `watchSources`は、`~compile`などでファイル変更をトリガーにしてコンパイルやテストをする機能がsbtにあるが、その監視対象にprotocol bufferのファイルを追加する設定。ファイル監視をトリガーにしてコンパイルする機能を使わないならば設定は必要ない


[^plugin-sbt]: これはsbtの一般的な話ですが、`plugin.sbt`というファイル名は単なる慣習であり、`project/`ディレクトリの下で`.sbt`という拡張子ならば、`a.sbt`や`foo.sbt`など、どんな名前でも構いません
[^scalapb-sbt-key]: なおかつ、Keyの名前が衝突している
