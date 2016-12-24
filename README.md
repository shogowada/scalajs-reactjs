# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React JS applications with Scala.

|Component|SBT|Scala Version|Scala JS Version|
|---|---|---|---|
|Core|```"io.github.shogowada" %%% "scalajs-reactjs" % "0.4.2"```|2.11|0.6|
|[Router](/router)|```"io.github.shogowada" %%% "scalajs-reactjs-router" % "0.4.2"```|2.11|0.6|

## Table of Contents

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

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(new HelloWorld()(HelloWorld.Props("World")), mountNode)
```

## Getting Started

1. Depend on scalajs-reactjs.

2. Apply [scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/getting-started.html) plugin.

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
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(new HelloWorld()(HelloWorld.Props("World")), mountNode)
    ```

## Tutorial

- [TODO App](example/todo-app)
