# 基本的な使い方

セットアップの次に、ScalaPBの具体的な使用方法を説明していきます。
一番単純な使い方としては

- `src/main/protobuf/` のディレクトリ以下に`.proto`の拡張子のファイルを置く
- その後sbtで`compile`をするだけで、sbtが管理するディレクトリに自動でScalaコードが生成されるため、それを使ったコードを書く

という手順です。
"sbtが管理するディレクトリ"とは、具体的には`target/scala-2.11/src_managed/main/`
などです。
他の大抵のコード生成系のsbt pluginに共通することですが、このディレクトリはsbtが完全に自動で管理するものなので、
このディレクトリ以下のScalaファイルを直接編集することは想定されていません。
編集したとしても、生成元の`.proto`ファイルを編集してコンパイルをやり直すと、編集内容が上書きされます。


具体的に、生成されるコードを見てみましょう。
例えば、`src/main/protobuf/user.proto`というファイルに、
以下の様なint64のidとstringのnameを持ったUserというmessageを定義してコンパイルすると

[include](main/protobuf/user.proto)

大体以下の様なScalaコードが生成されます。[^generated-code]
いくつかメソッドがありますが、基本的には普通のcase classです。
型の対応(int64がscala.Longになる、など)は、基本的にJavaと同じなので、protocol buffersのJavaの公式ドキュメントを参照してください。

シリアライズ、デシリアライズをするのに、一番良く使うメソッドは以下の2つです。
最低限これら2つを覚えれば、シリアライズとデシリアライズが可能なはずです。

```scala
// 生成されたcase classに必ずある
// 正確には親クラスに定義されているので呼び出せる
def toByteArray: Array[Byte]
```

https://github.com/scalapb/ScalaPB/blob/v0.9.5/scalapb-runtime/shared/src/main/scala/scalapb/GeneratedMessageCompanion.scala#L138

```scala
// 生成されたcase classのコンパニオンオブジェクトに必ずある
// これも正確には親クラスに定義されているので呼び出せる
def parseFrom(s: Array[Byte]): 生成されたCaseClassの型
```

https://github.com/scalapb/ScalaPB/blob/v0.9.5/scalapb-runtime/shared/src/main/scala/scalapb/GeneratedMessageCompanion.scala#L211


```tut:silent
import scala.collection.JavaConverters._

@SerialVersionUID(0L)
final case class User(
    id: Long = 0L,
    name: String = ""
    ) extends scalapb.GeneratedMessage with scalapb.Message[User] with scalapb.lenses.Updatable[User] {
    final override def serializedSize: Int = ??? // 実装省略
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): Unit = ??? // 実装省略
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): User = ??? // 実装省略
    def withId(__v: Long): User = copy(id = __v)
    def withName(__v: String): User = copy(name = __v)
    def getFieldByNumber(__fieldNumber: Int): scala.Any = ??? // 実装省略
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = ??? // 実装省略
    def toProtoString: String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = example.user.User
}

object User extends scalapb.GeneratedMessageCompanion[example.user.User] with scalapb.JavaProtoSupport[example.user.User, example.UserOuterClass.User] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[example.user.User] with scalapb.JavaProtoSupport[example.user.User, example.UserOuterClass.User] = this
  def toJavaProto(scalaPbSource: example.user.User): example.UserOuterClass.User = ??? // 実装省略
  def fromJavaProto(javaPbSource: example.UserOuterClass.User): example.user.User = ??? // 実装省略
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any]): example.user.User = ??? // 実装省略
  implicit def messageReads: _root_.scalapb.descriptors.Reads[example.user.User] = ??? // 実装省略
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ??? // 実装省略
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ??? // 実装省略
  def messageCompanionForFieldNumber(__number: Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = example.user.User(
  )
  implicit class UserLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, example.user.User]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, example.user.User](_l) {
    def id: _root_.scalapb.lenses.Lens[UpperPB, Long] = field(_.id)((c_, f_) => c_.copy(id = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, String] = field(_.name)((c_, f_) => c_.copy(name = f_))
  }
  final val ID_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
}
```


[^generated-code]: 全部の実装の中身を貼ると長いので、省略してあります
