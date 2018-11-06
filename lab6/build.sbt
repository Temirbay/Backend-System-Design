name := "lab6"

version := "0.1"

scalaVersion := "2.12.4"


lazy val scalaTestVersion = "3.0.4"
lazy val akkaVersion = "2.5.17"
lazy val akkaHttpVersion = "10.1.5"


lazy val slickVersion = "3.2.1"
lazy val mysqlVersion = "5.1.34"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",


  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "mysql" % "mysql-connector-java" % mysqlVersion,
  "org.slf4j" % "slf4j-nop" % "1.6.4",

  // test
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  // akka actors
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  // akka stream
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  // spray json for akka-http
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
)