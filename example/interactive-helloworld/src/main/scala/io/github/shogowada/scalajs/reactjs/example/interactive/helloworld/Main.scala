package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.Converters._
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.components.ReactInputComponent
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {

  @JSExport
  def main(element: HTMLElement): Unit = {
    object InteractiveHelloWorld extends ReactClassSpec {

      case class State(name: String)

      override def getInitialState(): State = State(name = "whoever you are")

      var nameComponent: ReactInputComponent = _

      override def render(): ReactElement = {
        React.createElement("div", null,
          React.createElement("input", js.Dictionary(
            "ref" -> ((element: ReactInputComponent) => {
              nameComponent = element
            }).asJs,
            "value" -> this.state.name,
            "onChange" -> (() => onChange()).asJs
          )),

          React.createElement("div", null, s"Hello, ${this.state.name}!")
        )
      }

      def onChange(): Unit = {
        this.setState(State(name = nameComponent.value))
      }
    }

    ReactDOM.render(InteractiveHelloWorld, element)
  }
}
