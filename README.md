# scalajs-reactjs

[![Build Status](https://travis-ci.org/shogowada/scalajs-reactjs.svg?branch=master)](https://travis-ci.org/shogowada/scalajs-reactjs)

Develop React applications with Scala. It is compatible with Scala 2.12 and Scala.js 0.6.14.

Optionally include react-router and react-redux facades, too.

## Quick Look

```scala
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

case class WrappedProps(name: String)

val reactClass = React.createClass[WrappedProps, Unit](
  (self) => <.div(^.id := "hello-world")(s"Hello, ${self.props.wrapped.name}!")
)

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(<(reactClass)(^.wrapped := WrappedProps("World"))(), mountNode)
```

You can also directly render without creating a React class:

```scala
import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom

val mountNode = dom.document.getElementById("mount-node")
ReactDOM.render(<.div(^.id := "hello-world")("Hello, World!"), mountNode)
```

## How to Use

1. Apply [scalajs-bundler](https://scalacenter.github.io/scalajs-bundler/getting-started.html) plugin.
2. Depend on the libraries.
   ```
   libraryDependencies ++= Seq(
     "io.github.shogowada" %%% "scalajs-reactjs" % "0.14.0", // For react facade
     "io.github.shogowada" %%% "scalajs-reactjs-router-dom" % "0.14.0", // Optional. For react-router-dom facade
     "io.github.shogowada" %%% "scalajs-reactjs-router-redux" % "0.14.0", // Optional. For react-router-redux facade
     "io.github.shogowada" %%% "scalajs-reactjs-redux" % "0.14.0", // Optional. For react-redux facade
     "io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % "0.14.0" // Optional. For redux-devtools facade
   )
   ```

## Examples

- [Basics](/example)
- [TODO App](/example/todo-app/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoapp/Main.scala)
- [Router](/example/routing/src/main/scala/io/github/shogowada/scalajs/reactjs/example/router/Main.scala)
- [Redux](/example/todo-app-redux/src/main/scala/io/github/shogowada/scalajs/reactjs/example/todoappredux)
- [Redux Middleware](/example/redux-middleware/src/main/scala/io/github/shogowada/scalajs/reactjs/example/redux/middleware/Main.scala)
- [Router Redux](/example/router-redux/src/main/scala/io/github/shogowada/scalajs/reactjs/example/router/redux/Main.scala)
- [Redux DevTools](/example/redux-devtools/src/main/scala/io/github/shogowada/scalajs/reactjs/example/redux/devtools/Main.scala)
- [I don't like `<` and `^`. How can I change them?](/example/custom-virtual-dom)

## Reusable Components

- [ReactCrop](https://github.com/shogowada/scalajs-reactjs-image-crop): Crop an image
