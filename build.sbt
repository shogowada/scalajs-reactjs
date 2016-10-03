val SCALA_VERSION = "2.11.8"

val commonSettings = Seq(
  name := "scalajs-reactjs",
  organization := "io.github.shogowada",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := SCALA_VERSION,
  ivyScala := ivyScala.value.map {
    _.copy(overrideScalaVersion = true)
  }
)

lazy val app = project.in(file("."))
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.0"
      )
    )
    .enablePlugins(ScalaJSPlugin)
