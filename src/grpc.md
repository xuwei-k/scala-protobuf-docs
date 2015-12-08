# gRPC

gRPCとは、[2015年2月にGoogleが発表](http://googledevjp.blogspot.jp/2015/03/http2-rpc-grpc.html)した、http2上のRPCプロトコルです。
gRPCそのものは、シリアライザ部分が入れ替え可能な仕様らしいですが、
デフォルトではProtocol Buffersが使用され、gRPC自体がまだそこまで成熟していないこともあり、
Protocol Buffers以外が使用されることはあまり多くないようです。

ここではgRPC自体の詳しい説明はしないので、以下の公式サイトなどを参考にしてください。

http://www.grpc.io/

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
