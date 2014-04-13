name := "akka-cluster-exp"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe (Releases)" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Typesafe (Snapshots)" at "http://repo.typesafe.com/typesafe/snapshots"

libraryDependencies ++= Seq(
  "com.typesafe.akka"           %%  "akka-actor"            % "2.4-SNAPSHOT",
  "com.typesafe.akka"           %%  "akka-cluster"          % "2.4-SNAPSHOT",
  "com.typesafe.akka"           %%  "akka-slf4j"            % "2.4-SNAPSHOT",
  "com.typesafe"                %%  "scalalogging-slf4j"    % "1.0.1",
  "ch.qos.logback"              %   "logback-classic"       % "1.0.13"
)

javaOptions in run ++= Seq(
  "-Dcom.sun.management.jmxremote.port=1190",
  "-Dcom.sun.management.jmxremote.authenticate=false",
  "-Dcom.sun.management.jmxremote.ssl=false"
)
