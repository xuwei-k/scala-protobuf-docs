import NpmCliBase._
import scalapb.compiler.Version.protobufVersion

val lintAll = taskKey[Unit]("lint text, html")
val testAll = taskKey[Unit]("test scala, links")
val buildWithCheck = taskKey[Unit]("lintAll testAll build")

// tutでsbtの設定を書く都合上、scalaVersionはわざと指定しないで、
// sbtが使用しているものと同じversionのScalaを使う
val root = project.in(file(".")).settings(
  PB.targets in Compile := Seq(
    PB.gens.java(protobufVersion) -> (sourceManaged in Compile).value,
    scalapb.gen(javaConversions=true) -> (sourceManaged in Compile).value
  ),
  tutSourceDirectory := srcDir,
  tutTargetDirectory := compiledSrcDir,
  GitBook.settings,
  TextLint.settings,
  LinkTest.settings,
  libraryDependencies += sbtDependency.value,
  scalacOptions in Tut ++= "-deprecation" :: Nil,
  resolvers += Classpaths.sbtPluginReleases,
  addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.20"),
  libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % scalapb.compiler.Version.scalapbVersion,
  libraryDependencies ++= (
    ("com.google.protobuf" % "protobuf-java-util" % scalapb.compiler.Version.protobufVersion) ::
    Nil
  ),
  lintAll := Def.sequential(LinkTest.eslint, TextLint.textlint.toTask("")).value,
  testAll := Def.sequential(compile in Test, LinkTest.linkTest).value,
  buildWithCheck := Def.sequential(lintAll, testAll, GitBook.build).value
).enablePlugins(TutPlugin)
