addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.7")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.3")

libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % "0.5.46"

fullResolvers ~= {_.filterNot(_.name == "jcenter")}
