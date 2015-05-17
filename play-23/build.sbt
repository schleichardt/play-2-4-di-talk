name := """play-23-demo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++=
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.47.3" ::
    "org.mongodb" % "mongodb-driver" % "3.0.1" ::
    "com.google.inject" % "guice" % "3.0" ::
    Nil