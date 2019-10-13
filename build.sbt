name := "red_goblin"

version := "0.1"

scalaVersion := "2.12.7"

val circeVersion = "0.11.1"

libraryDependencies ++= List(
  "com.softwaremill.sttp" %% "core" % "1.7.1",
//  "com.softwaremill.sttp" %% "json4s" % "1.7.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
//  "org.json4s" %% "json4s-native" % "3.6.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)
