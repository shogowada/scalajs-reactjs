package io.github.shogowada.scalajs.reactjs.example.helloworld

import io.github.shogowada.scalajs.reactjs.classes.specs.StatelessReactClassSpec
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main(element: HTMLElement) = {
    object HelloWorld extends StatelessReactClassSpec {

      case class Props(name: String)

      override def render() = React.createElement(
        "div",
        null,
        s"Hello, ${props.name}!"
      )
    }

    ReactDOM.render(HelloWorld, HelloWorld.Props("World"), element)
  }
}
