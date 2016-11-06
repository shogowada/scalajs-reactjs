# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React JS applications with Scala.

- [Quick Look](#quick-look)
- [Getting Started](#getting-started)
- [Tutorial](#tutorial)

## Quick Look

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

class HelloWorld extends StatelessReactClassSpec {
  override type Props = HelloWorld.Props
  override def render() = <.div()(s"Hello, ${props.name}!")
}

object HelloWorld {
  case class Props(name: String)
}

val mountNode = dom.document.getElementById("main")
ReactDOM.render(new HelloWorld()(HelloWorld.Props("World")), mountNode)
```

## Getting Started

1. Depend on scalajs-reactjs.

    |SBT|Scala Version|Scala JS Version|
    |---|---|---|
    |```"io.github.shogowada" %%% "scalajs-reactjs" % "0.2.0"```|2.11|0.6|

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
    object HelloWorld {
      case class Props(name: String)
    }
    ```

4. Define class spec.
    - [```VirtualDOM```](core/src/main/scala/io/github/shogowada/scalajs/reactjs/VirtualDOM.scala) enables you to create React elements using special version of [StaticTags](https://github.com/shogowada/statictags).

    ```scala
    import io.github.shogowada.scalajs.reactjs.VirtualDOM._

    class HelloWorld extends StatelessReactClassSpec {
      override type Props = HelloWorld.Props
      override def render() = <.div()(s"Hello, ${props.name}!")
    }
    ```

5. Render the class with the props.

    ```scala
    val mountNode = dom.document.getElementById("main")
    ReactDOM.render(new HelloWorld()(HelloWorld.Props("World")), mountNode)
    ```

## Tutorial

- [TODO App](example/todo-app)
