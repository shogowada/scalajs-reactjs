val SCALA_VERSION = "2.11.8"

lazy val app = project.in(file("."))
    .settings(
      name := "scalajs-reactjs",
      organization := "io.github.shogowada",
      version := "0.1.0-SNAPSHOT",
      scalaVersion := SCALA_VERSION,
      ivyScala := ivyScala.value.map {
        _.copy(overrideScalaVersion = true)
      }
    )
    .enablePlugins(ScalaJSPlugin)
