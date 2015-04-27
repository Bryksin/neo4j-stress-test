name := "neo4j_stres_test"

version := "1.0"

scalaVersion := "2.11.1"

val neo4jVersion = "2.2.1"

resolvers += "Neo4j" at "http://m2.neo4j.org/content/repositories/releases/"

resolvers += "Restlet" at "http://maven.restlet.com/"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.slf4j" % "slf4j-simple" % "1.7.10",
  "org.no-hope" % "test-utils-stress" % "0.1.8",
  "org.neo4j" % "neo4j" % neo4jVersion,
  "org.neo4j" % "neo4j-jdbc" % "2.0.2"
)


assemblyMergeStrategy in assembly := {
  case PathList("builddef.lst") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}