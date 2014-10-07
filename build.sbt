organization := "eu.route20"

name := "hft"

version := "1.0"

scalaVersion := "2.11.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.1.2" % "test" 

libraryDependencies += "org.zeromq" % "jeromq" % "0.3.4" 

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.1.0" 

instrumentSettings

org.scalastyle.sbt.ScalastylePlugin.Settings

de.johoop.cpd4sbt.CopyPasteDetector.cpdSettings

fork in run := true

javaOptions in run += "-Xmx4G"

javaOptions in run += "-Xms1G"

javaOptions in run += "-XX:+UseG1GC"

javaOptions in run += "-XX:MaxGCPauseMillis=10"

javaOptions in run += "-XX:+PrintGCDetails"

javaOptions in run += "-XX:+PrintGCApplicationConcurrentTime"

javaOptions in run += "-XX:+PrintGCApplicationStoppedTime"

javaOptions in run += "-Xloggc:gc_log"

baseDirectory in run := file("../perf/")

