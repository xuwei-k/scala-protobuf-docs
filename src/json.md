# Jsonとの相互変換

Protocol Buffersのversion 3からは、Jsonとの相互変換の規則が仕様として定められています。
仕様で定められているということは、どのプログラミング言語を使っているのか？に関係なく、使うライブラリなどが仕様通りに実装されていればその変換規則を使っているということで、相互運用性が上がるなどのメリットがあるので、その仕様に従うように(仕様に準拠しているライブラリを使うように)しましょう。

<https://developers.google.com/protocol-buffers/docs/proto3#json>

このページでは、その方法について説明します。

ここではJavaのProtocol Buffersのライブラリ経由で変換する方法で説明します[^scalapb-json]。
まず、JavaのProtocol BuffersのJson変換部分には、追加で依存ライブラリが必要です[^gson]。
`build.sbt`に以下のように設定してください。

```tut:invisible
import sbt._, Keys._
import example.user.User

import sbtprotoc.ProtocPlugin.autoImport._
```

```tut:silent
libraryDependencies += "com.google.protobuf" % "protobuf-java-util" % "3.5.1"
```

一旦Javaのオブジェクトに変換する都合上、`javaConversions`の設定も追加しておいてください。

どちらの変換の場合も`com.google.protobuf.util.JsonFormat`というclassを使用するので、さきにそのソースコードへのリンクを貼っておきます。

https://github.com/google/protobuf/blob/v3.5.1/java/util/src/main/java/com/google/protobuf/util/JsonFormat.java

また、Jsonとの相互変換するための例として、さきほど別のページで出したUserのcase classを使います。

## case classからJsonへ変換

```tut
import com.google.protobuf.util.JsonFormat

val user = example.user.User(id = 1, name = "foo")

val userJson = JsonFormat.printer.print(example.user.User.toJavaProto(user))
```

### 変換用の便利なclass

以下のようなclassを定義しておくと、pimp my libraryパターンで、
ScalaPBで生成されたどのcase classでも `toJsonString` というメソッド１つで変換できるようになるのでおすすめです。

```tut:silent
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.util.JsonFormat
import scalapb.JavaProtoSupport

implicit class GeneratedMessageOps[A](val self: A) extends AnyVal {
  def toJsonString[B <: MessageOrBuilder](implicit A: JavaProtoSupport[A, B]): String =
    JsonFormat.printer.print(A.toJavaProto(self))
}
```

```tut
User(id = 2, name = "bar").toJsonString
```

## Json文字列からcase classへの変換

```tut:silent
def jsonStringToUser(json: String): example.user.User = {
  val parser = JsonFormat.parser()
  val builder = example.UserOuterClass.User.newBuilder()
  parser.merge(json, builder)
  example.user.User.fromJavaProto(builder.build())
}
```

```tut
jsonStringToUser(userJson)
```

JSON に [Any](https://github.com/google/protobuf/blob/master/src/google/protobuf/any.proto) 型のメッセージが含まれている場合には、その Any で使われているメッセージの `Descriptor` を注入する必要があります。例えば、Any に `User` を格納している場合は以下のようにしてパーサーを構築します。[^type-registry]

```tut:silent
val registry = JsonFormat.TypeRegistry.newBuilder().add(example.user.User.javaDescriptor).build()
val parser = JsonFormat.parser().usingTypeRegistry(registry)
```

[^scalapb-json]: 2016年4月頃に https://github.com/scalapb/scalapb-json4s というものが出来ましたが、個人的にjson4sは非公式なリフレクションAPI使っていたりするなどの理由でお勧めしたくないので、説明しません。筆者がplay-jsonなど、他のjsonのライブラリを使った変換用のもの( https://github.com/scalapb-json/scalapb-playjson )は作っているので、もし使うなら scalapb-json4s よりも scalapb-playjson などのほうがおすすめです。(scalapb-playjsonがしっかりメンテナンスされている限りにおいて) https://xuwei-k.hatenablog.com/entry/2018/01/03/234858
[^gson]: このprotobuf-java-utilは、googleのgsonやguavaというライブラリに依存します。依存が衝突しないように注意してください https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java-util/3.5.1/protobuf-java-util-3.5.1.pom
[^type-registry]: あるメッセージの `Descriptor` を `TypeRegistry` に追加すると、そのメッセージが定義されている .proto ファイルと、その .proto ファイルが（直接・間接的に）インポートしている .proto ファイルに定義されている全てのメッセージの `Descriptor` が同時に追加されます。
