addSbtPlugin("org.scalameta" % "sbt-mdoc" % "2.9.0")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.8")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-language:existentials",
  "-language:implicitConversions",
)
