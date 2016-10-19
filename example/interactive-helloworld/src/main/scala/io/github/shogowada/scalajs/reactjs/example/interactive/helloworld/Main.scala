package io.github.shogowada.scalajs.reactjs.example.interactive.helloworld

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.classes.specs.ReactClassSpec
import io.github.shogowada.scalajs.reactjs.components.ReactInputComponent
import io.github.shogowada.scalajs.reactjs.elements.VirtualDOM._
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {

  @JSExport
  def main(element: HTMLElement): Unit = {
    object InteractiveHelloWorld extends ReactClassSpec {

      case class State(name: String)

      override def getInitialState(): State = State(name = "whoever you are")

      var nameComponent: ReactInputComponent = _

      override def render() = {
        <.div()(
          <.input(
            ^.ref := ((element: ReactInputComponent) => {
              nameComponent = element
            }),
            ^.value := state.name,
            ^.onChange := (() => onChange())
          )(),
          <.div()(s"Hello, ${state.name}")
        )
      }

      def onChange(): Unit = {
        setState(State(name = nameComponent.value))
      }
    }

    ReactDOM.render(InteractiveHelloWorld, element)
  }
}
