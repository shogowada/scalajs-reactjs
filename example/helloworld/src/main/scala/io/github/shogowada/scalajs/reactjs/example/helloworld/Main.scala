package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    object HelloWorld {
      case class WrappedProps(name: String)

      private lazy val reactClass = React.createClass[WrappedProps, Unit](
        render = (self) => <.div(^.id := "hello-world")(s"Hello, ${self.props.wrapped.name}!")
      )

      def apply() = reactClass
    }

    val mountNode = dom.document.getElementById("mount-node")
    ReactDOM.render(<(HelloWorld())(^.wrapped := HelloWorld.WrappedProps("World"))(), mountNode)
  }
}
