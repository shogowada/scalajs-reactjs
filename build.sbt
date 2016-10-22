val SCALA_VERSION = "2.11.8"

val REACT_VERSION = "15.3.2"

val commonSettings = Seq(
  organization := "io.github.shogowada",
  name := "scalajs-reactjs",
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
        "org.scala-js" %%% "scalajs-dom" % "0.9.0",
        "io.github.shogowada" %%% "statictags" % "1.+"
      )
    )
    .enablePlugins(ScalaJSPlugin)

val exampleCommonSettings = commonSettings ++ Seq(
  name += "-example",
  (unmanagedResourceDirectories in Compile) += baseDirectory.value / "src" / "main" / "webapp",
  jsDependencies ++= Seq(
    "org.webjars.bower" % "react" % REACT_VERSION / "react-with-addons.js"
        commonJSName "React",
    "org.webjars.bower" % "react" % REACT_VERSION / "react-dom.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM"
  )
)

lazy val exampleHelloWorld = project.in(file("example") / "helloworld")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-helloworld"
    )
    .enablePlugins(ScalaJSPlugin)
    .dependsOn(core)

lazy val exampleInteractiveHelloWorld = project.in(file("example") / "interactive-helloworld")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-interactive-helloworld"
    )
    .enablePlugins(ScalaJSPlugin)
    .dependsOn(core)
