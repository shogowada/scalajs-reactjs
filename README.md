# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React JS applications with Scala.

## Quick Look

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

case class HelloWorldProps(name: String)

class HelloWorldSpec extends StatelessReactClassSpec {

  override type Props = HelloWorldProps

  override def render() = <.div()(s"Hello, ${props.name}!")
}

val element = dom.document.getElementById("main")
ReactDOM.render(new HelloWorldSpec(), HelloWorldProps("World"), element)
```

## Getting Started

1. Depend on scalajs-reactjs.

    |SBT|Scala Version|Scala JS Version|
    |---|---|---|
    |```"io.github.shogowada" %%% "scalajs-reactjs" % "0.1.0"```|2.11|0.6|

2. Make ```React``` and ```ReactDOM``` available at global scope.

    ```scala
    jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.3.2" / "react.js"
          commonJSName "React",
      "org.webjars.bower" % "react" % "15.3.2" / "react-dom.js"
          dependsOn "react.js"
          commonJSName "ReactDOM"
    )
    ```

3. Define props.

    ```scala
    case class HelloWorldProps(name: String)
    ```

4. Define class spec.
    - [```VirtualDOM```](core/src/main/scala/io/github/shogowada/scalajs/reactjs/VirtualDOM.scala) enables you to create React elements using special version of [StaticTags](https://github.com/shogowada/statictags).

    ```scala
    import io.github.shogowada.scalajs.reactjs.VirtualDOM._

    class HelloWorldSpec extends StatelessReactClassSpec {

      override type Props = HelloWorldProps

      override def render() = <.div()(s"Hello, ${props.name}!")
    }
    ```

5. Render the class with the props.

    ```scala
    val element = dom.document.getElementById("main")
    ReactDOM.render(new HelloWorldSpec(), HelloWorldProps("World"), element)
    ```
