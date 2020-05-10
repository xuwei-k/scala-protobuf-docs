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

https://github.com/scalapb/ScalaPB/blob/v0.10.3/scalapb-runtime/shared/src/main/scala/scalapb/GeneratedMessageCompanion.scala#L103

```scala
// 生成されたcase classのコンパニオンオブジェクトに必ずある
// これも正確には親クラスに定義されているので呼び出せる
def parseFrom(s: Array[Byte]): 生成されたCaseClassの型
```

https://github.com/scalapb/ScalaPB/blob/v0.10.3/scalapb-runtime/shared/src/main/scala/scalapb/GeneratedMessageCompanion.scala#L174


```tut:silent
@SerialVersionUID(0L)
final case class User(
    id: _root_.scala.Long = 0L,
    name: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[User] {
    override def serializedSize: _root_.scala.Int = ??? // 実装省略
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = ??? // 実装省略
    def withId(__v: _root_.scala.Long): User = copy(id = __v)
    def withName(__v: _root_.scala.Predef.String): User = copy(name = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = ??? // 実装省略
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = ??? // 実装省略
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = example.user.User
}

object User extends scalapb.GeneratedMessageCompanion[example.user.User] with scalapb.JavaProtoSupport[example.user.User, example.UserOuterClass.User] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[example.user.User] with scalapb.JavaProtoSupport[example.user.User, example.UserOuterClass.User] = this
  def toJavaProto(scalaPbSource: example.user.User): example.UserOuterClass.User = ??? // 実装省略
  def fromJavaProto(javaPbSource: example.UserOuterClass.User): example.user.User = ??? // 実装省略
  def merge(`_message__`: example.user.User, `_input__`: _root_.com.google.protobuf.CodedInputStream): example.user.User = ??? // 実装省略
  implicit def messageReads: _root_.scalapb.descriptors.Reads[example.user.User] = ??? 
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = ??? // 実装省略
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = ??? // 実装省略
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = example.user.User(
    id = 0L,
    name = ""
  )
  implicit class UserLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, example.user.User]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, example.user.User](_l) {
    def id: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.id)((c_, f_) => c_.copy(id = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
  }
  final val ID_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
  def of(
    id: _root_.scala.Long,
    name: _root_.scala.Predef.String
  ): _root_.example.user.User = ??? // 実装省略
}
```


[^generated-code]: 全部の実装の中身を貼ると長いので、省略してあります
