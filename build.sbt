val CreateReactClassVersion = "^15.5.1"
val ReactVersion = "^15.5.3"
val ReactReduxVersion = "^5.0.3"
val ReactRouterVersion = "^3.0.0"
val ReduxVersion = "^3.6.0"
val WebpackVersion = "^2.3.2"

val StaticTagsVersion = "[2.4.0,3.0.0["

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
  version := "0.7.2-SNAPSHOT",
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
        "io.github.shogowada" %%% "statictags" % StaticTagsVersion
      ),
      npmDependencies in Compile ++= Seq(
        "create-react-class" -> CreateReactClassVersion,
        "react" -> ReactVersion,
        "react-dom" -> ReactVersion
      ),
      (version in webpack) := WebpackVersion,
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
        "react-router" -> ReactRouterVersion
      ),
      (webpack in(Compile, fastOptJS)) := Seq(),
      (webpack in(Compile, fullOptJS)) := Seq(),
      publishArtifact := true
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val redux = project.in(file("redux"))
    .settings(commonSettings: _*)
    .settings(
      name += "-redux",
      npmDependencies in Compile ++= Seq(
        "react-redux" -> ReactReduxVersion,
        "redux" -> ReduxVersion
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

lazy val exampleCustomVirtualDOM = project.in(file("example") / "custom-virtual-dom")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-custom-virtual-dom"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleHelloWorld = project.in(file("example") / "helloworld")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-helloworld"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleHelloWorldFunction = project.in(file("example") / "helloworld-function")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-helloworld-function"
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

lazy val exampleStyle = project.in(file("example") / "style")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-style"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleTodoApp = project.in(file("example") / "todo-app")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-todo-app"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleTodoAppRedux = project.in(file("example") / "todo-app-redux")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-todo-app-redux"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core, redux)

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
        "org.seleniumhq.selenium" % "selenium-java" % "3.+",

        "org.scalatest" %% "scalatest" % "3.+"
      ),
      javaOptions ++= Seq(
        s"-Dtarget.path.custom-virtual-dom=${(crossTarget in exampleCustomVirtualDOM).value}",
        s"-Dtarget.path.helloworld=${(crossTarget in exampleHelloWorld).value}",
        s"-Dtarget.path.helloworld-function=${(crossTarget in exampleHelloWorldFunction).value}",
        s"-Dtarget.path.interactive-helloworld=${(crossTarget in exampleInteractiveHelloWorld).value}",
        s"-Dtarget.path.lifecycle=${(crossTarget in exampleLifecycle).value}",
        s"-Dtarget.path.routing=${(crossTarget in exampleRouting).value}",
        s"-Dtarget.path.style=${(crossTarget in exampleStyle).value}",
        s"-Dtarget.path.todo-app=${(crossTarget in exampleTodoApp).value}",
        s"-Dtarget.path.todo-app-redux=${(crossTarget in exampleTodoAppRedux).value}",
        s"-Ddummy.custom-virtual-dom=${(webpack in fastOptJS in Compile in exampleCustomVirtualDOM).value}",
        s"-Ddummy.helloworld=${(webpack in fastOptJS in Compile in exampleHelloWorld).value}",
        s"-Ddummy.helloworld-function=${(webpack in fastOptJS in Compile in exampleHelloWorldFunction).value}",
        s"-Ddummy.interactive-helloworld=${(webpack in fastOptJS in Compile in exampleInteractiveHelloWorld).value}",
        s"-Ddummy.lifecycle=${(webpack in fastOptJS in Compile in exampleLifecycle).value}",
        s"-Ddummy.routing=${(webpack in fastOptJS in Compile in exampleRouting).value}",
        s"-Ddummy.style=${(webpack in fastOptJS in Compile in exampleStyle).value}",
        s"-Ddummy.todo-app=${(webpack in fastOptJS in Compile in exampleTodoApp).value}",
        s"-Ddummy.todo-app-redux=${(webpack in fastOptJS in Compile in exampleTodoAppRedux).value}"
      ),
      fork := true
    )
