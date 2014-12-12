name := """spending"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies += "net.sf.ofx4j" % "ofx4j" % "1.6"


libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)
