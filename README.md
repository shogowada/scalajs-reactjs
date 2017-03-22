# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React JS applications with Scala.

|Component|SBT|Scala Version|Scala JS Version|
|---|---|---|---|
|react|```"io.github.shogowada" %%% "scalajs-reactjs" % "0.5.1"```|2.11, 2.12|0.6.14+|
|react-router|```"io.github.shogowada" %%% "scalajs-reactjs-router" % "0.5.1"```|2.11, 2.12|0.6.14+|
|react-redux|```"io.github.shogowada" %%% "scalajs-reactjs-redux" % "0.5.1"```|2.11, 2.12|0.6.14+|

## Table of Contents

- [Quick Look](#quick-look)
- [Getting Started](#getting-started)
- [Tutorial](#tutorial)

## Quick Look

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

class HelloWorld extends StatelessReactClassSpec[HelloWorld.Props] {
  override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
}

object HelloWorld {
  case class Props(name: String)

  def apply(props: Props): ReactElement = (new HelloWorld) (props)()
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(HelloWorld(HelloWorld.Props("World")), mountNode)
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

    class HelloWorld extends StatelessReactClassSpec[HelloWorld.Props] {
      override def render() = <.div()(s"Hello, ${props.name}!")
    }
    ```

5. Render the class with the props.

    ```scala
    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(HelloWorld(HelloWorld.Props("World")), mountNode)
    ```

## Tutorial

- [TODO App](example/todo-app)
- [Routing](/router)
- [Redux](/redux)
