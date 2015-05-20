name := """play-2_4-di"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.5"

libraryDependencies ++=
  cache ::
  javaWs ::
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.47.3" ::
  "org.mongodb" % "mongodb-driver" % "3.0.1" ::
  filters ::
  "org.assertj" % "assertj-core" % "3.0.0" % "test" ::
  Nil

javacOptions += "-Xlint:deprecation"

dependencyOverrides += "com.typesafe.play" %% "play-omnidoc" % "2.4.0-RC2"

dependencyOverrides += "com.typesafe.netty" % "netty-http-pipelining" % "1.1.4"