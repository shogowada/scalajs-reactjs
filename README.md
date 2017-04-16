# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React applications with Scala. It is compatible with Scala 2.12 and Scala.js 0.6.14.

Optionally include react-router and react-redux facades, too.

## Quick Look

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

class HelloWorld extends StatelessReactClassSpec[HelloWorld.WrappedProps] {
  override def render() = <.div(^.id := "hello-world")(s"Hello, ${props.wrapped.name}!")
}

object HelloWorld {
  case class WrappedProps(name: String)

  def apply() = new HelloWorld()
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(<(HelloWorld())(^.wrapped := HelloWorld.WrappedProps("World"))(), mountNode)
```

You can also use a pure function to render:

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

object HelloWorld {
  def apply(name: String): ReactElement = <.div(^.id := "hello-world")(s"Hello, ${name}!")
}

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(HelloWorld("World"), mountNode)
```

## How to Use

1. Apply [scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/getting-started.html) plugin.
2. Depend on the libraries.
   ```
   libraryDependencies ++= Seq(
     "io.github.shogowada" %%% "scalajs-reactjs" % "0.9.0", // For react facade
     "io.github.shogowada" %%% "scalajs-reactjs-router-dom" % "0.9.0", // Optional. For react-router-dom facade
     "io.github.shogowada" %%% "scalajs-reactjs-redux" % "0.9.0" // Optional. For react-redux facade
   )
   ```

## Examples

- [Basics](./example)
- [TODO App](./example/todo-app/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoapp/Main.scala)
- [Routing](./example/routing/src/main/scala/io/github/shogowada/scalajs/reactjs/example/routing/Main.scala)
- [Redux](./example/todo-app-redux/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoappredux)
- [Redux DevTools](./example/redux-devtools/src/main/scala/io/github/shogowada/scalajs/reactjs/example/redux/devtools)
- [I don't like `<` and `^`. How can I change them?](./example/custom-virtual-dom)
