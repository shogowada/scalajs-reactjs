package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    object HelloWorld {
      case class WrappedProps(name: String)

      lazy val reactClass = React.createClass[WrappedProps, Unit](
        render = (context) => <.div(^.id := "hello-world")(s"Hello, ${context.props.wrapped.name}!")
      )
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(HelloWorld.reactClass)(^.wrapped := HelloWorld.WrappedProps("World"))(), mountNode)
  }
}
