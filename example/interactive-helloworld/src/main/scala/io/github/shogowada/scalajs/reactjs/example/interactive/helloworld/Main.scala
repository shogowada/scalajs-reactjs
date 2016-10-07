package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.infra.Pickler
import io.github.shogowada.scalajs.reactjs.{React, ReactClassSpec, ReactDOM, ReactElement}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {

  @JSExport
  def main(element: HTMLElement): Unit = {
    object InteractiveHelloWorld extends ReactClassSpec {

      val NAME_REF = "name"

      case class State(name: String)

      override def getInitialState(): State = State("whoever you are")

      var nameElement: HTMLElement = _

      override def render(): ReactElement = {
        React.createElement("div", null,
          React.createElement("input", js.Dictionary(
            "ref" -> Pickler.toJs((element: HTMLElement) => {
              nameElement = element
            }),
            "value" -> self.state.name,
            "onChange" -> Pickler.toJs(() => onChange())
          )),
          React.createElement("div", null, s"Hello, ${self.state.name}!")
        )
      }

      def onChange(): Unit = {
        self.setState(State(name = nameElement.nodeName))
      }
    }

    val interactiveHelloWorldClass = React.createClass(InteractiveHelloWorld)

    ReactDOM.render(React.createElement(interactiveHelloWorldClass), element)
  }
}
