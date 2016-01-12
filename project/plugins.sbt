addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.1")

addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.19")

libraryDependencies += "com.github.os72" % "protoc-jar" % "3.0.0-b2"

fullResolvers ~= {_.filterNot(_.name == "jcenter")}
