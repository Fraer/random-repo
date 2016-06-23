name := "lunatech"

version := "1.0"

scalaVersion := "2.11.8"

////////////////
// play 2.5.3 //
////////////////

libraryDependencies ++= Seq(
  ws,
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "org.apache.spark" %% "spark-sql" % "1.6.1",
  "com.databricks" %% "spark-csv" % "1.4.0"
)

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)

val webJars = Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "angularjs" % "1.4.7",
  "org.webjars" % "angular-ui-bootstrap" % "0.14.3",
  "org.webjars" % "angular-growl-2" % "0.7.3" exclude("org.webjars","angularjs")
)

lazy val `projectx` = (project in file("."))
  .settings(libraryDependencies ++= webJars)
  .enablePlugins(PlayScala)