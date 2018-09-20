name := "testcoverage"
import sbtsonar.SonarPlugin.autoImport.sonarProperties

version := "0.1"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.6")

sonarProperties := Map(
  "sonar.projectName" -> "testCoverage",
  "sonar.projectKey" -> "test-coverage",
  "sonar.sources" -> "controllers",
  "sonar.sourceEncoding" -> "UTF-8",
  "sonar.scoverage.reportPath" -> "target/scala-2.12/scoverage-report/scoverage.xml",
)

def gatlingVersion(scalaBinVer: String): String = scalaBinVer match {
  case "2.11" => "2.2.5"
  case "2.12" => "2.3.1"
}

libraryDependencies += guice
libraryDependencies += "org.joda" % "joda-convert" % "1.9.2"
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "4.11"

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.16"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion(scalaBinaryVersion.value) % Test
libraryDependencies += "io.gatling" % "gatling-test-framework" % gatlingVersion(scalaBinaryVersion.value) % Test
libraryDependencies += compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
// The Play project itself
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play-scala-rest-api-example""",
  ).disablePlugins(PlayLayoutPlugin)


lazy val docs = (project in file("docs")).enablePlugins(ParadoxPlugin).
  settings(
    paradoxProperties += ("download_url" -> "https://example.lightbend.com/v1/download/play-rest-api")
  )

crossScalaVersions := Seq("2.11.11", "2.12.3")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

scalacOptions in (Compile, doc) ++= Seq("-unchecked", "-deprecation", "-diagrams", "-implicits", "-skip-packages", "samples")

lazy val ciTests = taskKey[Unit]("Run tests for CI")

ciTests := {
  // Capture the test result
  val testResult = (test in Test).result.value
}

coverageMinimum := 70

coverageFailOnMinimum := false

coverageHighlighting := true

publishArtifact in Test := false

parallelExecution in Test := false
