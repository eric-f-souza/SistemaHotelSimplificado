name := """SistemaHotel"""
organization := "br.com.sistemahotel"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.5.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.5.0",
  "com.typesafe.play" %% "play-json" % "2.10.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.5.0",
  "org.postgresql" % "postgresql" % "42.6.0",
)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0"

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "br.com.sistemahotel.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "br.com.sistemahotel.binders._"
