# gRPC

## gRPCとは

gRPCとは、[2015年2月にGoogleが発表](http://googledevjp.blogspot.jp/2015/03/http2-rpc-grpc.html)した、http2上のRPCプロトコルです。
gRPCそのものは、シリアライザ部分が入れ替え可能な仕様らしいですが、
デフォルトではProtocol Buffersが使用され、gRPC自体がまだそこまで成熟していないこともあり、
Protocol Buffers以外が使用されることはあまり多くないようです。

ここではgRPC自体の詳しい説明はしないので、以下の公式サイトなどを参考にしてください。

http://www.grpc.io/

## ScalaPBとgRPC

ScalaPBは、version 0.5.18からgRPC用のコード生成に対応しています。
ScalaPBでは、現状serviceの定義があると自動でgRPC用のコード生成がされます。
しかしランタイムの追加が必要です。
(この状態は多少使いづらいので、後のversionで変更になるかもしれません)

現状では以下の一行を追加します。

```tut:invisible
import sbt._, Keys._
```

```tut:silent
libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.18"
```

## サンプル

以下に、公式のgRCPのJavaのサンプルコードを、ScalaPBを使用してScalaに翻訳したものがあります。

https://github.com/xuwei-k/grpc-scala-sample

- [gRPCのJava](https://github.com/grpc/grpc-java/)もsbt上からそのままmainをrunできるようにするために、git submoduleでgrpc-javaを参照している
- `.proto`ファイルやリソースファイルをできるだけコピーしないようにするために、`sourceDirectory in PB.protobufConfig` や `unmanagedResourceDirectories in Compile` などの設定をScala側に追加している

といったことをしていますが、それ以外は特別変わったことをしていない、一般的なプロジェクト構成になっているはずです。
公式のJavaのサンプルには、こちら http://www.grpc.io/docs/tutorials/basic/java.html の公式のチュートリアルで解説されているものが含まれています。

以下のようなことが可能になっているはずなので、実際にsbt runをして確認してみてください。

- Javaのserver側を起動して、Scalaのclientを接続
- Scalaのserver側を起動して、Javaのclientを接続
- serverもclientもScalaで接続
