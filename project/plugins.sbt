addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.6.11")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.21")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.9.0-M4"

fullResolvers ~= {_.filterNot(_.name == "jcenter")}

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-Xlint:-unused,_" ::
  "-Ywarn-value-discard" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-Yno-adapted-args" ::
  Nil
)
