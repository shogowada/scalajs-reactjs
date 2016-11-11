val REACT_VERSION = "15.3.2"

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
  version := "0.4.1-SNAPSHOT",
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/shogowada/scalajs-reactjs")),
  scalaVersion := "2.11.8",
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
        "io.github.shogowada" %%% "statictags" % "2.+"
      ),
      npmDependencies in Compile ++= Seq(
        "react" -> REACT_VERSION,
        "react-dom" -> REACT_VERSION
      ),
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

lazy val exampleTest = project.in(file("example") / "test")
    .settings(commonSettings: _*)
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
        s"-Dtarget.path.todo-app=${(crossTarget in exampleTodoApp).value}"
      ),
      fork := true
    )
