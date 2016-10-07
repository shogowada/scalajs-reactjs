package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.{React, ReactClassSpec, ReactDOM}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main(element: HTMLElement) = {
    object HelloWorld extends ReactClassSpec {

      case class State()

      case class Props(name: String)

      override def getInitialState(): State = State()

      override def render() = React.createElement(
        "div",
        null,
        s"Hello, ${self.props.name}!"
      )
    }

    val helloWorldClass = React.createClass(HelloWorld)

    ReactDOM.render(React.createElement(helloWorldClass, HelloWorld.Props("World")), element)
  }
}
