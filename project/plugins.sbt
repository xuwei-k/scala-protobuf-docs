addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.2")

addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.24")

libraryDependencies += "com.github.os72" % "protoc-jar" % "3.0.0-b2"

fullResolvers ~= {_.filterNot(_.name == "jcenter")}
