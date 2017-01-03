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
型の対応(int64がscala.Longになる、など)は、基本的にJavaと同じなので、protocol bufferのJavaの公式ドキュメントを参照してください。

```scala
@SerialVersionUID(0L)
final case class User(
    id: Long = 0L,
    name: String = ""
    ) extends com.trueaccord.scalapb.GeneratedMessage with com.trueaccord.scalapb.Message[User] with com.trueaccord.lenses.Updatable[User] {
    @transient
    lazy val serializedSize: Int = ??? // 実装省略
    def writeTo(output: com.google.protobuf.CodedOutputStream): Unit = ??? // 実装省略
    def mergeFrom(__input: com.google.protobuf.CodedInputStream): User = ??? // 実装省略
    def withId(__v: Long): User = copy(id = __v)
    def withName(__v: String): User = copy(name = __v)
    def getField(__field: com.google.protobuf.Descriptors.FieldDescriptor): Any = ??? // 実装省略
    def companion = User
}

object User extends com.trueaccord.scalapb.GeneratedMessageCompanion[User]  {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[User]  = this
  def fromFieldsMap(__fieldsMap: Map[com.google.protobuf.Descriptors.FieldDescriptor, Any]): User = ??? // 実装省略
  def descriptor: com.google.protobuf.Descriptors.Descriptor = UserProto.descriptor.getMessageTypes.get(0)
  def messageCompanionForField(__field: com.google.protobuf.Descriptors.FieldDescriptor): com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__field)
  def enumCompanionForField(__field: com.google.protobuf.Descriptors.FieldDescriptor): com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__field)
  lazy val defaultInstance = User(
  )
  implicit class UserLens[UpperPB](_l: com.trueaccord.lenses.Lens[UpperPB, User]) extends com.trueaccord.lenses.ObjectLens[UpperPB, User](_l) {
    def id: com.trueaccord.lenses.Lens[UpperPB, Long] = field(_.id)((c_, f_) => c_.copy(id = f_))
    def name: com.trueaccord.lenses.Lens[UpperPB, String] = field(_.name)((c_, f_) => c_.copy(name = f_))
  }
  final val ID_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
}
```


[^generated-code]: 全部の実装の中身を貼ると長いので、省略してあります
