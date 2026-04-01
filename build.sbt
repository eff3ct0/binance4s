val Scala3 = "3.3.6"

val CatsEffectV = "3.6.1"
val Http4sV     = "0.23.30"
val CirceV      = "0.14.13"
val Fs2V        = "3.12.0"
val MunitV      = "1.1.1"
val MunitCEV    = "2.1.0"

ThisBuild / scalaVersion       := Scala3
ThisBuild / organization       := "io.github.eff3ct0"
ThisBuild / versionScheme      := Some("semver-spec")
ThisBuild / version            := "2.0.0-SNAPSHOT"
ThisBuild / homepage           := Some(url("https://github.com/eff3ct0/binance4s"))
ThisBuild / licenses           := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / developers         := List(
  Developer("rafafrdz", "Rafael Fernández", "hello@rafaelfernandez.dev", url("https://rafaelfernandez.dev"))
)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-Wunused:all",
    "-Wvalue-discard",
    "-feature",
    "-deprecation",
    "-source:future",
    "-Xmax-inlines", "64"
  )
)

lazy val root = (project in file("."))
  .settings(name := "binance4s")
  .aggregate(core, http, ws, api, examples)
  .settings(publish / skip := true)

lazy val core = (project in file("modules/core"))
  .settings(name := "binance4s-core", commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"        % CatsEffectV,
      "io.circe"      %% "circe-core"         % CirceV,
      "io.circe"      %% "circe-generic"      % CirceV,
      "io.circe"      %% "circe-parser"       % CirceV,
      "org.scalameta" %% "munit"              % MunitV   % Test,
      "org.typelevel" %% "munit-cats-effect"  % MunitCEV % Test
    )
  )

lazy val http = (project in file("modules/http"))
  .settings(name := "binance4s-http", commonSettings)
  .dependsOn(core)
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % Http4sV,
      "org.http4s" %% "http4s-circe"        % Http4sV,
      "org.http4s" %% "http4s-dsl"          % Http4sV
    )
  )

lazy val ws = (project in file("modules/ws"))
  .settings(name := "binance4s-ws", commonSettings)
  .dependsOn(core)
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client"    % Http4sV,
      "org.http4s" %% "http4s-circe"           % Http4sV,
      "co.fs2"     %% "fs2-core"               % Fs2V,
      "org.http4s" %% "http4s-jdk-http-client" % "0.10.0"
    )
  )

lazy val api = (project in file("modules/api"))
  .settings(name := "binance4s-api", commonSettings)
  .dependsOn(http, ws)

lazy val examples = (project in file("modules/examples"))
  .settings(name := "binance4s-examples", commonSettings)
  .dependsOn(api)
  .settings(publish / skip := true)

lazy val integration = (project in file("modules/integration"))
  .settings(name := "binance4s-integration-tests", commonSettings)
  .dependsOn(api)
  .settings(
    publish / skip := true,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit"              % MunitV   % Test,
      "org.typelevel" %% "munit-cats-effect"  % MunitCEV % Test
    )
  )
