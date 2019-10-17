name := "red_goblin"

version := "0.1"

scalaVersion := "2.12.7"

val circeVersion = "0.11.1"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
testOptions in Test +=Tests.Cleanup((loader:java.lang.ClassLoader)=>{loader.loadClass("report.creator.HtmlReportInZip").newInstance})

libraryDependencies ++= List(
  "com.softwaremill.sttp" %% "core" % "1.7.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "org.pegdown"    %  "pegdown"     % "1.6.0"  % "test"
)
