# gRPC

## gRPCとは

gRPCとは、[2015年2月にGoogleが発表](https://developers-jp.googleblog.com/2015/03/http2-rpc-grpc.html)した、http2上のRPCプロトコルです。
gRPCそのものは、シリアライザ部分が入れ替え可能な仕様らしいですが、
デフォルトではProtocol Buffersが使用され、gRPC自体がまだそこまで成熟していないこともあり、
Protocol Buffers以外が使用されることはあまり多くないようです。

ここではgRPC自体の詳しい説明はしないので、以下の公式サイトなどを参考にしてください。

https://grpc.io

## ScalaPBとgRPC

ScalaPBは、version 0.5.18からgRPC用のコード生成に対応しています。
実行時には、JavaのgRPCに依存します。
これを書いている2019年9月現在の最新であるScalaPB 0.9.1 時点では、grpc-javaの1.23.0に対応しています。

ScalaPBでは、serviceの定義があると自動でgRPC用のコード生成がされます。
しかしランタイムの追加が必要です。

現状では、通常のprotocol buffersを使う場合の設定に加えて、以下のlibraryDependenciesを追加します。

```tut:invisible
import sbt._, Keys._

import sbtprotoc.ProtocPlugin.autoImport._
```

```tut:silent
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
```

ScalaPB 0.5.26までは、上記の依存の設定のみでOKでしたが、0.5.27以降は追加で設定が必要です。
以下は、ひとまずgrpc-javaのすべての依存を追加する場合です。

```tut:silent
libraryDependencies += "io.grpc" % "grpc-all" % scalapb.compiler.Version.grpcJavaVersion
```

grpc-allを追加するのではなく、もう少し細かく依存を選ぶことも可能です。
例えば、Android環境以外での普通の用途では "grpc-netty" を使うことになるはずです。
また、grpc-nettyはnettyの4.1系に依存しているため、基本的にnetty 4.0系に依存しているライブラリと同時に使えません(例: play 2.5, async-http-client 2.0 系)
そういう場合には "grpc-netty-shaded" というものを使用することにより、衝突する問題を回避可能です。(grpc-netty-shadedはgrpc-java 1.9から)
https://github.com/grpc/grpc-java/pull/2485

その他、詳細はgrpc-javaの公式のドキュメントを参照してください。

## サンプル

以下に、公式のgRPCのJavaのサンプルコードを、ScalaPBを使用してScalaに翻訳したものがあります[^scala-version]。

https://github.com/xuwei-k/grpc-scala-sample

- [gRPCのJava](https://github.com/grpc/grpc-java/)もsbt上からそのままmainをrunできるようにするために、git submoduleでgrpc-javaを参照している
- `.proto`ファイルやリソースファイルをできるだけコピーしないようにするために、`sourceDirectory in PB.protobufConfig` や `unmanagedResourceDirectories in Compile` などの設定をScala側に追加している

といったことをしていますが、それ以外は特別変わったことをしていない、一般的なプロジェクト構成になっているはずです。
公式のJavaのサンプルには、こちら https://grpc.io/docs/tutorials/basic/java.html の公式のチュートリアルで解説されているものが含まれています。

以下のようなことが可能になっているはずなので、実際にsbt runをして確認してみてください。

- Javaのserver側を起動して、Scalaのclientを接続
- Scalaのserver側を起動して、Javaのclientを接続
- serverもclientもScalaで接続


[^scala-version]: このサンプルコードは、Scala2.10だとコンパイルが通りません。 https://xuwei-k.hatenablog.com/entry/20151208/1449568614 Scala2.10で動かしたい場合は `ServerBuilder` ではなく `NettyServerBuilder` を直接使うか、`asInstanceOf` でキャストしてください
