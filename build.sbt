val scala3Version = "3.0.2"

ThisBuild / organization := "jacob.weather"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := scala3Version

lazy val root = project
  .in(file("."))
  .settings(
    name := "weather-summary-api",

    libraryDependencies ++= List(
      "com.softwaremill.sttp.tapir" %% "tapir-cats" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-client" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % TapirVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,

      "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,

      "org.specs2" %% "specs2-core" % Specs2Version % Test,
      "org.specs2" %% "specs2-scalacheck" % Specs2Version % Test,
      "org.specs2" %% "specs2-matcher-extra" % Specs2Version % Test,
    )
  )

val CirceVersion = "0.14.1"
val Http4sVersion = "0.23.3"
val PureconfigVersion = "0.16.0"
val Specs2Version = "5.0.0-RC-07"
val TapirVersion = "0.19.0-M8"
