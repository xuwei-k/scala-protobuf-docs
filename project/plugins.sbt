addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.6.1")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.14")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.7.0-rc7"

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
