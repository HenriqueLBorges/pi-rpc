name := "pi-rpc-client"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.twitter" % "finagle-core_2.12" % "19.5.0"
libraryDependencies += "com.twitter" %% "finagle-thrift" % "19.5.0" exclude("com.twitter", "libthrift")
libraryDependencies += "com.twitter" %% "scrooge-core" % "19.5.0" exclude("com.twitter", "libthrift")

mainClass in assembly := Some("Client")
assemblyJarName in assembly := "pi-rpc-client.jar"

mergeStrategy in assembly := {
  case x if x.contains("ivy2") => MergeStrategy.first
  case x if x.contains("io.netty") => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "BUILD" => MergeStrategy.discard
  case x =>
    val oldStrategy = (mergeStrategy in assembly).value
    oldStrategy(x)

}