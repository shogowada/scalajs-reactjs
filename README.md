# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React applications with Scala. It is compatible with Scala 2.12 and Scala.js 0.6.14.

Optionally include react-router and react-redux facades, too.

## Quick Look

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

object HelloWorld {
  case class WrappedProps(name: String)

  private lazy val reactClass = React.createClass[WrappedProps, Unit](
    render = (self) => <.div(^.id := "hello-world")(s"Hello, ${self.props.wrapped.name}!")
  )

  def apply() = reactClass
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(<(HelloWorld())(^.wrapped := HelloWorld.WrappedProps("World"))(), mountNode)
```

You can also use a pure function to render:

```scala
import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom

object HelloWorld {
  def apply(name: String) = <.div(^.id := "hello-world")(s"Hello, ${name}!")
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(HelloWorld("World"), mountNode)
```

## How to Use

1. Apply [scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/getting-started.html) plugin.
2. Depend on the libraries.
   ```
   libraryDependencies ++= Seq(
     "io.github.shogowada" %%% "scalajs-reactjs" % "0.10.0", // For react facade
     "io.github.shogowada" %%% "scalajs-reactjs-router-dom" % "0.10.0", // Optional. For react-router-dom facade
     "io.github.shogowada" %%% "scalajs-reactjs-redux" % "0.10.0", // Optional. For react-redux facade
     "io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % "0.10.0" // Optional. For redux-devtools facade
   )
   ```

## Examples

- [Basics](./example)
- [TODO App](./example/todo-app/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoapp/Main.scala)
- [Routing](./example/routing/src/main/scala/io/github/shogowada/scalajs/reactjs/example/routing/Main.scala)
- [Redux](./example/todo-app-redux/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoappredux)
- [Redux DevTools](./example/redux-devtools/src/main/scala/io/github/shogowada/scalajs/reactjs/example/redux/devtools/Main.scala)
- [I don't like `<` and `^`. How can I change them?](./example/custom-virtual-dom)
