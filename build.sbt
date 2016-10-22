val SCALA_VERSION = "2.11.8"

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
  version := "0.1.0-SNAPSHOT",
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/shogowada/scalajs-reactjs")),
  scalaVersion := SCALA_VERSION,
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
  ),
  publishArtifact := false
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
