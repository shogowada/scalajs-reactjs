# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React JS applications with Scala. It is compatible with Scala 2.11, 2.12, and Scala.js 0.6.14+.

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

You can also use a pure function to render:

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

object HelloWorld {
  case class Props(name: String)

  def apply(props: Props): ReactElement = <.div(^.id := "hello-world")(s"Hello, ${props.name}!")
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(HelloWorld(HelloWorld.Props("World")), mountNode)

```

## How to Use

1. Apply [scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/getting-started.html) plugin.
2. Depend on the libraries.
   ```
   libraryDependencies ++= Seq(
     "io.github.shogowada" %%% "scalajs-reactjs" % "0.6.1", // For react facade
     "io.github.shogowada" %%% "scalajs-reactjs-router" % "0.6.1", // Optional. For react-router facade
     "io.github.shogowada" %%% "scalajs-reactjs-redux" % "0.6.1" // Optional. For react-redux facade
   )
   ```

## Tutorial

- [TODO App](example/todo-app)
- [Routing](/router)
- [Redux](/redux)
