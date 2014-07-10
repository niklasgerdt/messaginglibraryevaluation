organization  := "eu.route20"

name := "hft"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"
 
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"

