# Jsonとの相互変換

Protocol Buffersのversion 3からは、Jsonとの相互変換の規則が仕様として定められています。

<https://developers.google.com/protocol-buffers/docs/proto3#json>

このページでは、その方法について説明します。

現状ではJavaのProtocol Buffersのライブラリ経由で変換する方法しか存在しないため、その方法で説明します[^scalapb-64]。
まず、JavaのProtocol BuffersのJson変換部分には、追加で依存ライブラリが必要です[^gson]。
`build.sbt`に以下のように設定してください。

```tut:invisible
import sbt._, Keys._
import example.user.User
```

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

PB.javaConversions in PB.protobufConfig := true

libraryDependencies += "com.google.protobuf" % "protobuf-java-util" % "3.0.0-beta-2"
```

ScalaPBの`javaConversions`の設定は、一旦Javaのオブジェクトに変換する都合上必要なものです。

どちらの変換の場合も`com.google.protobuf.util.JsonFormat`というclassを使用するので、さきにそのソースコードへのリンクを貼っておきます。

https://github.com/google/protobuf/blob/v3.0.0-beta-2/java/util/src/main/java/com/google/protobuf/util/JsonFormat.java

また、Jsonとの相互変換するための例として、さきほど別のページで出したUserのcase classを使います。

## case classからJsonへ変換

```tut
import com.google.protobuf.util.JsonFormat

val user = example.user.User(id = 1, name = "foo")

val userJson = JsonFormat.printer.print(example.user.User.toJavaProto(user))
```

## Json文字列からcase classへの変換

```tut:silent
def jsonStringToUser(json: String): example.user.User = {
  val registry = JsonFormat.TypeRegistry.newBuilder().add(example.user.User.descriptor).build()
  val parser = JsonFormat.parser().usingTypeRegistry(registry)
  val builder = example.UserOuterClass.User.newBuilder()
  parser.merge(json, builder)
  example.user.User.fromJavaProto(builder.build())
}
```

```tut
jsonStringToUser(userJson)
```

[^scalapb-64]: 現在pull requestを送信済みで、もう少し簡単に変換できるようになる予定です https://github.com/trueaccord/ScalaPB/pull/64
[^gson]: このprotobuf-java-utilは、googleのgsonやguavaというライブラリに依存します。依存が衝突しないように注意してください http://repo1.maven.org/maven2/com/google/protobuf/protobuf-java-util/3.0.0-beta-2/protobuf-java-util-3.0.0-beta-2.pom
