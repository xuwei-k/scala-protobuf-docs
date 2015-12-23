resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
  url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
  Resolver.ivyStylePatterns)

addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.0")

addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.18")

libraryDependencies += "com.github.os72" % "protoc-jar" % "3.0.0-b1"

fullResolvers ~= {_.filterNot(_.name == "jcenter")}
