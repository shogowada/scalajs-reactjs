val REACT_VERSION = "15.4.2"

crossScalaVersions := Seq("2.11.8", "2.12.1")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  isSnapshot.value match {
    case true => Some("snapshots" at nexus + "content/repositories/snapshots")
    case false => Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}
publishArtifact := false

val commonSettings = Seq(
  organization := "io.github.shogowada",
  name := "scalajs-reactjs",
  version := "0.5.1",
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/shogowada/scalajs-reactjs")),
  scalaVersion := "2.12.1",
  ivyScala := ivyScala.value.map {
    _.copy(overrideScalaVersion = true)
  },
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    isSnapshot.value match {
      case true => Some("snapshots" at nexus + "content/repositories/snapshots")
      case false => Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }
  },
  publishArtifact := false,
  pomExtra := <scm>
    <url>git@github.com:shogowada/scalajs-reactjs.git</url>
    <connection>scm:git:git@github.com:shogowada/scalajs-reactjs.git</connection>
  </scm>
      <developers>
        <developer>
          <id>shogowada</id>
          <name>Shogo Wada</name>
          <url>https://github.com/shogowada</url>
        </developer>
      </developers>
)

lazy val core = project.in(file("core"))
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.+",
        "io.github.shogowada" %%% "statictags" % "2.1.0"
      ),
      npmDependencies in Compile ++= Seq(
        "react" -> REACT_VERSION,
        "react-dom" -> REACT_VERSION
      ),
      (webpack in(Compile, fastOptJS)) := Seq(),
      (webpack in(Compile, fullOptJS)) := Seq(),
      publishArtifact := true
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

lazy val router = project.in(file("router"))
    .settings(commonSettings: _*)
    .settings(
      name += "-router",
      npmDependencies in Compile ++= Seq(
        "react-router" -> "3.0.0"
      ),
      (webpack in(Compile, fastOptJS)) := Seq(),
      (webpack in(Compile, fullOptJS)) := Seq(),
      publishArtifact := true
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

val exampleCommonSettings = commonSettings ++ Seq(
  name += "-example",
  (unmanagedResourceDirectories in Compile) += baseDirectory.value / "src" / "main" / "webapp"
)

lazy val exampleHelloWorld = project.in(file("example") / "helloworld")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-helloworld"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleInteractiveHelloWorld = project.in(file("example") / "interactive-helloworld")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-interactive-helloworld"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleRouting = project.in(file("example") / "routing")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-routing"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core, router)

lazy val exampleTodoApp = project.in(file("example") / "todo-app")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-todo-app"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleLifecycle = project.in(file("example") / "lifecycle")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-lifecycle"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleTest = project.in(file("example") / "test")
    .configs(IntegrationTest)
    .settings(commonSettings: _*)
    .settings(Defaults.itSettings: _*)
    .settings(
      name += "-example-test",
      libraryDependencies ++= Seq(
        "org.eclipse.jetty" % "jetty-server" % "9.3+",
        "org.seleniumhq.selenium" % "selenium-java" % "2.+",

        "org.scalatest" %% "scalatest" % "3.+"
      ),
      javaOptions ++= Seq(
        s"-Dtarget.path.helloworld=${(crossTarget in exampleHelloWorld).value}",
        s"-Dtarget.path.interactive-helloworld=${(crossTarget in exampleInteractiveHelloWorld).value}",
        s"-Dtarget.path.lifecycle=${(crossTarget in exampleLifecycle).value}",
        s"-Dtarget.path.routing=${(crossTarget in exampleRouting).value}",
        s"-Dtarget.path.todo-app=${(crossTarget in exampleTodoApp).value}",
        s"-Ddummy.helloworld=${(webpack in fastOptJS in Compile in exampleHelloWorld).value}",
        s"-Ddummy.interactive-helloworld=${(webpack in fastOptJS in Compile in exampleInteractiveHelloWorld).value}",
        s"-Ddummy.lifecycle=${(webpack in fastOptJS in Compile in exampleLifecycle).value}",
        s"-Ddummy.routing=${(webpack in fastOptJS in Compile in exampleRouting).value}",
        s"-Ddummy.todo-app=${(webpack in fastOptJS in Compile in exampleTodoApp).value}"
      ),
      fork := true
    )
