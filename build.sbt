val CreateReactClassVersion = "^15.5.1"
val ReactVersion = "^15.5.3"
val ReactReduxVersion = "^5.0.3"
val ReactRouterVersion = "^4.0.0"
val ReduxVersion = "^3.6.0"
val ReduxDevToolsVersion = "^2.13.0"
val WebpackVersion = "^2.3.2"

val StaticTagsVersion = "[2.4.0,3.0.0["

val JettyVersion = "9.+"
val SeleniumVersion = "[3.4.0,4.0.0["
val ScalaTestVersion = "[3.1.0,4.0.0["

crossScalaVersions := Seq("2.12.1")

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
  version := "0.10.0",
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

lazy val routerDom = project.in(file("router-dom"))
    .settings(commonSettings: _*)
    .settings(
      name += "-router-dom",
      npmDependencies in Compile ++= Seq(
        "react-router-dom" -> ReactRouterVersion
      ),
      (webpack in(Compile, fastOptJS)) := Seq(),
      (webpack in(Compile, fullOptJS)) := Seq(),
      publishArtifact := true
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core, router)

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

lazy val reduxDevTools = project.in(file("redux-devtools"))
    .settings(commonSettings: _*)
    .settings(
      name += "-redux-devtools",
      npmDependencies in Compile ++= Seq(
        "redux-devtools-extension" -> ReduxDevToolsVersion
      ),
      (webpack in(Compile, fastOptJS)) := Seq(),
      (webpack in(Compile, fullOptJS)) := Seq(),
      publishArtifact := true
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core, redux)

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

lazy val exampleLifecycle = project.in(file("example") / "lifecycle")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-lifecycle"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core)

lazy val exampleReduxDevTools = project.in(file("example") / "redux-devtools")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-redux-devtools"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core, redux, reduxDevTools)

lazy val exampleRouting = project.in(file("example") / "routing")
    .settings(exampleCommonSettings: _*)
    .settings(
      name += "-routing"
    )
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(core, routerDom)

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

lazy val exampleTest = project.in(file("example") / "test")
    .configs(IntegrationTest)
    .settings(commonSettings: _*)
    .settings(Defaults.itSettings: _*)
    .settings(
      name += "-example-test",
      libraryDependencies ++= Seq(
        "org.eclipse.jetty" % "jetty-server" % JettyVersion,
        "org.seleniumhq.selenium" % "selenium-java" % SeleniumVersion,

        "org.scalatest" %% "scalatest" % ScalaTestVersion
      ),
      javaOptions ++= Seq(
        s"-Dtarget.path.custom-virtual-dom=${(crossTarget in exampleCustomVirtualDOM).value}",
        s"-Dtarget.path.helloworld=${(crossTarget in exampleHelloWorld).value}",
        s"-Dtarget.path.helloworld-function=${(crossTarget in exampleHelloWorldFunction).value}",
        s"-Dtarget.path.interactive-helloworld=${(crossTarget in exampleInteractiveHelloWorld).value}",
        s"-Dtarget.path.lifecycle=${(crossTarget in exampleLifecycle).value}",
        s"-Dtarget.path.redux-devtools=${(crossTarget in exampleReduxDevTools).value}",
        s"-Dtarget.path.routing=${(crossTarget in exampleRouting).value}",
        s"-Dtarget.path.style=${(crossTarget in exampleStyle).value}",
        s"-Dtarget.path.todo-app=${(crossTarget in exampleTodoApp).value}",
        s"-Dtarget.path.todo-app-redux=${(crossTarget in exampleTodoAppRedux).value}",
        Seq(
          (webpack in fastOptJS in Compile in exampleCustomVirtualDOM).value,
          (webpack in fastOptJS in Compile in exampleHelloWorld).value,
          (webpack in fastOptJS in Compile in exampleHelloWorldFunction).value,
          (webpack in fastOptJS in Compile in exampleInteractiveHelloWorld).value,
          (webpack in fastOptJS in Compile in exampleLifecycle).value,
          (webpack in fastOptJS in Compile in exampleReduxDevTools).value,
          (webpack in fastOptJS in Compile in exampleRouting).value,
          (webpack in fastOptJS in Compile in exampleStyle).value,
          (webpack in fastOptJS in Compile in exampleTodoApp).value,
          (webpack in fastOptJS in Compile in exampleTodoAppRedux).value
        ).foldRight("-Dexamples.built=true")((_, option) => option)
      ),
      fork := true
    )
