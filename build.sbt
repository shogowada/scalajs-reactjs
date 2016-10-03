val SCALA_VERSION = "2.11.8"

val REACT_VERSION = "15.3.2"

val commonSettings = Seq(
  name := "scalajs-reactjs",
  organization := "io.github.shogowada",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := SCALA_VERSION,
  ivyScala := ivyScala.value.map {
    _.copy(overrideScalaVersion = true)
  }
)

lazy val core = project.in(file("core"))
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.0"
      )
    )
    .enablePlugins(ScalaJSPlugin)

lazy val example = project.in(file("example"))
    .settings(commonSettings: _*)
    .settings(Seq(
      name += "-example",
      (unmanagedResourceDirectories in Compile) += baseDirectory.value / "src" / "main" / "webapp",
      jsDependencies ++= Seq(
        "org.webjars.bower" % "react" % REACT_VERSION / "react-with-addons.js"
            commonJSName "React",
        "org.webjars.bower" % "react" % REACT_VERSION / "react-dom.js"
            dependsOn "react-with-addons.js"
            commonJSName "ReactDOM",
        "org.webjars.bower" % "react" % REACT_VERSION / "react-dom-server.js"
            dependsOn "react-dom.js"
            commonJSName "ReactDOMServer"
      )
    ))
    .enablePlugins(ScalaJSPlugin)
    .dependsOn(core)
