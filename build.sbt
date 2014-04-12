name := "akka-cluster-exp"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe" at "http://repo.typesafe.com/typesafe/snapshots"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-cluster" % "2.4-SNAPSHOT"
)
