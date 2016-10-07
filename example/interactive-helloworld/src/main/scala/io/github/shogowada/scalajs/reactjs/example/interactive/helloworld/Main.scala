package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.converters.Converter
import io.github.shogowada.scalajs.reactjs.elements.{ReactElement, ReactInputElement}
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
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

      var nameElement: ReactInputElement = _

      override def render(): ReactElement = {
        React.createElement("div", null,
          React.createElement("input", js.Dictionary(
            "ref" -> Converter.toJs((element: ReactInputElement) => {
              nameElement = element
            }),
            "value" -> self.state.name,
            "onChange" -> Converter.toJs(() => onChange())
          )),
          React.createElement("div", null, s"Hello, ${self.state.name}!")
        )
      }

      def onChange(): Unit = {
        self.setState(State(name = nameElement.value))
      }
    }

    ReactDOM.render(InteractiveHelloWorld, element)
  }
}
