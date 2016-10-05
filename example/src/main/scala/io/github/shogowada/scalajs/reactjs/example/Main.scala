package io.github.shogowada.scalajs.reactjs.example

import io.github.shogowada.scalajs.reactjs.{React, ReactClassSpec, ReactDOM}
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main() = {
    class HelloWorldClass() extends ReactClassSpec {

      case class Props(name: String)

      case class State()

      override def render(self: This) = React.createElement("div", null, s"Hello, ${self.props.name}")
    }

    val helloWorldClass = React.createClass(new HelloWorldClass())

    ReactDOM.render(helloWorldClass, dom.document.body)
  }
}
