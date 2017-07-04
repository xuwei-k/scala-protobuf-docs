import NpmCliBase._
import com.trueaccord.scalapb.compiler.Version.protobufVersion

val lintAll = taskKey[Unit]("lint text, html")
val testAll = taskKey[Unit]("test scala, links")
val buildWithCheck = taskKey[Unit]("lintAll testAll build")

  // tutでsbtの設定を書く都合上、scalaVersionはわざと指定しないでsbtと同じ2.10.xを使う
val root = project.in(file(".")).settings(
  PB.targets in Compile := Seq(
    PB.gens.java(protobufVersion) -> (sourceManaged in Compile).value,
    scalapb.gen(javaConversions=true) -> (sourceManaged in Compile).value
  ),
  tutSettings,
  tutSourceDirectory := srcDir,
  tutTargetDirectory := compiledSrcDir,
  GitBook.settings,
  TextLint.settings,
  LinkTest.settings,
  libraryDependencies += sbtDependency.value,
  tutScalacOptions ++= "-deprecation" :: Nil,
  resolvers += Classpaths.sbtPluginReleases,
  addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.11"),
  libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % com.trueaccord.scalapb.compiler.Version.scalapbVersion,
  libraryDependencies ++= (
    ("com.google.protobuf" % "protobuf-java-util" % com.trueaccord.scalapb.compiler.Version.protobufVersion) ::
    Nil
  ),
  lintAll := Def.sequential(LinkTest.eslint, TextLint.textlint.toTask("")).value,
  testAll := Def.sequential(compile in Test, LinkTest.linkTest).value,
  buildWithCheck := Def.sequential(lintAll, testAll, GitBook.build).value
)
