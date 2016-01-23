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
実行時には、JavaのgRPCに依存します。
また、[gRPCのJavaの0.12.0が2016-01-23にリリース](https://github.com/grpc/grpc-java/tree/v0.12.0)されましたが、現在ScalaPB 0.5.21時点で対応しているのはgRPCの0.9.0です。
gRPC Javaの0.12.0の対応は、できるだけはやめにする予定です。

ScalaPBでは、serviceの定義があると自動でgRPC用のコード生成がされます。
しかしランタイムの追加が必要です。

現状では、通常のprotocol bufferを使う場合の設定に加えて、以下のlibraryDependenciesを追加します。

```tut:invisible
import sbt._, Keys._
```

```tut:silent
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}

libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % (PB.scalapbVersion in PB.protobufConfig).value
```

## サンプル

以下に、公式のgRCPのJavaのサンプルコードを、ScalaPBを使用してScalaに翻訳したものがあります[^scala-version]。

https://github.com/xuwei-k/grpc-scala-sample

- [gRPCのJava](https://github.com/grpc/grpc-java/)もsbt上からそのままmainをrunできるようにするために、git submoduleでgrpc-javaを参照している
- `.proto`ファイルやリソースファイルをできるだけコピーしないようにするために、`sourceDirectory in PB.protobufConfig` や `unmanagedResourceDirectories in Compile` などの設定をScala側に追加している

といったことをしていますが、それ以外は特別変わったことをしていない、一般的なプロジェクト構成になっているはずです。
公式のJavaのサンプルには、こちら http://www.grpc.io/docs/tutorials/basic/java.html の公式のチュートリアルで解説されているものが含まれています。

以下のようなことが可能になっているはずなので、実際にsbt runをして確認してみてください。

- Javaのserver側を起動して、Scalaのclientを接続
- Scalaのserver側を起動して、Javaのclientを接続
- serverもclientもScalaで接続


[^scala-version]: このサンプルコードは、Scala2.10だとコンパイルが通りません。 http://d.hatena.ne.jp/xuwei/20151208/1449568614 Scala2.10で動かしたい場合は `ServerBuilder` ではなく `NettyServerBuilder` を直接使うか、`asInstanceOf` でキャストしてください
