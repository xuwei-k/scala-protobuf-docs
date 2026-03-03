import NpmCliBase._
import scalapb.compiler.Version.protobufVersion

val lintAll = taskKey[Unit]("lint text, html")
val testAll = taskKey[Unit]("test scala, links")
val buildWithCheck = taskKey[Unit]("lintAll testAll build")

// mdocでsbtの設定を書く都合上、scalaVersionはわざと指定しないで、
// sbtが使用しているものと同じversionのScalaを使う
val root = project.in(file(".")).settings(
  (Compile / PB.targets) ++= Seq[protocbridge.Target](
    PB.gens.java(protobufVersion) -> (Compile / sourceManaged).value,
    scalapb.gen(javaConversions=true) -> (Compile / sourceManaged).value
  ),
  mdocIn := srcDir,
  mdocOut := compiledSrcDir,
  Honkit.settings,
  TextLint.settings,
  LinkTest.settings,
  libraryDependencies += sbtDependency.value,
  scalacOptions ++= "-deprecation" :: Nil,
  resolvers += Classpaths.sbtPluginReleases,
  addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.8"),
  libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % scalapb.compiler.Version.scalapbVersion,
  libraryDependencies ++= (
    ("com.google.protobuf" % "protobuf-java-util" % "4.34.0") ::
    Nil
  ),
  lintAll := Def.sequential(LinkTest.eslint, TextLint.textlint.toTask("")).value,
  testAll := Def.sequential(Test / compile, LinkTest.linkTest).value,
  buildWithCheck := Def.sequential(lintAll, testAll, Honkit.build).value
).enablePlugins(MdocPlugin)
